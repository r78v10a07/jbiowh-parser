package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class CategoriesTags {

    private static CategoriesTags categoriesTags;
    public final String CATEGORIES = "categories";
    public final String CATEGORY = "category";

    private CategoriesTags() {
    }

    /**
     * Return a CategoriesTags
     *
     * @return a CategoriesTags
     */
    public static synchronized CategoriesTags getInstance() {
        if (categoriesTags == null) {
            categoriesTags = new CategoriesTags();
        }
        return categoriesTags;
    }
}
