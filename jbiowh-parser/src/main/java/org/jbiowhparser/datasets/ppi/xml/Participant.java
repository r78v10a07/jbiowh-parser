package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.ParticipantTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Participant Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 25, 2010
 * @see
 */
public class Participant extends ParticipantTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long MIFInteractionWID = 0;
    private Names names = null;
    private Xref xref = null;
    private AttributeList attributeList = null;
    private String id = null;
    private String interactorRef = null;
    private String interactionRef = null;
    private Interactor interactor = null;
    private CVTypeList participantIdentificationMethodList = null;
    private CVType biologicalRole = null;
    private CVTypeList experimentalRoleList = null;
    private CVTypeList experimentalPreparationList = null;
    private ExperimentalInteractorList experimentalInteractorList = null;
    private FeatureList featureList = null;
    private HostOrganismList hostOrganismList = null;
    private ConfidenceList confidenceList = null;
    private ParameterList parameterList = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Participant() {
        open = false;

        names = new Names();
        attributeList = new AttributeList();
        xref = new Xref();
        interactor = new Interactor();
        participantIdentificationMethodList = new CVTypeList(
                getPARTICIPANTIDENTIFICATIONMETHODLISTFLAGS(), getPARTICIPANTIDENTIFICATIONMETHODFLAGS());
        biologicalRole = new CVType(getBIOLOGICALROLEFLAGS());
        experimentalRoleList = new CVTypeList(
                getEXPERIMENTALROLELISTFLAGS(), getEXPERIMENTALROLEFLAGS());
        experimentalPreparationList = new CVTypeList(
                getEXPERIMENTALPREPARATIONLISTFLAGS(), getEXPERIMENTALPREPARATIONFLAGS());
        experimentalInteractorList = new ExperimentalInteractorList();
        featureList = new FeatureList();
        hostOrganismList = new HostOrganismList();
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
                if (attributeList.isOpen()) {
                    attributeList.endElement(qname, depth);
                }
                if (xref.isOpen()) {
                    xref.endElement(qname, depth);
                }
                if (interactor.isOpen()) {
                    interactor.endElement(qname, depth);
                }
                if (participantIdentificationMethodList.isOpen()) {
                    participantIdentificationMethodList.endElement(qname, depth, MIF25Tables.getInstance().MIFPARTICIPANTPARTIDENTMETH);
                }
                if (biologicalRole.isOpen()) {
                    biologicalRole.endElement(qname, depth, MIF25Tables.getInstance().MIFPARTICIPANTBIOLOGICALROLE);
                }
                if (experimentalRoleList.isOpen()) {
                    experimentalRoleList.endElement(qname, depth, MIF25Tables.getInstance().MIFPARTICIPANTEXPERIMENTALROLE);
                }
                if (experimentalPreparationList.isOpen()) {
                    experimentalPreparationList.endElement(qname, depth, MIF25Tables.getInstance().MIFPARTICIPANTEXPERIMENTALPREPARATION);
                }
                if (experimentalInteractorList.isOpen()) {
                    experimentalInteractorList.endElement(qname, depth);
                }
                if (featureList.isOpen()) {
                    featureList.endElement(qname, depth);
                }
                if (hostOrganismList.isOpen()) {
                    hostOrganismList.endElement(qname, depth);
                }
                if (confidenceList.isOpen()) {
                    confidenceList.endElement(qname, depth);
                }
                if (parameterList.isOpen()) {
                    parameterList.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getPARTICIPANTFLAGS())) {

            open = false;

            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFINTERACTIONPARTICIPANT, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFINTERACTIONPARTICIPANT, MIFInteractionWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFINTERACTIONPARTICIPANT, id, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFINTERACTIONPARTICIPANT, names.getShortLabel(), "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFINTERACTIONPARTICIPANT, names.getFullName(), "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFINTERACTIONPARTICIPANT, interactorRef, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFINTERACTIONPARTICIPANT, interactionRef, "\n");
        }
    }

    /**
     * This is the startElement method
     *
     * @param qname the tags name
     * @param depth the depth on the XML file
     * @param attributes tag's attributes
     */
    public void startElement(String qname, int depth, Attributes attributes, long MIFInteractionWID) {
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
                if (qname.equals(interactor.getINTERACTORFLAGS())) {
                    interactor.setOpen(true);
                }
                if (qname.equals(getPARTICIPANTIDENTIFICATIONMETHODLISTFLAGS())) {
                    participantIdentificationMethodList.setOpen(true);
                }
                if (qname.equals(getBIOLOGICALROLEFLAGS())) {
                    biologicalRole.setOpen(true);
                }
                if (qname.equals(getEXPERIMENTALROLELISTFLAGS())) {
                    experimentalRoleList.setOpen(true);
                }
                if (qname.equals(getEXPERIMENTALPREPARATIONFLAGS())) {
                    experimentalPreparationList.setOpen(true);
                }
                if (qname.equals(experimentalInteractorList.getEXPERIMENTALINTERACTORLISTFLAGS())) {
                    experimentalInteractorList.setOpen(true);
                }
                if (qname.equals(featureList.getFEATURELISTFLAGS())) {
                    featureList.setOpen(true);
                }
                if (qname.equals(hostOrganismList.getHOSTORGANISMLISTFLAGS())) {
                    hostOrganismList.setOpen(true);
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
                if (attributeList.isOpen()) {
                    attributeList.startElement(qname, depth, attributes, WID);
                }
                if (xref.isOpen()) {
                    xref.startElement(qname, depth, attributes, WID);
                }
                if (interactor.isOpen()) {
                    interactor.startElement(qname, depth, attributes, WID);
                }
                if (participantIdentificationMethodList.isOpen()) {
                    participantIdentificationMethodList.startElement(qname, depth, attributes, WID);
                }
                if (biologicalRole.isOpen()) {
                    biologicalRole.startElement(qname, depth, attributes, WID);
                }
                if (experimentalRoleList.isOpen()) {
                    experimentalRoleList.startElement(qname, depth, attributes, WID);
                }
                if (experimentalPreparationList.isOpen()) {
                    experimentalPreparationList.startElement(qname, depth, attributes, WID);
                }
                if (experimentalInteractorList.isOpen()) {
                    experimentalInteractorList.startElement(qname, depth, attributes, WID);
                }
                if (featureList.isOpen()) {
                    featureList.startElement(qname, depth, attributes, WID);
                }
                if (hostOrganismList.isOpen()) {
                    hostOrganismList.startElement(qname, depth, attributes, WID);
                }
                if (confidenceList.isOpen()) {
                    confidenceList.startElement(qname, depth, attributes, WID);
                }
                if (parameterList.isOpen()) {
                    parameterList.startElement(qname, depth, attributes, WID);
                }
            }
        }
        if (qname.equals(getPARTICIPANTFLAGS())) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();
            this.MIFInteractionWID = MIFInteractionWID;

            id = attributes.getValue(getIDFLAGS());

            names.setOpen(false);
            attributeList.setOpen(false);
            xref.setOpen(false);
            interactionRef = null;
            interactorRef = null;
            interactor.setOpen(false);
            participantIdentificationMethodList.setOpen(false);
            biologicalRole.setOpen(false);
            experimentalRoleList.setOpen(false);
            experimentalPreparationList.setOpen(false);
            experimentalInteractorList.setOpen(false);
            featureList.setOpen(false);
            hostOrganismList.setOpen(false);
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
                if (tagname.equals(getINTERACTIONREFFLAGS())) {
                    interactionRef = qname;
                }
                if (tagname.equals(getINTERACTORREFFLAGS())) {
                    interactorRef = qname;
                }
            }
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
                if (interactor.isOpen()) {
                    interactor.characters(tagname, qname, depth);
                }
                if (participantIdentificationMethodList.isOpen()) {
                    participantIdentificationMethodList.characters(tagname, qname, depth);
                }
                if (biologicalRole.isOpen()) {
                    biologicalRole.characters(tagname, qname, depth);
                }
                if (experimentalRoleList.isOpen()) {
                    experimentalRoleList.characters(tagname, qname, depth);
                }
                if (experimentalPreparationList.isOpen()) {
                    experimentalPreparationList.characters(tagname, qname, depth);
                }
                if (experimentalInteractorList.isOpen()) {
                    experimentalInteractorList.characters(tagname, qname, depth);
                }
                if (featureList.isOpen()) {
                    featureList.characters(tagname, qname, depth);
                }
                if (hostOrganismList.isOpen()) {
                    hostOrganismList.characters(tagname, qname, depth);
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
