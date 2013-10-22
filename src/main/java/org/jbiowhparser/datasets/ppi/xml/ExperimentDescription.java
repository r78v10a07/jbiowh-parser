package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.ExperimentDescriptionTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 21, 2010
 * @see
 */
public class ExperimentDescription extends ExperimentDescriptionTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long MIFEntryWID = 0;
    private String id = null;
    private Names names = null;
    private Bibref bibref = null;
    private Xref xref = null;
    private AttributeList attributeList = null;
    private HostOrganismList hostOrganismList = null;
    private CVType interactionDetectionMethod = null;
    private CVType participantIdentificationMethod = null;
    private CVType featureDetectionMethod = null;
    private ConfidenceList confidenceList = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public ExperimentDescription() {
        open = false;

        names = new Names();
        attributeList = new AttributeList();
        bibref = new Bibref();
        xref = new Xref();
        hostOrganismList = new HostOrganismList();
        interactionDetectionMethod = new CVType(getINTERACTIONDETECTIONMETHODFLAGS());
        participantIdentificationMethod = new CVType(getPARTICIPANTIDENTIFICATIONMETHODFLAGS());
        featureDetectionMethod = new CVType(getFEATUREDETECTIONMETHODFLAGS());
        confidenceList = new ConfidenceList();
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
                if (bibref.isOpen()) {
                    bibref.endElement(qname, depth);
                }
                if (xref.isOpen()) {
                    xref.endElement(qname, depth);
                }
                if (hostOrganismList.isOpen()) {
                    hostOrganismList.endElement(qname, depth);
                }
                if (interactionDetectionMethod.isOpen()) {
                    interactionDetectionMethod.endElement(qname, depth, MIF25Tables.getInstance().MIFEXPERIMENTINTERDETECMETHOD);
                }
                if (participantIdentificationMethod.isOpen()) {
                    participantIdentificationMethod.endElement(qname, depth, MIF25Tables.getInstance().MIFEXPERIMENTPARTIDENTMETHOD);
                }
                if (featureDetectionMethod.isOpen()) {
                    featureDetectionMethod.endElement(qname, depth, MIF25Tables.getInstance().MIFEXPERIMENTFEATDETECMETHOD);
                }
                if (confidenceList.isOpen()) {
                    confidenceList.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getEXPERIMENTDESCRIPTIONFLAGS())) {
            open = false;

            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYEXPERIMENT, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYEXPERIMENT, MIFEntryWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYEXPERIMENT, id, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYEXPERIMENT, names.getShortLabel(), "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYEXPERIMENT, names.getFullName(), "\n");
        }
    }

    /**
     * This is the startElement method
     *
     * @param qname the tags name
     * @param depth the depth on the XML file
     * @param attributes tag's attributes
     */
    public void startElement(String qname, int depth, Attributes attributes, long MIFEntryWID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(names.getNAMESFLAGS())) {
                    names.setOpen(true);
                }
                if (qname.equals(attributeList.getATTRIBUTELISTFLAGS())) {
                    attributeList.setOpen(true);
                }
                if (qname.equals(bibref.getBIBREFFLAGS())) {
                    bibref.setOpen(true);
                }
                if (qname.equals(xref.getXREFFLAGS())) {
                    xref.setOpen(true);
                }
                if (qname.equals(hostOrganismList.getHOSTORGANISMLISTFLAGS())) {
                    hostOrganismList.setOpen(true);
                }
                if (qname.equals(getINTERACTIONDETECTIONMETHODFLAGS())) {
                    interactionDetectionMethod.setOpen(true);
                }
                if (qname.equals(getPARTICIPANTIDENTIFICATIONMETHODFLAGS())) {
                    participantIdentificationMethod.setOpen(true);
                }
                if (qname.equals(getFEATUREDETECTIONMETHODFLAGS())) {
                    featureDetectionMethod.setOpen(true);
                }
                if (qname.equals(confidenceList.getCONFIDENCELISTFLAGS())) {
                    confidenceList.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (names.isOpen()) {
                    names.startElement(qname, depth, attributes);
                }
                if (attributeList.isOpen()) {
                    attributeList.startElement(qname, depth, attributes, WID);
                }
                if (bibref.isOpen()) {
                    bibref.startElement(qname, depth, attributes, WID);
                }
                if (xref.isOpen()) {
                    xref.startElement(qname, depth, attributes, WID);
                }
                if (hostOrganismList.isOpen()) {
                    hostOrganismList.startElement(qname, depth, attributes, WID);
                }
                if (interactionDetectionMethod.isOpen()) {
                    interactionDetectionMethod.startElement(qname, depth, attributes, WID);
                }
                if (participantIdentificationMethod.isOpen()) {
                    participantIdentificationMethod.startElement(qname, depth, attributes, WID);
                }
                if (featureDetectionMethod.isOpen()) {
                    featureDetectionMethod.startElement(qname, depth, attributes, WID);
                }
                if (confidenceList.isOpen()) {
                    confidenceList.startElement(qname, depth, attributes, WID);
                }
            }
        }
        if (qname.equals(getEXPERIMENTDESCRIPTIONFLAGS())) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();
            this.MIFEntryWID = MIFEntryWID;

            id = attributes.getValue(getIDFLAGS());

            names.setOpen(false);
            bibref.setOpen(false);
            attributeList.setOpen(false);
            xref.setOpen(false);
            hostOrganismList.setOpen(false);
            interactionDetectionMethod.setOpen(false);
            participantIdentificationMethod.setOpen(false);
            featureDetectionMethod.setOpen(false);
            confidenceList.setOpen(false);
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
                if (bibref.isOpen()) {
                    bibref.characters(tagname, qname, depth);
                }
                if (xref.isOpen()) {
                    xref.characters(tagname, qname, depth);
                }
                if (hostOrganismList.isOpen()) {
                    hostOrganismList.characters(tagname, qname, depth);
                }
                if (interactionDetectionMethod.isOpen()) {
                    interactionDetectionMethod.characters(tagname, qname, depth);
                }
                if (participantIdentificationMethod.isOpen()) {
                    participantIdentificationMethod.characters(tagname, qname, depth);
                }
                if (featureDetectionMethod.isOpen()) {
                    featureDetectionMethod.characters(tagname, qname, depth);
                }
                if (confidenceList.isOpen()) {
                    confidenceList.characters(tagname, qname, depth);
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
