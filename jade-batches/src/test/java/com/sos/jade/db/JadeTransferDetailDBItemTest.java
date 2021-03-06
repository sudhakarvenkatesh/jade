package com.sos.jade.db;

import static org.junit.Assert.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/** @author Uwe Risse */
public class JadeTransferDetailDBItemTest {

    private static final Logger LOGGER = Logger.getLogger(JadeTransferDetailDBItemTest.class);
    private JadeTransferDetailDBItem jadeTransferDetailDBItem;
    private String configurationFilename = "c:/temp/hibernate.cfg.xml";
    private File configurationFile;

    @Before
    public void setUp() throws Exception {
        configurationFile = new File(configurationFilename);
        jadeTransferDetailDBItem = new JadeTransferDetailDBItem();
    }

    private JadeTransferDBItem getNewTransferItem() {
        JadeTransferDBItem transferItem = new JadeTransferDBItem();
        transferItem.setMandator("myMandator");
        transferItem.setSourceHost("mySourceHost");
        transferItem.setSourceHostIp("mySourceHostIp");
        transferItem.setSourceUser("mySourceUser");
        transferItem.setSourceDir("mySourceDir");
        transferItem.setFileSize(new Long(1));
        transferItem.setProtocolType(1);
        transferItem.setPort(4711);
        transferItem.setTargetHost("myTargetHost");
        transferItem.setTargetHostIp("myTargetHostIp");
        transferItem.setTargetUser("myTargetUser");
        transferItem.setTargetDir("myTargetDir");
        transferItem.setTargetDir("myTargetDir");
        transferItem.setStartTime(new Date());
        transferItem.setEndTime(new Date());
        transferItem.setFilesCount(2);
        transferItem.setStatus(1);
        transferItem.setProfileName("myProfileName");
        transferItem.setProfile("myProfile");
        transferItem.setLog("myLog");
        transferItem.setLastErrorMessage("myLastErrorMessage");
        transferItem.setCommandType(3);
        transferItem.setCommand("myCommand");
        transferItem.setModifiedBy("myModifiedBy");
        transferItem.setModified(new Date());
        transferItem.setCreatedBy("myCreatedBy");
        transferItem.setCreated(new Date());
        return transferItem;
    }

    private JadeTransferDetailDBItem getNewTransferDetailDBItem() {
        JadeTransferDetailDBItem transferDetailItem = new JadeTransferDetailDBItem();
        transferDetailItem.setPid("2");
        transferDetailItem.setTargetFilename("myTargetFilename");
        transferDetailItem.setSourceFilename("mySourceFilename");
        transferDetailItem.setStartTime(new Date());
        transferDetailItem.setEndTime(new Date());
        transferDetailItem.setStatus(1);
        transferDetailItem.setFileSize(new Long(1));
        transferDetailItem.setCommandType(1);
        transferDetailItem.setCommand("myCommand");
        transferDetailItem.setMd5("myMd5");
        transferDetailItem.setLastErrorMessage("myLastErrorMessage");
        transferDetailItem.setModifiedBy("myModifiedBy");
        transferDetailItem.setCreatedBy("myCreatedBy");
        transferDetailItem.setCreated(new Date());
        transferDetailItem.setModified(new Date());
        return transferDetailItem;
    }

    @Test
    public void testJadeTransferDetailDBItem() {
        jadeTransferDetailDBItem = new JadeTransferDetailDBItem();
    }

    @Test
    public void testSetTransferDetailsId() {
        Long myTransferDetailsId = new Long(2);
        jadeTransferDetailDBItem.setTransferDetailsId(myTransferDetailsId);
        Long actTransferDetailsId = jadeTransferDetailDBItem.getTransferDetailsId();
        assertEquals("testSetprop faild: ", myTransferDetailsId, actTransferDetailsId);
    }

    @Test
    public void testSetTransferId() {
        Long myTransferId = new Long(29);
        jadeTransferDetailDBItem.setTransferId(myTransferId);
        Long actTransferId = jadeTransferDetailDBItem.getTransferId();
        assertEquals("testSetTransferid faild: ", myTransferId, actTransferId);
    }

    @Test
    public void testSetFileSizeLong() {
        Long myFileSize = new Long(23);
        jadeTransferDetailDBItem.setFileSize(myFileSize);
        Long actFileSize = jadeTransferDetailDBItem.getFileSize();
        assertEquals("testSetFileSize faild: ", myFileSize, actFileSize);
    }

    @Test
    public void testSetFileSizeString() {
        String myFileSize = "234";
        jadeTransferDetailDBItem.setFileSize(myFileSize);
        Long actFileSize = jadeTransferDetailDBItem.getFileSize();
        Long lFleSize = Long.parseLong(myFileSize);
        assertEquals("testSetFileSize faild: ", lFleSize, actFileSize);
    }

    @Test
    public void testSetCommandType() {
        Integer myCommandType = 3;
        jadeTransferDetailDBItem.setCommandType(myCommandType);
        Integer actCommandType = jadeTransferDetailDBItem.getCommandType();
        assertEquals("testSetCommandType faild: ", myCommandType, actCommandType);
    }

    @Test
    public void testSetCommand() {
        String myCommand = "myCommand";
        jadeTransferDetailDBItem.setCommand(myCommand);
        String actCommand = jadeTransferDetailDBItem.getCommand();
        assertEquals("testSetCommand faild: ", myCommand, actCommand);
    }

    @Test
    public void testSetPid() {
        String myPid = "myPid";
        jadeTransferDetailDBItem.setPid(myPid);
        String actPid = jadeTransferDetailDBItem.getPid();
        assertEquals("testSetPid faild: ", myPid, actPid);
    }

    @Test
    public void testSetLastErrorMessage() {
        String myLastErrorMessage = "myLastErrorMessage";
        jadeTransferDetailDBItem.setLastErrorMessage(myLastErrorMessage);
        String actLastErrorMessage = jadeTransferDetailDBItem.getLastErrorMessage();
        assertEquals("testSetLastErrorMessage faild: ", myLastErrorMessage, actLastErrorMessage);
    }

    @Test
    public void testSetTargetFilename() {
        String myTargetFilename = "myTargetFielname";
        jadeTransferDetailDBItem.setTargetFilename(myTargetFilename);
        String actTargetFilename = jadeTransferDetailDBItem.getTargetFilename();
        assertEquals("testSetTargetFielname faild: ", myTargetFilename, actTargetFilename);
    }

    @Test
    public void testSetSourceFilename() {
        String mySourceFilename = "mySourceFilename";
        jadeTransferDetailDBItem.setSourceFilename(mySourceFilename);
        String actSourceFilename = jadeTransferDetailDBItem.getSourceFilename();
        assertEquals("testSetSourceFilename faild: ", mySourceFilename, actSourceFilename);
    }

    @Test
    public void testSetMd5() {
        String myMd5 = "myMd5";
        jadeTransferDetailDBItem.setMd5(myMd5);
        String actMd5 = jadeTransferDetailDBItem.getMd5();
        assertEquals("testSetMd5 faild: ", myMd5, actMd5);
    }

    @Test
    public void testSetStatus() {
        Integer myStatus = 32;
        jadeTransferDetailDBItem.setStatus(myStatus);
        Integer actStatus = jadeTransferDetailDBItem.getStatus();
        assertEquals("testSetStatus faild: ", myStatus, actStatus);
    }

    @Test
    public void testSetStartTime() {
        Date myStartTime = new Date();
        jadeTransferDetailDBItem.setStartTime(myStartTime);
        Date actStartTime = jadeTransferDetailDBItem.getStartTime();
        assertEquals("testSetStartTime faild: ", 0, myStartTime.compareTo(actStartTime));
    }

    @Test
    public void testSetEndTime() {
        Date myEndTime = new Date();
        jadeTransferDetailDBItem.setEndTime(myEndTime);
        Date actEndTime = jadeTransferDetailDBItem.getEndTime();
        assertEquals("testSetEndTime faild: ", 0, myEndTime.compareTo(actEndTime));
    }

    @Test
    public void testGetStartTimeIso() {
        assertEquals("testGetStartTimeIso faild: ", "", jadeTransferDetailDBItem.getStartTimeIso());
        Date myStartTime = new Date();
        jadeTransferDetailDBItem.setStartTime(myStartTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String getStartTimeIso = formatter.format(jadeTransferDetailDBItem.getStartTime());
        String myStartTimeIso = formatter.format(myStartTime);
        assertEquals("testSetEndTime faild: ", myStartTimeIso, getStartTimeIso);
    }

    @Test
    public void testGetEndTimeIso() {
        assertEquals("testGetEndTimeIso faild: ", "", jadeTransferDetailDBItem.getEndTimeIso());
        Date myEndTime = new Date();
        jadeTransferDetailDBItem.setEndTime(myEndTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String getEndTimeIso = formatter.format(jadeTransferDetailDBItem.getEndTime());
        String myEndTimeIso = formatter.format(myEndTime);
        assertEquals("testSetEndTime faild: ", myEndTimeIso, getEndTimeIso);
    }

    @Test
    public void testSetModifiedBy() {
        String myModifiedBy = "myModifiedBy";
        jadeTransferDetailDBItem.setModifiedBy(myModifiedBy);
        String actModifiedBy = jadeTransferDetailDBItem.getModifiedBy();
        assertEquals("testSetModifiedBy faild: ", myModifiedBy, actModifiedBy);
    }

    @Test
    public void testSetCreatedBy() {
        String myCreatedBy = "myCreatedBy";
        jadeTransferDetailDBItem.setCreatedBy(myCreatedBy);
        String actCreatedBy = jadeTransferDetailDBItem.getCreatedBy();
        assertEquals("testSetCreatedBy faild: ", myCreatedBy, actCreatedBy);
    }

    @Test
    public void testSetCreated() {
        Date myCreated = new Date();
        jadeTransferDetailDBItem.setCreated(myCreated);
        Date actCreated = jadeTransferDetailDBItem.getCreated();
        assertEquals("testSetCreated faild: ", 0, myCreated.compareTo(actCreated));
    }

    @Test
    public void testSetModified() {
        Date myModified = new Date();
        jadeTransferDetailDBItem.setModified(myModified);
        Date actModified = jadeTransferDetailDBItem.getModified();
        assertEquals("testSetCreated faild: ", 0, myModified.compareTo(actModified));
    }
 
}