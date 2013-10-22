package org.jbiowhparser.datasets.protein.xml.tags;

/**
 * This Class storage the XML Conflict tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Oct 1, 2010
 * @see
 */
public class ConflictTags {

    private final String CONFLICTFLAGS = "conflict";
    private final String SEQUENCEFLAGS = "sequence";
    private final String RESOURCEFLAGS = "resource";
    private final String IDFLAGS = "id";
    private final String VERSIONFLAGS = "version";
    private final String TYPEFLAGS = "type";

    public String getCONFLICTFLAGS() {
        return CONFLICTFLAGS;
    }

    public String getIDFLAGS() {
        return IDFLAGS;
    }

    public String getRESOURCEFLAGS() {
        return RESOURCEFLAGS;
    }

    public String getSEQUENCEFLAGS() {
        return SEQUENCEFLAGS;
    }

    public String getTYPEFLAGS() {
        return TYPEFLAGS;
    }

    public String getVERSIONFLAGS() {
        return VERSIONFLAGS;
    }
}
