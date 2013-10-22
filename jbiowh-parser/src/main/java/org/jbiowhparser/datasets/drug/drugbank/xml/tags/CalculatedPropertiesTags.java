package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the CalculatedProperties XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class CalculatedPropertiesTags {

    private static CalculatedPropertiesTags calculatedPropertiesTags;
    public final String CALCULATEDPROPERTIES = "calculated-properties";

    private CalculatedPropertiesTags() {
    }

    /**
     * Return a CalculatedPropertiesTags
     *
     * @return a CalculatedPropertiesTags
     */
    public static synchronized CalculatedPropertiesTags getInstance() {
        if (calculatedPropertiesTags == null) {
            calculatedPropertiesTags = new CalculatedPropertiesTags();
        }
        return calculatedPropertiesTags;
    }
}
