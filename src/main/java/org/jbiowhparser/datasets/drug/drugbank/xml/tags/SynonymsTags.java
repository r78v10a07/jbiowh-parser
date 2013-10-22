package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the Synonyms XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class SynonymsTags {

    public final String SYNONYMS = "synonyms";
    public final String SYNONYM = "synonym";
    private static SynonymsTags singleton;

    private SynonymsTags() {
    }

    /**
     * Return a SynonymsTags
     *
     * @return a SynonymsTags
     */
    public static synchronized SynonymsTags getInstance() {
        if (singleton == null) {
            singleton = new SynonymsTags();
        }
        return singleton;
    }
}
