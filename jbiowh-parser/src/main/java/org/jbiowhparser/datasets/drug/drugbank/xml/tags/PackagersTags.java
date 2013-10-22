package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the Packagers XML tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class PackagersTags {

    public final String PACKAGERS = "packagers";
    public final String PACKAGER = "packager";
    public final String NAME = "name";
    public final String URL = "url";
    private static PackagersTags singleton;

    private PackagersTags() {
    }

    /**
     * Return a PackagersTags
     *
     * @return a PackagersTags
     */
    public static synchronized PackagersTags getInstance() {
        if (singleton == null) {
            singleton = new PackagersTags();
        }
        return singleton;
    }
}
