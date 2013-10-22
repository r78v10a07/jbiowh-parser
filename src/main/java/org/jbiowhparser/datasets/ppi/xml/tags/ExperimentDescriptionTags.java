package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class is the ExperimentDescription XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 21, 2010
 * @see
 */
public class ExperimentDescriptionTags {

    private final String EXPERIMENTDESCRIPTIONFLAGS = "experimentDescription";
    private final String IDFLAGS = "id";
    private final String INTERACTIONDETECTIONMETHODFLAGS = "interactionDetectionMethod";
    private final String PARTICIPANTIDENTIFICATIONMETHODFLAGS = "participantIdentificationMethod";
    private final String FEATUREDETECTIONMETHODFLAGS = "featureDetectionMethod";

    public String getEXPERIMENTDESCRIPTIONFLAGS() {
        return EXPERIMENTDESCRIPTIONFLAGS;
    }

    public String getFEATUREDETECTIONMETHODFLAGS() {
        return FEATUREDETECTIONMETHODFLAGS;
    }

    public String getIDFLAGS() {
        return IDFLAGS;
    }

    public String getINTERACTIONDETECTIONMETHODFLAGS() {
        return INTERACTIONDETECTIONMETHODFLAGS;
    }

    public String getPARTICIPANTIDENTIFICATIONMETHODFLAGS() {
        return PARTICIPANTIDENTIFICATIONMETHODFLAGS;
    }
}
