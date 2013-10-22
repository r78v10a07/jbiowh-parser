package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the PFams XMl Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 15, 2011
 */
public class PFamsTags {

    public final String PFAMS = "pfams";
    public final String PFAM = "pfam";
    public final String IDENTIFIER = "identifier";
    public final String NAME = "name";
    private static PFamsTags singleton;

    private PFamsTags() {
    }

    /**
     * Return a PFamsTags
     *
     * @return a PFamsTags
     */
    public static synchronized PFamsTags getInstance() {
        if (singleton == null) {
            singleton = new PFamsTags();
        }
        return singleton;
    }
}
