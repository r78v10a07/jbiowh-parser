package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.ExperimentalInteractorTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML ExperimentalInteractor Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 25, 2010
 * @see
 */
public class ExperimentalInteractor extends ExperimentalInteractorTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long MIFParticipantWID = 0;
    private String interactorRef = null;
    private Interactor interactor = null;
    private ExperimentRefList experimentRefList = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public ExperimentalInteractor() {
        open = false;

        interactor = new Interactor();
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
                if (interactor.isOpen()) {
                    interactor.endElement(qname, depth);
                }
                if (experimentRefList.isOpen()) {
                    experimentRefList.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getEXPERIMENTALINTERACTORFLAGS())) {

            open = false;

            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTEXPERIMENTALINTERACTOR, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTEXPERIMENTALINTERACTOR, MIFParticipantWID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFPARTICIPANTEXPERIMENTALINTERACTOR, interactorRef, "\n");
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
                if (qname.equals(interactor.getINTERACTORFLAGS())) {
                    interactor.setOpen(true);
                }
                if (qname.equals(experimentRefList.getEXPERIMENTREFLISTFLAGS())) {
                    experimentRefList.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (interactor.isOpen()) {
                    interactor.startElement(qname, depth, attributes, WID);
                }
                if (experimentRefList.isOpen()) {
                    experimentRefList.startElement(qname, depth, attributes, WID);
                }
            }
        }
        if (qname.equals(getEXPERIMENTALINTERACTORFLAGS())) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();
            this.MIFParticipantWID = MIFParticipantWID;

            interactorRef = null;

            interactor.setOpen(false);
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
                if (tagname.equals(getINTERACTORREFFLAGS())) {
                    interactorRef = qname;
                }
            }
            if (depth >= this.depth + 1) {
                if (interactor.isOpen()) {
                    interactor.characters(tagname, qname, depth);
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
