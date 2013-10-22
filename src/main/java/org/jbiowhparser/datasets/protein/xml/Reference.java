package org.jbiowhparser.datasets.protein.xml;

import org.jbiowhparser.datasets.protein.xml.tags.ReferenceTags;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Reference tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 30, 2010
 * @see
 */
public class Reference extends ReferenceTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    DBReference dbReference = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Reference() {
        open = false;

        dbReference = new DBReference();
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 2) {
                dbReference.endElement(qname, depth, true);
            }
        }
        if (qname.equals(getREFERENCEFLAGS())) {
            open = false;
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     */
    public void startElement(String qname, int depth, Attributes attributes, long WID) {
        if (open) {
            if (depth >= this.depth + 2) {
                dbReference.startElement(qname, depth, attributes, this.WID);
            }
        }
        if (qname.equals(getREFERENCEFLAGS())) {
            this.depth = depth;
            this.WID = WID;
            open = true;
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
