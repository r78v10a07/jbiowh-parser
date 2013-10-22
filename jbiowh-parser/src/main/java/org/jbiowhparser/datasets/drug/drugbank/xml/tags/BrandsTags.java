package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the Brands XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class BrandsTags {

    private static BrandsTags brandsTags;
    public final String BRANDS = "brands";
    public final String BRAND = "brand";

    private BrandsTags() {
    }

    /**
     * Return a BrandsTags
     *
     * @return a BrandsTags
     */
    public static synchronized BrandsTags getInstance() {
        if (brandsTags == null) {
            brandsTags = new BrandsTags();
        }
        return brandsTags;
    }
}
