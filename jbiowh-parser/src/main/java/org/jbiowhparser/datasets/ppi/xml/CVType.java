package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML cvType Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 21, 2010
 * @see
 */
public class CVType {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long MIFOtherWID = 0;
    private Names names = null;
    private Xref xref = null;
    private String mainTag = null;
    private ExperimentRefList experimentRefList = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public CVType(String mainTag) {
        this.mainTag = mainTag;
        open = false;

        names = new Names();
        xref = new Xref();
        experimentRefList = new ExperimentRefList();
    }

    /**
     * This is the endElement method
     *
     * @param qname the tags name
     * @param depth XML depth
     */
    public void endElement(String qname, int depth, String tableName) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (names.isOpen()) {
                    names.endElement(qname, depth, WID, MIF25Tables.getInstance().MIFOTHERALIAS);
                }
                if (xref.isOpen()) {
                    xref.endElement(qname, depth);
                }
                if (experimentRefList.isOpen()) {
                    experimentRefList.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(mainTag)) {
            open = false;

            ParseFiles.getInstance().printOnTSVFile(tableName, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(tableName, MIFOtherWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(tableName, names.getShortLabel(), "\t");
            ParseFiles.getInstance().printOnTSVFile(tableName, names.getFullName(), "\n");
        }
    }

    /**
     * This is the startElement method
     *
     * @param qname the tags name
     * @param depth the depth on the XML file
     * @param attributes tag's attributes
     */
    public void startElement(String qname, int depth, Attributes attributes, long MIFOtherWID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(names.getNAMESFLAGS())) {
                    names.setOpen(true);
                }
                if (qname.equals(xref.getXREFFLAGS())) {
                    xref.setOpen(true);
                }
                if (qname.equals(experimentRefList.getEXPERIMENTREFLISTFLAGS())) {
                    experimentRefList.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (names.isOpen()) {
                    names.startElement(qname, depth, attributes);
                }
                if (xref.isOpen()) {
                    xref.startElement(qname, depth, attributes, WID);
                }
                if (experimentRefList.isOpen()) {
                    experimentRefList.startElement(qname, depth, attributes, WID);
                }
            }
        }
        if (qname.equals(mainTag)) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();
            this.MIFOtherWID = MIFOtherWID;

            names.setOpen(false);
            xref.setOpen(false);
            experimentRefList.setOpen(false);
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
                if (names.isOpen()) {
                    names.characters(tagname, qname, depth);
                }
                if (xref.isOpen()) {
                    xref.characters(tagname, qname, depth);
                }
                if (experimentRefList.isOpen()) {
                    experimentRefList.characters(tagname, qname, depth);
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
