//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.02 at 02:31:20 PM MESZ 
//


package com.sos.jade.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}PollInterval" minOccurs="0"/>
 *         &lt;element ref="{}PollTimeout" minOccurs="0"/>
 *         &lt;element ref="{}MinFiles" minOccurs="0"/>
 *         &lt;element ref="{}WaitForSourceFolder" minOccurs="0"/>
 *         &lt;element ref="{}PollErrorState" minOccurs="0"/>
 *         &lt;element ref="{}PollingServer" minOccurs="0"/>
 *         &lt;element ref="{}PollingServerDuration" minOccurs="0"/>
 *         &lt;element ref="{}PollForever" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pollInterval",
    "pollTimeout",
    "minFiles",
    "waitForSourceFolder",
    "pollErrorState",
    "pollingServer",
    "pollingServerDuration",
    "pollForever"
})
@XmlRootElement(name = "Polling")
public class Polling {

    @XmlElement(name = "PollInterval", defaultValue = "60")
    protected Integer pollInterval;
    @XmlElement(name = "PollTimeout", defaultValue = "0")
    protected Integer pollTimeout;
    @XmlElement(name = "MinFiles", defaultValue = "1")
    protected Integer minFiles;
    @XmlElement(name = "WaitForSourceFolder")
    protected Boolean waitForSourceFolder;
    @XmlElement(name = "PollErrorState")
    protected String pollErrorState;
    @XmlElement(name = "PollingServer", defaultValue = "false")
    protected Boolean pollingServer;
    @XmlElement(name = "PollingServerDuration", defaultValue = "0")
    protected Integer pollingServerDuration;
    @XmlElement(name = "PollForever", defaultValue = "false")
    protected Boolean pollForever;

    /**
     * Gets the value of the pollInterval property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPollInterval() {
        return pollInterval;
    }

    /**
     * Sets the value of the pollInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPollInterval(Integer value) {
        this.pollInterval = value;
    }

    /**
     * Gets the value of the pollTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPollTimeout() {
        return pollTimeout;
    }

    /**
     * Sets the value of the pollTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPollTimeout(Integer value) {
        this.pollTimeout = value;
    }

    /**
     * Gets the value of the minFiles property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMinFiles() {
        return minFiles;
    }

    /**
     * Sets the value of the minFiles property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMinFiles(Integer value) {
        this.minFiles = value;
    }

    /**
     * Gets the value of the waitForSourceFolder property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isWaitForSourceFolder() {
        return waitForSourceFolder;
    }

    /**
     * Sets the value of the waitForSourceFolder property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWaitForSourceFolder(Boolean value) {
        this.waitForSourceFolder = value;
    }

    /**
     * Gets the value of the pollErrorState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPollErrorState() {
        return pollErrorState;
    }

    /**
     * Sets the value of the pollErrorState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPollErrorState(String value) {
        this.pollErrorState = value;
    }

    /**
     * Gets the value of the pollingServer property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPollingServer() {
        return pollingServer;
    }

    /**
     * Sets the value of the pollingServer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPollingServer(Boolean value) {
        this.pollingServer = value;
    }

    /**
     * Gets the value of the pollingServerDuration property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPollingServerDuration() {
        return pollingServerDuration;
    }

    /**
     * Sets the value of the pollingServerDuration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPollingServerDuration(Integer value) {
        this.pollingServerDuration = value;
    }

    /**
     * Gets the value of the pollForever property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPollForever() {
        return pollForever;
    }

    /**
     * Sets the value of the pollForever property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPollForever(Boolean value) {
        this.pollForever = value;
    }

}
