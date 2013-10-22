package org.jbiowhparser.datasets.protein.xml.tags;

/**
 * This Class storage the XML Sequence Tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Oct 3, 2010
 * @see
 */
public class SequenceTags {

    private final String SEQUENCEFLAGS = "sequence";
    private final String LENGTHFLAGS = "length";
    private final String MASSFLAGS = "mass";
    private final String CHECKSUMFLAGS = "checksum";
    private final String MODIFIEDFLAGS = "modified";
    private final String VERSIONFLAGS = "version";
    private final String PRECURSORFLAGS = "precursor";
    private final String FRAGMENTFLAGS = "fragment";

    public String getCHECKSUMFLAGS() {
        return CHECKSUMFLAGS;
    }

    public String getFRAGMENTFLAGS() {
        return FRAGMENTFLAGS;
    }

    public String getLENGTHFLAGS() {
        return LENGTHFLAGS;
    }

    public String getMASSFLAGS() {
        return MASSFLAGS;
    }

    public String getMODIFIEDFLAGS() {
        return MODIFIEDFLAGS;
    }

    public String getPRECURSORFLAGS() {
        return PRECURSORFLAGS;
    }

    public String getSEQUENCEFLAGS() {
        return SEQUENCEFLAGS;
    }

    public String getVERSIONFLAGS() {
        return VERSIONFLAGS;
    }
}
