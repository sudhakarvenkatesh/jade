package com.sos.jade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sos.hibernate.classes.DbItem;

/**
* \class JadeHistoryFilter 
* 
* \brief JadeHistoryFilter - 
* 
* \details
*
* \section JadeHistoryFilter.java_intro_sec Introduction
*
* \section JadeHistoryFilter.java_samples Some Samples
*
* \code
*   .... code goes here ...
* \endcode
*
* <p style="text-align:center">
* <br />---------------------------------------------------------------------------
* <br /> APL/Software GmbH - Berlin
* <br />##### generated by ClaviusXPress (http://www.sos-berlin.com) #########
* <br />---------------------------------------------------------------------------
* </p>
* \author Uwe Risse
* \version 14.12.2011
* \see reference
*
* Created on 14.12.2011 13:53:37
 */

public class JadeHistoryFilter implements com.sos.hibernate.interfaces.ISOSHibernateFilter {

	@SuppressWarnings("unused")
	private final String	conClassName	= "JadeHistoryFilter";
	private String dateFormat         = "yyyy-MM-dd HH:mm:ss";
	private Date plannedFrom;
	private Date plannedTo;
 
 	private String status="";
 	
	 
	public JadeHistoryFilter() {
		//
	}

	public Date getPlannedFrom() {
		return plannedFrom;
	}
 

	public void setPlannedFrom(Date plannedFrom) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	    String d = formatter.format(plannedFrom);
		try {
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.plannedFrom = formatter.parse(d);
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setPlannedFrom(String plannedFrom) throws ParseException {
		if (plannedFrom.equals("")) {
			this.plannedFrom = null;
		}
		else {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			Date d = formatter.parse(plannedFrom);
			setPlannedFrom(d);
		  }
		}

	public void setPlannedFrom(String plannedFrom, String dateFormat) throws ParseException {
		this.dateFormat = dateFormat;
		setPlannedFrom(plannedFrom);
		}

	public void setPlannedTo(String plannedTo, String dateFormat) throws ParseException {
		this.dateFormat = dateFormat;
		setPlannedTo(plannedTo);
		}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}


 

	public Date getPlannedTo() {
		return plannedTo;
	}



	public void setPlannedTo(Date plannedTo) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
	    String d = formatter.format(plannedTo);
		try {
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.plannedTo = formatter.parse(d);
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	}

	public void setPlannedTo(String plannedTo) throws ParseException {
	if (plannedTo.equals("")) {
		this.plannedTo = null;
	}
	else {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		Date d = formatter.parse(plannedTo);
		setPlannedTo(d);
	  }
	}
	
	
	public boolean isFiltered(DbItem dbitem) {
	 return false;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}
	
 
}
