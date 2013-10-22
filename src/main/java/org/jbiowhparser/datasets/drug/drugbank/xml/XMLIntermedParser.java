package org.jbiowhparser.datasets.drug.drugbank.xml;

import org.xml.sax.Attributes;

/**
 * This class is the XMLIntermed XML Parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class XMLIntermedParser {

    private int depth = 0;
    private boolean open = false;
    private long drug_WID = 0;
    private String mainTag = null;
    private MultipleFieldXMLParser multiple = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public XMLIntermedParser(String tableName, String mainTag, String[] tags) {
        open = false;

        this.mainTag = mainTag;
        multiple = new MultipleFieldXMLParser(tableName, tags);
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth, boolean hasWID) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (multiple.isOpen()) {
                    multiple.endElement(qname, depth, hasWID);
                }
            }
        }
        if (qname.equals(mainTag) && depth == this.depth) {
            open = false;
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     */
    public void startElement(String qname, int depth, Attributes attributes, long drug_WID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(multiple.getTags()[0])) {
                    multiple.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (multiple.isOpen()) {
                    multiple.startElement(qname, depth, attributes, this.drug_WID);
                }
            }
        }
        if (qname.equals(mainTag)) {

            this.depth = depth;
            open = true;

            this.drug_WID = drug_WID;
        }
    }

    /**
     *
     * @param tagname
     * @param name
     * @param depth
     */
    public void characters(String tagname, String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (multiple.isOpen()) {
                    multiple.characters(tagname, qname, depth);
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
