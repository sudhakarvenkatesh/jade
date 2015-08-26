//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.02 at 02:31:20 PM MESZ 
//


package com.sos.jade.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element ref="{}FTPPreProcessing" minOccurs="0"/>
 *         &lt;element ref="{}FTPPostProcessing" minOccurs="0"/>
 *         &lt;element ref="{}SFTPPreProcessing" minOccurs="0"/>
 *         &lt;element ref="{}SFTPPostProcessing" minOccurs="0"/>
 *         &lt;element ref="{}Rename" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "ftpPreProcessing",
    "ftpPostProcessing",
    "sftpPreProcessing",
    "sftpPostProcessing",
    "rename"
})
@XmlRootElement(name = "WriteableAlternativeFragmentRef")
public class WriteableAlternativeFragmentRef {

    @XmlElement(name = "FTPPreProcessing")
    protected FTPPreProcessingType ftpPreProcessing;
    @XmlElement(name = "FTPPostProcessing")
    protected FTPPostProcessingType ftpPostProcessing;
    @XmlElement(name = "SFTPPreProcessing")
    protected SFTPPreProcessingType sftpPreProcessing;
    @XmlElement(name = "SFTPPostProcessing")
    protected SFTPPostProcessingType sftpPostProcessing;
    @XmlElement(name = "Rename")
    protected RenameType rename;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String ref;

    /**
     * Gets the value of the ftpPreProcessing property.
     * 
     * @return
     *     possible object is
     *     {@link FTPPreProcessingType }
     *     
     */
    public FTPPreProcessingType getFTPPreProcessing() {
        return ftpPreProcessing;
    }

    /**
     * Sets the value of the ftpPreProcessing property.
     * 
     * @param value
     *     allowed object is
     *     {@link FTPPreProcessingType }
     *     
     */
    public void setFTPPreProcessing(FTPPreProcessingType value) {
        this.ftpPreProcessing = value;
    }

    /**
     * Gets the value of the ftpPostProcessing property.
     * 
     * @return
     *     possible object is
     *     {@link FTPPostProcessingType }
     *     
     */
    public FTPPostProcessingType getFTPPostProcessing() {
        return ftpPostProcessing;
    }

    /**
     * Sets the value of the ftpPostProcessing property.
     * 
     * @param value
     *     allowed object is
     *     {@link FTPPostProcessingType }
     *     
     */
    public void setFTPPostProcessing(FTPPostProcessingType value) {
        this.ftpPostProcessing = value;
    }

    /**
     * Gets the value of the sftpPreProcessing property.
     * 
     * @return
     *     possible object is
     *     {@link SFTPPreProcessingType }
     *     
     */
    public SFTPPreProcessingType getSFTPPreProcessing() {
        return sftpPreProcessing;
    }

    /**
     * Sets the value of the sftpPreProcessing property.
     * 
     * @param value
     *     allowed object is
     *     {@link SFTPPreProcessingType }
     *     
     */
    public void setSFTPPreProcessing(SFTPPreProcessingType value) {
        this.sftpPreProcessing = value;
    }

    /**
     * Gets the value of the sftpPostProcessing property.
     * 
     * @return
     *     possible object is
     *     {@link SFTPPostProcessingType }
     *     
     */
    public SFTPPostProcessingType getSFTPPostProcessing() {
        return sftpPostProcessing;
    }

    /**
     * Sets the value of the sftpPostProcessing property.
     * 
     * @param value
     *     allowed object is
     *     {@link SFTPPostProcessingType }
     *     
     */
    public void setSFTPPostProcessing(SFTPPostProcessingType value) {
        this.sftpPostProcessing = value;
    }

    /**
     * Gets the value of the rename property.
     * 
     * @return
     *     possible object is
     *     {@link RenameType }
     *     
     */
    public RenameType getRename() {
        return rename;
    }

    /**
     * Sets the value of the rename property.
     * 
     * @param value
     *     allowed object is
     *     {@link RenameType }
     *     
     */
    public void setRename(RenameType value) {
        this.rename = value;
    }

    /**
     * Gets the value of the ref property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRef(String value) {
        this.ref = value;
    }

}
