package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the Prices XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class PricesTags {

    public final String PRICES = "prices";
    public final String PRICE = "price";
    public final String DESCRIPTION = "description";
    public final String COST = "cost";
    public final String UNIT = "unit";
    public final String CURRENCY = "currency";
    private static PricesTags singleton;

    private PricesTags() {
    }

    /**
     * Return a PricesTags
     *
     * @return a PricesTags
     */
    public static synchronized PricesTags getInstance() {
        if (singleton == null) {
            singleton = new PricesTags();
        }
        return singleton;
    }
}
