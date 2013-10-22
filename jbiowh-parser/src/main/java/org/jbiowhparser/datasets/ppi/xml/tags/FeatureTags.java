package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML Feature Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 25, 2010
 * @see
 */
public class FeatureTags {

    private final String FEATUREFLAGS = "feature";
    private final String IDFLAGS = "id";
    private final String FEATURETYPEFLAGS = "featureType";
    private final String FEATUREDETECTIONMETHODFLAGS = "featureDetectionMethod";

    public String getFEATUREDETECTIONMETHODFLAGS() {
        return FEATUREDETECTIONMETHODFLAGS;
    }

    public String getFEATUREFLAGS() {
        return FEATUREFLAGS;
    }

    public String getFEATURETYPEFLAGS() {
        return FEATURETYPEFLAGS;
    }

    public String getIDFLAGS() {
        return IDFLAGS;
    }
}
