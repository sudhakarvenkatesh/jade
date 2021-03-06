package sos.jadehistory.db;

import java.util.Date;

import javax.persistence.*;

import com.sos.hibernate.classes.DbItem;

@Entity
@Table(name = "JADE_FILES")
public class JadeFilesDBItem extends DbItem {

    private Long id;
    private String mandator;
    private String sourceHost;
    private String sourceHostIp;
    private String sourceUser;
    private String sourceDir;
    private String sourceFilename;
    private String md5;
    private Long fileSize;
    private Date created;
    private String createdBy;
    private Date modified;
    private String modifiedBy;

    public JadeFilesDBItem() {
        // nothing to do
    }

    @Id
    @GeneratedValue(generator = "JADE_FILES_ID_GEN", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "JADE_FILES_ID_GEN", sequenceName = "JADE_FILES_ID_SEQ", allocationSize = 1)
    @Column(name = "`ID`", nullable = false)
    public Long getId() {
        return id;
    }

    @Column(name = "`ID`", nullable = false)
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "`MANDATOR`", nullable = false)
    public void setMandator(String mandator) {
        this.mandator = mandator;
    }

    @Column(name = "`MANDATOR`", nullable = false)
    public String getMandator() {
        return mandator;
    }

    @Column(name = "`FILE_SIZE`", nullable = false)
    public Long getFileSize() {
        return fileSize;
    }

    @Column(name = "`FILE_SIZE`", nullable = false)
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Column(name = "`SOURCE_HOST`", nullable = true)
    public String getSourceHost() {
        return sourceHost;
    }

    @Column(name = "`SOURCE_HOST`", nullable = true)
    public void setSourceHost(String sourceHost) {
        this.sourceHost = sourceHost;
    }

    @Column(name = "`SOURCE_HOST_IP`", nullable = false)
    public void setSourceHostIp(String sourceHostIp) {
        this.sourceHostIp = sourceHostIp;
    }

    @Column(name = "`SOURCE_HOST_IP`", nullable = false)
    public String getSourceHostIp() {
        return sourceHostIp;
    }

    @Column(name = "`SOURCE_USER`", nullable = false)
    public void setSourceUser(String sourceUser) {
        this.sourceUser = sourceUser;
    }

    @Column(name = "`SOURCE_USER`", nullable = false)
    public String getSourceUser() {
        return sourceUser;
    }

    @Column(name = "`SOURCE_DIR`", nullable = false)
    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    @Column(name = "`SOURCE_DIR`", nullable = false)
    public String getSourceDir() {
        return sourceDir;
    }

    @Column(name = "`SOURCE_FILENAME`", nullable = false)
    public void setSourceFilename(String sourceFilename) {
        this.sourceFilename = sourceFilename;
    }

    @Column(name = "`SOURCE_FILENAME`", nullable = false)
    public String getSourceFilename() {
        return sourceFilename;
    }

    @Column(name = "`MD5`", nullable = false)
    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Column(name = "`MD5`", nullable = false)
    public String getMd5() {
        return md5;
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

}
