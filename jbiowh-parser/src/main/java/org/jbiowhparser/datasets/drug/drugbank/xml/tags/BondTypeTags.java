package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the BondType XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 12, 2011
 */
public class BondTypeTags {

    private static BondTypeTags bondTypeTags;
    public final String ACTIONS = "actions";
    public final String ACTION = "action";
    public final String REFERENCES = "references";
    public final String POSITION = "position";
    public final String PARTNER = "partner";

    private BondTypeTags() {
    }

    /**
     * Return a BondTypeTags
     *
     * @return a BondTypeTags
     */
    public static synchronized BondTypeTags getInstance() {
        if (bondTypeTags == null) {
            bondTypeTags = new BondTypeTags();
        }
        return bondTypeTags;
    }
}
