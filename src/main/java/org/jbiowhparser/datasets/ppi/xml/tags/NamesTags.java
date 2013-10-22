package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML Names Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 20, 2010
 * @see
 */
public class NamesTags {

    private final String NAMESFLAGS = "names";
    private final String SHORTLABELFLAGS = "shortLabel";
    private final String FULLNAMEFLAGS = "fullName";
    private final String ALIASFLAGS = "alias";
    private final String TYPEACFLAGS = "typeAc";
    private final String TYPEFLAGS = "type";

    public String getALIASFLAGS() {
        return ALIASFLAGS;
    }

    public String getFULLNAMEFLAGS() {
        return FULLNAMEFLAGS;
    }

    public String getNAMESFLAGS() {
        return NAMESFLAGS;
    }

    public String getSHORTLABELFLAGS() {
        return SHORTLABELFLAGS;
    }

    public String getTYPEACFLAGS() {
        return TYPEACFLAGS;
    }

    public String getTYPEFLAGS() {
        return TYPEFLAGS;
    }
}
