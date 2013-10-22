package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the Carriers XMl Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 12, 2011
 */
public class CarriersTags {

    private static CarriersTags carriersTags;
    public final String CARRIERS = "carriers";
    public final String CARRIER = "carrier";

    private CarriersTags() {
    }

    /**
     * Return a CarriersTags
     *
     * @return a CarriersTags
     */
    public static synchronized CarriersTags getInstance() {
        if (carriersTags == null) {
            carriersTags = new CarriersTags();
        }
        return carriersTags;
    }
}
