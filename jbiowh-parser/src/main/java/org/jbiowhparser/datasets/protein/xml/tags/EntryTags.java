package org.jbiowhparser.datasets.protein.xml.tags;

/**
 * This Class storage the Uniprot XML Header Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 29, 2010
 * @see
 */
public class EntryTags {
    private final String UNIPROT = "uniprot";
    private final String ENTRY = "entry";
    private final String NAME = "name";
    private final String ACCESSION = "accession";
    private final String CREATED = "created";
    private final String MODIFIED = "modified";
    private final String VERSION = "version";
    private final String DATASET = "dataset";
    private final String PROTEINFLAGS = "protein";
    private final String PROTEINEXISTENCEFLAGS = "proteinExistence";
    private final String TYPEFLAGS = "type";

    public String getACCESSION() {
        return ACCESSION;
    }

    public String getCREATED() {
        return CREATED;
    }

    public String getDATASET() {
        return DATASET;
    }

    public String getENTRY() {
        return ENTRY;
    }

    public String getMODIFIED() {
        return MODIFIED;
    }

    public String getNAME() {
        return NAME;
    }

    public String getPROTEINEXISTENCEFLAGS() {
        return PROTEINEXISTENCEFLAGS;
    }

    public String getPROTEINFLAGS() {
        return PROTEINFLAGS;
    }

    public String getTYPEFLAGS() {
        return TYPEFLAGS;
    }

    public String getUNIPROT() {
        return UNIPROT;
    }

    public String getVERSION() {
        return VERSION;
    }
}
