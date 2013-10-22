package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML Interaction Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 22, 2010
 * @see
 */
public class InteractionTags {

    private final String INTERACTIONFLAGS = "interaction";
    private final String IMEXIDFLAGS = "imexId";
    private final String IDFLAGS = "id";
    private final String MODELLEDFLAGS = "modelled";
    private final String INTRAMOLECULARFLAGS = "intraMolecular";
    private final String NEGATIVEFLAGS = "negative";
    private final String AVAILABILITYREFFLAGS = "availabilityRef";
    private final String INTERACTIONTYPEFLAGS = "interactionType";

    public String getAVAILABILITYREFFLAGS() {
        return AVAILABILITYREFFLAGS;
    }

    public String getIDFLAGS() {
        return IDFLAGS;
    }

    public String getIMEXIDFLAGS() {
        return IMEXIDFLAGS;
    }

    public String getINTERACTIONFLAGS() {
        return INTERACTIONFLAGS;
    }

    public String getINTRAMOLECULARFLAGS() {
        return INTRAMOLECULARFLAGS;
    }

    public String getMODELLEDFLAGS() {
        return MODELLEDFLAGS;
    }

    public String getNEGATIVEFLAGS() {
        return NEGATIVEFLAGS;
    }

    public String getINTERACTIONTYPEFLAGS() {
        return INTERACTIONTYPEFLAGS;
    }
}
