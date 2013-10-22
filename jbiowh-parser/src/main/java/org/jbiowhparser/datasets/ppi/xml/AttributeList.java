package org.jbiowhparser.datasets.ppi.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.AttributeListTags;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML AttributeList Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 20, 2010
 * @see
 */
public class AttributeList extends AttributeListTags {

    private int depth = 0;
    private boolean open = false;
    private long otherWID = 0;
    private Attribute attribute = null;
    private ArrayList<Attribute> attributeList = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public AttributeList() {
        open = false;
        attributeList = new ArrayList<>();
    }

    /**
     * This is the endElement method
     *
     * @param qname the tags name
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (attribute.open) {
                    attribute.open = false;
                    attributeList.add(attribute);
                }
            }
        }
        if (qname != null) {
            if (qname.equals(getATTRIBUTELISTFLAGS())) {
                open = false;

                if (!attributeList.isEmpty()) {
                    Iterator<Attribute> it = attributeList.iterator();
                    while (it.hasNext()) {
                        attribute = it.next();

                        ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERATTRIBUTE, WIDFactory.getInstance().getWid(), "\t");
                        WIDFactory.getInstance().increaseWid();
                        ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERATTRIBUTE, otherWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERATTRIBUTE, attribute.attribute, "\t");
                        ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERATTRIBUTE, attribute.nameAc, "\t");
                        ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERATTRIBUTE, attribute.name, "\t");
                        ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERATTRIBUTE, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
                    }
                    attributeList.clear();
                }
            }
        }
    }

    /**
     * This is the startElement method
     *
     * @param qname the tags name
     * @param depth the depth on the XML file
     * @param attributes tag's attributes
     */
    public void startElement(String qname, int depth, Attributes attributes, long otherWID) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (qname.equals(getATTRIBUTEFLAGS())) {
                    attribute = new Attribute();
                    attribute.name = attributes.getValue(getNAMEFLAGS());
                    attribute.nameAc = attributes.getValue(getNAMEACFLAGS());
                }
            }
        }
        if (qname.equals(getATTRIBUTELISTFLAGS())) {
            this.depth = depth;
            open = true;
            this.otherWID = otherWID;

            attribute = null;
            attributeList.clear();
        }
    }

    /**
     * This is the characters method
     *
     * @param tagname the tags name
     * @param qname text value
     * @param depth the depth on the XML file
     */
    public void characters(String tagname, String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (attribute.open) {
                    attribute.attribute = qname;
                }
            }
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    private class Attribute {

        private boolean open = false;
        private String attribute = null;
        private String name = null;
        private String nameAc = null;

        public Attribute() {
            open = true;
        }
    }
}
