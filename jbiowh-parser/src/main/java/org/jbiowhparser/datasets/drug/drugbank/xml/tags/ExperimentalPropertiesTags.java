package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is ExperimentalProperties XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class ExperimentalPropertiesTags {

    private static ExperimentalPropertiesTags experimentalPropertiesTags;
    public final String EXPERIMENTALPROPERTIES = "experimental-properties";

    private ExperimentalPropertiesTags() {
    }

    /**
     * Return a ExperimentalPropertiesTags
     *
     * @return a ExperimentalPropertiesTags
     */
    public static synchronized ExperimentalPropertiesTags getInstance() {
        if (experimentalPropertiesTags == null) {
            experimentalPropertiesTags = new ExperimentalPropertiesTags();
        }
        return experimentalPropertiesTags;
    }
}
