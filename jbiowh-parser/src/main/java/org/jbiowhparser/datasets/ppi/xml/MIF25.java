package org.jbiowhparser.datasets.ppi.xml;

/**
 * This Class is the XML start point for MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 20, 2010
 * @see
 */
public class MIF25 {

    private EntrySet entrySet = null;

    public MIF25() {
        entrySet = new EntrySet();
    }

    public EntrySet getEntrySet() {
        return entrySet;
    }
}
