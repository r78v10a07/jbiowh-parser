package org.jbiowhparser.datasets.protein.xml.tags;

/**
 * This Class storage the XML Feature tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Oct 2, 2010
 * @see
 */
public class FeatureTags {

    private final String FEATUREFLAGS = "feature";
    private final String ORIGINALFLAGS = "original";
    private final String VARIATIONFLAGS = "variation";
    private final String TYPEFLAGS = "type";
    private final String STATUSFLAGS = "status";
    private final String IDFLAGS = "id";
    private final String DESCRIPTIONFLAGS = "description";
    private final String EVIDENCEFLAGS = "evidence";
    private final String REFFLAGS = "ref";

    public String getDESCRIPTIONFLAGS() {
        return DESCRIPTIONFLAGS;
    }

    public String getEVIDENCEFLAGS() {
        return EVIDENCEFLAGS;
    }

    public String getFEATUREFLAGS() {
        return FEATUREFLAGS;
    }

    public String getIDFLAGS() {
        return IDFLAGS;
    }

    public String getORIGINALFLAGS() {
        return ORIGINALFLAGS;
    }

    public String getREFFLAGS() {
        return REFFLAGS;
    }

    public String getSTATUSFLAGS() {
        return STATUSFLAGS;
    }

    public String getTYPEFLAGS() {
        return TYPEFLAGS;
    }

    public String getVARIATIONFLAGS() {
        return VARIATIONFLAGS;
    }
}
