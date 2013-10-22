package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the Dosages XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class DosagesTags {

    private static DosagesTags dosagesTags;
    public final String DOSAGES = "dosages";
    public final String DOSAGE = "dosage";
    public final String FORM = "form";
    public final String ROUTE = "route";
    public final String STRENGTH = "strength";

    private DosagesTags() {
    }

    /**
     * Return a DosagesTags
     *
     * @return a DosagesTags
     */
    public static synchronized DosagesTags getInstance() {
        if (dosagesTags == null) {
            dosagesTags = new DosagesTags();
        }
        return dosagesTags;
    }
}
