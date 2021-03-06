//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.10.09 um 03:42:30 PM CEST 
//


package com.sos.jade.converter.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}FTPFragment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}FTPSFragment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}HTTPFragment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}HTTPSFragment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}JumpFragment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}SFTPFragment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}SMBFragment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}WebDAVFragment" maxOccurs="unbounded" minOccurs="0"/>
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
    "ftpFragment",
    "ftpsFragment",
    "httpFragment",
    "httpsFragment",
    "jumpFragment",
    "sftpFragment",
    "smbFragment",
    "webDAVFragment"
})
@XmlRootElement(name = "ProtocolFragments")
public class ProtocolFragments {

    @XmlElement(name = "FTPFragment")
    protected List<FTPFragment> ftpFragment;
    @XmlElement(name = "FTPSFragment")
    protected List<FTPSFragment> ftpsFragment;
    @XmlElement(name = "HTTPFragment")
    protected List<HTTPFragment> httpFragment;
    @XmlElement(name = "HTTPSFragment")
    protected List<HTTPSFragment> httpsFragment;
    @XmlElement(name = "JumpFragment")
    protected List<JumpFragment> jumpFragment;
    @XmlElement(name = "SFTPFragment")
    protected List<SFTPFragment> sftpFragment;
    @XmlElement(name = "SMBFragment")
    protected List<SMBFragment> smbFragment;
    @XmlElement(name = "WebDAVFragment")
    protected List<WebDAVFragment> webDAVFragment;

    /**
     * Gets the value of the ftpFragment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ftpFragment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFTPFragment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FTPFragment }
     * 
     * 
     */
    public List<FTPFragment> getFTPFragment() {
        if (ftpFragment == null) {
            ftpFragment = new ArrayList<FTPFragment>();
        }
        return this.ftpFragment;
    }

    /**
     * Gets the value of the ftpsFragment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ftpsFragment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFTPSFragment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FTPSFragment }
     * 
     * 
     */
    public List<FTPSFragment> getFTPSFragment() {
        if (ftpsFragment == null) {
            ftpsFragment = new ArrayList<FTPSFragment>();
        }
        return this.ftpsFragment;
    }

    /**
     * Gets the value of the httpFragment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the httpFragment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHTTPFragment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HTTPFragment }
     * 
     * 
     */
    public List<HTTPFragment> getHTTPFragment() {
        if (httpFragment == null) {
            httpFragment = new ArrayList<HTTPFragment>();
        }
        return this.httpFragment;
    }

    /**
     * Gets the value of the httpsFragment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the httpsFragment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHTTPSFragment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HTTPSFragment }
     * 
     * 
     */
    public List<HTTPSFragment> getHTTPSFragment() {
        if (httpsFragment == null) {
            httpsFragment = new ArrayList<HTTPSFragment>();
        }
        return this.httpsFragment;
    }

    /**
     * Gets the value of the jumpFragment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the jumpFragment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJumpFragment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JumpFragment }
     * 
     * 
     */
    public List<JumpFragment> getJumpFragment() {
        if (jumpFragment == null) {
            jumpFragment = new ArrayList<JumpFragment>();
        }
        return this.jumpFragment;
    }

    /**
     * Gets the value of the sftpFragment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sftpFragment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSFTPFragment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SFTPFragment }
     * 
     * 
     */
    public List<SFTPFragment> getSFTPFragment() {
        if (sftpFragment == null) {
            sftpFragment = new ArrayList<SFTPFragment>();
        }
        return this.sftpFragment;
    }

    /**
     * Gets the value of the smbFragment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the smbFragment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSMBFragment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SMBFragment }
     * 
     * 
     */
    public List<SMBFragment> getSMBFragment() {
        if (smbFragment == null) {
            smbFragment = new ArrayList<SMBFragment>();
        }
        return this.smbFragment;
    }

    /**
     * Gets the value of the webDAVFragment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the webDAVFragment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWebDAVFragment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WebDAVFragment }
     * 
     * 
     */
    public List<WebDAVFragment> getWebDAVFragment() {
        if (webDAVFragment == null) {
            webDAVFragment = new ArrayList<WebDAVFragment>();
        }
        return this.webDAVFragment;
    }

}
