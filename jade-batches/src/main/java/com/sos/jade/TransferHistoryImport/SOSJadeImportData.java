package com.sos.jade.TransferHistoryImport;

import java.util.Date;
import java.util.HashMap;

import com.sos.VirtualFileSystem.Interfaces.IJadeTransferHistoryData;

/** \class JadeTransferHistoryExportDataTest
 * 
 * \brief JadeTransferHistoryExportDataTest -
 * 
 * \details
 *
 * \section JadeTransferHistoryExportDataTest.java_intro_sec Introduction
 *
 * \section JadeTransferHistoryExportDataTest.java_samples Some Samples
 *
 * \code .... code goes here ... \endcode
 *
 * <p style="text-align:center">
 * <br />
 * --------------------------------------------------------------------------- <br />
 * APL/Software GmbH - Berlin <br />
 * ##### generated by ClaviusXPress (http://www.sos-berlin.com) ######### <br />
 * ---------------------------------------------------------------------------
 * </p>
 * \author Uwe Risse \version 20.10.2011 \see reference
 *
 * Created on 20.10.2011 11:55:47 */

public class SOSJadeImportData implements IJadeTransferHistoryData {

    @SuppressWarnings("unused")
    private final String conClassName = "JadeTransferHistoryExportDataTest";
    private HashMap data = null;

    public SOSJadeImportData() {
        //
    }

    public void setData(HashMap h) {
        this.data = h;
    }

    @Override
    public Integer getTransferId() {
        return Integer.parseInt(this.data.get("transfer_id").toString());
    }

    @Override
    public String getMandator() {
        return this.data.get("mandator").toString();
    }

    @Override
    public Long getFileSize() {
        return Long.parseLong(this.data.get("transfer_id").toString());
    }

    @Override
    public String getSourceHost() {
        return this.data.get("source_host").toString();
    }

    @Override
    public String getSourceHostIp() {
        return this.data.get("source_host_ip").toString();
    }

    @Override
    public String getSourceUser() {
        return this.data.get("source_user").toString();
    }

    @Override
    public String getSourceDir() {
        return this.data.get("source_dir").toString();
    }

    @Override
    public String getTargetHost() {
        return this.data.get("target_dir").toString();
    }

    @Override
    public String getTargetHostIp() {
        return this.data.get("target_host_ip").toString();
    }

    @Override
    public String getTargetUser() {
        return this.data.get("target_user").toString();
    }

    @Override
    public String getTargetDir() {
        return this.data.get("target_dir").toString();
    }

    @Override
    public Integer getProtocolType() {
        return Integer.parseInt(this.data.get("protocol_type").toString());
    }

    @Override
    public Integer getPort() {
        return Integer.parseInt(this.data.get("port").toString());
    }

    @Override
    public Integer getFilesCount() {
        return Integer.parseInt(this.data.get("files_count").toString());
    }

    @Override
    public String getProfileName() {
        return this.data.get("profile_name").toString();
    }

    @Override
    public String getProfile() {
        return this.data.get("profile").toString();
    }

    @Override
    public Integer getCommandType() {
        return Integer.parseInt(this.data.get("command_type").toString());
    }

    @Override
    public String getCommand() {
        return this.data.get("command").toString();
    }

    @Override
    public String getLog() {
        return this.data.get("log").toString();
    }

    @Override
    public Integer getStatus() {
        return Integer.parseInt(this.data.get("status").toString());
    }

    @Override
    public String getLastErrorMessage() {
        return this.data.get("last_error_message").toString();
    }

    @Override
    public Date getStartTime() {
        return new Date();
    }

    @Override
    public Date getEndTime() {
        return new Date();
    }

    @Override
    public String getStatusText() {
        return this.data.get("status_text").toString();
    }

    @Override
    public String getModifiedBy() {
        return this.data.get("modified_by").toString();
    }

    @Override
    public String getCreatedBy() {
        return this.data.get("created_by").toString();
    }

    @Override
    public Date getCreated() {
        return new Date();
    }

    @Override
    public Date getModified() {
        return new Date();
    }

}
