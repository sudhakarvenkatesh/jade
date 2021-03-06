package com.sos.DataExchange;

import static com.sos.DataExchange.SOSJadeMessageCodes.EXCEPTION_RAISED;
import static com.sos.DataExchange.SOSJadeMessageCodes.SOSJADE_D_0200;
import static com.sos.DataExchange.SOSJadeMessageCodes.SOSJADE_E_0098;
import static com.sos.DataExchange.SOSJadeMessageCodes.SOSJADE_E_0099;
import static com.sos.DataExchange.SOSJadeMessageCodes.SOSJADE_E_0100;
import static com.sos.DataExchange.SOSJadeMessageCodes.SOSJADE_E_0101;
import static com.sos.DataExchange.SOSJadeMessageCodes.SOSJADE_I_0100;
import static com.sos.DataExchange.SOSJadeMessageCodes.SOSJADE_I_0101;
import static com.sos.DataExchange.SOSJadeMessageCodes.SOSJADE_I_0102;
import static com.sos.DataExchange.SOSJadeMessageCodes.SOSJADE_I_0103;
import static com.sos.DataExchange.SOSJadeMessageCodes.SOSJADE_I_0115;
import static com.sos.DataExchange.SOSJadeMessageCodes.SOSJADE_T_0012;
import static com.sos.DataExchange.SOSJadeMessageCodes.SOSJADE_T_0013;
import static com.sos.DataExchange.SOSJadeMessageCodes.TRANSACTION_ABORTED;
import static com.sos.DataExchange.SOSJadeMessageCodes.TRANSFER_ABORTED;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.sos.DataExchange.Options.JADEOptions;
import com.sos.DataExchange.helpers.UpdateXmlToOptionHelper;
import com.sos.DataExchange.helpers.YadeDBOperationHelper;
import com.sos.JSHelper.Basics.VersionInfo;
import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.JSHelper.Options.JSOptionsClass;
import com.sos.JSHelper.Options.SOSOptionBoolean;
import com.sos.JSHelper.Options.SOSOptionFolderName;
import com.sos.JSHelper.Options.SOSOptionRegExp;
import com.sos.JSHelper.Options.SOSOptionTime;
import com.sos.JSHelper.Options.SOSOptionTransferType;
import com.sos.JSHelper.concurrent.SOSThreadPoolExecutor;
import com.sos.JSHelper.interfaces.IJadeEngine;
import com.sos.JSHelper.interfaces.IJobSchedulerEventHandler;
import com.sos.JSHelper.io.Files.JSFile;
import com.sos.VirtualFileSystem.DataElements.SOSFileList;
import com.sos.VirtualFileSystem.DataElements.SOSFileListEntry;
import com.sos.VirtualFileSystem.DataElements.SOSTransferStateCounts;
import com.sos.VirtualFileSystem.DataElements.SOSVfsConnectionFactory;
import com.sos.VirtualFileSystem.HTTP.SOSVfsHTTP;
import com.sos.VirtualFileSystem.Interfaces.ISOSVFSHandler;
import com.sos.VirtualFileSystem.Interfaces.ISOSVfsFileTransfer;
import com.sos.VirtualFileSystem.Interfaces.ISOSVfsFileTransfer2;
import com.sos.VirtualFileSystem.Interfaces.ISOSVirtualFile;
import com.sos.VirtualFileSystem.Options.SOSConnection2OptionsAlternate;
import com.sos.VirtualFileSystem.common.SOSFileEntries;
import com.sos.exception.SOSYadeSourceConnectionException;
import com.sos.exception.SOSYadeTargetConnectionException;
import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateOpenSessionException;
import com.sos.jade.db.DBItemYadeTransfers;
import com.sos.scheduler.model.SchedulerObjectFactory;

import sos.net.SOSMail;
import sos.net.mail.options.SOSSmtpMailOptions;
import sos.net.mail.options.SOSSmtpMailOptions.enuMailClasses;
import sos.util.SOSString;

public class SOSDataExchangeEngine extends JadeBaseEngine implements Runnable, IJadeEngine {

    private static final Logger LOGGER = Logger.getLogger(SOSDataExchangeEngine.class);
    protected static final String JADE_LOGGER_NAME = "JadeReportLog";
    private static final Logger JADE_REPORT_LOGGER = Logger.getLogger(JADE_LOGGER_NAME);
    private static final String KEYWORD_LAST_ERROR = "last_error";
    private static final String KEYWORD_STATE = "state";
    private static final String KEYWORD_SUCCESSFUL_TRANSFERS = "successful_transfers";
    private static final String KEYWORD_FAILED_TRANSFERS = "failed_transfers";
    private static final String KEYWORD_SKIPPED_TRANSFERS = "skipped_transfers";
    private static final String KEYWORD_STATUS = "status";
    private static final int CONST1000 = 1000;
    private ISOSVFSHandler targetHandler = null;
    private SOSFileList sourceFileList = null;
    private SOSVfsConnectionFactory factory = null;
    private SchedulerObjectFactory schedulerFactory = null;
    private long countPollingServerFiles = 0;
    private long countSentHistoryRecords = 0;
    private ISOSVfsFileTransfer targetClient = null;
    private ISOSVfsFileTransfer sourceClient = null;
    private SOSHibernateFactory dbFactory = null;
    private SOSHibernateSession dbSession = null;
    private YadeDBOperationHelper dbHelper = null;
    private Long transferId;
    private Long parentTransferId;
    private IJobSchedulerEventHandler eventHandler = null;
    private boolean isIntervention = false;
    private String filePathRestriction = null;

    public SOSDataExchangeEngine() throws Exception {
        this.getOptions();
    }

    public SOSDataExchangeEngine(final HashMap<String, String> settings) throws Exception {
        this.getOptions();
        objOptions.setAllOptions(settings);
    }

    public SOSDataExchangeEngine(final Properties properties) throws Exception {
        this.getOptions();
    }

    public SOSDataExchangeEngine(final JADEOptions jadeOptions) throws Exception {
        super(jadeOptions);
        objOptions = jadeOptions;
        if (objOptions.settings.isDirty()) {
            objOptions.readSettingsFile();
        }
    }

    public SOSFileEntries getSOSFileEntries() {
        return sourceClient.getSOSFileEntries();
    }

    public boolean checkSteadyStateOfFiles() {
        boolean allFilesAreSteady = true;
        if (objOptions.checkSteadyStateOfFiles.isTrue() && sourceFileList != null) {
            long interval = objOptions.checkSteadyStateInterval.getTimeAsSeconds();
            setInfo("checking file(s) for steady state");
            for (int i = 0; i < objOptions.checkSteadyCount.value(); i++) {
                allFilesAreSteady = true;
                String msg = String.format("steady check (%s of %s):", i + 1, objOptions.checkSteadyCount.value());
                LOGGER.info(String.format("%s waiting %ss.", msg, interval));
                doSleep(interval);
                for (SOSFileListEntry entry : sourceFileList.getList()) {
                    if (!checkSteadyStateOfFile(entry, msg)) {
                        allFilesAreSteady = false;
                    }
                }
                if (allFilesAreSteady) {
                    LOGGER.info(String.format("all files seem steady! Extra %s waiting %ss for late comers.", msg, interval));
                    doSleep(interval);
                    for (SOSFileListEntry entry : sourceFileList.getList()) {
                        entry.setSteady(false);
                        if (!checkSteadyStateOfFile(entry, msg)) {
                            allFilesAreSteady = false;
                        }
                    }
                }
                if (allFilesAreSteady) {
                    LOGGER.info(String.format("%s break steady check. all files are steady.", msg));
                    break;
                }
            }
            if (!allFilesAreSteady) {
                String msg = "not all files are steady";
                LOGGER.error(msg);
                for (SOSFileListEntry objFile : sourceFileList.getList()) {
                    if (!objFile.isSteady()) {
                        LOGGER.info(String.format("File '%1$s' is not steady", objFile.getSourceFileName()));
                    }
                }
                if (objOptions.steadyStateErrorState.isDirty()) {
                    objJSJobUtilities.setNextNodeState(objOptions.steadyStateErrorState.getValue());
                } else {
                    throw new JobSchedulerException(msg);
                }
            }
        }
        return allFilesAreSteady;
    }

    private boolean checkSteadyStateOfFile(SOSFileListEntry entry, String msg) {
        boolean fileIsSteady = true;
        if (!entry.isSteady()) {
            if (entry.getLastCheckedFileSize() < 0) {
                entry.setLastCheckedFileSize(entry.getFileSize());
            }
            entry.setSourceFileProperties(sourceClient.getFileHandle(entry.getSourceFileName()));
            if (entry.getLastCheckedFileSize().equals(entry.getFileSize())) {
                entry.setSteady(true);
                LOGGER.debug(String.format("%s Not changed. file size: %s bytes. '%s'", msg, entry.getLastCheckedFileSize(), entry
                        .getSourceFileName()));
            } else {
                fileIsSteady = false;
                LOGGER.info(String.format("%s Changed. file size: new = %s bytes, old = %s bytes. '%s'", msg, entry.getFileSize(), entry
                        .getLastCheckedFileSize(), entry.getSourceFileName()));
            }
            entry.setLastCheckedFileSize(entry.getFileSize());
        }
        return fileIsSteady;
    }

    private void doLogout(ISOSVfsFileTransfer client) throws Exception {
        if (client != null) {
            client.logout();
            client.disconnect();
            client.close();
            client = null;
        }
    }

    private String[] doPollingForFiles() {
        String[] fileList = null;
        if (objOptions.isFilePollingEnabled()) {
            long pollTimeout = getPollTimeout();
            long pollInterval = objOptions.pollInterval.getTimeAsSeconds();
            long currentPollingTime = 0;
            String sourceDir = objOptions.sourceDir.getValue();
            boolean isSourceDirFounded = false;
            long filesCount = 0;
            long currentFilesCount = sourceFileList.size();
            ISOSVirtualFile sourceFile = null;
            PollingLoop: while (true) {
                if (currentPollingTime > pollTimeout) {
                    setInfo(String.format("file-polling: time '%1$s' is over. polling terminated", getPollTimeoutText()));
                    break PollingLoop;
                }
                if (!isSourceDirFounded) {
                    sourceFile = sourceClient.getFileHandle(sourceDir);
                    if (objOptions.pollingWait4SourceFolder.isFalse()) {
                        isSourceDirFounded = true;
                    } else {
                        try {
                            if (sourceFile.notExists()) {
                                LOGGER.info(String.format("directory %1$s not found. Wait for the directory due to polling mode.", sourceDir));
                            } else {
                                isSourceDirFounded = true;
                            }
                        } catch (Exception e) {
                            isSourceDirFounded = false;
                        }
                    }
                }
                if (isSourceDirFounded) {
                    try {
                        if (objOptions.oneOrMoreSingleFilesSpecified()) {
                            currentFilesCount = 0;
                            objOptions.pollMinfiles.value(sourceFileList.count());
                            for (SOSFileListEntry objItem : sourceFileList.getList()) {
                                if (objItem.isSourceFileExists()) {
                                    currentFilesCount++;
                                    objItem.setParent(sourceFileList);
                                }
                            }
                        } else {
                            String integrityHashFileExtention = objOptions.checkIntegrityHash.isTrue() ? "." + objOptions.integrityHashType.getValue()
                                    : null;
                            selectFilesOnSource(sourceFile, objOptions.sourceDir, objOptions.fileSpec, objOptions.recursive,
                                    integrityHashFileExtention);
                            currentFilesCount = sourceFileList.count();
                        }
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                    }
                    if ((objOptions.pollMinfiles.isNotDirty() && currentFilesCount > 0) || (objOptions.pollMinfiles.isDirty()
                            && currentFilesCount >= objOptions.pollMinfiles.value())) {
                        break PollingLoop;
                    }
                }
                setInfo(String.format("file-polling: going to sleep for %1$d seconds. regexp '%2$s'", pollInterval, objOptions.fileSpec.getValue()));
                doSleep(pollInterval);
                currentPollingTime += pollInterval;
                setInfo(String.format("file-polling: %1$d files found for regexp '%2$s' on directory '%3$s'.", currentFilesCount, objOptions.fileSpec
                        .getValue(), sourceDir));
                if (filesCount >= currentFilesCount && filesCount != 0) {
                    if (objOptions.waitingForLateComers.isTrue()) {
                        objOptions.waitingForLateComers.setFalse();
                    } else {
                        break PollingLoop;
                    }
                }
            }
        }
        return fileList;
    }

    private void doProcessMail(final enuMailClasses mailClasses) {
        SOSSmtpMailOptions mailOptions = objOptions.getMailOptions();
        SOSSmtpMailOptions mailOptionsWithMailClasses = mailOptions.getOptions(mailClasses);
        if (mailOptionsWithMailClasses == null || !mailOptionsWithMailClasses.FileNotificationTo.isDirty()) {
            mailOptionsWithMailClasses = mailOptions;
        }
        processSendMail(mailOptionsWithMailClasses);
    }

    private void doSleep(final long time) {
        try {
            Thread.sleep(time * CONST1000);
        } catch (InterruptedException e) {
            // nothing to do
        }
    }

    @Override
    public boolean execute() throws Exception {

        setLogger();
        objOptions.getTextProperties().put("version", VersionInfo.VERSION_STRING);
        objOptions.logFilename.setLogger(JADE_REPORT_LOGGER);
        boolean ok = false;
        try {
            JobSchedulerException.LastErrorMessage = "";
            try {
                getOptions().checkMandatory();
            } catch (JobSchedulerException e) {
                throw e;
            } catch (Exception e) {
                throw new JobSchedulerException(e.getMessage());
            } finally {
                showBanner();
            }
            UpdateXmlToOptionHelper updateHelper = new UpdateXmlToOptionHelper(objOptions);
            if (updateHelper.checkBefore()) {
                updateHelper.executeBefore();
                objOptions = updateHelper.getOptions();
            }
            if (dbFactory != null) {
                dbSession = initStatelessSession();
                dbHelper = new YadeDBOperationHelper(this, eventHandler);
                if (parentTransferId != null) {
                    dbHelper.setParentTransferId(parentTransferId);
                    DBItemYadeTransfers existingTransfer = dbHelper.getTransfer(parentTransferId, dbSession);
                    if (existingTransfer != null && existingTransfer.getJobChainNode().equals(objOptions.getJobChainNodeName()) && existingTransfer
                            .getOrderId().equals(objOptions.getOrderId()) && existingTransfer.getState() == 3) {
                        existingTransfer.setHasIntervention(true);
                        dbSession.beginTransaction();
                        dbSession.update(existingTransfer);
                        dbSession.commit();
                        transferId = dbHelper.storeInitialTransferInformations(dbSession, existingTransfer.getId());
                    } else {
                        transferId = dbHelper.storeInitialTransferInformations(dbSession);
                    }
                } else {
                    transferId = dbHelper.storeInitialTransferInformations(dbSession);
                }
                if (eventHandler != null && transferId != null) {
                    Map<String, String> values = new HashMap<String, String>();
                    values.put("transferId", transferId.toString());
                    eventHandler.sendEvent("YADETransferStarted", values);
                }
            }
            ok = transfer();
            if (dbSession != null) {
                dbHelper.updateSuccessfulTransfer(dbSession);
            }
            if (!JobSchedulerException.LastErrorMessage.isEmpty()) {
                throw new JobSchedulerException(JobSchedulerException.LastErrorMessage);
            }
        } catch (SOSYadeSourceConnectionException | SOSYadeTargetConnectionException e) {
            if (dbSession != null) {
                dbHelper.updateFailedTransfer(dbSession, String.format("%1$s: %2$s", e.getClass().getSimpleName(), e.getMessage()));
            }
            throw new JobSchedulerException(e.getCause());
        } catch (JobSchedulerException e) {
            if (dbSession != null) {
                dbHelper.updateFailedTransfer(dbSession, String.format("%1$s: %2$s", e.getClass().getSimpleName(), e.getMessage()));
            }
            throw e;
        } catch (Exception e) {
            if (dbSession != null) {
                dbHelper.updateFailedTransfer(dbSession, String.format("%1$s: %2$s", e.getClass().getSimpleName(), e.getMessage()));
            }
            throw new JobSchedulerException(e.getMessage());
        } finally {
            if (dbSession != null) {
                dbSession.close();
                dbFactory.close();
            }
            if (eventHandler != null && transferId != null) {
                Map<String, String> values = new HashMap<String, String>();
                values.put("transferId", transferId.toString());
                eventHandler.sendEvent("YADETransferFinished", values);
            }
            showResult();
            sendNotifications();
        }
        return ok;
    }

    private void showResult() {
        String msg = "";
        if (objOptions.bannerFooter.isDirty()) {
            msg = objOptions.bannerFooter.getJSFile().getContent();
        } else {
            msg = SOSJadeMessageCodes.SOSJADE_T_0011.get();
        }
        setTextProperties();
        msg = objOptions.replaceVars(msg);
        JADE_REPORT_LOGGER.info(msg);
        LOGGER.info(msg);
    }

    private String showBannerHeaderSource(SOSConnection2OptionsAlternate options, boolean isSource) {
        StringBuilder sb = new StringBuilder();
        String pattern4String = "  | %-22s= %s%n";
        String pattern4Bool = "  | %-22s= %b%n";
        String pattern4IntegrityHash = "  | %-22s= %b (%s)%n";
        String pattern4Rename = "  | %-22s= %s -> %s%n";
        sb.append(String.format(pattern4String, "Protocol", options.protocol.getValue()));
        sb.append(String.format(pattern4String, "Host", options.host.getValue()));
        try {
            sb.append(String.format(pattern4String, "IP", InetAddress.getByName(options.host.getValue()).getHostAddress()));
        } catch (UnknownHostException e) {
            sb.append(String.format(pattern4String, "IP", "could not be resolved!"));
        }
        if (!options.protocol.isLocal()) {
            sb.append(String.format(pattern4String, "User", options.user.getValue()));
            if (options.protocol.getEnum() != SOSOptionTransferType.enuTransferTypes.sftp && options.protocol
                    .getEnum() != SOSOptionTransferType.enuTransferTypes.ftp && options.protocol
                            .getEnum() != SOSOptionTransferType.enuTransferTypes.zip) {
                sb.append(String.format(pattern4String, "AuthMethod", options.sshAuthMethod.getValue()));
            }
            if (options.protocol.getEnum() == SOSOptionTransferType.enuTransferTypes.sftp) {
                if (options.required_authentications.isNotEmpty()) {
                    sb.append(String.format(pattern4String, "RequiredAuths", options.required_authentications.getValue()));
                    sb.append(String.format(pattern4String, "Password", "***"));
                    sb.append(String.format(pattern4String, "AuthFile", "***"));
                    if (options.passphrase.isNotEmpty()) {
                        sb.append(String.format(pattern4String, "Passphrase", "***"));
                    }
                } else {
                    String am = "";
                    if (options.password.isNotEmpty() && options.authFile.isNotEmpty()) {
                        am = "password,publickey";
                    } else {
                        am = options.authMethod.getValue();
                    }
                    String pa = options.preferred_authentications.getValue();
                    if (SOSString.isEmpty(pa)) {
                        pa = am;
                    }
                    sb.append(String.format(pattern4String, am.indexOf(",") == -1 ? "AuthMethod" : "AuthMethods", am));
                    if (pa.indexOf(",") != -1) {
                        sb.append(String.format(pattern4String, "PreferredAuths", pa));
                    }
                    if (options.password.isNotEmpty()) {
                        sb.append(String.format(pattern4String, "Password", "***"));
                    }
                    if (options.authFile.isNotEmpty()) {
                        sb.append(String.format(pattern4String, "AuthFile", "***"));
                    }
                    if (options.passphrase.isNotEmpty()) {
                        sb.append(String.format(pattern4String, "Passphrase", "***"));
                    }
                }

            } else {
                sb.append(String.format(pattern4String, "Password", "***"));
            }

            if (options.protocol.getEnum() == SOSOptionTransferType.enuTransferTypes.ftp) {
                sb.append(String.format(pattern4Bool, "Passive", options.passiveMode.value()));
                sb.append(String.format(pattern4String, "TransferMode", options.transferMode.getValue()));
            }
        }
        if (isSource) {
            if (options.directory.isDirty()) {
                sb.append(String.format(pattern4String, "Directory", options.directory.getValue()));
            }
            if (getOptions().filePath.isNotEmpty()) {
                sb.append(String.format(pattern4String, "FilePath", getOptions().filePath.getValue()));
            }
            if (getOptions().fileListName.isDirty()) {
                sb.append(String.format(pattern4String, "FileList", getOptions().fileListName.getValue()));
            }
            if (getOptions().fileSpec.isDirty()) {
                sb.append(String.format(pattern4String, "FileSpec", getOptions().fileSpec.getValue()));
            }
            sb.append(String.format(pattern4Bool, "ErrorWhenNoFilesFound", getOptions().forceFiles.value()));
            sb.append(String.format(pattern4Bool, "Recursive", getOptions().recursive.value()));
            if (getOptions().skipTransfer.isFalse()) {
                sb.append(String.format(pattern4Bool, "Remove", getOptions().removeFiles.value()));
                if (getOptions().pollInterval.isDirty() || getOptions().pollTimeout.isDirty() || getOptions().pollMinfiles.isDirty()) {
                    sb.append(String.format(pattern4String, "PollingInterval", getOptions().pollInterval.getValue()));
                    sb.append(String.format(pattern4String, "PollingTimeout", getOptions().pollTimeout.getValue()));
                    sb.append(String.format(pattern4String, "PollingMinFiles", getOptions().pollMinfiles.getValue()));
                }
                if (getOptions().checkSteadyStateOfFiles.isTrue()) {
                    sb.append(String.format(pattern4String, "CheckSteadyInterval", getOptions().checkSteadyStateInterval.getValue()));
                    sb.append(String.format(pattern4String, "CheckSteadyCount", getOptions().checkSteadyCount.getValue()));
                }
            }
            if (getOptions().checkIntegrityHash.isDirty()) {
                sb.append(String.format(pattern4IntegrityHash, "CheckIntegrity", getOptions().checkIntegrityHash.value(),
                        getOptions().integrityHashType.getValue()));
            }
        } else {
            sb.append(String.format(pattern4String, "Directory", options.directory.getValue()));
            sb.append(String.format(pattern4Bool, "OverwriteFiles", getOptions().overwriteFiles.value()));
            if (getOptions().appendFiles.isTrue()) {
                sb.append(String.format(pattern4Bool, "AppendFiles", getOptions().appendFiles.value()));
            }
            if (getOptions().compressFiles.isTrue()) {
                sb.append(String.format(pattern4Bool, "CompressFiles", getOptions().compressFiles.value()));
            }
            if (getOptions().cumulateFiles.isTrue()) {
                sb.append(String.format(pattern4Bool, "CumulateFiles", getOptions().cumulateFiles.value()));
                sb.append(String.format(pattern4Bool, "CumulateFileName", getOptions().cumulativeFileName.getValue()));
            }
            if (getOptions().atomicPrefix.isDirty()) {
                sb.append(String.format(pattern4String, "AtomicPrefix", getOptions().atomicPrefix.getValue()));
            }
            if (getOptions().atomicSuffix.isDirty()) {
                sb.append(String.format(pattern4String, "AtomicSuffix", getOptions().atomicSuffix.getValue()));
            }
            if (getOptions().createIntegrityHashFile.isDirty()) {
                sb.append(String.format(pattern4IntegrityHash, "CreateIntegrityFile", getOptions().createIntegrityHashFile.value(),
                        getOptions().integrityHashType.getValue()));
            }
        }
        if (options.replacement.isDirty() && options.replacing.isNotEmpty()) {
            sb.append(String.format(pattern4Rename, "Rename", options.replacing.getValue(), options.replacement.getValue()));
        }
        return sb.toString();
    }

    private void showBanner() {
        StringBuilder sb = new StringBuilder();
        if (objOptions.bannerHeader.isDirty()) {
            // this parameter should deprecated
            sb = new StringBuilder(objOptions.replaceVars(objOptions.bannerHeader.getJSFile().getContent()));
        } else {
            String timestamp = "";
            try {
                timestamp = SOSOptionTime.getCurrentDateAsString(getOptions().dateFormatMask.getValue()) + " " + SOSOptionTime.getCurrentTimeAsString(
                        getOptions().timeFormatMask.getValue());
            } catch (Exception e) {
                timestamp = getOptions().getDate() + " " + getOptions().getTime();
            }
            String pattern4String = "  %-24s= %s%n";
            String pattern4Bool = "  %-24s= %b%n";
            String pattern4SourceTarget = "%n  +------------%s------------%n";
            sb.append(String.format("%n%072d%n", 0).replace('0', '*'));
            sb.append(String.format("*%70s*%n", " "));
            sb.append(String.format("*%25s%-45s*%n", "YADE", " - Managed File Transfer"));
            sb.append(String.format("*%44s%-26s*%n", "-----www.sos-berlin.com", "-----"));
            sb.append(String.format("*%70s*%n", " "));
            sb.append(String.format("%072d%n", 0).replace('0', '*'));
            sb.append(String.format(pattern4String, "Version", VersionInfo.VERSION_STRING));
            sb.append(String.format(pattern4String, "Date", timestamp));
            if (getOptions().settings.isNotEmpty()) {
                sb.append(String.format(pattern4String, "SettingsFile", getOptions().getOriginalSettingsFile()));
            }
            if (getOptions().profile.isNotEmpty()) {
                sb.append(String.format(pattern4String, "Profile", getOptions().profile.getValue()));
            }
            sb.append(String.format(pattern4String, "Operation", getOptions().operation.getValue()));
            sb.append(String.format(pattern4Bool, "Transactional", getOptions().transactional.value()));
            if (getOptions().skipTransfer.isDirty()) {
                sb.append(String.format(pattern4Bool, "SkipTransfer", getOptions().skipTransfer.value()));
            }
            if (getOptions().history.isDirty()) {
                sb.append(String.format(pattern4String, "History", getOptions().history.getValue()));
            }
            if (getOptions().logFilename.isDirty()) {
                sb.append(String.format(pattern4String, "LogFile", getOptions().logFilename.getValue()));
            }
            sb.append(String.format(pattern4SourceTarget, "Source"));
            sb.append(showBannerHeaderSource(getOptions().getSource(), true));
            if (objOptions.isNeedTargetClient()) {
                sb.append(String.format(pattern4SourceTarget, "Target"));
                sb.append(showBannerHeaderSource(getOptions().getTarget(), false));
            }
            sb.append("\n");
        }
        JADE_REPORT_LOGGER.info(sb.toString());
        LOGGER.info(sb.toString());
    }

    private void fillFileList(final String[] fileList, final String sourceDir) {
        if (objOptions.maxFiles.isDirty() && fileList.length > objOptions.maxFiles.value()) {
            for (int i = 0; i < objOptions.maxFiles.value(); i++) {
                sourceFileList.add(fileList[i]);
            }
        } else {
            sourceFileList.add(fileList, sourceDir);
        }
    }

    public SOSFileList getFileList() {
        return sourceFileList;
    }

    private long getPollTimeout() {
        long pollTimeout = 0;
        if (objOptions.pollTimeout.isDirty()) {
            pollTimeout = objOptions.pollTimeout.value() * 60;
        } else {
            pollTimeout = objOptions.pollingDuration.getTimeAsSeconds();
        }
        return pollTimeout;
    }

    private String getPollTimeoutText() {
        String pollTimeout = "";
        if (objOptions.pollTimeout.isDirty()) {
            pollTimeout = objOptions.pollTimeout.getValue();
        } else {
            pollTimeout = objOptions.pollingDuration.getValue();
        }
        return pollTimeout;
    }

    private String[] getSingleFileNames() {
        Vector<String> fileList = new Vector<String>();
        String localDir = objOptions.sourceDir.getValueWithFileSeparator();

        if (objOptions.filePath.isNotEmpty()) {
            String filePath = objOptions.filePath.getValue();
            LOGGER.debug(String.format("single file(s) specified : '%1$s'", filePath));
            try {
                String[] arr = filePath.split(";");
                for (String filename : arr) {
                    filename = filename.trim();
                    if (!filename.isEmpty()) {
                        if (!localDir.trim().isEmpty() && !isAPathName(filename)) {
                            filename = localDir + filename;
                        }
                        fileList.add(filename);
                    }
                }
            } catch (Exception e) {
                throw new JobSchedulerException(String.format("error while reading file_path='%1$s': %2$s", objOptions.filePath.getValue(), e
                        .toString()));
            }
        }
        if (objOptions.fileListName.isNotEmpty()) {
            String fileListName = objOptions.fileListName.getValue();
            JSFile file = new JSFile(fileListName);
            if (file.exists()) {
                try {
                    StringBuffer line = null;
                    String filename = "";
                    while ((line = file.getLine()) != null) {
                        filename = line.toString().trim();
                        if (filename.length() > 0) {
                            if (localDir.trim().length() > 0 && isAPathName(filename)) {
                                filename = localDir + filename;
                            }
                            fileList.add(filename);
                        }
                    }
                } catch (JobSchedulerException e1) {
                    throw e1;
                } catch (Exception e1) {
                    throw new JobSchedulerException(String.format("error while reading '%1$s': %2$s", objOptions.fileListName.getValue(), e1
                            .toString()));
                } finally {
                    try {
                        file.close();
                    } catch (IOException e) {
                        // nothing to do
                    }
                }
            } else {
                throw new JobSchedulerException(String.format("%1$s doesn't exist.", objOptions.fileListName.getValue()));
            }
        }
        return !fileList.isEmpty() ? fileList.toArray(new String[fileList.size()]) : new String[0];
    }

    @Override
    public String getState() {
        return (String) objOptions.getTextProperties().get(KEYWORD_STATE);
    }

    protected boolean isAPathName(final String path) {
        boolean ok = false;
        String aPath = path.replaceAll("\\\\", "/");
        if (!(aPath.startsWith("./") || aPath.startsWith("../"))) {
            // drive:/, protocol:/, /, ~/, $ (Unix Env), %...% (Windows Env)
            if (aPath.matches("^[a-zA-Z]+:/.*") || aPath.startsWith("/") || aPath.startsWith("~/") || aPath.startsWith("$") || aPath.matches(
                    "^%[a-zA-Z_0-9.-]+%.*")) {
                ok = true;
            } else {
                // nothing to do
            }
        }
        return ok;
    }

    @Override
    public void logout() {
        try {
            doLogout(targetClient);
            doLogout(sourceClient);
        } catch (Exception e) {
            // nothing to do
        }
        if (getOptions().getDeleteSettingsFileOnExit()) {
            try {
                String msg = "deleted";
                if (!Files.deleteIfExists(Paths.get(objOptions.settings.getValue()))) {
                    msg = "cant'be deleted";
                }
                LOGGER.debug(String.format("settings file %s[%s]", msg, objOptions.settings.getValue()));
            } catch (IOException e) {
                LOGGER.debug(String.format("settings file can't be deleted[%s]exception %s", objOptions.settings.getValue(), e.toString()), e);
            }
        }
    }

    private void makeDirs() {
        if (objOptions.skipTransfer.isFalse()) {
            makeDirs(objOptions.targetDir.getValue());
        }
    }

    private boolean makeDirs(final String path) {
        boolean cd = true;
        try {
            if (objOptions.makeDirs.value()) {
                if (SOSString.isEmpty(path)) {
                    throw new Exception("objOptions.TargetDir is empty");
                }
                if (targetClient.isDirectory(path)) {
                    cd = true;
                } else {
                    targetClient.mkdir(path);
                    cd = targetClient.isDirectory(path);
                }
            } else {
                if (path != null && !path.isEmpty()) {
                    cd = targetClient.isDirectory(path);
                }
            }
        } catch (Exception e) {
            throw new JobSchedulerException("..error in makeDirs: " + e, e);
        }
        return cd;
    }

    private long oneOrMoreSingleFilesSpecified(final String sourceDir) {
        long founded = 0;
        sourceFileList.add(getSingleFileNames(), sourceDir, true);
        long currentFounded = sourceFileList.size();
        if (objOptions.isFilePollingEnabled()) {
            long currentPollingTime = 0;
            long pollInterval = objOptions.pollInterval.getTimeAsSeconds();
            long pollTimeout = getPollTimeout();
            while (true) {
                founded = 0;
                if (currentPollingTime > pollTimeout) {
                    LOGGER.info(String.format("polling: time '%1$s' is over. polling terminated", getPollTimeoutText()));
                    break;
                }
                for (SOSFileListEntry entry : sourceFileList.getList()) {
                    if (entry.isSourceFileExists()) {
                        founded++;
                        entry.setParent(sourceFileList);
                    }
                }
                if (founded == currentFounded) {
                    break;
                }
                if (objOptions.pollMinfiles.value() > 0 && founded >= objOptions.pollMinfiles.value()) {
                    break;
                }
                String msg = String.format("file-polling: going to sleep for %1$d seconds. '%2$d' files found, waiting for '%3$d' files",
                        pollInterval, founded, currentFounded);
                setInfo(msg);
                doSleep(pollInterval);
                currentPollingTime += pollInterval;
            }
        } else {
            founded = currentFounded;
        }
        setInfo(String.format("%1$d files found.", sourceFileList.size()));
        return founded;
    }

    @Override
    public JADEOptions getOptions() {
        if (objOptions == null) {
            objOptions = new JADEOptions();
        }
        return objOptions;
    }

    @Override
    public void setJadeOptions(final JSOptionsClass options) {
        objOptions = (JADEOptions) options;
    }

    private boolean printState(boolean rc) throws Exception {
        String state = "processing successful ended";
        if (objOptions.fileListName.isNotEmpty()) {
            state = SOSJADE_E_0098.params(objOptions.fileListName.getValue());
        } else if (objOptions.filePath.isNotEmpty()) {
            state = SOSJADE_E_0099.params(objOptions.filePath.getValue());
        } else {
            state = SOSJADE_E_0100.params(objOptions.fileSpec.getValue());
        }
        SOSTransferStateCounts counts = getTransferCounts();
        if (sourceFileList.isEmpty()) {
            if (objOptions.forceFiles.isTrue()) {
                objOptions.getTextProperties().put(KEYWORD_STATE, state);
                throw new JobSchedulerException(state);
            }
        } else {
            long countSuccess = counts.getSuccessTransfers();
            long countSkipped = counts.getSkippedTransfers();
            long countZeroByte = counts.getZeroBytesTransfers();
            if (countSuccess == 1) {
                state = SOSJADE_I_0100.get();
            } else {
                state = SOSJADE_I_0101.params(countSuccess);
            }
            if (countZeroByte > 0) {
                state += " " + SOSJADE_I_0102.params(countZeroByte);
            }
            if (countSkipped > 0) {
                state += " " + SOSJADE_I_0103.params(countSkipped);
            }
        }
        LOGGER.info(state);
        JADE_REPORT_LOGGER.info(state);
        objOptions.getTextProperties().put(KEYWORD_STATE, state);
        if (counts.getFailedTranfers() > 0 || !JobSchedulerException.LastErrorMessage.isEmpty()) {
            return false;
        }
        return true;
    }

    private void processSendMail(final SOSSmtpMailOptions mailOptions) {
        if (mailOptions != null && mailOptions.FileNotificationTo.isDirty()) {
            try {
                StringBuilder attachment = new StringBuilder(mailOptions.attachment.getValue());
                if (mailOptions.attachment.isDirty()) {
                    attachment.append(";");
                }
                if (objOptions.logFilename.isDirty()) {
                    String logFileName = objOptions.logFilename.getHtmlLogFileName();
                    if (logFileName.length() > 0) {
                        attachment.append(logFileName);
                    }
                    logFileName = objOptions.logFilename.getValue();
                    if (logFileName.length() > 0) {
                        if (attachment.length() > 0) {
                            attachment.append(";");
                        }
                        attachment.append(logFileName);
                    }
                    if (attachment.length() > 0) {
                        mailOptions.attachment.setValue(attachment.toString());
                    }
                }
                if (!mailOptions.subject.isDirty()) {
                    mailOptions.subject.setValue("JADE: ");
                }
                StringBuilder subject = new StringBuilder(mailOptions.subject.getValue());
                mailOptions.subject.setValue(objOptions.replaceVars(subject.toString()));
                StringBuilder body = new StringBuilder(objOptions.replaceVars(mailOptions.body.getValue()));
                body.append("\n").append("List of transferred Files:").append("\n");
                for (SOSFileListEntry entry : sourceFileList.getList()) {
                    body.append(entry.getSourceFilename().replaceAll("\\\\", "/")).append("\n");
                }
                mailOptions.body.setValue(body.toString());
                if (!mailOptions.from.isDirty()) {
                    mailOptions.from.setValue("JADE@sos-berlin.com");
                }
                SOSMail mail = new SOSMail(mailOptions.host.getValue());
                LOGGER.debug(mailOptions.dirtyString());
                mail.sendMail(mailOptions);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        try {
            this.execute();
        } catch (Exception e) {
            throw new JobSchedulerException(EXCEPTION_RAISED.get(e), e);
        }
    }

    private void sendFiles(final SOSFileList fileList) {
        int maxParallelTransfers = 0;
        if (objOptions.concurrentTransfer.isTrue()) {
            // TODO resolve problem with apache ftp client in multithreading
            // mode
        }
        if (maxParallelTransfers <= 0 || objOptions.cumulateFiles.isTrue()) {
            for (SOSFileListEntry entry : fileList.getList()) {
                entry.setOptions(objOptions);
                entry.setDataSourceClient(sourceClient);
                entry.setDataTargetClient(targetClient);
                entry.setConnectionPool4Source(factory.getSourcePool());
                entry.setConnectionPool4Target(factory.getTargetPool());
                entry.setEventHandler(eventHandler);
                entry.run();
            }
        } else {
            SOSThreadPoolExecutor executor = new SOSThreadPoolExecutor(maxParallelTransfers);
            for (SOSFileListEntry entry : fileList.getList()) {
                entry.setOptions(objOptions);
                entry.setConnectionPool4Source(factory.getSourcePool());
                entry.setConnectionPool4Target(factory.getTargetPool());
                executor.runTask(entry);
            }
            try {
                executor.shutDown();
                executor.objThreadPool.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private void sendNotifications() {
        if (objOptions.mailOnError.isTrue() && (sourceFileList.getFailedTransfers() > 0 || !JobSchedulerException.LastErrorMessage.isEmpty())) {
            doProcessMail(enuMailClasses.MailOnError);
        } else if (objOptions.mailOnSuccess.isTrue() && sourceFileList.getSuccessfulTransfers() > 0) {
            doProcessMail(enuMailClasses.MailOnSuccess);
        }
        if (objOptions.mailOnEmptyFiles.isTrue() && sourceFileList.getZeroByteCount() > 0) {
            doProcessMail(enuMailClasses.MailOnEmptyFiles);
        }
    }

    private void setInfo(final String msg) {
        LOGGER.info(msg);
        objJSJobUtilities.setStateText(msg);
    }

    private void setSystemProperties() {
        try {
            if (!SOSString.isEmpty(getOptions().system_property_files.getValue())) {
                String files = getOptions().system_property_files.getValue();
                if (!SOSString.isEmpty(files)) {
                    LOGGER.info(String.format("set system properties from files: %s", files));
                    setSystemProperties(files);
                }
            }
        } catch (Exception ex) {
            LOGGER.warn("error on setSystemProperties: " + ex.toString());
        }
    }

    private void setSystemProperties(String files) {
        String[] arr = files.trim().split(";");
        for (String file : arr) {
            file = file.trim();
            BufferedReader in = null;
            FileReader fr = null;
            try {
                LOGGER.debug(String.format("read property file: %s", file));
                // extra not use prop.load(input) - load() not read the
                // properties in the original order
                fr = new FileReader(file);
                in = new BufferedReader(fr);
                String line;
                while ((line = in.readLine()) != null) {
                    line = line.trim();
                    if (!SOSString.isEmpty(line)) {
                        Properties p = new Properties();
                        StringReader s = null;
                        try {
                            s = new StringReader(line);
                            p.load(s);
                        } catch (Exception e) {
                            LOGGER.warn(String.format("can't load property from line [%s]: %s", line, e.toString()));
                        } finally {
                            if (s != null) {
                                try {
                                    s.close();
                                } catch (Exception ex) {
                                }
                            }
                        }

                        Optional<Entry<Object, Object>> o = p.entrySet().stream().findFirst();
                        if (o.isPresent()) {
                            String key = (String) o.get().getKey();
                            String value = (String) o.get().getValue();
                            LOGGER.debug(String.format("set system property: %s = %s", key, value));
                            System.setProperty(key, value);
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.warn(String.format("error on read property file [%s]: %s", file, e.toString()));
            } finally {
                if (fr != null) {
                    try {
                        fr.close();
                    } catch (Exception ex) {
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception ex) {
                    }
                }
            }
        }
    }

    private void setTextProperties() {
        SOSTransferStateCounts counts = getTransferCounts();
        objOptions.getTextProperties().put(KEYWORD_SUCCESSFUL_TRANSFERS, String.valueOf(counts.getSuccessTransfers()));
        objOptions.getTextProperties().put(KEYWORD_FAILED_TRANSFERS, String.valueOf(counts.getFailedTranfers()));
        objOptions.getTextProperties().put(KEYWORD_SKIPPED_TRANSFERS, String.valueOf(counts.getSkippedTransfers() + counts.getZeroBytesTransfers()));
        if (JobSchedulerException.LastErrorMessage.length() <= 0) {
            objOptions.getTextProperties().put(KEYWORD_STATUS, SOSJADE_T_0012.get());
        } else {
            objOptions.getTextProperties().put(KEYWORD_STATUS, SOSJADE_T_0013.get());
        }
        objOptions.getTextProperties().put(KEYWORD_LAST_ERROR, JobSchedulerException.LastErrorMessage);
    }

    private long getSuccessfulTransfers() {
        long count = 0;
        if (sourceFileList != null) {
            count = sourceFileList.getSuccessfulTransfers();
            if (countPollingServerFiles > 0) {
                count = countPollingServerFiles;
            }
        }
        return count;
    }

    private SOSTransferStateCounts getTransferCounts() {
        SOSTransferStateCounts counts = new SOSTransferStateCounts();
        if (sourceFileList != null) {
            counts = sourceFileList.countTransfers();
            if (countPollingServerFiles > 0) {
                counts.setSuccessTransfers(countPollingServerFiles);
            }
        }
        return counts;
    }

    public ISOSVfsFileTransfer getTargetClient() {
        return targetClient;
    }

    public ISOSVfsFileTransfer getSourceClient() {
        return sourceClient;
    }

    /** Send files by .... from source to a target
     *
     * @return boolean
     * @throws Exception */
    public boolean transfer() throws Exception {
        boolean rc = false;
        Exception exception = null;
        try {
            getOptions().checkMandatory();
            LOGGER.debug(getOptions().dirtyString());
            LOGGER.debug("Source : " + getOptions().getSource().dirtyString());
            LOGGER.debug("Target : " + getOptions().getTarget().dirtyString());
            setSystemProperties();
            setTextProperties();
            sourceFileList = new SOSFileList(targetHandler);
            sourceFileList.setOptions(objOptions);
            sourceFileList.startTransaction();
            factory = new SOSVfsConnectionFactory(objOptions);
            factory.createConnectionPool();
            if (objOptions.lazyConnectionMode.isFalse() && objOptions.isNeedTargetClient()) {
                targetClient = (ISOSVfsFileTransfer) factory.getTargetPool().getUnused();
                sourceFileList.objDataTargetClient = targetClient;
                makeDirs();
            }
            try {
                sourceClient = (ISOSVfsFileTransfer) factory.getSourcePool().getUnused();
                sourceFileList.objDataSourceClient = sourceClient;
                String sourceDir = objOptions.sourceDir.getValue();
                String targetDir = objOptions.targetDir.getValue();
                long startPollingServer = System.currentTimeMillis() / CONST1000;
                countPollingServerFiles = 0;
                executePreTransferCommands();
                if (dbSession != null) {
                    // if transfer was restarted with a reduced fileList
                    if (filePathRestriction != null) {
                        LOGGER.info("*** transfer was restarted with a reduced fileList");
                        LOGGER.info("*** with the filePathRestriction: " + filePathRestriction);
                        if (objOptions.fileListName.isNotEmpty()) {
                            objOptions.fileListName.setNull();
                        }
                        objOptions.filePath.setValue(filePathRestriction);
                    }
                }
                PollingServerLoop: while (true) {
                    if (objOptions.isFilePollingEnabled()) {
                        doPollingForFiles();
                        if (sourceFileList.isEmpty() && objOptions.pollingServer.isFalse() && objOptions.pollErrorState.isDirty()) {
                            String pollErrorState = objOptions.pollErrorState.getValue();
                            LOGGER.info("set order-state to " + pollErrorState);
                            setNextNodeState(pollErrorState);
                            break PollingServerLoop;
                        }
                    } else {
                        if (objOptions.oneOrMoreSingleFilesSpecified()) {
                            oneOrMoreSingleFilesSpecified(sourceDir);
                        } else {
                            ISOSVirtualFile fileHandle = sourceClient.getFileHandle(sourceDir);
                            String msg = "";
                            if (objOptions.isNeedTargetClient()) {
                                msg = "source directory/file: " + sourceDir + ", target directory: " + targetDir + ", file regexp: "
                                        + objOptions.fileSpec.getValue();
                            } else {
                                msg = SOSJADE_D_0200.params(sourceDir, objOptions.fileSpec.getValue());
                            }
                            LOGGER.debug(msg);
                            String integrityHashFileExtention = objOptions.checkIntegrityHash.isTrue() ? "." + objOptions.integrityHashType.getValue()
                                    : null;
                            selectFilesOnSource(fileHandle, objOptions.sourceDir, objOptions.fileSpec, objOptions.recursive,
                                    integrityHashFileExtention);
                        }
                    }
                    if (!checkSteadyStateOfFiles()) {
                        break PollingServerLoop;
                    }
                    sourceFileList.handleZeroByteFiles();
                    try {
                        if (objOptions.operation.isOperationGetList()) {
                            String msg = SOSJADE_I_0115.get();
                            LOGGER.info(msg);
                            JADE_REPORT_LOGGER.info(msg);
                            objOptions.removeFiles.setFalse();
                            objOptions.forceFiles.setFalse();
                            sourceFileList.logFileList();
                            sourceFileList.createResultSetFile();
                        } else {
                            sourceFileList.createResultSetFile();
                            if (!sourceFileList.isEmpty() && objOptions.skipTransfer.isFalse()) {
                                if (objOptions.lazyConnectionMode.isTrue()) {
                                    targetClient = (ISOSVfsFileTransfer) factory.getTargetPool().getUnused();
                                    sourceFileList.objDataTargetClient = targetClient;
                                    makeDirs();
                                }
                                if (dbSession != null) {
                                    dbHelper.updateTransfersNumOfFiles(dbSession, sourceFileList.size());
                                    if (transferId != null) {
                                        dbHelper.storeInitialFilesInformationToDB(transferId, dbSession, sourceFileList);
                                    }
                                }
                                sendFiles(sourceFileList);
                                if (dbSession != null) {
                                    for (SOSFileListEntry entry : sourceFileList.getList()) {
                                        dbHelper.updateFileInformationToDB(dbSession, entry, true, null);
                                    }
                                    dbHelper.updateTransfersNumOfFiles(dbSession, sourceFileList.count());
                                }
                                sourceFileList.renameTargetAndSourceFiles();
                                executePostTransferCommands();
                                sourceFileList.deleteSourceFiles();
                                sourceFileList.endTransaction();
                            }
                        }
                        if (objOptions.pollingServer.isFalse() || objOptions.skipTransfer.isTrue()) {
                            if (objOptions.isNeedTargetClient()) {
                                targetClient.close();
                            }
                            sourceClient.close();
                            break PollingServerLoop;
                        } else if (objOptions.pollingServerDuration.isDirty() && objOptions.pollingServerPollForever.isFalse()) {
                            long currentTime = System.currentTimeMillis() / CONST1000;
                            long duration = currentTime - startPollingServer;
                            if (duration >= objOptions.pollingServerDuration.getTimeAsSeconds()) {
                                LOGGER.debug("PollingServerMode: time elapsed. terminate polling server");
                                break PollingServerLoop;
                            }
                            LOGGER.debug("PollingServerMode: start next polling cycle");
                            countPollingServerFiles += sourceFileList.size();
                            sourceFileList.clearFileList();
                        }
                    } catch (JobSchedulerException e) {
                        String msg = null;
                        if (objOptions.transactional.value()) {
                            msg = TRANSACTION_ABORTED.get(e);
                        } else {
                            msg = TRANSFER_ABORTED.get(e);
                        }
                        if (dbSession != null) {
                            for (SOSFileListEntry entry : sourceFileList.getList()) {
                                dbHelper.updateFileInformationToDB(dbSession, entry, true, null);
                            }
                            dbHelper.updateTransfersNumOfFiles(dbSession, sourceFileList.count());
                        }
                        LOGGER.error(msg);
                        JADE_REPORT_LOGGER.error(msg);
                        sourceFileList.rollback();
                        if (dbSession != null) {
                            for (SOSFileListEntry entry : sourceFileList.getList()) {
                                dbHelper.updateFileInformationToDB(dbSession, entry, true, null);
                            }
                            dbHelper.updateTransfersNumOfFiles(dbSession, sourceFileList.count());
                        }
                        throw e;
                    }
                }
                rc = printState(rc);
                return rc;
            } catch (Exception e) {
                if (dbSession != null) {
                    for (SOSFileListEntry entry : sourceFileList.getList()) {
                        if (dbSession != null) {
                            dbHelper.updateFileInformationToDB(dbSession, entry, true, null);
                        }
                    }
                }
                rc = false;
                throw e;
            } finally {
                setTextProperties();
            }
        } catch (Exception e) {
            setTextProperties();
            objOptions.getTextProperties().put(KEYWORD_STATUS, SOSJADE_T_0013.get());
            JADE_REPORT_LOGGER.info(SOSJADE_E_0101.params(e.getMessage()), e);
            try {
                executePostTransferCommandsOnError(e);
            } catch (Exception ex) {
                LOGGER.error(ex.toString());
            }
            exception = e;
            throw e;
        } finally {
            try {
                executePostTransferCommandsFinal(exception);
            } catch (Exception e) {
                LOGGER.error(e.toString());
            }
        }
    }

    private void executePreTransferCommands() throws Exception {
        SOSConnection2OptionsAlternate target = objOptions.getTarget();
        if (target.alternateOptionsUsed.isTrue()) {
            target = target.getAlternatives();
            executeTransferCommands("alternative_target_pre_transfer_commands", targetClient, target.preTransferCommands.getValue(),
                    target.commandDelimiter.getValue());
        } else {
            executeTransferCommands("pre_transfer_commands", targetClient, objOptions.preTransferCommands.getValue(), target.commandDelimiter
                    .getValue());
            executeTransferCommands("target_pre_transfer_commands", targetClient, target.preTransferCommands.getValue(), target.commandDelimiter
                    .getValue());
        }
        SOSConnection2OptionsAlternate source = objOptions.getSource();
        String caller = "source_pre_transfer_commands";
        if (source.alternateOptionsUsed.isTrue()) {
            source = source.getAlternatives();
            caller = "alternative_" + caller;
        }
        executeTransferCommands(caller, sourceClient, source.preTransferCommands.getValue(), source.commandDelimiter.getValue());
        if (objOptions.isNeedTargetClient()) {
            targetClient.reconnect(target);
        }
    }

    private void executePostTransferCommands() throws Exception {
        SOSConnection2OptionsAlternate target = objOptions.getTarget();
        if (target.alternateOptionsUsed.isTrue()) {
            target = target.getAlternatives();
            executeTransferCommands("alternative_target_post_transfer_commands", targetClient, target.postTransferCommands.getValue(),
                    target.commandDelimiter.getValue());
        } else {
            executeTransferCommands("post_transfer_commands", targetClient, objOptions.postTransferCommands.getValue(), target.commandDelimiter
                    .getValue());
            executeTransferCommands("target_post_transfer_commands", targetClient, target.postTransferCommands.getValue(), target.commandDelimiter
                    .getValue());
        }
        SOSConnection2OptionsAlternate source = objOptions.getSource();
        String caller = "source_post_transfer_commands";
        if (source.alternateOptionsUsed.isTrue()) {
            source = source.getAlternatives();
            caller = "alternative_" + caller;
        }
        if (!SOSString.isEmpty(source.postTransferCommands.getValue())) {
            if (sourceClient == null) {
                throw new Exception("sourceClient is NULL");
            }
            sourceClient.reconnect(source);
            executeTransferCommands(caller, sourceClient, source.postTransferCommands.getValue(), source.commandDelimiter.getValue());
        }
    }

    private void executePostTransferCommandsOnError(Exception e) throws Exception {
        StringBuilder exception = new StringBuilder();
        String caller = "";
        if (!(e instanceof SOSYadeTargetConnectionException)) {
            SOSConnection2OptionsAlternate target = objOptions.getTarget();
            caller = "target_post_transfer_commands_on_error";
            if (target.alternateOptionsUsed.isTrue()) {
                target = target.getAlternatives();
                caller = "alternative_" + caller;
            }
            try {
                executeTransferCommands(caller, targetClient, target.postTransferCommandsOnError.getValue(), target.commandDelimiter.getValue());
            } catch (Exception ex) {
                exception.append(String.format("[%s]:%s", caller, ex.toString()));
            }
        }
        if (!(e instanceof SOSYadeSourceConnectionException)) {
            SOSConnection2OptionsAlternate source = objOptions.getSource();
            caller = "source_post_transfer_commands_on_error";
            if (source.alternateOptionsUsed.isTrue()) {
                source = source.getAlternatives();
                caller = "alternative_" + caller;
            }
            if (!SOSString.isEmpty(source.postTransferCommandsOnError.getValue())) {
                try {
                    // with JADE4DMZ it could be that the
                    // target.PostTransferCommands
                    // needs more time than the source connection is still
                    // established
                    if (sourceClient == null) {
                        throw new Exception("sourceClient is NULL");
                    }
                    sourceClient.reconnect(source);
                    executeTransferCommands(caller, sourceClient, source.postTransferCommandsOnError.getValue(), source.commandDelimiter.getValue());
                } catch (Exception ex) {
                    if (exception.length() > 0) {
                        exception.append(", ");
                    }
                    exception.append(String.format("[%s]:%s", caller, ex.toString()));
                }
            }
        }
        if (exception.length() > 0) {
            throw new Exception(exception.toString());
        }
    }

    private void executePostTransferCommandsFinal(Exception e) throws Exception {
        StringBuilder exception = new StringBuilder();
        String caller = "";
        if (e == null || !(e instanceof SOSYadeTargetConnectionException)) {
            SOSConnection2OptionsAlternate target = objOptions.getTarget();
            caller = "target_post_transfer_commands_final";
            if (target.alternateOptionsUsed.isTrue()) {
                target = target.getAlternatives();
                caller = "alternative_" + caller;
            }
            try {
                executeTransferCommands(caller, targetClient, target.postTransferCommandsFinal.getValue(), target.commandDelimiter.getValue());
            } catch (Exception ex) {
                exception.append(String.format("[%s]:%s", caller, ex.toString()));
            }
        }

        if (e == null || !(e instanceof SOSYadeSourceConnectionException)) {
            SOSConnection2OptionsAlternate source = objOptions.getSource();
            caller = "source_post_transfer_commands_final";
            if (source.alternateOptionsUsed.isTrue()) {
                source = source.getAlternatives();
                caller = "alternative_" + caller;
            }
            if (!SOSString.isEmpty(source.postTransferCommandsFinal.getValue())) {
                try {
                    // with JADE4DMZ it could be that the
                    // target.PostTransferCommands
                    // needs more time than the source connection is still
                    // established
                    if (sourceClient == null) {
                        throw new Exception("objDataSourceClient is NULL");
                    }
                    sourceClient.reconnect(source);
                    executeTransferCommands(caller, sourceClient, source.postTransferCommandsFinal.getValue(), source.commandDelimiter.getValue());
                } catch (Exception ex) {
                    if (exception.length() > 0) {
                        exception.append(", ");
                    }
                    exception.append(String.format("[%s]:%s", caller, ex.toString()));
                }
            }
        }
        if (exception.length() > 0) {
            throw new Exception(exception.toString());
        }
    }

    protected void executeTransferCommands(String commandOptionName, final ISOSVfsFileTransfer fileTransfer, final String commands,
            final String delimiter) throws Exception {
        if (commands != null && commands.trim().length() > 0) {
            if (SOSString.isEmpty(delimiter)) {
                LOGGER.info(String.format("[%s]%s", commandOptionName, commands.trim()));
                fileTransfer.getHandler().executeCommand(commands);
            } else {
                String[] values = commands.split(delimiter);
                if (values.length > 1) {
                    LOGGER.debug(String.format("[%s]commands=%s", commandOptionName, commands.trim()));
                }
                for (String command : values) {
                    if (!SOSString.isEmpty(command.trim())) {
                        LOGGER.info(String.format("[%s]%s", commandOptionName, command.trim()));
                        fileTransfer.getHandler().executeCommand(command);
                    }
                }
            }
        }
    }

    private long selectFilesOnSource(final ISOSVirtualFile localeFile, final SOSOptionFolderName sourceDir, final SOSOptionRegExp regExp,
            final SOSOptionBoolean recursive, final String integrityHashFileExtention) throws Exception {
        if (sourceClient instanceof SOSVfsHTTP) {
            throw new JobSchedulerException("a file spec selection is not supported with http(s) protocol");
        }
        if (localeFile.isDirectory()) {
            if (sourceClient instanceof ISOSVfsFileTransfer2) {
                ISOSVfsFileTransfer2 ft = (ISOSVfsFileTransfer2) sourceClient;
                ft.clearFileListEntries();
                sourceFileList = ft.getFileListEntries(sourceFileList, sourceDir.getValue(), regExp.getValue(), recursive.value());
            } else {
                String[] fileList = sourceClient.getFilelist(sourceDir.getValue(), regExp.getValue(), 0, recursive.value(),
                        integrityHashFileExtention);
                fillFileList(fileList, sourceDir.getValue());
            }
            setInfo(String.format("%1$d files found for regexp '%2$s'.", sourceFileList.size(), regExp.getValue()));
        }
        return sourceFileList.size();
    }

    public void setDBFactory(SOSHibernateFactory factory) {
        this.dbFactory = factory;
    }

    private SOSHibernateSession initStatelessSession() {
        SOSHibernateSession dbSession = null;
        try {
            dbSession = dbFactory.openStatelessSession("YadeJob");
        } catch (SOSHibernateOpenSessionException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return dbSession;
    }

    public void setJobSchedulerEventHandler(IJobSchedulerEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getParentTransferId() {
        return parentTransferId;
    }

    public void setParentTransferId(Long parentTransferId) {
        this.parentTransferId = parentTransferId;
    }

    public void setIsIntervention(boolean isIntervention) {
        this.isIntervention = isIntervention;
    }

    public void setFilePathRestriction(String filePathRestriction) {
        this.filePathRestriction = filePathRestriction;
    }

}
