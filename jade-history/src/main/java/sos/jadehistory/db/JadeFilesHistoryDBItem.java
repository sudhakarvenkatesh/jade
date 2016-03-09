package sos.jadehistory.db;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.sos.hibernate.classes.DbItem;

@Entity
@Table(name = "JADE_FILES_HISTORY")
public class JadeFilesHistoryDBItem extends DbItem {

    private String guid;
    private Long jadeId;
    private String operation;
    private Date transferStart;
    private Date transferEnd;
    private Integer pid;
    private Integer ppid;
    private String targetHost;
    private String targetHostIp;
    private String targetUser;
    private String targetDir;
    private String targetFilename;
    private String protocol;
    private Integer port;
    private String status;
    private String lastErrorMessage;
    private String logFilename;
    private String jumpHost;
    private String jumpHostIp;
    private String jumpUser;
    private String jumpProtocol;
    private Integer jumpPort;
    private Date created;
    private String createdBy;
    private Date modified;
    private String modifiedBy;

    private JadeFilesDBItem jadeFilesDBItem;

    public JadeFilesHistoryDBItem() {
        // nothing to do
    }

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "`JADE_ID`", nullable = true)
    public JadeFilesDBItem getJadeFilesDBItem() {
        return this.jadeFilesDBItem;
    }

    public void setJadeFilesDBItem(JadeFilesDBItem jadeFilesDBItem) {
        this.jadeFilesDBItem = jadeFilesDBItem;
    }

    @Id
    @Column(name = "`GUID`", nullable = false)
    public String getGuid() {
        return guid;
    }

    @Column(name = "`GUID`", nullable = false)
    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Column(name = "`JADE_ID`", nullable = false, updatable = false, insertable = false)
    public Long getJadeId() {
        return jadeId;
    }

    @Column(name = "`JADE_ID`", nullable = false, updatable = false, insertable = false)
    public void setJadeId(Long jadeId) {
        this.jadeId = jadeId;
    }

    @Column(name = "`OPERATION`", nullable = false)
    public String getOperation() {
        return operation;
    }

    @Column(name = "`OPERATION`", nullable = false)
    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Column(name = "`TRANSFER_START`", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getTransferStart() {
        return transferStart;
    }

    @Column(name = "`TRANSFER_START`", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public void setTransferStart(Date transferStart) {
        this.transferStart = transferStart;
    }

    @Column(name = "`TRANSFER_END`", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getTransferEnd() {
        return transferEnd;
    }

    @Column(name = "`TRANSFER_END`", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public void setTransferEnd(Date transferEnd) {
        this.transferEnd = transferEnd;
    }

    @Column(name = "`PID`", nullable = false)
    public Integer getPid() {
        return pid;
    }

    @Column(name = "`PID`", nullable = false)
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    @Column(name = "`PPID`", nullable = false)
    public Integer getPPid() {
        return ppid;
    }

    @Column(name = "`PPID`", nullable = false)
    public void setPPid(Integer ppid) {
        this.ppid = ppid;
    }

    @Column(name = "`TARGET_HOST`", nullable = false)
    public String getTargetHost() {
        return targetHost;
    }

    @Column(name = "`TARGET_HOST`", nullable = false)
    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    @Column(name = "`TARGET_HOST_IP`", nullable = false)
    public void setTargetHostIp(String targetHostIp) {
        this.targetHostIp = targetHostIp;
    }

    @Column(name = "`TARGET_HOST_IP`", nullable = false)
    public String getTargetHostIp() {
        return targetHostIp;
    }

    @Column(name = "`TARGET_USER`", nullable = false)
    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    @Column(name = "`TARGET_USER`", nullable = false)
    public String getTargetUser() {
        return targetUser;
    }

    @Column(name = "`TARGET_DIR`", nullable = false)
    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    @Column(name = "`TARGET_DIR`", nullable = false)
    public String getTargetDir() {
        return targetDir;
    }

    @Column(name = "`TARGET_FILENAME`", nullable = false)
    public void setTargetFilename(String targetFilename) {
        this.targetFilename = targetFilename;
    }

    @Column(name = "`TARGET_FILENAME`", nullable = false)
    public String getTargetFilename() {
        return targetFilename;
    }

    @Column(name = "`PROTOCOL`", nullable = false)
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Column(name = "`PROTOCOL`", nullable = false)
    public String getProtocol() {
        return protocol;
    }

    @Column(name = "`PORT`", nullable = false)
    public void setPort(Integer port) {
        this.port = port;
    }

    @Column(name = "`PORT`", nullable = false)
    public Integer getPort() {
        return port;
    }

    @Column(name = "`STATUS`", nullable = false)
    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "`STATUS`", nullable = false)
    public String getStatus() {
        return status;
    }

    @Column(name = "`LAST_ERROR_MESSAGE`", nullable = true)
    public void setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
    }

    @Column(name = "`LAST_ERROR_MESSAGE`", nullable = true)
    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    @Column(name = "`LOG_FILENAME`", nullable = true)
    public void setLogFilename(String logFilename) {
        this.logFilename = logFilename;
    }

    @Column(name = "`LOG_FILENAME`", nullable = true)
    public String getLogFilename() {
        return logFilename;
    }

    @Column(name = "`JUMP_HOST`", nullable = true)
    public void setJumpHost(String jumpHost) {
        this.jumpHost = jumpHost;
    }

    @Column(name = "`JUMP_HOST`", nullable = true)
    public String getJumpHost() {
        return jumpHost;
    }

    @Column(name = "`JUMP_HOST_IP`", nullable = true)
    public void setJumpHostIp(String jumpHostIp) {
        this.jumpHostIp = jumpHostIp;
    }

    @Column(name = "`JUMP_HOST_IP`", nullable = true)
    public String getJumpHostIp() {
        return jumpHostIp;
    }

    @Column(name = "`JUMP_USER`", nullable = true)
    public void setJumpUser(String jumpUser) {
        this.jumpUser = jumpUser;
    }

    @Column(name = "`JUMP_USER`", nullable = true)
    public String getJumpUser() {
        return jumpUser;
    }

    @Column(name = "`JUMP_PROTOCOL`", nullable = true)
    public void setJumpProtocol(String jumpProtocol) {
        this.jumpProtocol = jumpProtocol;
    }

    @Column(name = "`JUMP_PROTOCOL`", nullable = true)
    public String getJumpProtocol() {
        return jumpProtocol;
    }

    @Column(name = "`JUMP_PORT`", nullable = true)
    public void setJumpPort(Integer jumpPort) {
        this.jumpPort = jumpPort;
    }

    @Column(name = "`JUMP_PORT`", nullable = true)
    public Integer getJumpPort() {
        return jumpPort;
    }

    @Column(name = "`MODIFIED_BY`", nullable = false)
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Column(name = "`MODIFIED_BY`", nullable = false)
    public String getModifiedBy() {
        return modifiedBy;
    }

    @Column(name = "`CREATED_BY`", nullable = false)
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "`CREATED_BY`", nullable = false)
    public String getCreatedBy() {
        return createdBy;
    }

    @Column(name = "`CREATED`", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    @Column(name = "`CREATED`", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public void setCreated(Date created) {
        this.created = created;
    }

    @Column(name = "`MODIFIED`", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getModified() {
        return modified;
    }

    @Column(name = "`MODIFIED`", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public void setModified(Date modified) {
        this.modified = modified;
    }

    @Transient
    public String getTransferStartIso() {
        if (this.getTransferStart() == null) {
            return "";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            return formatter.format(this.getTransferStart());
        }
    }

    @Transient
    public String getTransferEndIso() {
        if (this.getTransferEnd() == null) {
            return "";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            return formatter.format(this.getTransferEnd());
        }
    }

}
