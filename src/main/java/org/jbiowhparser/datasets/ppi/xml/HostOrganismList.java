package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhparser.datasets.ppi.xml.tags.HostOrganismListTags;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML HostOrganismList Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 21, 2010
 * @see
 */
public class HostOrganismList extends HostOrganismListTags {

    private int depth = 0;
    private boolean open = false;
    private long MIExperimentWID = 0;
    private BioSourceType hostOrganism = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public HostOrganismList() {
        open = false;

        hostOrganism = new BioSourceType(getHOSTORGANISMFLAGS());
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
                if (hostOrganism.isOpen()) {
                    hostOrganism.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getHOSTORGANISMLISTFLAGS())) {
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
    public void startElement(String qname, int depth, Attributes attributes, long MIExperimentWID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getHOSTORGANISMFLAGS())) {
                    hostOrganism.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (hostOrganism.isOpen()) {
                    hostOrganism.startElement(qname, depth, attributes, this.MIExperimentWID);
                }
            }
        }
        if (qname.equals(getHOSTORGANISMLISTFLAGS())) {
            this.depth = depth;
            open = true;

            this.MIExperimentWID = MIExperimentWID;

            hostOrganism.setOpen(false);
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
                if (hostOrganism.isOpen()) {
                    hostOrganism.characters(tagname, qname, depth);
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
