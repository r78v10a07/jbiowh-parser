package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML Participant Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 25, 2010
 * @see
 */
public class ParticipantTags {

    private final String PARTICIPANTFLAGS = "participant";
    private final String IDFLAGS = "id";
    private final String INTERACTORREFFLAGS = "interactorRef";
    private final String INTERACTIONREFFLAGS = "interactionRef";
    private final String BIOLOGICALROLEFLAGS = "biologicalRole";
    private final String PARTICIPANTIDENTIFICATIONMETHODLISTFLAGS = "participantIdentificationMethodList";
    private final String PARTICIPANTIDENTIFICATIONMETHODFLAGS = "participantIdentificationMethod";
    private final String EXPERIMENTALROLELISTFLAGS = "experimentalRoleList";
    private final String EXPERIMENTALROLEFLAGS = "experimentalRole";
    private final String EXPERIMENTALPREPARATIONLISTFLAGS = "experimentalPreparationList";
    private final String EXPERIMENTALPREPARATIONFLAGS = "experimentalPreparation";

    public String getBIOLOGICALROLEFLAGS() {
        return BIOLOGICALROLEFLAGS;
    }

    public String getEXPERIMENTALPREPARATIONFLAGS() {
        return EXPERIMENTALPREPARATIONFLAGS;
    }

    public String getEXPERIMENTALPREPARATIONLISTFLAGS() {
        return EXPERIMENTALPREPARATIONLISTFLAGS;
    }

    public String getEXPERIMENTALROLEFLAGS() {
        return EXPERIMENTALROLEFLAGS;
    }

    public String getEXPERIMENTALROLELISTFLAGS() {
        return EXPERIMENTALROLELISTFLAGS;
    }

    public String getIDFLAGS() {
        return IDFLAGS;
    }

    public String getINTERACTIONREFFLAGS() {
        return INTERACTIONREFFLAGS;
    }

    public String getINTERACTORREFFLAGS() {
        return INTERACTORREFFLAGS;
    }

    public String getPARTICIPANTFLAGS() {
        return PARTICIPANTFLAGS;
    }

    public String getPARTICIPANTIDENTIFICATIONMETHODFLAGS() {
        return PARTICIPANTIDENTIFICATIONMETHODFLAGS;
    }

    public String getPARTICIPANTIDENTIFICATIONMETHODLISTFLAGS() {
        return PARTICIPANTIDENTIFICATIONMETHODLISTFLAGS;
    }
}
