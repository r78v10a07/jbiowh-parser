package org.jbiowhparser.datasets.disease.omim.files.tags;

/**
 * This Class is the OMIM Tabs definition
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Jul 16, 2012
 */
public class OMIMTabs {

    public final String RECORD = "*RECORD*";
    public final String FIELD = "\\*FIELD\\*";
    public final String AV = "AV";
    public final String CD = "CD";
    public final String CN = "CN";
    public final String CS = "CS";
    public final String ED = "ED";
    public final String NO = "NO";
    public final String RF = "RF";
    public final String SA = "SA";
    public final String TI = "TI";
    public final String TX = "TX";
    
    protected boolean isRecord(String line) {
        return line.startsWith(RECORD);
    }
    
    protected boolean isField(String line) {
        return line.matches(FIELD + ".+");
    }
    
    protected boolean isAV(String line) {
        return line.matches(FIELD + "\\s+" + AV);
    }
    
    protected boolean isCD(String line) {
        return line.matches(FIELD + "\\s+" + CD);
    }
    
    protected boolean isCN(String line) {
        return line.matches(FIELD + "\\s+" + CN);
    }
    
    protected boolean isCS(String line) {
        return line.matches(FIELD + "\\s+" + CS);
    }
    
    protected boolean isED(String line) {
        return line.matches(FIELD + "\\s+" + ED);
    }
    
    protected boolean isNO(String line) {
        return line.matches(FIELD + "\\s+" + NO);
    }
    
    protected boolean isRF(String line) {
        return line.matches(FIELD + "\\s+" + RF);
    }
    
    protected boolean isSA(String line) {
        return line.matches(FIELD + "\\s+" + SA);
    }
    
    protected boolean isTI(String line) {
        return line.matches(FIELD + "\\s+" + TI);
    }
    
    protected boolean isTX(String line) {
        return line.matches(FIELD + "\\s+" + TX);
    }
}
