//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.11.16 at 12:32:28 PM EST 
//
package org.jbiowhparser.datasets.pathway.kegg.kgml.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * This is the Relation XML parser from JAXB for KGML file format
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "subtype"
})
@XmlRootElement(name = "relation")
public class Relation {

    @XmlAttribute(name = "entry1", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String entry1;
    @XmlAttribute(name = "entry2", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String entry2;
    @XmlAttribute(name = "type", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;
    protected List<Subtype> subtype;

    /**
     * Gets the value of the entry1 property.
     *
     * @return possible object is
     *     {@link String }
     *
     */
    public String getEntry1() {
        return entry1;
    }

    /**
     * Sets the value of the entry1 property.
     *
     * @param value allowed object is
     *     {@link String }
     *
     */
    public void setEntry1(String value) {
        this.entry1 = value;
    }

    /**
     * Gets the value of the entry2 property.
     *
     * @return possible object is
     *     {@link String }
     *
     */
    public String getEntry2() {
        return entry2;
    }

    /**
     * Sets the value of the entry2 property.
     *
     * @param value allowed object is
     *     {@link String }
     *
     */
    public void setEntry2(String value) {
        this.entry2 = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     *     {@link String }
     *
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *     {@link String }
     *
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the subtype property.
     *
     * <p> This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the subtype property.
     *
     * <p> For example, to add a new item, do as follows:
     * <pre>
     *    getSubtype().add(newItem);
     * </pre>
     *
     *
     * <p> Objects of the following type(s) are allowed in the list
     * {@link Subtype }
     *
     *
     */
    public List<Subtype> getSubtype() {
        if (subtype == null) {
            subtype = new ArrayList<>();
        }
        return this.subtype;
    }
}