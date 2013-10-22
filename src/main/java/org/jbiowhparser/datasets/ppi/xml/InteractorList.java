package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhparser.datasets.ppi.xml.tags.InteractorListTags;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML InteractorList Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 21, 2010
 * @see
 */
public class InteractorList extends InteractorListTags {

    private int depth = 0;
    private boolean open = false;
    private long MIFEntryWID = 0;
    private Interactor interactor = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public InteractorList() {
        open = false;

        interactor = new Interactor();
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
            }
        }
        if (qname.equals(getINTERACTORLISTFLAGS())) {
            open = false;
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
                if (qname.equals(interactor.getINTERACTORFLAGS())) {
                    interactor.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (interactor.isOpen()) {
                    interactor.startElement(qname, depth, attributes, this.MIFEntryWID);
                }
            }
        }
        if (qname.equals(getINTERACTORLISTFLAGS())) {
            this.depth = depth;
            open = true;

            this.MIFEntryWID = MIFEntryWID;

            interactor.setOpen(false);
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
                if (interactor.isOpen()) {
                    interactor.characters(tagname, qname, depth);
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
