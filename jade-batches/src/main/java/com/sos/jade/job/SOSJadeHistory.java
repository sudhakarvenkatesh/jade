package com.sos.jade.job;

import java.io.File;

import org.apache.log4j.Logger;

import com.sos.JSHelper.Basics.JSJobUtilitiesClass;
import com.sos.jade.TransferHistoryImport.SOSJadeDetailImportData;
import com.sos.jade.TransferHistoryImport.SOSJadeImport;
import com.sos.jade.TransferHistoryImport.SOSJadeImportData;

/** @author Uwe Risse */
public class SOSJadeHistory extends JSJobUtilitiesClass<SOSJadeHistoryOptions> {

    private static final Logger LOGGER = Logger.getLogger(SOSJadeHistory.class);

    public SOSJadeHistory() {
        super(new SOSJadeHistoryOptions());
    }

    @Override
    public SOSJadeHistoryOptions getOptions() {
        if (objOptions == null) {
            objOptions = new SOSJadeHistoryOptions();
        }
        return objOptions;
    }

    public SOSJadeHistory Execute() throws Exception {
        final String methodName = "SOSJadeHistory::Execute";
        SOSJadeImport sosJadeImport = null;
        LOGGER.debug(String.format(Messages.getMsg("JSJ-I-110"), methodName));
        try {
            getOptions().checkMandatory();
            LOGGER.debug(getOptions().toString());
            sosJadeImport = new SOSJadeImport(new File(objOptions.getconfiguration_file().getValue()));
            SOSJadeImportData sosJadeImportData = new SOSJadeImportData();
            sosJadeImportData.setData(getOptions().settings());
            SOSJadeDetailImportData sosJadeDetailImportData = new SOSJadeDetailImportData();
            sosJadeDetailImportData.setData(getOptions().settings());
            sosJadeImport.setJadeTransferData(sosJadeImportData);
            sosJadeImport.setJadeTransferDetailData(sosJadeDetailImportData);
            sosJadeImport.doTransferSummary();
            sosJadeImport.doTransferDetail();
        } catch (Exception e) {
            LOGGER.error(String.format(Messages.getMsg("JSJ-I-107"), methodName), e);
        } finally {
            LOGGER.debug(String.format(Messages.getMsg("JSJ-I-111"), methodName));
        }
        return this;
    }

    public void init() {
        doInitialize();
    }

    private void doInitialize() {
        // doInitialize
    }

}