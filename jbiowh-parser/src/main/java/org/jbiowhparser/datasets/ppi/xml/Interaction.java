package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.InteractionTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Interaction Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 22, 2010
 * @see
 */
public class Interaction extends InteractionTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long MIFEntryWID = 0;
    private String imexId = null;
    private String id = null;
    private Names names = null;
    private Xref xref = null;
    private AttributeList attributeList = null;
    private String availabilityRef = null;
    private String modelled = null;
    private String intraMolecular = null;
    private String negative = null;
    private Availability availability = null;
    private ExperimentList experimentList = null;
    private ParticipantList participantList = null;
    private InferredInteractionList inferredInteractionList = null;
    private CVType interactionType = null;
    private ConfidenceList confidenceList = null;
    private ParameterList parameterList = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Interaction() {
        open = false;

        names = new Names();
        xref = new Xref();
        attributeList = new AttributeList();
        availability = new Availability();
        experimentList = new ExperimentList();
        participantList = new ParticipantList();
        inferredInteractionList = new InferredInteractionList();
        interactionType = new CVType(getINTERACTIONTYPEFLAGS());
        confidenceList = new ConfidenceList();
        parameterList = new ParameterList();
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
                if (xref.isOpen()) {
                    xref.endElement(qname, depth);
                }
                if (attributeList.isOpen()) {
                    attributeList.endElement(qname, depth);
                }
                if (availability.isOpen()) {
                    availability.endElement(qname, depth);
                }
                if (experimentList.isOpen()) {
                    experimentList.endElement(qname, depth);
                }
                if (participantList.isOpen()) {
                    participantList.endElement(qname, depth);
                }
                if (inferredInteractionList.isOpen()) {
                    inferredInteractionList.endElement(qname, depth);
                }
                if (interactionType.isOpen()) {
                    interactionType.endElement(qname, depth, MIF25Tables.getInstance().MIFINTERACTIONINTERACTIONTYPE);
                }
                if (confidenceList.isOpen()) {
                    confidenceList.endElement(qname, depth);
                }
                if (parameterList.isOpen()) {
                    parameterList.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getINTERACTIONFLAGS())) {

            open = false;

            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTION, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTION, MIFEntryWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTION, imexId, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTION, id, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTION, names.getShortLabel(), "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTION, names.getFullName(), "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTION, availabilityRef, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTION, modelled, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTION, intraMolecular, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYINTERACTION, negative, "\n");
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
                if (qname.equals(xref.getXREFFLAGS())) {
                    xref.setOpen(true);
                }
                if (qname.equals(attributeList.getATTRIBUTELISTFLAGS())) {
                    attributeList.setOpen(true);
                }
                if (qname.equals(availability.getAVAILABILITYFLAGS())) {
                    availability.setOpen(true);
                }
                if (qname.equals(experimentList.getEXPERIMENTLISTFLAGS())) {
                    experimentList.setOpen(true);
                }
                if (qname.equals(participantList.getPARTICIPANTLISTFLAGS())) {
                    participantList.setOpen(true);
                }
                if (qname.equals(inferredInteractionList.getINFERREDINTERACTIONLISTFLAGS())) {
                    inferredInteractionList.setOpen(true);
                }
                if (qname.equals(getINTERACTIONTYPEFLAGS())) {
                    interactionType.setOpen(true);
                }
                if (qname.equals(confidenceList.getCONFIDENCELISTFLAGS())) {
                    confidenceList.setOpen(true);
                }
                if (qname.equals(parameterList.getPARAMETERLISTFLAGS())) {
                    parameterList.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (names.isOpen()) {
                    names.startElement(qname, depth, attributes);
                }
                if (xref.isOpen()) {
                    xref.startElement(qname, depth, attributes, WID);
                }
                if (attributeList.isOpen()) {
                    attributeList.startElement(qname, depth, attributes, WID);
                }
                if (availability.isOpen()) {
                    availability.startElement(qname, depth, attributes, this.MIFEntryWID);
                }
                if (experimentList.isOpen()) {
                    experimentList.startElement(qname, depth, attributes, WID);
                }
                if (participantList.isOpen()) {
                    participantList.startElement(qname, depth, attributes, WID);
                }
                if (inferredInteractionList.isOpen()) {
                    inferredInteractionList.startElement(qname, depth, attributes, WID);
                }
                if (interactionType.isOpen()) {
                    interactionType.startElement(qname, depth, attributes, WID);
                }
                if (confidenceList.isOpen()) {
                    confidenceList.startElement(qname, depth, attributes, WID);
                }
                if (parameterList.isOpen()) {
                    parameterList.startElement(qname, depth, attributes, WID);
                }
            }
        }
        if (qname.equals(getINTERACTIONFLAGS())) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();
            this.MIFEntryWID = MIFEntryWID;

            imexId = attributes.getValue(getIMEXIDFLAGS());
            id = attributes.getValue(getIDFLAGS());

            names.setOpen(false);
            xref.setOpen(false);
            attributeList.setOpen(false);
            availabilityRef = null;
            modelled = null;
            intraMolecular = null;
            negative = null;
            availability.setOpen(false);
            experimentList.setOpen(false);
            participantList.setOpen(false);
            inferredInteractionList.setOpen(false);
            interactionType.setOpen(false);
            confidenceList.setOpen(false);
            parameterList.setOpen(false);
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
                if (tagname.equals(getAVAILABILITYREFFLAGS())) {
                    availabilityRef = qname;
                }
                if (tagname.equals(getMODELLEDFLAGS())) {
                    modelled = qname;
                }
                if (tagname.equals(getINTRAMOLECULARFLAGS())) {
                    intraMolecular = qname;
                }
                if (tagname.equals(getNEGATIVEFLAGS())) {
                    negative = qname;
                }
            }
            if (depth >= this.depth + 1) {
                if (names.isOpen()) {
                    names.characters(tagname, qname, depth);
                }
                if (xref.isOpen()) {
                    xref.characters(tagname, qname, depth);
                }
                if (attributeList.isOpen()) {
                    attributeList.characters(tagname, qname, depth);
                }
                if (availability.isOpen()) {
                    availability.characters(tagname, qname, depth);
                }
                if (experimentList.isOpen()) {
                    experimentList.characters(tagname, qname, depth);
                }
                if (participantList.isOpen()) {
                    participantList.characters(tagname, qname, depth);
                }
                if (inferredInteractionList.isOpen()) {
                    inferredInteractionList.characters(tagname, qname, depth);
                }
                if (interactionType.isOpen()) {
                    interactionType.characters(tagname, qname, depth);
                }
                if (confidenceList.isOpen()) {
                    confidenceList.characters(tagname, qname, depth);
                }
                if (parameterList.isOpen()) {
                    parameterList.characters(tagname, qname, depth);
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
