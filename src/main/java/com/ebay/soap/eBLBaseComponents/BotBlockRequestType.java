
package com.ebay.soap.eBLBaseComponents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * 
 * 				Container of token and user reply.
 * 			
 * 
 * <p>Java class for BotBlockRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BotBlockRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BotBlockToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BotBlockUserInput" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;any/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BotBlockRequestType", propOrder = {
    "botBlockToken",
    "botBlockUserInput",
    "any"
})
public class BotBlockRequestType
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "BotBlockToken")
    protected String botBlockToken;
    @XmlElement(name = "BotBlockUserInput")
    protected String botBlockUserInput;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the botBlockToken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBotBlockToken() {
        return botBlockToken;
    }

    /**
     * Sets the value of the botBlockToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBotBlockToken(String value) {
        this.botBlockToken = value;
    }

    /**
     * Gets the value of the botBlockUserInput property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBotBlockUserInput() {
        return botBlockUserInput;
    }

    /**
     * Sets the value of the botBlockUserInput property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBotBlockUserInput(String value) {
        this.botBlockUserInput = value;
    }

    /**
     * 
     * 
     * @return
     *     array of
     *     {@link Object }
     *     {@link Element }
     *     
     */
    public Object[] getAny() {
        if (this.any == null) {
            return new Object[ 0 ] ;
        }
        return ((Object[]) this.any.toArray(new Object[this.any.size()] ));
    }

    /**
     * 
     * 
     * @return
     *     one of
     *     {@link Object }
     *     {@link Element }
     *     
     */
    public Object getAny(int idx) {
        if (this.any == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.any.get(idx);
    }

    public int getAnyLength() {
        if (this.any == null) {
            return  0;
        }
        return this.any.size();
    }

    /**
     * 
     * 
     * @param values
     *     allowed objects are
     *     {@link Object }
     *     {@link Element }
     *     
     */
    public void setAny(Object[] values) {
        this._getAny().clear();
        int len = values.length;
        for (int i = 0; (i<len); i ++) {
            this.any.add(values[i]);
        }
    }

    protected List<Object> _getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return any;
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     {@link Element }
     *     
     */
    public Object setAny(int idx, Object value) {
        return this.any.set(idx, value);
    }

}
