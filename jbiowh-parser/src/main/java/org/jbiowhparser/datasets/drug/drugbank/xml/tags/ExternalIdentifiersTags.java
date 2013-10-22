package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the ExternalIdentifiers XML tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class ExternalIdentifiersTags {

    private static ExternalIdentifiersTags externalIdentifiersTags;
    public final String EXTERNALIDENTIFIERS = "external-identifiers";
    public final String EXTERNALIDENTIFIER = "external-identifier";
    public final String RESOURCE = "resource";
    public final String IDENTIFIER = "identifier";

    private ExternalIdentifiersTags() {
    }

    /**
     * Return a ExternalIdentifiersTags
     *
     * @return a ExternalIdentifiersTags
     */
    public static synchronized ExternalIdentifiersTags getInstance() {
        if (externalIdentifiersTags == null) {
            externalIdentifiersTags = new ExternalIdentifiersTags();
        }
        return externalIdentifiersTags;
    }
}
