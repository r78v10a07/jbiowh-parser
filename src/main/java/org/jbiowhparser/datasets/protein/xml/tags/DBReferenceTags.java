package org.jbiowhparser.datasets.protein.xml.tags;

/**
 * This Class storage the Uniprot XML dbReference Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 30, 2010
 * @see
 */
public class DBReferenceTags {

    private final String DBREFERENCEFLAGS = "dbReference";
    private final String DBREFERENCETYPEFLAGS = "dbReferenceType";
    private final String PROPERTYFLAGS = "property";
    private final String TYPEFLAGS = "type";
    private final String IDFLAGS = "id";
    private final String EVIDENCEFLAGS = "evidence";
    private final String KEYFLAGS = "key";
    private final String PROPERTYTYPEFLAGS = "propertyType";
    private final String VALUEFLAGS = "value";

    public String getDBREFERENCEFLAGS() {
        return DBREFERENCEFLAGS;
    }

    public String getDBREFERENCETYPEFLAGS() {
        return DBREFERENCETYPEFLAGS;
    }

    public String getEVIDENCEFLAGS() {
        return EVIDENCEFLAGS;
    }

    public String getIDFLAGS() {
        return IDFLAGS;
    }

    public String getKEYFLAGS() {
        return KEYFLAGS;
    }

    public String getPROPERTYFLAGS() {
        return PROPERTYFLAGS;
    }

    public String getPROPERTYTYPEFLAGS() {
        return PROPERTYTYPEFLAGS;
    }

    public String getTYPEFLAGS() {
        return TYPEFLAGS;
    }

    public String getVALUEFLAGS() {
        return VALUEFLAGS;
    }
}
