package org.jbiowhparser.datasets.protein.xml.tags;

/**
 * This Class storage the XML Isoform Tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Oct 1, 2010
 * @see
 */
public class IsoformTags {

    private final String ISOFORMFLAGS = "isoform";
    private final String ISOFORMTYPEFLAGS = "isoformType";
    private final String IDFLAGS = "id";
    private final String NAMEFLAGS = "name";
    private final String EVIDENCEFLAGS = "evidence";
    private final String SEQUENCEFLAGS = "sequence";
    private final String TYPEFLAGS = "type";
    private final String REFFLAGS = "ref";
    private final String NOTEFLAGS = "note";

    public String getEVIDENCEFLAGS() {
        return EVIDENCEFLAGS;
    }

    public String getIDFLAGS() {
        return IDFLAGS;
    }

    public String getISOFORMFLAGS() {
        return ISOFORMFLAGS;
    }

    public String getISOFORMTYPEFLAGS() {
        return ISOFORMTYPEFLAGS;
    }

    public String getNAMEFLAGS() {
        return NAMEFLAGS;
    }

    public String getNOTEFLAGS() {
        return NOTEFLAGS;
    }

    public String getREFFLAGS() {
        return REFFLAGS;
    }

    public String getSEQUENCEFLAGS() {
        return SEQUENCEFLAGS;
    }

    public String getTYPEFLAGS() {
        return TYPEFLAGS;
    }
}
