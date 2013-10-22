package org.jbiowhparser.datasets.protclust.xml;

import org.jbiowhparser.datasets.protclust.xml.tags.UniRefXMLTags;
import org.xml.sax.Attributes;

/**
 * This Class is the UniRef start XML def
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Aug 19, 2011
 */
public class UniRefXML extends UniRefXMLTags {

    private int depth = 0;
    private boolean open = false;
    private Entry entry = null;

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (entry.isOpen()) {
                    entry.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(UNIREF)
                || qname.equals(UNIREF50)
                || qname.equals(UNIREF90)
                || qname.equals(UNIREF100)) {
            open = false;
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     */
    public void startElement(String qname, int depth, Attributes attributes) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(entry.ENTRY)) {
                    entry.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (entry.isOpen()) {
                    entry.startElement(qname, depth, attributes);
                }
            }
        }
        if (qname.equals(UNIREF)
                || qname.equals(UNIREF50)
                || qname.equals(UNIREF90)
                || qname.equals(UNIREF100)) {
            this.depth = depth;
            open = true;

            entry = new Entry();
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
                if (entry.isOpen()) {
                    entry.characters(tagname, qname, depth);
                }
            }
        }
    }
}
