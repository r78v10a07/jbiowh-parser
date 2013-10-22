package org.jbiowhparser.datasets.protein.xml;

/**
 * This Class handled the XML on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 29, 2010
 * @see
 */
public class Uniprot {

    private Entry entry = null;

    public Uniprot() {
        entry = new Entry();
    }

    public Entry getEntry() {
        return entry;
    }
}
