package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the ExternalLinks XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class ExternalLinksTags {

    private static ExternalLinksTags externalLinksTags;
    public final String EXTERNALLINKS = "external-links";
    public final String EXTERNALLINK = "external-link";
    public final String RESOURCE = "resource";
    public final String URL = "url";

    private ExternalLinksTags() {
    }

    /**
     * Return a ExternalLinksTags
     *
     * @return a ExternalLinksTags
     */
    public static synchronized ExternalLinksTags getInstance() {
        if (externalLinksTags == null) {
            externalLinksTags = new ExternalLinksTags();
        }
        return externalLinksTags;
    }
}
