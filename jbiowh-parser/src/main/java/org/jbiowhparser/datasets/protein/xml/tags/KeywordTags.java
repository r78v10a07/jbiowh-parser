package org.jbiowhparser.datasets.protein.xml.tags;

/**
 * This Class storage the XML Keyword Tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Oct 2, 2010
 * @see
 */
public class KeywordTags {

    private final String KEYWORDFLAGS = "keyword";
    private final String EVIDENCEFLAGS = "evidence";
    private final String IDFLAGS = "id";

    public String getEVIDENCEFLAGS() {
        return EVIDENCEFLAGS;
    }

    public String getIDFLAGS() {
        return IDFLAGS;
    }

    public String getKEYWORDFLAGS() {
        return KEYWORDFLAGS;
    }
}
