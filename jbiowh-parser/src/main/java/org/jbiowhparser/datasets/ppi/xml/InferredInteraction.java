package org.jbiowhparser.datasets.ppi.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.InferredInteractionTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML InferredInteraction Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 29, 2010
 * @see
 */
public class InferredInteraction extends InferredInteractionTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long MIFInteractionWID = 0;
    private ExperimentRefList experimentRefList = null;
    private Participant participant = null;
    private ArrayList<Participant> participants = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public InferredInteraction() {
        open = false;

        experimentRefList = new ExperimentRefList();
        participants = new ArrayList<>();
    }

    /**
     * This is the endElement method
     *
     * @param qname the tags name
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getPARTICIPANTFLAGS())) {
                    if (participant != null) {
                        if (participant.open) {
                            participant.open = false;
                            participants.add(participant);
                        }
                    }
                }
            }
            if (depth >= this.depth + 1) {
                if (experimentRefList.isOpen()) {
                    experimentRefList.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getINFERREDINTERACTIONFLAGS())) {
            open = false;

            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFINTERACTIONINFERREDINTERACTION, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFINTERACTIONINFERREDINTERACTION, MIFInteractionWID, "\n");

            if (!participants.isEmpty()) {
                for (Iterator<Participant> it = participants.iterator(); it.hasNext();) {
                    participant = it.next();
                    ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFINFERREDINTERACTIONPARTICIPANT, WID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFINFERREDINTERACTIONPARTICIPANT, participant.participant, "\t");
                    ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFINFERREDINTERACTIONPARTICIPANT, participant.participantType, "\n");
                }
                participants.clear();
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
    public void startElement(String qname, int depth, Attributes attributes, long MIFInteractionWID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(experimentRefList.getEXPERIMENTREFLISTFLAGS())) {
                    experimentRefList.setOpen(true);
                }
                if (qname.equals(getPARTICIPANTFLAGS())) {
                    participant = new Participant();
                }
            }
            if (depth >= this.depth + 1) {
                if (experimentRefList.isOpen()) {
                    experimentRefList.startElement(qname, depth, attributes, WID);
                }
            }
        }
        if (qname.equals(getINFERREDINTERACTIONFLAGS())) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();
            this.MIFInteractionWID = MIFInteractionWID;

            experimentRefList.setOpen(false);
            participants.clear();
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
            if (depth == this.depth + 2) {
                if (participant != null) {
                    if (participant.open) {
                        if (tagname.equals(getPARTICIPANTREFFLAGS())) {
                            participant.participant = qname;
                            participant.participantType = getPARTICIPANTREFFLAGS();
                        }
                        if (tagname.equals(getPARTICIPANTFEATUREREFFLAGS())) {
                            participant.participant = qname;
                            participant.participantType = getPARTICIPANTFEATUREREFFLAGS();
                        }
                    }
                }
            }
            if (depth >= this.depth + 1) {
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

    private class Participant {

        private boolean open = false;
        private String participant = null;
        private String participantType = null;

        public Participant() {
            open = true;
            participant = null;
            participantType = null;
        }
    }
}
