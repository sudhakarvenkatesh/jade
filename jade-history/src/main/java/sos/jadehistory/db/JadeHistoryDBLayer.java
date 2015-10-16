package sos.jadehistory.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import sos.jadehistory.JadeFilesFilter;
import sos.jadehistory.JadeFilesHistoryFilter;

import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.classes.SOSHibernateConnection;

public class JadeHistoryDBLayer {
	
	private SOSHibernateConnection connection;
    protected JadeFilesFilter filesFilter;
    protected JadeFilesHistoryFilter historyFilesFilter;

	public JadeHistoryDBLayer(SOSHibernateConnection connection) {
		this.connection = connection;
        this.resetFilter();
	}
	
    public JadeFilesDBItem get(Long id) {
        if (id == null) {
            return null;
        }
        
        try {
            return (JadeFilesDBItem) ((Session)connection.getCurrentSession()).get(JadeFilesDBItem.class, id);
        }
        catch (ObjectNotFoundException e) {
            return null;
        } catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            return null;
		}
    }

    public void resetFilter() {
        this.filesFilter = new JadeFilesFilter();
        this.filesFilter.setDateFormat("yyyy-MM-dd HH:mm:ss");
        this.filesFilter.setOrderCriteria("startTime");
        this.filesFilter.setSortMode("desc");
        
        this.historyFilesFilter = new JadeFilesHistoryFilter();
        this.historyFilesFilter.setDateFormat("yyyy-MM-dd HH:mm:ss");
        this.historyFilesFilter.setOrderCriteria("startTime");
        this.historyFilesFilter.setSortMode("desc");
        
    }
    
    // TODO Umbauen auf Criterias
    protected String getFilesWhere() {

        String where = "";
        String and = "";

        if (filesFilter.getCreatedFrom() != null) {
            where += and + " created >= :createdFrom";
            and = " and ";
        }

        if (filesFilter.getCreatedTo() != null) {
            where += and + " created <= :createdTo ";
            and = " and ";
        }

        if (filesFilter.getModifiedFrom() != null) {
            where += and + " modified >= :modifiedFrom";
            and = " and ";
        }

        if (filesFilter.getModifiedTo() != null) {
            where += and + " modified <= :modifiedTo ";
            and = " and ";
        }

        if (filesFilter.getModificationDateFrom() != null) {
            where += and + " modificationDate >= :modificationDateFrom";
            and = " and ";
        }

        if (filesFilter.getModificationDateTo() != null) {
            where += and + " modificationDate <= :modificationDateTo ";
            and = " and ";
        }

        if (filesFilter.getMandator() != null && !filesFilter.getMandator().equals("")) {
            where += and + " mandator=:mandator";
            and = " and ";
        }

        if (filesFilter.getCreatedBy() != null && !filesFilter.getCreatedBy().equals("")) {
            where += and + " createdBy=:createdBy";
            and = " and ";
        }

        if (filesFilter.getModifiedBy() != null && !filesFilter.getModifiedBy().equals("")) {
            where += and + " modifiedBy=:modifiedBy";
            and = " and ";
        }

        if (filesFilter.getSourceDir() != null && !filesFilter.getSourceDir().equals("")) {
            where += and + " sourceDir=:sourceDir";
            and = " and ";
        }

        if (filesFilter.getSourceFilename() != null && !filesFilter.getSourceFilename().equals("")) {
            where += and + " sourceFilename like :sourceFilename";
            and = " and ";
        }

        if (filesFilter.getSourceHost() != null && !filesFilter.getSourceHost().equals("")) {
            where += and + " sourceHost=:sourceHost";
            and = " and ";
        }

        if (filesFilter.getSourceHostIp() != null && !filesFilter.getSourceHostIp().equals("")) {
            where += and + " sourceHostIp=:sourceHostIp";
            and = " and ";
        }

        if (filesFilter.getSourceUser() != null && !filesFilter.getSourceUser().equals("")) {
            where += and + " sourceUser=:sourceUser";
            and = " and ";
        }

        if (filesFilter.getFileSize() != null) {
            where += and + " fileSize=:fileSize";
            and = " and ";
        }

        if (where.trim().equals("")) {

        }
        else {
            where = "where " + where;
        }
        return where;

    }

    
    protected String getFilesWhereFromTo() {

        String where = "";
        String and = "";

        if (filesFilter.getCreatedFrom() != null) {
            where += and + " created >= :createdFrom";
            and = " and ";
        }

        if (filesFilter.getCreatedTo() != null) {
            where += and + " created <= :createdTo ";
            and = " and ";
        }

        if (where.trim().equals("")) {

        }
        else {
            where = "where " + where;
        }
        return where;

    }
    
    private void setFilesWhere(Query query) {

        if (filesFilter.getCreatedFrom() != null && !filesFilter.getCreatedFrom().equals("")) {
            query.setTimestamp("createdFrom", filesFilter.getCreatedFrom());
        }

        if (filesFilter.getCreatedTo() != null && !filesFilter.getCreatedTo().equals("")) {
            query.setTimestamp("createdTo", filesFilter.getCreatedTo());
        }

        if (filesFilter.getModifiedFrom() != null && !filesFilter.getModifiedFrom().equals("")) {
            query.setTimestamp("modifiedFrom", filesFilter.getModifiedFrom());
        }

        if (filesFilter.getModifiedTo() != null && !filesFilter.getModifiedTo().equals("")) {
            query.setTimestamp("modifiedTo", filesFilter.getModifiedTo());
        }

        if (filesFilter.getModificationDateFrom() != null && !filesFilter.getModificationDateFrom().equals("")) {
            query.setTimestamp("modificationDateFrom", filesFilter.getModificationDateFrom());
        }

        if (filesFilter.getModificationDateTo() != null && !filesFilter.getModificationDateTo().equals("")) {
            query.setTimestamp("modificationDateTo", filesFilter.getModificationDateTo());
        }

        if (filesFilter.getMandator() != null && !filesFilter.getMandator().equals("")) {
            query.setText("mandator", filesFilter.getMandator());
        }

        if (filesFilter.getCreatedBy() != null && !filesFilter.getCreatedBy().equals("")) {
            query.setText("createdBy", filesFilter.getCreatedBy());
        }

        if (filesFilter.getModifiedBy() != null && !filesFilter.getModifiedBy().equals("")) {
            query.setText("modifiedBy", filesFilter.getModifiedBy());
        }

        if (filesFilter.getSourceDir() != null && !filesFilter.getSourceDir().equals("")) {
            query.setText("sourceDir", filesFilter.getSourceDir());
        }

        if (filesFilter.getSourceFilename() != null && !filesFilter.getSourceFilename().equals("")) {
            query.setText("sourceFilename", filesFilter.getSourceFilename());
        }

        if (filesFilter.getSourceHost() != null && !filesFilter.getSourceHost().equals("")) {
            query.setText("sourceHost", filesFilter.getSourceHost());
        }

        if (filesFilter.getSourceHostIp() != null && !filesFilter.getSourceHostIp().equals("")) {
            query.setText("sourceHostIp", filesFilter.getSourceHostIp());
        }

        if (filesFilter.getSourceUser() != null && !filesFilter.getSourceUser().equals("")) {
            query.setText("sourceUser", filesFilter.getSourceUser());
        }

        if (filesFilter.getFileSize() != null) {
            query.setInteger("fileSize", filesFilter.getFileSize());
        }

    }

    public int delete() throws Exception {
    	connection.beginTransaction();

        String q = "delete from JadeFilesHistoryDBItem e where e.jadeFilesDBItem.id IN (select id from JadeFilesDBItem " + getFilesWhere() + ")";
        Query query = connection.createQuery(q);
        setFilesWhere(query);

        int row = query.executeUpdate();

        String hql = "delete from JadeFilesDBItem " + getFilesWhere();
        query = connection.createQuery(hql);

        setFilesWhere(query);

        row = query.executeUpdate();
        return row;
    }

 

    public List<DbItem> getFilesFromTo(Date from, Date to) throws Exception  {

        filesFilter.setCreatedFrom(from); 
        filesFilter.setCreatedTo(to);

        connection.beginTransaction();
        Query query = connection.createQuery("  from JadeFilesDBItem " + getFilesWhere());

        if (filesFilter.getCreatedFrom() != null) {
           query.setTimestamp("createdFrom", filesFilter.getCreatedFrom());
        }
        if (filesFilter.getCreatedTo() != null) {
           query.setTimestamp("createdTo", filesFilter.getCreatedTo());
        }

        List<DbItem> resultset = query.list();

        return resultset;

    }

 

    public List<JadeFilesHistoryDBItem> getFilesHistoryById(Long jadeId) throws Exception {
        connection.beginTransaction();
        Query query = connection.createQuery("  from JadeFilesHistoryDBItem where jadeId=:jadeId");
        query.setLong("jadeId", jadeId);
        List<JadeFilesHistoryDBItem> resultset = query.list();

        connection.commit();
        return resultset;
    	
    }
    
    public List<JadeFilesDBItem> getFiles() throws Exception {
      
        connection.beginTransaction();
        Query query = connection.createQuery("  from JadeFilesDBItem " + getFilesWhere());
        setFilesWhere(query);
        List<JadeFilesDBItem> resultset = query.list();

        connection.commit();
        return resultset;

    }
    
    
    public void setCreatedFrom(Date createdFrom) {
        filesFilter.setCreatedFrom(createdFrom);
    }

    public void setCreatedTo(Date createdTo) {
        filesFilter.setCreatedTo(createdTo);
    }

    public void setDateFormat(String dateFormat) {
        filesFilter.setDateFormat(dateFormat);
    }

    public void setCreatedFrom(String createdFrom) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(filesFilter.getDateFormat());
        setCreatedFrom(formatter.parse(createdFrom));
    }

    public void setCreatedTo(String createdTo) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(filesFilter.getDateFormat());
        setCreatedTo(formatter.parse(createdTo));
    }

    public JadeFilesFilter getFilesFilter() {
        return filesFilter;
    }

    public void setFilesFilter(JadeFilesFilter filter) {
        this.filesFilter = filter;
    }

    public JadeFilesHistoryFilter getFilesHistoryFilter() {
        return historyFilesFilter;
    }

    public void setFilesHistoryFilter(JadeFilesHistoryFilter filter) {
        this.historyFilesFilter = filter;
    }

//    public void onAfterDeleting(DbItem h) {        
//    }

    public List<DbItem> getListOfItemsToDelete() throws Exception  {
         return getFilesFromTo(filesFilter.getCreatedFrom(),filesFilter.getCreatedTo());
             
    }

    public long deleteInterval() throws Exception {
        connection.beginTransaction();
        
        String q = "delete from JadeFilesHistoryDBItem e where e.jadeFilesDBItem.id IN (select id from JadeFilesDBItem " + getFilesWhereFromTo() + ")";
        
     
        Query query = connection.createQuery(q);
        if (filesFilter.getCreatedFrom() != null) {
            query.setTimestamp("createdFrom", filesFilter.getCreatedFrom());
         }
         if (filesFilter.getCreatedTo() != null) {
            query.setTimestamp("createdTo", filesFilter.getCreatedTo());
         }
        
        int row = query.executeUpdate();

        String hql = "delete from JadeFilesDBItem " + getFilesWhereFromTo();
        query = connection.createQuery(hql);

        if (filesFilter.getCreatedFrom() != null) {
            query.setTimestamp("createdFrom", filesFilter.getCreatedFrom());
         }
         if (filesFilter.getCreatedTo() != null) {
            query.setTimestamp("createdTo", filesFilter.getCreatedTo());
         }
          
        row = query.executeUpdate();
        return row;       
    }
	
    //**************************************************************
    
    public JadeFilesHistoryDBItem get(String guid) throws Exception {
        if (guid == null || "".equals(guid)) {
            return null;
        }
        
        try {
            return (JadeFilesHistoryDBItem) ((Session)connection.getCurrentSession()).get(JadeFilesHistoryDBItem.class, guid);
        }
        catch (ObjectNotFoundException e) {
            return null;
        }
    }

    protected String getHistoryWhere() {

        String where = "";
        String and = "";

        if (historyFilesFilter.getCreatedFrom() != null) {
            where += and + " created >= :createdFrom";
            and = " and ";
        }

        if (historyFilesFilter.getCreatedTo() != null) {
            where += and + " created <= :createdTo ";
            and = " and ";
        }

        if (historyFilesFilter.getModifiedFrom() != null) {
            where += and + " modified >= :modifiedFrom";
            and = " and ";
        }

        if (historyFilesFilter.getModifiedTo() != null) {
            where += and + " modified <= :modifiedTo ";
            and = " and ";
        }

        if (historyFilesFilter.getTransferStartFrom() != null) {
            where += and + " transferStart >= :transferStartFrom";
            and = " and ";
        }

        if (historyFilesFilter.getTransferStartTo() != null) {
            where += and + " transferStart <= :transferStartTo";
            and = " and ";
        }

        if (historyFilesFilter.getTransferEndFrom() != null) {
            where += and + " transferEnd >= :transferEndFrom";
            and = " and ";
        }

        if (historyFilesFilter.getTransferEndTo() != null) {
            where += and + " transferEnd <= :transferEndTo";
            and = " and ";
        }

        if (historyFilesFilter.getCreatedBy() != null && !historyFilesFilter.getCreatedBy().equals("")) {
            where += and + " createdBy=:createdBy";
            and = " and ";
        }

        if (historyFilesFilter.getModifiedBy() != null && !historyFilesFilter.getModifiedBy().equals("")) {
            where += and + " modifiedBy=:modifiedBy";
            and = " and ";
        }

        if (historyFilesFilter.getGuid() != null && !"".equals(historyFilesFilter.getGuid())) {
            where += and + " guid=:guid";
            and = " and ";
        }

        if (historyFilesFilter.getPid() != null && !"".equals(historyFilesFilter.getPid())) {
            where += and + " pid=:pid";
            and = " and ";
        }

        if (historyFilesFilter.getPpid() != null && !"".equals(historyFilesFilter.getPpid())) {
            where += and + " ppid=:ppid";
            and = " and ";
        }

        if (historyFilesFilter.getLastErrorMessage() != null && !"".equals(historyFilesFilter.getLastErrorMessage())) {
            where += and + " lastErrorMessage=:lastErrorMessage";
            and = " and ";
        }

        if (historyFilesFilter.getStatus() != null && !"".equals(historyFilesFilter.getStatus())) {
            where += and + " status=:status";
            and = " and ";
        }

        if (historyFilesFilter.getLogFilename() != null && !"".equals(historyFilesFilter.getLogFilename())) {
            where += and + " logFilename=:logFilename";
            and = " and ";
        }

        if (historyFilesFilter.getJumpHost() != null && !"".equals(historyFilesFilter.getJumpHost())) {
            where += and + " jumpHost=:jumpHost";
            and = " and ";
        }

        if (historyFilesFilter.getJumpHostIp() != null && !"".equals(historyFilesFilter.getJumpHostIp())) {
            where += and + " jumpHostIp=:jumpHostIp";
            and = " and ";
        }

        if (historyFilesFilter.getJumpProtocol() != null && !"".equals(historyFilesFilter.getJumpProtocol())) {
            where += and + " jumpProtocol=:jumpProtocol";
            and = " and ";
        }

        if (historyFilesFilter.getJumpPort() != null && !"".equals(historyFilesFilter.getJumpPort())) {
            where += and + " jumpPort=:jumpPort";
            and = " and ";
        }

        if (historyFilesFilter.getJumpUser() != null && !"".equals(historyFilesFilter.getJumpUser())) {
            where += and + " jumpUser=:jumpUser";
            and = " and ";
        }

        if (historyFilesFilter.getOperation() != null && !"".equals(historyFilesFilter.getOperation())) {
            where += and + " operation=:operation";
            and = " and ";
        }

        if (historyFilesFilter.getPort() != null && !"".equals(historyFilesFilter.getPort())) {
            where += and + " port=:port";
            and = " and ";
        }

        if (historyFilesFilter.getProtocol() != null && !"".equals(historyFilesFilter.getProtocol())) {
            where += and + " protocol=:protocol";
            and = " and ";
        }

        if (historyFilesFilter.getJadeId() != null && !"".equals(historyFilesFilter.getJadeId())) {
            where += and + " jadeId=:jadeId";
            and = " and ";
        }

        if (historyFilesFilter.getTargetDir() != null && !"".equals(historyFilesFilter.getTargetDir())) {
            where += and + " targetDir=:targetDir";
            and = " and ";
        }

        if (historyFilesFilter.getTargetFilename() != null && !"".equals(historyFilesFilter.getTargetFilename())) {
            where += and + " targetFilename like :targetFilename";
            and = " and ";
        }

        if (historyFilesFilter.getTargetHost() != null && !"".equals(historyFilesFilter.getTargetHost())) {
            where += and + " targetHost=:targetHost";
            and = " and ";
        }

        if (historyFilesFilter.getTargetHostIp() != null && !"".equals(historyFilesFilter.getTargetHostIp())) {
            where += and + " targetHostIp=:targetHostIp";
            and = " and ";
        }

        if (historyFilesFilter.getTargetUser() != null && !"".equals(historyFilesFilter.getTargetUser())) {
            where += and + " targetUser=:targetUser";
            and = " and ";
        }

        if (historyFilesFilter.getMandator() != null && !"".equals(historyFilesFilter.getMandator())) {
            where += and + " history.jadeFilesDBItem.mandator=:mandator";
            and = " and ";
        }

        if (historyFilesFilter.getFileSize() != null && !"".equals(historyFilesFilter.getFileSize())) {
            where += and + " history.jadeFilesDBItem.fileSize=:fileSize";
            and = " and ";
        }

        if (historyFilesFilter.getSourceFile() != null && !"".equals(historyFilesFilter.getSourceFile())) {
            where += and + " history.jadeFilesDBItem.sourceFilename like :sourceFilename";
            and = " and ";
        }

        if (historyFilesFilter.getSourceHost() != null && !"".equals(historyFilesFilter.getSourceHost())) {
            where += and + " history.jadeFilesDBItem.sourceHost=:sourceHost";
            and = " and ";
        }

  


        if (where.trim().equals("")) {

        }
        else {
            where = "where " + where;
        }
        return where;

    }

    
    protected String getHistoryWhereFromTo() {

        String where = "";
        String and = "";

        if (historyFilesFilter.getCreatedFrom() != null) {
            where += and + " created >= :createdFrom";
            and = " and ";
        }

        if (historyFilesFilter.getCreatedTo() != null) {
            where += and + " created <= :createdTo ";
            and = " and ";
        }

 


        if (where.trim().equals("")) {

        }
        else {
            where = "where " + where;
        }
        return where;

    }    
    
    private void setHistoryWhere(Query query) {

        if (historyFilesFilter.getCreatedFrom() != null && !historyFilesFilter.getCreatedFrom().equals("")) {
            query.setTimestamp("createdFrom", historyFilesFilter.getCreatedFrom());
        }

        if (historyFilesFilter.getCreatedTo() != null && !historyFilesFilter.getCreatedTo().equals("")) {
            query.setTimestamp("createdTo", historyFilesFilter.getCreatedTo());
        }

        if (historyFilesFilter.getModifiedFrom() != null && !historyFilesFilter.getModifiedFrom().equals("")) {
            query.setTimestamp("modifiedFrom", historyFilesFilter.getModifiedFrom());
        }

        if (historyFilesFilter.getModifiedTo() != null && !historyFilesFilter.getModifiedTo().equals("")) {
            query.setTimestamp("modifiedTo", historyFilesFilter.getModifiedTo());
        }

        if (historyFilesFilter.getCreatedBy() != null && !historyFilesFilter.getCreatedBy().equals("")) {
            query.setText("createdBy", historyFilesFilter.getCreatedBy());
        }

        if (historyFilesFilter.getModifiedBy() != null && !historyFilesFilter.getModifiedBy().equals("")) {
            query.setText("modifiedBy", historyFilesFilter.getModifiedBy());
        }

        if (historyFilesFilter.getGuid() != null && !"".equals(historyFilesFilter.getGuid())) {
            query.setText("guid", historyFilesFilter.getGuid());
        }

        if (historyFilesFilter.getJadeId() != null && !"".equals(historyFilesFilter.getJadeId())) {
            query.setLong("jadeId", historyFilesFilter.getJadeId());
        }

        if (historyFilesFilter.getOperation() != null && !"".equals(historyFilesFilter.getOperation())) {
            query.setText("operation", historyFilesFilter.getOperation());
        }

        if (historyFilesFilter.getTransferStartFrom() != null && !"".equals(historyFilesFilter.getTransferStartFrom())) {
            query.setTimestamp("transferStartFrom", historyFilesFilter.getTransferStartFrom());
        }

        if (historyFilesFilter.getTransferStartTo() != null && !"".equals(historyFilesFilter.getTransferStartTo())) {
            query.setTimestamp("transferStartTo", historyFilesFilter.getTransferStartTo());
        }

        if (historyFilesFilter.getTransferEndFrom() != null && !"".equals(historyFilesFilter.getTransferEndFrom())) {
            query.setTimestamp("transferEndFrom", historyFilesFilter.getTransferEndFrom());
        }

        if (historyFilesFilter.getTransferEndTo() != null && !"".equals(historyFilesFilter.getTransferEndTo())) {
            query.setTimestamp("transferEndTo", historyFilesFilter.getTransferEndTo());
        }

        if (historyFilesFilter.getPid() != null && !"".equals(historyFilesFilter.getPid())) {
            query.setInteger("pid", historyFilesFilter.getPid());
        }

        if (historyFilesFilter.getPpid() != null && !"".equals(historyFilesFilter.getPpid())) {
            query.setInteger("ppid", historyFilesFilter.getPpid());
        }

        if (historyFilesFilter.getTargetHost() != null && !"".equals(historyFilesFilter.getTargetHost())) {
            query.setText("targetHost", historyFilesFilter.getTargetHost());
        }

        if (historyFilesFilter.getTargetHostIp() != null && !"".equals(historyFilesFilter.getTargetHostIp())) {
            query.setText("targetHostIp", historyFilesFilter.getTargetHostIp());
        }

        if (historyFilesFilter.getTargetUser() != null && !"".equals(historyFilesFilter.getTargetUser())) {
            query.setText("targetUser", historyFilesFilter.getTargetUser());
        }

        if (historyFilesFilter.getTargetDir() != null && !"".equals(historyFilesFilter.getTargetDir())) {
            query.setText("targetDir", historyFilesFilter.getTargetDir());
        }

        if (historyFilesFilter.getTargetFilename() != null && !"".equals(historyFilesFilter.getTargetFilename())) {
            query.setText("targetFilename", historyFilesFilter.getTargetFilename());
        }

        if (historyFilesFilter.getProtocol() != null && !"".equals(historyFilesFilter.getProtocol())) {
            query.setText("protocol", historyFilesFilter.getProtocol());
        }

        if (historyFilesFilter.getPort() != null && !"".equals(historyFilesFilter.getPort())) {
            query.setInteger("port", historyFilesFilter.getPort());
        }

        if (historyFilesFilter.getStatus() != null && !"".equals(historyFilesFilter.getStatus())) {
            query.setText("status", historyFilesFilter.getStatus());
        }

        if (historyFilesFilter.getLastErrorMessage() != null && !"".equals(historyFilesFilter.getLastErrorMessage())) {
            query.setText("lastErrorMessage", historyFilesFilter.getLastErrorMessage());
        }

        if (historyFilesFilter.getLogFilename() != null && !"".equals(historyFilesFilter.getLogFilename())) {
            query.setText("logFilename", historyFilesFilter.getLogFilename());
        }

        if (historyFilesFilter.getJumpHost() != null && !"".equals(historyFilesFilter.getJumpHost())) {
            query.setText("jumpHost", historyFilesFilter.getJumpHost());
        }

        if (historyFilesFilter.getJumpHostIp() != null && !"".equals(historyFilesFilter.getJumpHostIp())) {
            query.setText("jumpHostIp", historyFilesFilter.getJumpHostIp());
        }

        if (historyFilesFilter.getJumpUser() != null && !"".equals(historyFilesFilter.getJumpUser())) {
            query.setText("jumpUser", historyFilesFilter.getJumpUser());
        }

        if (historyFilesFilter.getJumpProtocol() != null && !"".equals(historyFilesFilter.getJumpProtocol())) {
            query.setText("jumpProtocol", historyFilesFilter.getJumpProtocol());
        }

        if (historyFilesFilter.getJumpPort() != null && !"".equals(historyFilesFilter.getJumpPort())) {
            query.setInteger("jumpPort", historyFilesFilter.getJumpPort());
        }

        if (historyFilesFilter.getJadeFilesDBItem() != null && historyFilesFilter.getJadeFilesDBItem().getId() != null) {
            query.setLong("jadeId", historyFilesFilter.getJadeFilesDBItem().getId());
        }

        if (historyFilesFilter.getMandator() != null && !"".equals(historyFilesFilter.getMandator())) {
            query.setText("mandator", historyFilesFilter.getMandator());
        }

        if (historyFilesFilter.getFileSize() != null && !"".equals(historyFilesFilter.getFileSize())) {
            query.setInteger("fileSize", historyFilesFilter.getFileSize());
        }

        if (historyFilesFilter.getSourceFile() != null && !"".equals(historyFilesFilter.getSourceFile())) {
            query.setText("sourceFilename", historyFilesFilter.getSourceFile());
        }

        if (historyFilesFilter.getSourceHost() != null && !"".equals(historyFilesFilter.getSourceHost())) {
            query.setText("sourceHost", historyFilesFilter.getSourceHost());
        }
    }

  
    public List<DbItem> getFilesHistoryFromTo(Date from, Date to) throws Exception{
        
        historyFilesFilter.setCreatedFrom(from); 
        historyFilesFilter.setCreatedTo(to);
      
        connection.beginTransaction();
        Query query = connection.createQuery("  from JadeFilesHistoryDBItem " + getHistoryWhere());

        if (historyFilesFilter.getCreatedFrom() != null) {
            query.setTimestamp("createdFrom", historyFilesFilter.getCreatedFrom());
        }
        
        if (historyFilesFilter.getCreatedTo() != null) {
            query.setTimestamp("createdTo", historyFilesFilter.getCreatedTo());
        }

        List<DbItem> resultset = query.list();

        return resultset;

    }    
    
    public JadeFilesDBItem getJadeFileItemById(Long jadeId) throws Exception {
        connection.beginTransaction();
        Query query = connection.createQuery("  from JadeFilesDBItem where id=:jadeId");
        query.setLong("jadeId", jadeId);
        List<JadeFilesDBItem> resultset = query.list();

        connection.commit();
        // id is unique, therefore only one item has to be returned
        return resultset.get(0);
    }
    
    public List<JadeFilesHistoryDBItem> getHistoryFiles() throws Exception {
        
        connection.beginTransaction();
        Query query = connection.createQuery("  from JadeFilesHistoryDBItem history " + getHistoryWhere());
        setHistoryWhere(query);
        List<JadeFilesHistoryDBItem> resultset = query.list();

        connection.commit();
        return resultset;

    }
    
    public List<JadeFilesHistoryDBItem> getHistoryFilesOrderedByTransferEnd() throws Exception {
        
        connection.beginTransaction();
        Query query = connection.createQuery("  from JadeFilesHistoryDBItem history " + getHistoryWhere() + " order by transferEnd desc");
        setHistoryWhere(query);
        List<JadeFilesHistoryDBItem> resultset = query.list();

        connection.commit();
        return resultset;

    }
    
    
    public void setHistoryCreatedFrom(Date createdFrom) {
        historyFilesFilter.setCreatedFrom(createdFrom);
    }

    public void setHistoryCreatedTo(Date createdTo) {
        historyFilesFilter.setCreatedTo(createdTo);
    }

    public void setHistoryDateFormat(String dateFormat) {
        historyFilesFilter.setDateFormat(dateFormat);
    }

    public void setHistoryCreatedFrom(String createdFrom) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(historyFilesFilter.getDateFormat());
        setHistoryCreatedFrom(formatter.parse(createdFrom));
    }

    public void setHistoryCreatedTo(String createdTo) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(historyFilesFilter.getDateFormat());
        setHistoryCreatedTo(formatter.parse(createdTo));
    }

    public JadeFilesHistoryFilter getHistoryFilter() {
        return historyFilesFilter;
    }

    public void setHistoryFilter(JadeFilesHistoryFilter historyFilesFilter) {
        this.historyFilesFilter = historyFilesFilter;
    }

    public List<DbItem> getListOfHistoryItemsToDelete() throws Exception  {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
         return getFilesHistoryFromTo(historyFilesFilter.getCreatedFrom(),historyFilesFilter.getCreatedTo());
             
    }

//    public long deleteInterval() {
//         return 0;
//    }

}