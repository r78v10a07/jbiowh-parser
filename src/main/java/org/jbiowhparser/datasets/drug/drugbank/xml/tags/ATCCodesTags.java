package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the ATCCodes XMl Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class ATCCodesTags {

    private static ATCCodesTags aTCCodesTags;
    public final String ATCCODES = "atc-codes";
    public final String ATCCODE = "atc-code";

    private ATCCodesTags() {
    }

    /**
     * Return a ATCCodesTags
     *
     * @return a ATCCodesTags
     */
    public static synchronized ATCCodesTags getInstance() {
        if (aTCCodesTags == null) {
            aTCCodesTags = new ATCCodesTags();
        }
        return aTCCodesTags;
    }
}
