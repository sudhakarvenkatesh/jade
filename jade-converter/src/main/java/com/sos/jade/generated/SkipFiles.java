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
 *       &lt;choice>
 *         &lt;sequence>
 *           &lt;element ref="{}SkipFirstFiles" minOccurs="0"/>
 *           &lt;element ref="{}SkipLastFiles" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "skipFirstFiles",
    "skipLastFiles"
})
@XmlRootElement(name = "SkipFiles")
public class SkipFiles {

    @XmlElement(name = "SkipFirstFiles", defaultValue = "0")
    protected Integer skipFirstFiles;
    @XmlElement(name = "SkipLastFiles", defaultValue = "0")
    protected Integer skipLastFiles;

    /**
     * Gets the value of the skipFirstFiles property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSkipFirstFiles() {
        return skipFirstFiles;
    }

    /**
     * Sets the value of the skipFirstFiles property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSkipFirstFiles(Integer value) {
        this.skipFirstFiles = value;
    }

    /**
     * Gets the value of the skipLastFiles property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSkipLastFiles() {
        return skipLastFiles;
    }

    /**
     * Sets the value of the skipLastFiles property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSkipLastFiles(Integer value) {
        this.skipLastFiles = value;
    }

}
