package org.jbiowhparser.datasets.protein.xml.tags;

/**
 * This Class storage the XML Comment tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 30, 2010
 * @see
 */
public class CommentTags {

    private final String COMMENTFLAGS = "comment";
    private final String COMMENTTYPEFLAGS = "commentType";
    private final String TEXTFLAGS = "text";
    private final String NAMEFLAGS = "name";
    private final String MASSFLAGS = "mass";
    private final String ERRORFLAGS = "error";
    private final String METHODFLAGS = "method";
    private final String TYPEFLAGS = "type";
    private final String LOCATIONTYPEFLAGS = "locationType";
    private final String EVIDENCEFLAGS = "evidence";
    private final String STATUSFLAGS = "status";
    private final String MOLECULEFLAGS = "molecule";
    private final String LINKFLAGS = "link";
    private final String URIFLAGS = "uri";
    private final String EVENTFLAGS = "event";
    private final String ORGANISMSDIFFERFLAGS = "organismsDiffer";
    private final String EXPERIMENTSFLAGS = "experiments";

    public String getCOMMENTFLAGS() {
        return COMMENTFLAGS;
    }

    public String getCOMMENTTYPEFLAGS() {
        return COMMENTTYPEFLAGS;
    }

    public String getERRORFLAGS() {
        return ERRORFLAGS;
    }

    public String getEVENTFLAGS() {
        return EVENTFLAGS;
    }

    public String getEVIDENCEFLAGS() {
        return EVIDENCEFLAGS;
    }

    public String getEXPERIMENTSFLAGS() {
        return EXPERIMENTSFLAGS;
    }

    public String getLINKFLAGS() {
        return LINKFLAGS;
    }

    public String getLOCATIONTYPEFLAGS() {
        return LOCATIONTYPEFLAGS;
    }

    public String getMASSFLAGS() {
        return MASSFLAGS;
    }

    public String getMETHODFLAGS() {
        return METHODFLAGS;
    }

    public String getMOLECULEFLAGS() {
        return MOLECULEFLAGS;
    }

    public String getNAMEFLAGS() {
        return NAMEFLAGS;
    }

    public String getORGANISMSDIFFERFLAGS() {
        return ORGANISMSDIFFERFLAGS;
    }

    public String getSTATUSFLAGS() {
        return STATUSFLAGS;
    }

    public String getTEXTFLAGS() {
        return TEXTFLAGS;
    }

    public String getTYPEFLAGS() {
        return TYPEFLAGS;
    }

    public String getURIFLAGS() {
        return URIFLAGS;
    }

}
