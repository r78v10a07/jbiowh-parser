package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.ParameterTags;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Parameter Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 29, 2010
 * @see
 */
public class Parameter extends ParameterTags {

    private int depth = 0;
    private boolean open = false;
    private long MIFParticipantWID = 0;
    private String term = null;
    private String termAc = null;
    private String unit = null;
    private String unitAc = null;
    private String base = null;
    private String exponent = null;
    private String factor = null;
    private String uncertainty = null;
    private String experimentRef = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Parameter() {
        open = false;

    }

    /**
     * This is the endElement method
     *
     * @param qname the tags name
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (qname.equals(getPARAMETERFLAGS())) {
            open = false;

            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTPARAMETER, MIFParticipantWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTPARAMETER, experimentRef, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTPARAMETER, term, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTPARAMETER, termAc, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTPARAMETER, unit, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTPARAMETER, unitAc, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTPARAMETER, base, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTPARAMETER, exponent, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTPARAMETER, factor, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTPARAMETER, uncertainty, "\n");
        }
    }

    /**
     * This is the startElement method
     *
     * @param qname the tags name
     * @param depth the depth on the XML file
     * @param attributes tag's attributes
     */
    public void startElement(String qname, int depth, Attributes attributes, long MIFParticipantWID) {
        if (qname.equals(getPARAMETERFLAGS())) {
            this.depth = depth;
            open = true;

            this.MIFParticipantWID = MIFParticipantWID;

            term = attributes.getValue(getTERMFLAGS());
            termAc = attributes.getValue(getTERMACFLAGS());
            unit = attributes.getValue(getUNITFLAGS());
            unitAc = attributes.getValue(getUNITACFLAGS());
            base = attributes.getValue(getBASEFLAGS());
            exponent = attributes.getValue(getEXPONENTFLAGS());
            factor = attributes.getValue(getFACTORFLAGS());
            uncertainty = attributes.getValue(getUNCERTAINTYFLAGS());
            experimentRef = null;

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
                if (tagname.equals(getEXPERIMENTREFFLAGS())) {
                    experimentRef = qname;
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
