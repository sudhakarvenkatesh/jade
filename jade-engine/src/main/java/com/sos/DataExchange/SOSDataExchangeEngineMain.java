package com.sos.DataExchange;
import org.apache.log4j.Logger;

import com.sos.DataExchange.Options.JADEOptions;
import com.sos.JSHelper.Basics.JSJobUtilities;
import com.sos.JSHelper.Basics.VersionInfo;
import com.sos.JSHelper.Exceptions.ParametersMissingButRequiredException;
import com.sos.VirtualFileSystem.enums.JADEExitCodes;
import com.sos.VirtualFileSystem.exceptions.JADEException;
import com.sos.i18n.I18NBase;
import com.sos.i18n.annotation.I18NMessage;
import com.sos.i18n.annotation.I18NMessages;
import com.sos.i18n.annotation.I18NResourceBundle;

/**
 * \class 		SOSDataExchangeEngineMain - Main-Class for "Transfer files by FTP/SFTP and execute commands by SSH"
 *
 * \brief MainClass to launch sosftp as an executable command-line program
 *
 * This Class SOSDataExchangeEngineMain is the worker-class.
 *

 *
 * see \see J:\E\java\development\com.sos.scheduler\src\sos\scheduler\jobdoc\sosftp.xml for (more) details.
 *
 * \verbatim ;
 * mechanicaly created by C:\Users\KB\eclipse\xsl\JSJobDoc2JSMainClass.xsl from http://www.sos-berlin.com at 20100930175655
 * \endverbatim
 */
@I18NResourceBundle(baseName = "SOSDataExchange", defaultLocale = "en")
public class SOSDataExchangeEngineMain extends I18NBase implements JSJobUtilities {
	private final static String		conClassName	= "SOSDataExchangeEngineMain";
	public static final String		conSVNVersion	= "$Id$";
	private static Logger			logger			= Logger.getLogger(SOSDataExchangeEngineMain.class);
	protected JADEOptions			objOptions		= null;
	protected SOSDataExchangeEngine	objM;
	private static boolean			flgEntryViaMain	= false;																	// just to avoid exit on junit-tests

	/**
	 *
	 * \brief main
	 *
	 * \details
	 *
	 * \return void
	 *
	 * @param pstrArgs
	 * @throws Exception
	 */
	public final static void main(final String[] pstrArgs) {
		@SuppressWarnings("unused") final String conMethodName = conClassName + "::Main"; //$NON-NLS-1$
		// will setup basic logging to the console, and the error messages will be gone.
		org.apache.log4j.BasicConfigurator.configure();
		flgEntryViaMain = true;
		SOSDataExchangeEngineMain objEngine = new SOSDataExchangeEngineMain();
		objEngine.Execute(pstrArgs);
		System.exit(0);
	}

	protected SOSDataExchangeEngineMain() {
		super("SOSDataExchange");
	}

	/**
	 *
	 * @param pstrArgs
	 */
	private void Execute(final String[] pstrArgs) {
		final String conMethodName = conClassName + "::Execute";
		try {
			objM = new SOSDataExchangeEngine();
			JADEOptions objO = objM.Options();
			objO.ApplicationName.Value(new SOSMsgJade("SOSJADE_T_0020").get());
			objO.ApplicationDocuUrl.Value(new SOSMsgJade("SOSJADE_T_0021").get());
			objO.AllowEmptyParameterList.setFalse();
			objO.CheckNotProcessedOptions.setTrue();
			@SuppressWarnings("unused")
			String strLog4jPropertyFileName = objO.log4jPropertyFileName.Value();
			for (String strParam : pstrArgs) {
				if (strParam.toLowerCase().startsWith("-log4jPropertyFileName")) {
					String[] strS = strParam.split("=");
					strLog4jPropertyFileName = strS[1];
				}
			}
			logger = objO.log4jPropertyFileName.getLoggerInstance(conClassName);
			objM.setJSJobUtilites(this);
			objO.SendTransferHistory.value(true);
			objO.CommandLineArgs(pstrArgs);
			if (objO.log4jPropertyFileName.isDirty()) {
				logger = objO.log4jPropertyFileName.getLoggerInstance(conClassName);
			}
			logger.info(getMsg(SOSDX_Intro) + " -- " + VersionInfo.VERSION_STRING);
			logger.debug(conSVNVersion);
			objO.CheckMandatory();

			objO.CommandLineArgs(pstrArgs);
			if (objO.CheckNotProcessedOptions.isTrue()) {
				if (objO.CheckNotProcessedOptions() == true) {
				}
				else {
					JADEException objE = new JADEException("Unsupported or wrong Options found.");
					objE.setExitCode(JADEExitCodes.UnsupportedParametersFound);
					throw objE;
				}
			}

			boolean flgResult = objM.Execute();

			objM.Logout();
			if (flgResult == false || objM.flgGlobalError == true) {
				setExitCode(99);
			}
		}
		catch (ParametersMissingButRequiredException p) {
			setExitCode(JADEExitCodes.ParametersMissingButRequired.ExitCode);
		}
		catch (JADEException e) {
			logger.error(String.format(getMsg(SOSDX_E_0001), conMethodName, e.getMessage()), e);
			setExitCode(e.getExitCode().ExitCode);
		}
		logger.info(String.format(getMsg(SOS_EXIT_WO_ERRORS), conMethodName));
	} // private void Execute

	@SuppressWarnings("unused")
	private void setExitCode() {
		setExitCode(99);
	}

	private void setExitCode(final int pintExitCode) {
		int intExitCode = pintExitCode;
		// TODO check for exit-code from the configuration: make it possible to define exit-code dependend on the error
		// e.g. no connection = 10
		//      invalid login = 11
		//      no files found = 12
		//      ...
		logger.error(String.format(getMsg(SOS_EXIT_CODE_RAISED), "JADE", intExitCode));
		if (flgEntryViaMain == true) {
			System.exit(intExitCode);
		}
		else {
			logger.info("exit with code = " + intExitCode);
		}
	}
	@I18NMessages(value = { @I18NMessage("SOSDataExchange - Main routine started ..."), //
			@I18NMessage(value = "SOSDataExchange - Main", locale = "en_UK", //
			explanation = "SOSDataExchange - Main" //
			), //
			@I18NMessage(value = "SOSDataExchange - Kommandozeilenprogram startet ....", locale = "de", //
			explanation = "SOSDataExchange - Main" //
			) //
	}, msgnum = "SOSDX-I-9999", msgurl = "")
	/*!
	 * \var SOSDX-Intro
	 * \brief SOSDataExchange - Main
	 */
	public static final String	SOSDX_Intro				= "SOSDataExchangeEngineMain.SOSDX-Intro";
	@I18NMessages(value = { @I18NMessage("%1$s: Error occurred ...: %2$s, exit-code 99 raised"), //
			@I18NMessage(value = "%1$s: Error occurred ...: %2$s", locale = "en_UK", //
			explanation = "%1$s: Error occurred ...: %2$s" //
			), //
			@I18NMessage(value = "%1$s: Fehler aufgetreten: %2$s, Programm wird mit Exit-Code 99 beendet.", locale = "de", //
			explanation = "%1$s: Error occurred ...: %2$s" //
			) //
	}, msgnum = "SOSDX-E-0001", msgurl = "")
	/*!
	 * \var SOSDX_E_0001
	 * \brief %1$s: Error occurred ...: %2$s
	 */
	public static final String	SOSDX_E_0001			= "SOSDataExchangeEngineMain.SOSDX_E_0001";
	@I18NMessages(value = { @I18NMessage("%1$s - ended without errorsended without errors"), //
			@I18NMessage(value = "%1$s - ended without errors", locale = "en_UK", //
			explanation = "%1$s - ended without errorsended without errors" //
			), //
			@I18NMessage(value = "%1$s - Programm wurde ohne Fehler beendet", locale = "de", //
			explanation = "%1$s - ended without errorsended without errors" //
			) //
	}, msgnum = "SOS-I-106", msgurl = "")
	/*!
	 * \var SOS_EXIT_WO_ERRORS
	 * \brief %1$s - ended without errorsended without errors
	 */
	public static final String	SOS_EXIT_WO_ERRORS		= "SOSDataExchangeEngineMain.SOS_EXIT_WO_ERRORS";
	@I18NMessages(value = { @I18NMessage("%1$s - terminated with exit-code %2$d"), //
			@I18NMessage(value = "%1$s - terminated with exit-code %2$d", locale = "en_UK", //
			explanation = "%1$s - terminated with exit-code %2$d" //
			), //
			@I18NMessage(value = "%1$s - Fehlercode %2$d wurde gesetzt", locale = "de", //
			explanation = "%1$s - terminated with exit-code %2$d" //
			) //
	}, msgnum = "SOSDX_E_0002", msgurl = "")
	/*!
	 * \var SOS_EXIT_CODE_RAISED
	 * \brief %1$s - terminated with exit-code %2$d
	 */
	public static final String	SOS_EXIT_CODE_RAISED	= "SOSDataExchangeEngineMain.SOS_EXIT_CODE_RAISED";

	@Override
	public String myReplaceAll(final String source, final String what, final String replacement) {
		return source;
	}

	@Override
	public String replaceSchedulerVars(final boolean isWindows, final String pstrString2Modify) {
		return pstrString2Modify;
	}

	@Override
	public void setJSParam(final String pstrKey, final String pstrValue) {
	}

	@Override
	public void setJSParam(final String pstrKey, final StringBuffer pstrValue) {
	}

	@Override
	public String getCurrentNodeName() {
		return "";
	}

	@Override
	public void setJSJobUtilites(final JSJobUtilities pobjJSJobUtilities) {
	}

	@Override
	public void setStateText(final String pstrStateText) {
	}

	@Override
	public void setCC(final int pintCC) {
	}

	@Override
	public void setNextNodeState(final String pstrNodeName) {
	}
} // class SOSDataExchangeEngineMain