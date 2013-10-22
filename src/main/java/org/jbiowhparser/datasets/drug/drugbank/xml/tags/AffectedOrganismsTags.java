package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is Affected Organisms XMl Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class AffectedOrganismsTags {

    private static AffectedOrganismsTags affectedOrganismsTags;
    public final String AFFECTEDORGANISMS = "affected-organisms";
    public final String AFFECTEDORGANISM = "affected-organism";

    private AffectedOrganismsTags() {
    }

    /**
     * Return a AffectedOrganismsTags
     *
     * @return a AffectedOrganismsTags
     */
    public static synchronized AffectedOrganismsTags getInstance() {
        if (affectedOrganismsTags == null) {
            affectedOrganismsTags = new AffectedOrganismsTags();
        }
        return affectedOrganismsTags;
    }
}
