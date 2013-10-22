package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the Enzymes XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 12, 2011
 */
public class EnzymesTags {

    private static EnzymesTags enzymesTags;
    public final String ENZYMES = "enzymes";
    public final String ENZYME = "enzyme";

    private EnzymesTags() {
    }

    /**
     * Return a EnzymesTags
     *
     * @return a EnzymesTags
     */
    public static synchronized EnzymesTags getInstance() {
        if (enzymesTags == null) {
            enzymesTags = new EnzymesTags();
        }
        return enzymesTags;
    }
}
