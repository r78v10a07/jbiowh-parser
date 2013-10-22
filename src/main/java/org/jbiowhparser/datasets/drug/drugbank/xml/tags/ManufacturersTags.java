package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the Manufacturers XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class ManufacturersTags {

    private static ManufacturersTags manufacturersTags;
    public final String MANUFACTURERS = "manufacturers";
    public final String MANUFACTURER = "manufacturer";
    public final String GENERIC = "generic";

    private ManufacturersTags() {
    }

    /**
     * Return a ManufacturersTags
     *
     * @return a ManufacturersTags
     */
    public static synchronized ManufacturersTags getInstance() {
        if (manufacturersTags == null) {
            manufacturersTags = new ManufacturersTags();
        }
        return manufacturersTags;
    }
}
