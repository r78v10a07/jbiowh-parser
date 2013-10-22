package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the Patents XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class PatentsTags {

    public final String PATENTS = "patents";
    public final String PATENT = "patent";
    public final String NUMBER = "number";
    public final String COUNTRY = "country";
    public final String APPROVED = "approved";
    public final String EXPIRES = "expires";
    private static PatentsTags singleton;

    private PatentsTags() {
    }

    /**
     * Return a PatentsTags
     *
     * @return a PatentsTags
     */
    public static synchronized PatentsTags getInstance() {
        if (singleton == null) {
            singleton = new PatentsTags();
        }
        return singleton;
    }
}
