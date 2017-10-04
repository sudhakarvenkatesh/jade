package com.sos.DataExchange.helpers;

import java.net.URL;
import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.DataExchange.SOSDataExchangeEngine;
import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.JSHelper.Options.SOSOptionJadeOperation;
import com.sos.JSHelper.Options.SOSOptionTransferType;
import com.sos.VirtualFileSystem.DataElements.SOSFileList;
import com.sos.VirtualFileSystem.DataElements.SOSFileListEntry;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jade.db.DBItemYadeFiles;
import com.sos.jade.db.DBItemYadeProtocols;
import com.sos.jade.db.DBItemYadeTransfers;
import com.sos.jade.db.YadeDBLayer;


public class YadeDBOperationHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(YadeDBOperationHelper.class);
    private String currentNodeName;
    private String currentJobChain;
    private String currentJob;
    private String currentOrderId;
    private String currentJobschedulerId;
    private DBItemYadeProtocols sourceProtocolDBItem;
    private DBItemYadeProtocols targetProtocolDBItem;
    private DBItemYadeProtocols jumpProtocolDBItem;
    private DBItemYadeTransfers transferDBItem;
    private SOSDataExchangeEngine yadeEngine;
    private Long parentTransferId = null;
    private Long taskId = null;
    private Boolean hasErrors = null; 
    
    public YadeDBOperationHelper(SOSDataExchangeEngine yadeEngine) {
        this.yadeEngine = yadeEngine;
        addAdditionalJobInfosFromOptions();
    }

    public Long storeTransferInformationToDB(SOSHibernateSession dbSession) {
        Long transferId = null;
        YadeDBLayer dbLayer = new YadeDBLayer(dbSession);
        try {
            if (sourceProtocolDBItem == null && yadeEngine.getOptions().sourceDir.isDirty()) {
                dbSession.beginTransaction();
                sourceProtocolDBItem = new DBItemYadeProtocols();
                sourceProtocolDBItem.setHostname(yadeEngine.getOptions().getSource().host.getValue());
                sourceProtocolDBItem.setPort(yadeEngine.getOptions().getSource().getPort().value());
                Integer sourceProtocol = getProtocolFromTransferType(yadeEngine.getOptions().getSource().getProtocol().getEnum());
                if (sourceProtocol == 7) {
                    URL url = yadeEngine.getOptions().getSource().getUrl().getUrl();
                    if (url.getProtocol().toLowerCase().equals("webdavs")) {
                        sourceProtocolDBItem.setProtocol(8);
                    } else {
                        sourceProtocolDBItem.setProtocol(sourceProtocol);
                    }
                } else {
                    sourceProtocolDBItem.setProtocol(sourceProtocol);
                }
                sourceProtocolDBItem.setAccount(yadeEngine.getOptions().getSource().user.getValue());
                LOGGER.debug("source Host = " + yadeEngine.getOptions().getSource().host.getValue());
                LOGGER.debug("source port = " + yadeEngine.getOptions().getSource().getPort().value());
                LOGGER.debug("source protocol = " + yadeEngine.getOptions().getSource().getProtocol().getEnum().getText());
                LOGGER.debug("source account = " + yadeEngine.getOptions().getSource().user.getValue());
                DBItemYadeProtocols sourceProtocolFromDb = null;
                try {
                    sourceProtocolFromDb = dbLayer.getProtocolFromDb(sourceProtocolDBItem.getHostname(),
                            sourceProtocolDBItem.getPort(), sourceProtocolDBItem.getProtocol());
                } catch (SOSHibernateException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                Long sourceProtocolId = null;
                if (sourceProtocolFromDb != null) {
                    sourceProtocolId = sourceProtocolFromDb.getId();
                    sourceProtocolDBItem = sourceProtocolFromDb;
                } else {
                    try {
                        dbLayer.getSession().save(sourceProtocolDBItem);
                        sourceProtocolId = sourceProtocolDBItem.getId();
                        LOGGER.debug("source protocol id = " + sourceProtocolId);
                    } catch (SOSHibernateException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
                dbSession.commit();
            }
            
            if (targetProtocolDBItem == null && yadeEngine.getOptions().targetDir.isDirty()) {
                dbSession.beginTransaction();
                targetProtocolDBItem = new DBItemYadeProtocols();
                targetProtocolDBItem.setHostname(yadeEngine.getOptions().getTarget().host.getValue());
                targetProtocolDBItem.setPort(yadeEngine.getOptions().getTarget().getPort().value());
                Integer targetProtocol = getProtocolFromTransferType(yadeEngine.getOptions().getTarget().getProtocol().getEnum());
                if (targetProtocol == 7) {
                    URL url = yadeEngine.getOptions().getTarget().getUrl().getUrl();
                    if (url.getProtocol().toLowerCase().equals("https")) {
                        targetProtocolDBItem.setProtocol(8);
                    } else {
                        targetProtocolDBItem.setProtocol(targetProtocol);
                    }
                } else {
                    targetProtocolDBItem.setProtocol(targetProtocol);
                }
                targetProtocolDBItem.setAccount(yadeEngine.getOptions().getTarget().user.getValue());
                LOGGER.debug("target Host = " + yadeEngine.getOptions().getTarget().host.getValue());
                LOGGER.debug("target port = " + yadeEngine.getOptions().getTarget().getPort().value());
                LOGGER.debug("target protocol = " + yadeEngine.getOptions().getTarget().getProtocol().getEnum().getText());
                LOGGER.debug("target account = " + yadeEngine.getOptions().getTarget().user.getValue());
                DBItemYadeProtocols targetProtocolFromDb = null;
                try {
                    targetProtocolFromDb = dbLayer.getProtocolFromDb(targetProtocolDBItem.getHostname(),
                            targetProtocolDBItem.getPort(), targetProtocolDBItem.getProtocol());
                } catch (SOSHibernateException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                Long targetProtocolId = null;
                if (targetProtocolFromDb != null) {
                    targetProtocolId = targetProtocolFromDb.getId();
                    targetProtocolDBItem = targetProtocolFromDb;
                } else {
                    try {
                        dbLayer.getSession().save(targetProtocolDBItem);
                        targetProtocolId = targetProtocolDBItem.getId();
                        LOGGER.debug("target protocol id = " + targetProtocolId);
                    } catch (SOSHibernateException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
                dbSession.commit();
            }
            
            if (jumpProtocolDBItem == null && yadeEngine.getOptions().getJumpHost().isDirty()) { 
                dbSession.beginTransaction();
                jumpProtocolDBItem = new DBItemYadeProtocols();
                jumpProtocolDBItem.setHostname(yadeEngine.getOptions().getJumpHost().getValue());
                jumpProtocolDBItem.setPort(yadeEngine.getOptions().getJumpPort().value());
                jumpProtocolDBItem.setProtocol(getProtocolFromString(yadeEngine.getOptions().getJumpProtocol().getValue()));
                jumpProtocolDBItem.setAccount(yadeEngine.getOptions().getJumpUser().getValue());
                LOGGER.debug("jump Host = " + yadeEngine.getOptions().getJumpHost().getValue());
                LOGGER.debug("jump port = " + yadeEngine.getOptions().getJumpPort().value());
                LOGGER.debug("jump protocol = " + yadeEngine.getOptions().getJumpProtocol().getValue());
                LOGGER.debug("jump account = " + yadeEngine.getOptions().getJumpUser().getValue());
                DBItemYadeProtocols jumpProtocolFromDb = null;
                try {
                    jumpProtocolFromDb = dbLayer.getProtocolFromDb(jumpProtocolDBItem.getHostname(),
                            jumpProtocolDBItem.getPort(), jumpProtocolDBItem.getProtocol());
                } catch (SOSHibernateException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                Long jumpProtocolId = null;
                if (jumpProtocolFromDb != null) {
                    jumpProtocolId = jumpProtocolFromDb.getId();
                    jumpProtocolDBItem = jumpProtocolFromDb;
                } else {
                    try {
                        dbLayer.getSession().save(jumpProtocolDBItem);
                        jumpProtocolId = jumpProtocolDBItem.getId();
                        LOGGER.debug("jump protocol id = " + jumpProtocolId);
                    } catch (SOSHibernateException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
                dbSession.commit();
            }

            dbSession.beginTransaction();
            DBItemYadeTransfers transferFromDb = null;
            try {
                if (transferDBItem != null && transferDBItem.getId() != null) {
                    transferFromDb = dbLayer.getTransferFromDb(transferDBItem.getId());
                }
                if (transferFromDb != null) {
                    LOGGER.debug(String.format("transfer item with id = %1$s found in DB!", transferDBItem.getId()));
                }
            } catch (SOSHibernateException e) {
                LOGGER.error(e.getMessage(), e);
            }
            Date now = Date.from(Instant.now());
            if(transferFromDb != null) {
                transferId = transferFromDb.getId();
                transferFromDb.setEnd(now);
                if (hasErrors == null) {
                    transferFromDb.setState(2);
                    transferFromDb.setErrorCode(null);
                    transferFromDb.setErrorMessage(null);
                } else if (!hasErrors) {
                    transferFromDb.setState(1);
                    transferFromDb.setErrorCode(null);
                    transferFromDb.setErrorMessage(null);
                } else {
                    transferFromDb.setState(3);
                    transferFromDb.setErrorCode(null);
                    transferFromDb.setErrorMessage(JobSchedulerException.LastErrorMessage);
                }
                transferFromDb.setTaskId(taskId);
                transferFromDb.setModified(now);
                try {
                    dbLayer.getSession().update(transferFromDb);
                    LOGGER.debug("transfer DB Item updated!");
                } catch (SOSHibernateException e) {
                    LOGGER.error("error occurred trying to update transfer DB Item!");
                    LOGGER.error(e.getMessage(), e);
                }
                transferDBItem = transferFromDb;
            } else {
                LOGGER.debug("processing new transfer data!");
                DBItemYadeTransfers newTransfer = new DBItemYadeTransfers();
                if (sourceProtocolDBItem != null) {
                    newTransfer.setSourceProtocolId(sourceProtocolDBItem.getId());
                    LOGGER.debug("source protocol id = " + sourceProtocolDBItem.getId());
                }
                if (targetProtocolDBItem != null) {
                    newTransfer.setTargetProtocolId(targetProtocolDBItem.getId());
                    LOGGER.debug("target protocol id = " + targetProtocolDBItem.getId());
                }
                if (jumpProtocolDBItem != null) {
                    newTransfer.setJumpProtocolId(jumpProtocolDBItem.getId());
                    LOGGER.debug("jump protocol id = " + jumpProtocolDBItem.getId());
                }
                newTransfer.setMandator(yadeEngine.getOptions().getMandator().getValue());
                LOGGER.debug("mandator = " + newTransfer.getMandator());
                newTransfer.setOperation(getOperation(yadeEngine.getOptions().getOperation()));
                LOGGER.debug("operation = " + newTransfer.getOperation());
                newTransfer.setStart(Date.from(Instant.now()));
                newTransfer.setEnd(null);
                newTransfer.setState(2);
                LOGGER.debug("state = " + newTransfer.getState());
                newTransfer.setErrorCode(null);
                newTransfer.setErrorMessage(null);
                newTransfer.setJobschedulerId(currentJobschedulerId);
                LOGGER.debug("jobschedulerId = " + currentJobschedulerId);
                newTransfer.setJob(currentJob);
                LOGGER.debug("job = " + currentJob);
                newTransfer.setJobChain(currentJobChain);
                LOGGER.debug("job chain = " + currentJobChain);
                newTransfer.setJobChainNode(currentNodeName);
                LOGGER.debug("current job chain node = " + currentNodeName);
                newTransfer.setOrderId(currentOrderId);
                LOGGER.debug("orderId = " + currentOrderId);
                newTransfer.setTaskId(taskId);
                if (yadeEngine.getFileList() != null) {
                    newTransfer.setNumOfFiles(yadeEngine.getFileList().count());
                    LOGGER.debug("Num of Files = " + yadeEngine.getFileList().count());
                }
                if (yadeEngine.getOptions().getProfile() != null) {
                    newTransfer.setProfileName(yadeEngine.getOptions().getProfile().getValue());
                    LOGGER.debug("profile = " + yadeEngine.getOptions().getProfile().getValue());
                }
                newTransfer.setModified(now);
                dbLayer.getSession().save(newTransfer);
                transferId = newTransfer.getId();
                transferDBItem = newTransfer;
            }
            LOGGER.debug("transfer id = " + transferId);
            dbSession.commit();
        } catch (SOSHibernateException e) {
            LOGGER.error("trying db rollback");
            try {
                dbSession.rollback();
            } catch (SOSHibernateException e1) {}
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.debug("store transfer information finished!");
        return transferId;
    }
    
    private void storeInitialFilesInformationToDB(Long transferId, SOSHibernateSession dbSession, SOSFileList files) {
        if (files != null) {
            // TODO: implementation
            for (SOSFileListEntry fileEntry : files.getList()) {
                DBItemYadeFiles file = new DBItemYadeFiles();
                file.setTransferId(transferId);
                file.setSourcePath(fileEntry.getSourceFilename());
                file.setTargetPath(fileEntry.getTargetFileNameAndPath());
                file.setSize(fileEntry.getFileSize());
                file.setState(fileEntry.getStatus());
                String lastErrorMessage = fileEntry.getLastErrorMessage();
                if (lastErrorMessage != null && !lastErrorMessage.isEmpty()) {
                    file.setErrorCode("ERRORCODE");
                    file.setErrorMessage(lastErrorMessage);
                } else {
                    file.setErrorCode(null);
                    file.setErrorMessage(null);
                }
                file.setIntegrityHash(fileEntry.getMd5());
                file.setModificationDate(fileEntry.getModified());
                file.setModified(Date.from(Instant.now()));
                try {
                    dbSession.beginTransaction();
                    dbSession.save(file);
                    dbSession.commit();
                    LOGGER.debug("file saved: " + file.getSourcePath());
                } catch (SOSHibernateException e) {
                    LOGGER.error(e.getMessage(), e);
                    try {
                        dbSession.rollback();
                    } catch (SOSHibernateException e1) {
                    }
                }
            }
            LOGGER.debug("store transfer files information finished!");
        }
    }
    public void updateFileInformationToDB(SOSHibernateSession dbSession, SOSFileListEntry fileEntry) {
        updateFileInformationToDB(dbSession, fileEntry, false);
    }
    
    public void updateFileInformationToDB(SOSHibernateSession dbSession, SOSFileListEntry fileEntry, boolean finalUpdate) {
        YadeDBLayer dbLayer = new YadeDBLayer(dbSession);
        if (fileEntry != null) {
            DBItemYadeFiles fileFromDb = null;
            try {
                fileFromDb = dbLayer.getTransferFileFromDbByConstraint(transferDBItem.getId(), fileEntry.getSourceFilename());
            } catch (SOSHibernateException e) {
                LOGGER.error(e.getMessage(), e);
            }
            if (fileFromDb != null) {
                if (finalUpdate && fileEntry.getStatus() == 4) {
                    fileFromDb.setState(5);
                    hasErrors = false;
                } else {
                    hasErrors = getHasErrorsFromFileState(fileEntry.getStatus());
                    fileFromDb.setState(fileEntry.getStatus());
                }
                fileFromDb.setTargetPath(fileEntry.getTargetFileNameAndPath());
                String lastErrorMessage = fileEntry.getLastErrorMessage();
                if (lastErrorMessage != null && !lastErrorMessage.isEmpty()) {
                    fileFromDb.setErrorCode("ERRORCODE");
                    fileFromDb.setErrorMessage(lastErrorMessage);
                } else {
                    fileFromDb.setErrorCode(null);
                    fileFromDb.setErrorMessage(null);
                }
                fileFromDb.setIntegrityHash(fileEntry.getMd5());
                fileFromDb.setModificationDate(fileEntry.getModificationDate());
                fileFromDb.setModified(Date.from(Instant.now()));
                try {
                    dbSession.beginTransaction();
                    dbSession.update(fileFromDb);
                    dbSession.commit();
                    LOGGER.debug("file saved: " + fileFromDb.getSourcePath());
                } catch (SOSHibernateException e) {
                    LOGGER.error(e.getMessage(), e);
                    try {
                        dbSession.rollback();
                    } catch (SOSHibernateException e1) {
                    }
                }
            } else {
                DBItemYadeFiles file = new DBItemYadeFiles();
                file.setTransferId(transferDBItem.getId());
                file.setSourcePath(fileEntry.getSourceFilename());
                file.setTargetPath(fileEntry.getTargetFileNameAndPath());
                file.setSize(fileEntry.getFileSize());
                file.setState(fileEntry.getStatus());
                file.setModificationDate(fileEntry.getModified());
                String lastErrorMessage = fileEntry.getLastErrorMessage();
                if (lastErrorMessage != null && !lastErrorMessage.isEmpty()) {
                    file.setErrorCode("ERRORCODE");
                    file.setErrorMessage(lastErrorMessage);
                } else {
                    file.setErrorCode(null);
                    file.setErrorMessage(null);
                }
                file.setIntegrityHash(fileEntry.getMd5());
                file.setModificationDate(fileEntry.getModified());
                file.setModified(Date.from(Instant.now()));
                try {
                    dbSession.beginTransaction();
                    dbSession.save(file);
                    dbSession.commit();
                    LOGGER.debug("file saved: " + file.getSourcePath());
                } catch (SOSHibernateException e) {
                    LOGGER.error(e.getMessage(), e);
                    try {
                        dbSession.rollback();
                    } catch (SOSHibernateException e1) {
                    }
                }
            }
            LOGGER.debug("store transfer files information finished!");
        }
    }

    public void storeInitialTransferInformations(SOSHibernateSession dbSession) {
        if (dbSession != null) {
            Long transferId = storeTransferInformationToDB(dbSession);
            LOGGER.debug("initial transfer information stored to DB!");
            storeInitialFilesInformationToDB(transferDBItem.getId(), dbSession, yadeEngine.getFileList());
            LOGGER.debug("initial file informations stored to DB!");
        }
    }

    public void addAdditionalJobInfosFromOptions() {
        if (yadeEngine.getOptions().getJobSchedulerId() != null) {
            currentJobschedulerId = yadeEngine.getOptions().getJobSchedulerId();
        }
        if (yadeEngine.getOptions().getJobChain() != null) {
            currentJobChain = yadeEngine.getOptions().getJobChain();
        }
        if (yadeEngine.getOptions().getJob() != null) {
            currentJob = yadeEngine.getOptions().getJob();
        }
        if (yadeEngine.getOptions().getJobChainNodeName() != null) {
            currentNodeName = yadeEngine.getOptions().getJobChainNodeName();
        }
        if (yadeEngine.getOptions().getOrderId() != null) {
            currentOrderId = yadeEngine.getOptions().getOrderId();
        }
        if (yadeEngine.getOptions().getParentTransferId() != null) {
            parentTransferId = yadeEngine.getOptions().getParentTransferId();
        }
        if (yadeEngine.getOptions().getTaskId() != null) {
            String taskIdFromOptions = yadeEngine.getOptions().getTaskId();
            taskId = Long.parseLong(taskIdFromOptions);
        }
    }
    
    public void storeFailedTransfer(SOSHibernateSession dbSession, String errorMessage) throws SOSHibernateException {
        if (transferDBItem != null) {
            transferDBItem.setState(3);
            transferDBItem.setErrorMessage(errorMessage);
            transferDBItem.setEnd(Date.from(Instant.now()));
            transferDBItem.setModified(Date.from(Instant.now()));
            dbSession.beginTransaction();
            dbSession.update(transferDBItem);
            dbSession.commit();
        }        
    }
    
    public void updateTransfersNumOfFiles(SOSHibernateSession dbSession, Long numOfFiles) throws SOSHibernateException {
        if (transferDBItem != null) {
            transferDBItem.setNumOfFiles(numOfFiles);
            transferDBItem.setModified(Date.from(Instant.now()));
            dbSession.beginTransaction();
            dbSession.update(transferDBItem);
            dbSession.commit();
        }
    }
    
    private Integer getOperation(SOSOptionJadeOperation jadeOperation) {
        switch(jadeOperation.value()) {
        case copy:
            return 1;
        case move:
            return 2;
        case getlist:
            return 3;
        case rename:
            return 4;
        case copytointernet:
        case sendusingdmz:
            return 5;
        case copyfrominternet:
        case receiveusingdmz:
            return 6;
        default:
            return 0;    
        }
    }
    
    private Integer getProtocolFromTransferType(SOSOptionTransferType.enuTransferTypes transferType) {
        switch(transferType) {
        case local:
            return 1;
        case ftp:
            return 2;
        case ftps:
            return 3;
        case sftp:
            return 4;
        case http:
            return 5;
        case https:
            return 6;
        case webdav:
            return 7;
        case smb:
            return 9;
        default:
            return null;
        }
    }
    
    private Integer getProtocolFromString (String protocolName) {
        switch(protocolName.toLowerCase()) {
        case "local":
            return 1;
        case "ftp":
            return 2;
        case "ftps":
            return 3;
        case "sftp":
            return 4;
        case "http":
            return 5;
        case "https":
            return 6;
        case "webdav":
            return 7;
        case "webdavs":
            return 8;
        case "smb":
            return 9;
        default:
            return null;
        }
    }

    private Boolean getHasErrorsFromFileState(Integer fileEntryState) {
        switch(fileEntryState) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 8:
        case 9:
        case 11:
        case 12:
        case 13:
        case 14:
        case 15:
            return false;
        case 6:
        case 7:
        case 10:
            return true;
        }
        return null;
    }

    public DBItemYadeTransfers getTransferDBItem() {
        return transferDBItem;
    }

    public void setTransferDBItem(DBItemYadeTransfers transferDBItem) {
        this.transferDBItem = transferDBItem;
    }

    public SOSDataExchangeEngine getYadeEngine() {
        return yadeEngine;
    }

    public void setYadeEngine(SOSDataExchangeEngine yadeEngine) {
        this.yadeEngine = yadeEngine;
    }

}