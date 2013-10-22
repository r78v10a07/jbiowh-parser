package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the Mixtures XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class MixturesTags {

    private static MixturesTags mixturesTags;
    public final String MIXTURES = "mixtures";
    public final String MIXTURE = "mixture";
    public final String NAME = "name";
    public final String INGREDIENTS = "ingredients";

    private MixturesTags() {
    }

    /**
     * Return a MixturesTags
     *
     * @return a MixturesTags
     */
    public static synchronized MixturesTags getInstance() {
        if (mixturesTags == null) {
            mixturesTags = new MixturesTags();
        }
        return mixturesTags;
    }
}
