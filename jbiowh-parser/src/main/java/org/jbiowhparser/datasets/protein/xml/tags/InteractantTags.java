package org.jbiowhparser.datasets.protein.xml.tags;

/**
 * This Class storage the XML Interactant tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Oct 2, 2010
 * @see
 */
public class InteractantTags {

    private final String INTERACTANTFLAGS = "interactant";
    private final String INTACTIDFLAGS = "intactId";
    private final String IDFLAGS = "id";
    private final String LABELFLAGS = "label";

    public String getIDFLAGS() {
        return IDFLAGS;
    }

    public String getINTACTIDFLAGS() {
        return INTACTIDFLAGS;
    }

    public String getINTERACTANTFLAGS() {
        return INTERACTANTFLAGS;
    }

    public String getLABELFLAGS() {
        return LABELFLAGS;
    }
}
