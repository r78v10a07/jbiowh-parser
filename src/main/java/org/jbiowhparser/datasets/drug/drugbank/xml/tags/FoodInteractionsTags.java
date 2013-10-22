package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the FoodInteractions XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class FoodInteractionsTags {

    private static FoodInteractionsTags foodInteractionsTags;
    public final String FOODINTERACTIONS = "food-interactions";
    public final String FOODINTERACTION = "food-interaction";

    private FoodInteractionsTags() {
    }

    /**
     * Return a FoodInteractionsTags
     *
     * @return a FoodInteractionsTags
     */
    public static synchronized FoodInteractionsTags getInstance() {
        if (foodInteractionsTags == null) {
            foodInteractionsTags = new FoodInteractionsTags();
        }
        return foodInteractionsTags;
    }
}
