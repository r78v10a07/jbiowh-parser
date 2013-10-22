package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML FeatureRange Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 29, 2010
 * @see
 */
public class FeatureRangeTags {

    private final String FEATURERANGEFLAGS = "featureRange";
    private final String STARTSTATUSFLAGS = "startStatus";
    private final String ENDSTATUSFLAGS = "endStatus";
    private final String BEGINFLAGS = "begin";
    private final String POSITIONFLAGS = "position";
    private final String BEGININTERVALFLAGS = "beginInterval";
    private final String ENDFLAGS = "end";
    private final String ENDINTERVALFLAGS = "endInterval";
    private final String ISLINKFLAGS = "isLink";

    public String getBEGINFLAGS() {
        return BEGINFLAGS;
    }

    public String getBEGININTERVALFLAGS() {
        return BEGININTERVALFLAGS;
    }

    public String getENDFLAGS() {
        return ENDFLAGS;
    }

    public String getENDINTERVALFLAGS() {
        return ENDINTERVALFLAGS;
    }

    public String getENDSTATUSFLAGS() {
        return ENDSTATUSFLAGS;
    }

    public String getFEATURERANGEFLAGS() {
        return FEATURERANGEFLAGS;
    }

    public String getISLINKFLAGS() {
        return ISLINKFLAGS;
    }

    public String getPOSITIONFLAGS() {
        return POSITIONFLAGS;
    }

    public String getSTARTSTATUSFLAGS() {
        return STARTSTATUSFLAGS;
    }
}
