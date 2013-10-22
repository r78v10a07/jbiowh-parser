package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML Parameter Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 29, 2010
 * @see
 */
public class ParameterTags {

    private final String PARAMETERFLAGS = "parameter";
    private final String TERMFLAGS = "term";
    private final String TERMACFLAGS = "termAc";
    private final String UNITFLAGS = "unit";
    private final String UNITACFLAGS = "unitAc";
    private final String BASEFLAGS = "base";
    private final String EXPONENTFLAGS = "exponent";
    private final String FACTORFLAGS = "factor";
    private final String UNCERTAINTYFLAGS = "uncertainty";
    private final String EXPERIMENTREFFLAGS = "experimentRef";

    public String getBASEFLAGS() {
        return BASEFLAGS;
    }

    public String getEXPERIMENTREFFLAGS() {
        return EXPERIMENTREFFLAGS;
    }

    public String getEXPONENTFLAGS() {
        return EXPONENTFLAGS;
    }

    public String getFACTORFLAGS() {
        return FACTORFLAGS;
    }

    public String getPARAMETERFLAGS() {
        return PARAMETERFLAGS;
    }

    public String getTERMACFLAGS() {
        return TERMACFLAGS;
    }

    public String getTERMFLAGS() {
        return TERMFLAGS;
    }

    public String getUNCERTAINTYFLAGS() {
        return UNCERTAINTYFLAGS;
    }

    public String getUNITACFLAGS() {
        return UNITACFLAGS;
    }

    public String getUNITFLAGS() {
        return UNITFLAGS;
    }
}
