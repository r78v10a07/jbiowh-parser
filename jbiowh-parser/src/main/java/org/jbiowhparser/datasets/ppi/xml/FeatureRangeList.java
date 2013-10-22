package org.jbiowhparser.datasets.ppi.xml;

import org.jbiowhparser.datasets.ppi.xml.tags.FeatureRangeListTags;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML FeatureRangeList Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 29, 2010
 * @see
 */
public class FeatureRangeList extends FeatureRangeListTags {

    private int depth = 0;
    private boolean open = false;
    private long MIFeatureWID = 0;
    private FeatureRange featureRange = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public FeatureRangeList() {
        open = false;

        featureRange = new FeatureRange();
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
                if (featureRange.isOpen()) {
                    featureRange.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getFEATURERANGELISTFLAGS())) {
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
    public void startElement(String qname, int depth, Attributes attributes, long MIFeatureWID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(featureRange.getFEATURERANGEFLAGS())) {
                    featureRange.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (featureRange.isOpen()) {
                    featureRange.startElement(qname, depth, attributes, this.MIFeatureWID);
                }
            }
        }
        if (qname.equals(getFEATURERANGELISTFLAGS())) {
            this.depth = depth;
            open = true;

            this.MIFeatureWID = MIFeatureWID;

            featureRange.setOpen(false);
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
                if (featureRange.isOpen()) {
                    featureRange.characters(tagname, qname, depth);
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
