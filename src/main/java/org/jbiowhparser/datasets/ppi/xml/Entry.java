package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.EntryTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ppi.MIF25Tables;
import org.xml.sax.Attributes;

/**
 * This Class is the Entry parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 20, 2010
 * @see
 */
public class Entry extends EntryTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long MIFEntrySetWID = 0;
    private Source source = null;
    private AvailabilityList availabilityList = null;
    private ExperimentList experimentList = null;
    private InteractorList interactorList = null;
    private InteractionList interactionList = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Entry() {
        open = false;

        source = new Source();
        availabilityList = new AvailabilityList();
        experimentList = new ExperimentList();
        interactorList = new InteractorList();
        interactionList = new InteractionList();
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
                if (source.isOpen()) {
                    source.endElement(qname, depth);
                }
                if (availabilityList.isOpen()) {
                    availabilityList.endElement(qname, depth);
                }
                if (experimentList.isOpen()) {
                    experimentList.endElement(qname, depth);
                }
                if (interactorList.isOpen()) {
                    interactorList.endElement(qname, depth);
                }
                if (interactionList.isOpen()) {
                    interactionList.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getENTRYFLAGS())) {
            open = false;

            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYSETENTRY, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(MIF25Tables.getInstance().MIFENTRYSETENTRY, MIFEntrySetWID, "\n");
        }
    }

    /**
     * This is the startElement method
     *
     * @param qname the tags name
     * @param depth the depth on the XML file
     * @param attributes tag's attributes
     */
    public void startElement(String qname, int depth, Attributes attributes, long MIFEntrySetWID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(source.getSOURCEFLAGS())) {
                    source.setOpen(true);
                }
                if (qname.equals(availabilityList.getAVAILABILITYLISTFLAGS())) {
                    availabilityList.setOpen(true);
                }
                if (qname.equals(experimentList.getEXPERIMENTLISTFLAGS())) {
                    experimentList.setOpen(true);
                }
                if (qname.equals(interactorList.getINTERACTORLISTFLAGS())) {
                    interactorList.setOpen(true);
                }
                if (qname.equals(interactionList.getINTERACTIONLISTFLAGS())) {
                    interactionList.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (source.isOpen()) {
                    source.startElement(qname, depth, attributes, WID);
                }
                if (availabilityList.isOpen()) {
                    availabilityList.startElement(qname, depth, attributes, WID);
                }
                if (experimentList.isOpen()) {
                    experimentList.startElement(qname, depth, attributes, WID);
                }
                if (interactorList.isOpen()) {
                    interactorList.startElement(qname, depth, attributes, WID);
                }
                if (interactionList.isOpen()) {
                    interactionList.startElement(qname, depth, attributes, WID);
                }
            }
        }
        if (qname.equals(getENTRYFLAGS())) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();
            this.MIFEntrySetWID = MIFEntrySetWID;

            source.setOpen(false);
            availabilityList.setOpen(false);
            experimentList.setOpen(false);
            interactorList.setOpen(false);
            interactionList.setOpen(false);
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
                if (source.isOpen()) {
                    source.characters(tagname, qname, depth);
                }
                if (availabilityList.isOpen()) {
                    availabilityList.characters(tagname, qname, depth);
                }
                if (experimentList.isOpen()) {
                    experimentList.characters(tagname, qname, depth);
                }
                if (interactorList.isOpen()) {
                    interactorList.characters(tagname, qname, depth);
                }
                if (interactionList.isOpen()) {
                    interactionList.characters(tagname, qname, depth);
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
