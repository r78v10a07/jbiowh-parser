package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the TargetBondType XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 12, 2011
 */
public class TargetBondTypeTags {

    public final String KNOWNACTION = "known-action";
    private static TargetBondTypeTags singleton;

    private TargetBondTypeTags() {
    }

    /**
     * Return a TargetBondTypeTags
     *
     * @return a TargetBondTypeTags
     */
    public static synchronized TargetBondTypeTags getInstance() {
        if (singleton == null) {
            singleton = new TargetBondTypeTags();
        }
        return singleton;
    }
}
