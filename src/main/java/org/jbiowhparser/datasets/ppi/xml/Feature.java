package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.FeatureTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Feature Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 25, 2010
 * @see
 */
public class Feature extends FeatureTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long MIFParticipantWID = 0;
    private String id = null;
    private Names names = null;
    private Xref xref = null;
    private AttributeList attributeList = null;
    private CVType featureType = null;
    private CVType featureDetectionMethod = null;
    private ExperimentRefList experimentRefList = null;
    private FeatureRangeList featureRangeList = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Feature() {
        open = false;

        names = new Names();
        attributeList = new AttributeList();
        xref = new Xref();
        featureType = new CVType(getFEATURETYPEFLAGS());
        featureDetectionMethod = new CVType(getFEATUREDETECTIONMETHODFLAGS());
        experimentRefList = new ExperimentRefList();
        featureRangeList = new FeatureRangeList();
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
                if (names.isOpen()) {
                    names.endElement(qname, depth, WID, MIF25Tables.getInstance().MIFOTHERALIAS);
                }
                if (attributeList.isOpen()) {
                    attributeList.endElement(qname, depth);
                }
                if (xref.isOpen()) {
                    xref.endElement(qname, depth);
                }
                if (featureType.isOpen()) {
                    featureType.endElement(qname, depth, MIF25Tables.getInstance().MIFFEATUREFEATURETYPE);
                }
                if (featureDetectionMethod.isOpen()) {
                    featureDetectionMethod.endElement(qname, depth, MIF25Tables.getInstance().MIFFEATUREFEATDETMETH);
                }
                if (experimentRefList.isOpen()) {
                    experimentRefList.endElement(qname, depth);
                }
                if (featureRangeList.isOpen()) {
                    featureRangeList.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getFEATUREFLAGS())) {

            open = false;

            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTFEATURE, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTFEATURE, MIFParticipantWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTFEATURE, id, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTFEATURE, names.getShortLabel(), "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTFEATURE, names.getFullName(), "\n");
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
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(names.getNAMESFLAGS())) {
                    names.setOpen(true);
                }
                if (qname.equals(attributeList.getATTRIBUTELISTFLAGS())) {
                    attributeList.setOpen(true);
                }
                if (qname.equals(xref.getXREFFLAGS())) {
                    xref.setOpen(true);
                }
                if (qname.equals(getFEATURETYPEFLAGS())) {
                    featureType.setOpen(true);
                }
                if (qname.equals(getFEATUREDETECTIONMETHODFLAGS())) {
                    featureDetectionMethod.setOpen(true);
                }
                if (qname.equals(experimentRefList.getEXPERIMENTREFLISTFLAGS())) {
                    experimentRefList.setOpen(true);
                }
                if (qname.equals(featureRangeList.getFEATURERANGELISTFLAGS())) {
                    featureRangeList.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (names.isOpen()) {
                    names.startElement(qname, depth, attributes);
                }
                if (attributeList.isOpen()) {
                    attributeList.startElement(qname, depth, attributes, WID);
                }
                if (xref.isOpen()) {
                    xref.startElement(qname, depth, attributes, WID);
                }
                if (featureType.isOpen()) {
                    featureType.startElement(qname, depth, attributes, WID);
                }
                if (featureDetectionMethod.isOpen()) {
                    featureDetectionMethod.startElement(qname, depth, attributes, WID);
                }
                if (experimentRefList.isOpen()) {
                    experimentRefList.startElement(qname, depth, attributes, WID);
                }
                if (featureRangeList.isOpen()) {
                    featureRangeList.startElement(qname, depth, attributes, WID);
                }
            }
        }
        if (qname.equals(getFEATUREFLAGS())) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();
            this.MIFParticipantWID = MIFParticipantWID;

            id = attributes.getValue(getIDFLAGS());

            names.setOpen(false);
            attributeList.setOpen(false);
            xref.setOpen(false);
            featureType.setOpen(false);
            featureDetectionMethod.setOpen(false);
            experimentRefList.setOpen(false);
            featureRangeList.setOpen(false);
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
                if (attributeList.isOpen()) {
                    attributeList.characters(tagname, qname, depth);
                }
                if (xref.isOpen()) {
                    xref.characters(tagname, qname, depth);
                }
                if (featureType.isOpen()) {
                    featureType.characters(tagname, qname, depth);
                }
                if (featureDetectionMethod.isOpen()) {
                    featureDetectionMethod.characters(tagname, qname, depth);
                }
                if (experimentRefList.isOpen()) {
                    experimentRefList.characters(tagname, qname, depth);
                }
                if (featureRangeList.isOpen()) {
                    featureRangeList.characters(tagname, qname, depth);
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
