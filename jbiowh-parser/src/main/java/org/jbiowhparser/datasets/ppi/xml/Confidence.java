package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.ConfidenceTags;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Confidence Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 21, 2010
 * @see
 */
public class Confidence extends ConfidenceTags {

    private int depth = 0;
    private boolean open = false;
    private long MIFOtherWID = 0;
    private OpenCvType unit = null;
    private String value = null;
    private ExperimentRefList experimentRefList = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Confidence() {
        open = false;

        unit = new OpenCvType(getUNITFLAGS());
        experimentRefList = new ExperimentRefList();
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
                if (unit.isOpen()) {
                    unit.endElement(qname, depth);
                }
                if (experimentRefList.isOpen()) {
                    experimentRefList.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getCONFIDENCEFLAGS())) {
            open = false;

            if (unit.isFill()) {
                ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERCONFIDENCE, unit.getWID(), "\t");
                ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERCONFIDENCE, unit.getMIFOtherWID(), "\t");
                ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERCONFIDENCE, unit.getNames().getShortLabel(), "\t");
                ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERCONFIDENCE, unit.getNames().getFullName(), "\t");
                ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERCONFIDENCE, value, "\t");
                ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFOTHERCONFIDENCE, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
                unit.setFill(false);
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
    public void startElement(String qname, int depth, Attributes attributes, long MIFOtherWID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getUNITFLAGS())) {
                    unit.setOpen(true);
                }
                if (qname.equals(experimentRefList.getEXPERIMENTREFLISTFLAGS())) {
                    experimentRefList.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (unit.isOpen()) {
                    unit.startElement(qname, depth, attributes, this.MIFOtherWID);
                }
                if (experimentRefList.isOpen()) {
                    experimentRefList.startElement(qname, depth, attributes, this.MIFOtherWID);
                }
            }
        }
        if (qname.equals(getCONFIDENCEFLAGS())) {
            this.depth = depth;
            open = true;

            this.MIFOtherWID = MIFOtherWID;

            unit.setOpen(false);
            unit.setFill(false);
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
            if (depth == this.depth + 1) {
                if (tagname.equals(getVALUEFLAGS())) {
                    value = qname;
                }
            }
            if (depth >= this.depth + 1) {
                if (unit.isOpen()) {
                    unit.characters(tagname, qname, depth);
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
