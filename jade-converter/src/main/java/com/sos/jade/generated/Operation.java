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
 *         &lt;element ref="{}Copy"/>
 *         &lt;element ref="{}Move"/>
 *         &lt;element ref="{}Remove"/>
 *         &lt;element ref="{}GetList"/>
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
    "copy",
    "move",
    "remove",
    "getList"
})
@XmlRootElement(name = "Operation")
public class Operation {

    @XmlElement(name = "Copy")
    protected Copy copy;
    @XmlElement(name = "Move")
    protected Move move;
    @XmlElement(name = "Remove")
    protected Remove remove;
    @XmlElement(name = "GetList")
    protected GetList getList;

    /**
     * Gets the value of the copy property.
     * 
     * @return
     *     possible object is
     *     {@link Copy }
     *     
     */
    public Copy getCopy() {
        return copy;
    }

    /**
     * Sets the value of the copy property.
     * 
     * @param value
     *     allowed object is
     *     {@link Copy }
     *     
     */
    public void setCopy(Copy value) {
        this.copy = value;
    }

    /**
     * Gets the value of the move property.
     * 
     * @return
     *     possible object is
     *     {@link Move }
     *     
     */
    public Move getMove() {
        return move;
    }

    /**
     * Sets the value of the move property.
     * 
     * @param value
     *     allowed object is
     *     {@link Move }
     *     
     */
    public void setMove(Move value) {
        this.move = value;
    }

    /**
     * Gets the value of the remove property.
     * 
     * @return
     *     possible object is
     *     {@link Remove }
     *     
     */
    public Remove getRemove() {
        return remove;
    }

    /**
     * Sets the value of the remove property.
     * 
     * @param value
     *     allowed object is
     *     {@link Remove }
     *     
     */
    public void setRemove(Remove value) {
        this.remove = value;
    }

    /**
     * Gets the value of the getList property.
     * 
     * @return
     *     possible object is
     *     {@link GetList }
     *     
     */
    public GetList getGetList() {
        return getList;
    }

    /**
     * Sets the value of the getList property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetList }
     *     
     */
    public void setGetList(GetList value) {
        this.getList = value;
    }

}
