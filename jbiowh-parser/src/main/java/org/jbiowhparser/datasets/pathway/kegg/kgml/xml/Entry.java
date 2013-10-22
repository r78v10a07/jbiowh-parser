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
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * This is the Entry XML parser from JAXB for KGML file format
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "graphics",
    "component"
})
@XmlRootElement(name = "entry")
public class Entry {

    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String id;
    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String name;
    @XmlAttribute(name = "type", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;
    @XmlAttribute(name = "link")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String link;
    @XmlAttribute(name = "reaction")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String reaction;
    protected List<Graphics> graphics;
    protected List<Component> component;

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     *     {@link String }
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *     {@link String }
     *
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
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
     * Gets the value of the link property.
     *
     * @return possible object is
     *     {@link String }
     *
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the value of the link property.
     *
     * @param value allowed object is
     *     {@link String }
     *
     */
    public void setLink(String value) {
        this.link = value;
    }

    /**
     * Gets the value of the reaction property.
     *
     * @return possible object is
     *     {@link String }
     *
     */
    public String getReaction() {
        return reaction;
    }

    /**
     * Sets the value of the reaction property.
     *
     * @param value allowed object is
     *     {@link String }
     *
     */
    public void setReaction(String value) {
        this.reaction = value;
    }

    /**
     * Gets the value of the graphics property.
     *
     * <p> This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the graphics property.
     *
     * <p> For example, to add a new item, do as follows:
     * <pre>
     *    getGraphics().add(newItem);
     * </pre>
     *
     *
     * <p> Objects of the following type(s) are allowed in the list
     * {@link Graphics }
     *
     *
     */
    public List<Graphics> getGraphics() {
        if (graphics == null) {
            graphics = new ArrayList<>();
        }
        return this.graphics;
    }

    /**
     * Gets the value of the component property.
     *
     * <p> This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the component property.
     *
     * <p> For example, to add a new item, do as follows:
     * <pre>
     *    getComponent().add(newItem);
     * </pre>
     *
     *
     * <p> Objects of the following type(s) are allowed in the list
     * {@link Component }
     *
     *
     */
    public List<Component> getComponent() {
        if (component == null) {
            component = new ArrayList<>();
        }
        return this.component;
    }
}
