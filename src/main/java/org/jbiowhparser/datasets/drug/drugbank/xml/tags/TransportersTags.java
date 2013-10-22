package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is Transporters XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 12, 2011
 */
public class TransportersTags {

    public final String TRANSPORTERS = "transporters";
    public final String TRANSPORTER = "transporter";
    private static TransportersTags singleton;

    private TransportersTags() {
    }

    /**
     * Return a TransportersTags
     *
     * @return a TransportersTags
     */
    public static synchronized TransportersTags getInstance() {
        if (singleton == null) {
            singleton = new TransportersTags();
        }
        return singleton;
    }
}
