//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.10.09 um 03:42:30 PM CEST 
//


package com.sos.jade.converter.generated;

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
 *       &lt;choice>
 *         &lt;element ref="{}HTTPProxy"/>
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
    "httpProxy"
})
@XmlRootElement(name = "ProxyForWebDAV")
public class ProxyForWebDAV {

    @XmlElement(name = "HTTPProxy")
    protected AuthenticatedProxyType httpProxy;

    /**
     * Ruft den Wert der httpProxy-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AuthenticatedProxyType }
     *     
     */
    public AuthenticatedProxyType getHTTPProxy() {
        return httpProxy;
    }

    /**
     * Legt den Wert der httpProxy-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthenticatedProxyType }
     *     
     */
    public void setHTTPProxy(AuthenticatedProxyType value) {
        this.httpProxy = value;
    }

}
