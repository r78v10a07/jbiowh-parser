package org.jbiowhparser.datasets.ontology.xml.tags;

/**
 * This Class storage the GO-Ontology XML Header Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 23, 2010
 * @see
 */
public class GOHeaderTags extends GOCommonTags {

    private final String HEADER = "header";
    private final String SUBSETDEF = "subsetdef";

    /**
     * Get the HEADER tag
     *
     * @return HEADER
     */
    public String getHEADER() {
        return HEADER;
    }

    /**
     * Get the SUBSETDEF tag
     *
     * @return SUBSETDEF
     */
    public String getSUBSETDEF() {
        return SUBSETDEF;
    }
}
