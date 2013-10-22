package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML Xref Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 20, 2010
 * @see
 */
public class XrefTags {

    private final String XREFFLAGS = "xref";
    private final String PRIMARYREFFLAGS = "primaryRef";
    private final String SECONDARYREFFLAGS = "secondaryRef";
    private final String DBFLAGS = "db";
    private final String DBACFLAGS = "dbAc";
    private final String IDFLAGS = "id";
    private final String SECONDARYFLAGS = "secondary";
    private final String VERSIONFLAGS = "version";
    private final String REFTYPEFLAGS = "refType";
    private final String REFTYPEACFLAGS = "refTypeAc";

    public String getDBACFLAGS() {
        return DBACFLAGS;
    }

    public String getDBFLAGS() {
        return DBFLAGS;
    }

    public String getIDFLAGS() {
        return IDFLAGS;
    }

    public String getPRIMARYREFFLAGS() {
        return PRIMARYREFFLAGS;
    }

    public String getREFTYPEACFLAGS() {
        return REFTYPEACFLAGS;
    }

    public String getREFTYPEFLAGS() {
        return REFTYPEFLAGS;
    }

    public String getSECONDARYFLAGS() {
        return SECONDARYFLAGS;
    }

    public String getSECONDARYREFFLAGS() {
        return SECONDARYREFFLAGS;
    }

    public String getVERSIONFLAGS() {
        return VERSIONFLAGS;
    }

    public String getXREFFLAGS() {
        return XREFFLAGS;
    }
}
