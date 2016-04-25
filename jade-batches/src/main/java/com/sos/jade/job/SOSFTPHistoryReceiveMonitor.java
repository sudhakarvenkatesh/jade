package com.sos.jade.job;

import sos.connection.SOSConnection;
import sos.spooler.Monitor_impl;
import sos.spooler.Variable_set;
import sos.util.SOSClassUtil;
import sos.util.SOSSchedulerLogger;

public class SOSFTPHistoryReceiveMonitor extends Monitor_impl {

    public boolean spooler_process_before() {
        try {
            spooler_task.order().params().set_var("replacing", "[.]csv");
            spooler_task.order().params().set_var("replacement", "{sos[date:yyyyMMddHHmmssSSS]sos}.csv");
            return true;
        } catch (Exception e) {
            spooler_log.warn("error occurred in spooler_process_before(222): " + e.getMessage());
            return false;
        }
    }

    public boolean spooler_process_after(boolean arg0) throws Exception {
        if (!arg0) {
            return arg0;
        }
        SOSConnection conn = null;
        SOSSchedulerLogger log = null;
        Variable_set parameters = null;
        String host = null;
        String remoteDir = null;
        try {
            parameters = spooler.create_variable_set();
            if (spooler_task.params() != null) {
                parameters.merge(spooler_task.params());
            }
            if (spooler_job.order_queue() != null) {
                parameters.merge(spooler_task.order().params());
            }
            SOSFTPHistory.debugParams(parameters, spooler_log);
            if (parameters != null && parameters.count() > 0) {
                if ("0".equalsIgnoreCase(parameters.value("ftp_result_files"))) {
                    spooler_log.debug9("no files were received");
                } else {
                    host = parameters.value("ftp_host");
                    remoteDir = parameters.value("ftp_remote_dir");
                    if (host != null && !host.isEmpty() && remoteDir != null && !remoteDir.isEmpty()) {
                        try {
                            String[] files = parameters.value("ftp_result_filepaths").split(";");
                            log = new SOSSchedulerLogger(spooler_log);
                            conn = SOSFTPHistory.getConnection(spooler, conn, parameters, log);
                            for (int i = 0; i < files.length; i++) {
                                fillPosition(conn, host, remoteDir, files[i]);
                            }
                        } catch (Exception e) {
                            spooler_log.info("ERROR : cannot found position : " + e.getMessage());
                        } finally {
                            try {
                                if (conn != null) {
                                    conn.disconnect();
                                }
                            } catch (Exception e) {
                            }
                        }
                    } else {
                        spooler_log.debug3("missing host or ftp_remote_dir for file positions : host = " + host + " ftp_remote_dir = " + remoteDir);
                    }
                }
            }
        } catch (Exception e) {

        }
        return super.spooler_process_after(arg0);
    }

    private void fillPosition(SOSConnection conn, String host, String remoteDir, String file) throws Exception {
        StringBuilder sql = new StringBuilder();
        String remoteFilename = "";
        String localFilename = "";
        try {
            host = SOSFTPHistory.getNormalizedValue(host);
            remoteDir = SOSFTPHistory.getNormalizedValue(remoteDir);
            file = SOSFTPHistory.getNormalizedValue(file);
            String[] fp = file.split("/");
            localFilename = fp[fp.length - 1];
            remoteFilename = localFilename.toLowerCase().replaceAll("(\\{sos)(.)*(sos\\})(\\.csv)$", "\\.csv");
            sql = new StringBuilder();
            sql.append("select \"LOCAL_FILENAME\" from ").append(SOSFTPHistory.TABLE_FILES_POSITIONS).append(" ").append("where \"HOST\" = '")
                .append(SOSFTPHistory.getNormalizedField(conn, host, 128)).append("' and ").append("   \"REMOTE_DIR\" = '")
                .append(SOSFTPHistory.getNormalizedField(conn, remoteDir, 255)).append("' and ").append("   \"REMOTE_FILENAME\" = '")
                .append(SOSFTPHistory.getNormalizedField(conn, remoteFilename, 255)).append("'");
            String lastLocalFileName = conn.getSingleValue(sql.toString());
            if (lastLocalFileName == null || lastLocalFileName.isEmpty()) {
                sql = new StringBuilder();
                sql.append("insert into ").append(SOSFTPHistory.TABLE_FILES_POSITIONS)
                    .append("(\"HOST\",\"REMOTE_DIR\",\"REMOTE_FILENAME\",\"LOCAL_FILENAME\",\"FILE_SIZE\",\"POSITION\") ").append("values('")
                    .append(SOSFTPHistory.getNormalizedField(conn, host, 128)).append("','").append(SOSFTPHistory.getNormalizedField(conn, remoteDir, 255))
                    .append("','").append(SOSFTPHistory.getNormalizedField(conn, remoteFilename, 255)).append("','")
                    .append(SOSFTPHistory.getNormalizedField(conn, localFilename, 255)).append("',0,0)");
            } else {
                sql = new StringBuilder();
                sql.append("update ").append(SOSFTPHistory.TABLE_FILES_POSITIONS).append(" ").append("set \"LOCAL_FILENAME\" = '")
                    .append(SOSFTPHistory.getNormalizedField(conn, localFilename, 255)).append("' ").append("where \"LOCAL_FILENAME\" = '")
                    .append(SOSFTPHistory.getNormalizedField(conn, lastLocalFileName, 255)).append("'");
            }
            conn.execute(sql.toString());
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ee) {
            }
            throw new Exception(SOSClassUtil.getMethodName() + " : " + e.getMessage(), e);
        }
    }

}