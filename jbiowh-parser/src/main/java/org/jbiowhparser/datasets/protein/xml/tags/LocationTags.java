package org.jbiowhparser.datasets.protein.xml.tags;

/**
 * This Class storage the XML Location tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 30, 2010
 * @see
 */
public class LocationTags {

    private final String LOCATIONFLAGS = "location";
    private final String POSITIONTYPEFLAGS = "positionType";
    private final String POSITIONFLAGS = "position";
    private final String STATUSFLAGS = "status";
    private final String LOCATIONTYPEFLAGS = "locationType";
    private final String BEGINFLAGS = "begin";
    private final String ENDFLAGS = "end";
    private final String SEQUENCEFLAGS = "sequence";

    public String getBEGINFLAGS() {
        return BEGINFLAGS;
    }

    public String getENDFLAGS() {
        return ENDFLAGS;
    }

    public String getLOCATIONFLAGS() {
        return LOCATIONFLAGS;
    }

    public String getLOCATIONTYPEFLAGS() {
        return LOCATIONTYPEFLAGS;
    }

    public String getPOSITIONFLAGS() {
        return POSITIONFLAGS;
    }

    public String getPOSITIONTYPEFLAGS() {
        return POSITIONTYPEFLAGS;
    }

    public String getSEQUENCEFLAGS() {
        return SEQUENCEFLAGS;
    }

    public String getSTATUSFLAGS() {
        return STATUSFLAGS;
    }
}
