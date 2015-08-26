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
 *         &lt;element ref="{}OrderJobChainName"/>
 *         &lt;element ref="{}CreateOrderForAllFiles" minOccurs="0"/>
 *         &lt;element ref="{}NextState" minOccurs="0"/>
 *         &lt;element ref="{}MergeOrderParameters" minOccurs="0"/>
 *         &lt;element ref="{}CreateOrderOnRemoteJobScheduler" minOccurs="0"/>
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
    "orderJobChainName",
    "createOrderForAllFiles",
    "nextState",
    "mergeOrderParameters",
    "createOrderOnRemoteJobScheduler"
})
@XmlRootElement(name = "CreateOrder")
public class CreateOrder {

    @XmlElement(name = "OrderJobChainName", required = true)
    protected String orderJobChainName;
    @XmlElement(name = "CreateOrderForAllFiles", defaultValue = "false")
    protected Boolean createOrderForAllFiles;
    @XmlElement(name = "NextState")
    protected String nextState;
    @XmlElement(name = "MergeOrderParameters", defaultValue = "false")
    protected Boolean mergeOrderParameters;
    @XmlElement(name = "CreateOrderOnRemoteJobScheduler")
    protected CreateOrderOnRemoteJobScheduler createOrderOnRemoteJobScheduler;

    /**
     * Gets the value of the orderJobChainName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderJobChainName() {
        return orderJobChainName;
    }

    /**
     * Sets the value of the orderJobChainName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderJobChainName(String value) {
        this.orderJobChainName = value;
    }

    /**
     * Gets the value of the createOrderForAllFiles property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCreateOrderForAllFiles() {
        return createOrderForAllFiles;
    }

    /**
     * Sets the value of the createOrderForAllFiles property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCreateOrderForAllFiles(Boolean value) {
        this.createOrderForAllFiles = value;
    }

    /**
     * Gets the value of the nextState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNextState() {
        return nextState;
    }

    /**
     * Sets the value of the nextState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNextState(String value) {
        this.nextState = value;
    }

    /**
     * Gets the value of the mergeOrderParameters property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMergeOrderParameters() {
        return mergeOrderParameters;
    }

    /**
     * Sets the value of the mergeOrderParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMergeOrderParameters(Boolean value) {
        this.mergeOrderParameters = value;
    }

    /**
     * Gets the value of the createOrderOnRemoteJobScheduler property.
     * 
     * @return
     *     possible object is
     *     {@link CreateOrderOnRemoteJobScheduler }
     *     
     */
    public CreateOrderOnRemoteJobScheduler getCreateOrderOnRemoteJobScheduler() {
        return createOrderOnRemoteJobScheduler;
    }

    /**
     * Sets the value of the createOrderOnRemoteJobScheduler property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreateOrderOnRemoteJobScheduler }
     *     
     */
    public void setCreateOrderOnRemoteJobScheduler(CreateOrderOnRemoteJobScheduler value) {
        this.createOrderOnRemoteJobScheduler = value;
    }

}
