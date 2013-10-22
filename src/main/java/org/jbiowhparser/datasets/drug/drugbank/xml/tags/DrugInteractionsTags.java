package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the DrugInteractions XML tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class DrugInteractionsTags {

    private static DrugInteractionsTags drugInteractionsTags;
    public final String DRUGINTERACTIONS = "drug-interactions";
    public final String DRUGINTERACTION = "drug-interaction";
    public final String DRUG = "drug";
    public final String DESCRIPTION = "description";

    private DrugInteractionsTags() {
    }

    /**
     * Return a DrugInteractionsTags
     *
     * @return a DrugInteractionsTags
     */
    public static synchronized DrugInteractionsTags getInstance() {
        if (drugInteractionsTags == null) {
            drugInteractionsTags = new DrugInteractionsTags();
        }
        return drugInteractionsTags;
    }
}
