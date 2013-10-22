package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the SecondaryAccessionNumbers XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 10, 2011
 */
public class SecondaryAccessionNumbersTags {

    public final String SECONDARYACCESSIONNUMBERS = "secondary-accession-numbers";
    public final String SECONDARYACCESSIONNUMBER = "secondary-accession-number";
    private static SecondaryAccessionNumbersTags singleton;

    private SecondaryAccessionNumbersTags() {
    }

    /**
     * Return a SecondaryAccessionNumbersTags
     *
     * @return a SecondaryAccessionNumbersTags
     */
    public static synchronized SecondaryAccessionNumbersTags getInstance() {
        if (singleton == null) {
            singleton = new SecondaryAccessionNumbersTags();
        }
        return singleton;
    }
}
