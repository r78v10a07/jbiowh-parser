package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.BibrefTags;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Bibref Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 20, 2010
 * @see
 */
public class Bibref extends BibrefTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long MIFSourceWID = 0;
    private AttributeList attributeList = null;
    private Xref xref = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Bibref() {
        open = false;

        attributeList = new AttributeList();
        xref = new Xref();
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
                if (attributeList.isOpen()) {
                    attributeList.endElement(qname, depth);
                }
                if (xref.isOpen()) {
                    xref.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getBIBREFFLAGS())) {
            open = false;

            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERBIBREF, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERBIBREF, MIFSourceWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERBIBREF, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
        }
    }

    /**
     * This is the startElement method
     *
     * @param qname the tags name
     * @param depth the depth on the XML file
     * @param attributes tag's attributes
     */
    public void startElement(String qname, int depth, Attributes attributes, long MIFSourceWID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(attributeList.getATTRIBUTELISTFLAGS())) {
                    attributeList.setOpen(true);
                }
                if (qname.equals(xref.getXREFFLAGS())) {
                    xref.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (attributeList.isOpen()) {
                    attributeList.startElement(qname, depth, attributes, WID);
                }
                if (xref.isOpen()) {
                    xref.startElement(qname, depth, attributes, WID);
                }
            }
        }
        if (qname.equals(getBIBREFFLAGS())) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();
            this.MIFSourceWID = MIFSourceWID;

            attributeList.setOpen(false);
            xref.setOpen(false);
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
                if (attributeList.isOpen()) {
                    attributeList.characters(tagname, qname, depth);
                }
                if (xref.isOpen()) {
                    xref.characters(tagname, qname, depth);
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
}
