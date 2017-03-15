package com.sos.jade.db;

import static org.junit.Assert.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/** \class JadeTransferDBItemTest
 * 
 * \brief JadeTransferDBItemTest -
 * 
 * \details
 *
 * \section JadeTransferDBItemTest.java_intro_sec Introduction
 *
 * \section JadeTransferDBItemTest.java_samples Some Samples
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
 * Created on 20.10.2011 16:40:39 */

public class JadeTransferDBItemTest {

    @SuppressWarnings("unused")
    private final String conClassName = "JadeTransferDBItemTest";
    private JadeTransferDBItem jadeTransferDBItem;
    private String configurationFilename = "c:/temp/hibernate.cfg.xml";
    private File configurationFile;

    public JadeTransferDBItemTest() {
        //
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

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        configurationFile = new File(configurationFilename);
        jadeTransferDBItem = new JadeTransferDBItem();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testJadeTransferDBItem() {
        JadeTransferDBItem jadeTransferDBItem = new JadeTransferDBItem();
    }

    @Test
    public void testSetTransferId() {
        Long myTransferId = new Long(2);
        jadeTransferDBItem.setTransferId(myTransferId);
        Long TransferId = jadeTransferDBItem.getTransferId();
        assertEquals("testSetTransferId faild: ", myTransferId, TransferId);
    }

    @Test
    public void testSetMandator() {
        String myMandator = "myMandator";
        jadeTransferDBItem.setMandator(myMandator);
        String actMandator = jadeTransferDBItem.getMandator();
        assertEquals("testSetMandator faild: ", myMandator, actMandator);
    }

    @Test
    public void testSetFileSize() {
        Long myFileSize = new Long(2);
        jadeTransferDBItem.setFileSize(myFileSize);
        Long actFileSize = jadeTransferDBItem.getFileSize();
        assertEquals("testSetFileSize faild: ", myFileSize, actFileSize);
    }

    @Test
    public void testSetSourceHost() {
        String mySourceHost = "mySourceHost";
        jadeTransferDBItem.setSourceHost(mySourceHost);
        String actSourceHost = jadeTransferDBItem.getSourceHost();
        assertEquals("testSetSourceHost faild: ", mySourceHost, actSourceHost);
    }

    @Test
    public void testSetSourceHostIp() {
        String mySourceHostIp = "mySourceHostIp";
        jadeTransferDBItem.setSourceHostIp(mySourceHostIp);
        String actSourceHostIp = jadeTransferDBItem.getSourceHostIp();
        assertEquals("testSetSourceHostIp faild: ", mySourceHostIp, actSourceHostIp);
    }

    @Test
    public void testSetSourceUser() {
        String mySourceUser = "mySourceUser";
        jadeTransferDBItem.setSourceUser(mySourceUser);
        String actSourceUser = jadeTransferDBItem.getSourceUser();
        assertEquals("testSetSourceUser faild: ", mySourceUser, actSourceUser);
    }

    @Test
    public void testSetSourceDir() {
        String mySourceDir = "mySourceDir";
        jadeTransferDBItem.setSourceDir(mySourceDir);
        String actSourceDir = jadeTransferDBItem.getSourceDir();
        assertEquals("testSetSourceDir faild: ", mySourceDir, actSourceDir);
    }

    @Test
    public void testSetTargetHost() {
        String myTargetHost = "myTargetHost";
        jadeTransferDBItem.setTargetHost(myTargetHost);
        String actTargetHost = jadeTransferDBItem.getTargetHost();
        assertEquals("testSetTargetHost faild: ", myTargetHost, actTargetHost);
    }

    @Test
    public void testSetTargetHostIp() {
        String myTargetHostIp = "myTargetHostIp";
        jadeTransferDBItem.setTargetHostIp(myTargetHostIp);
        String actTargetHostIp = jadeTransferDBItem.getTargetHostIp();
        assertEquals("testSetTargetHostIp faild: ", myTargetHostIp, actTargetHostIp);
    }

    @Test
    public void testSetTargetUser() {
        String myTargetUser = "myTargetUser";
        jadeTransferDBItem.setTargetUser(myTargetUser);
        String actTargetUser = jadeTransferDBItem.getTargetUser();
        assertEquals("testSetTargetUser faild: ", myTargetUser, actTargetUser);
    }

    @Test
    public void testSetTargetDir() {
        String myTargetDir = "myTargetDir";
        jadeTransferDBItem.setTargetDir(myTargetDir);
        String actTargetDir = jadeTransferDBItem.getTargetDir();
        assertEquals("testSetTargetDir faild: ", myTargetDir, actTargetDir);
    }

    @Test
    public void testSetProtocolType() {
        Integer myProtocolType = 32;
        jadeTransferDBItem.setProtocolType(myProtocolType);
        Integer actProtocolType = jadeTransferDBItem.getProtocolType();
        assertEquals("testSetProtocolTyp faild: ", myProtocolType, actProtocolType);
    }

    @Test
    public void testSetPort() {
        Integer myPort = 45;
        jadeTransferDBItem.setPort(myPort);
        Integer actPort = jadeTransferDBItem.getPort();
        assertEquals("testSetPort faild: ", myPort, actPort);
    }

    @Test
    public void testSetFilesCount() {
        Integer myFilesCount = 324;
        jadeTransferDBItem.setFilesCount(myFilesCount);
        Integer actFilesCount = jadeTransferDBItem.getFilesCount();
        assertEquals("testSetFilesCount faild: ", myFilesCount, actFilesCount);
    }

    @Test
    public void testSetProfileName() {
        String myProfileName = "myProfileName";
        jadeTransferDBItem.setProfileName(myProfileName);
        String actProfileName = jadeTransferDBItem.getProfileName();
        assertEquals("testSetProfileName faild: ", myProfileName, actProfileName);

    }

    @Test
    public void testSetProfile() {
        String myProfile = "myProfile";
        jadeTransferDBItem.setProfile(myProfile);
        String actProfile = jadeTransferDBItem.getProfile();
        assertEquals("testSetProfile faild: ", myProfile, actProfile);
    }

    @Test
    public void testSetCommandType() {
        Integer myCommandType = 342;
        jadeTransferDBItem.setCommandType(myCommandType);
        Integer actCommandType = jadeTransferDBItem.getCommandType();
        assertEquals("testSetCommandType faild: ", myCommandType, actCommandType);
    }

    @Test
    public void testSetCommand() {
        String myCommand = "myCommand";
        jadeTransferDBItem.setCommand(myCommand);
        String actCommand = jadeTransferDBItem.getCommand();
        assertEquals("testSetCommand faild: ", myCommand, actCommand);
    }

    @Test
    public void testSetLog() {
        String myLog = "myLog";
        jadeTransferDBItem.setLog(myLog);
        String actLog = jadeTransferDBItem.getLog();
        assertEquals("testSetLog faild: ", myLog, actLog);
    }

    @Test
    public void testSetStatus() {
        Integer myStatus = 123;
        jadeTransferDBItem.setStatus(myStatus);
        Integer actStatus = jadeTransferDBItem.getStatus();
        assertEquals("testSetStatus faild: ", myStatus, actStatus);
    }

    @Test
    public void testSetLastErrorMessage() {
        String myLastErrorMessage = "myLastErrorMessage";
        jadeTransferDBItem.setLastErrorMessage(myLastErrorMessage);
        String actLastErrorMessage = jadeTransferDBItem.getLastErrorMessage();
        assertEquals("testSetLastErrorMessage faild: ", myLastErrorMessage, actLastErrorMessage);
    }

    @Test
    public void testSetStartTime() {
        Date myStartTime = new Date();
        jadeTransferDBItem.setStartTime(myStartTime);
        Date actStartTime = jadeTransferDBItem.getStartTime();
        assertEquals("testSetStartTime faild: ", 0, myStartTime.compareTo(actStartTime));
    }

    @Test
    public void testSetEndTime() {
        Date myEndTime = new Date();
        jadeTransferDBItem.setEndTime(myEndTime);
        Date actEndTime = jadeTransferDBItem.getEndTime();
        assertEquals("testSetEndTime faild: ", 0, myEndTime.compareTo(actEndTime));
    }

    @Test
    public void testGetStartTimeIso() {
        assertEquals("testGetStartTimeIso faild: ", "", jadeTransferDBItem.getStartTimeIso());

        Date myStartTime = new Date();
        jadeTransferDBItem.setStartTime(myStartTime);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String getStartTimeIso = formatter.format(jadeTransferDBItem.getStartTime());
        String myStartTimeIso = formatter.format(myStartTime);

        assertEquals("testSetEndTime faild: ", myStartTimeIso, getStartTimeIso);
    }

    @Test
    public void testGetEndTimeIso() {
        assertEquals("testGetEndTimeIso faild: ", "", jadeTransferDBItem.getEndTimeIso());

        Date myEndTime = new Date();
        jadeTransferDBItem.setEndTime(myEndTime);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String getEndTimeIso = formatter.format(jadeTransferDBItem.getEndTime());
        String myEndTimeIso = formatter.format(myEndTime);

        assertEquals("testSetEndTime faild: ", myEndTimeIso, getEndTimeIso);
    }

    @Test
    public void testSetModifiedBy() {
        String myModifiedBy = "myModifiedBy";
        jadeTransferDBItem.setModifiedBy(myModifiedBy);
        String actModifiedBy = jadeTransferDBItem.getModifiedBy();
        assertEquals("testSetModifiedBy faild: ", myModifiedBy, actModifiedBy);
    }

    @Test
    public void testSetCreatedBy() {
        String myCreatedBy = "myCreatedBy";
        jadeTransferDBItem.setCreatedBy(myCreatedBy);
        String actCreatedBy = jadeTransferDBItem.getCreatedBy();
        assertEquals("testSetCreatedBy faild: ", myCreatedBy, actCreatedBy);
    }

    @Test
    public void testSetCreated() {
        Date myCreated = new Date();
        jadeTransferDBItem.setCreated(myCreated);
        Date actCreated = jadeTransferDBItem.getCreated();
        assertEquals("testSetCreated faild: ", 0, myCreated.compareTo(actCreated));
    }

    @Test
    public void testSetModified() {
        Date myModified = new Date();
        jadeTransferDBItem.setModified(myModified);
        Date actModified = jadeTransferDBItem.getModified();
        assertEquals("testSetCreated faild: ", 0, myModified.compareTo(actModified));
    }

    @Test
    @Ignore("Test set to Ignore for later examination")
    public void testSave() throws Exception {
        JadeTransferDetailDBItem jadeTransferDetailDBItem = getNewTransferDetailDBItem();
        JadeTransferDBItem transferItem = getNewTransferItem();
        JadeTransferDBLayer jadeTransferDBLayer = new JadeTransferDBLayer(configurationFile.getAbsolutePath());
        jadeTransferDBLayer.getSession().connect();
        jadeTransferDBLayer.getSession().beginTransaction();
        transferItem.addTransferDetail(jadeTransferDetailDBItem);
        jadeTransferDBLayer.getSession().save(transferItem);
        jadeTransferDBLayer.getSession().commit();
    }
}
