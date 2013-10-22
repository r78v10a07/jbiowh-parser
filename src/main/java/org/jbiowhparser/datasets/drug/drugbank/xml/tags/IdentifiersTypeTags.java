package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the IdentifiersType XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 15, 2011
 */
public class IdentifiersTypeTags {

    private static IdentifiersTypeTags identifiersTypeTags;
    public final String EXTERNALIDENTIFIERS = "external-identifiers";
    public final String EXTERNALIDENTIFIER = "external-identifier";
    public final String RESOURCE = "resource";
    public final String IDENTIFIER = "identifier";

    private IdentifiersTypeTags() {
    }

    /**
     * Return a IdentifiersTypeTags
     *
     * @return a IdentifiersTypeTags
     */
    public static synchronized IdentifiersTypeTags getInstance() {
        if (identifiersTypeTags == null) {
            identifiersTypeTags = new IdentifiersTypeTags();
        }
        return identifiersTypeTags;
    }
}
