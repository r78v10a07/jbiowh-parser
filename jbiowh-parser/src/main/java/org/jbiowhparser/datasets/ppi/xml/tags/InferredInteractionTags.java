package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML InferredInteraction Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 29, 2010
 * @see
 */
public class InferredInteractionTags {

    private final String INFERREDINTERACTIONFLAGS = "inferredInteraction";
    private final String PARTICIPANTFLAGS = "participant";
    private final String PARTICIPANTREFFLAGS = "participantRef";
    private final String PARTICIPANTFEATUREREFFLAGS = "participantFeatureRef";

    public String getINFERREDINTERACTIONFLAGS() {
        return INFERREDINTERACTIONFLAGS;
    }

    public String getPARTICIPANTFEATUREREFFLAGS() {
        return PARTICIPANTFEATUREREFFLAGS;
    }

    public String getPARTICIPANTFLAGS() {
        return PARTICIPANTFLAGS;
    }

    public String getPARTICIPANTREFFLAGS() {
        return PARTICIPANTREFFLAGS;
    }
}
