package org.jbiowhparser.datasets.protein.xml.tags;

/**
 * This Class staorage the XML GeneLocation on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 30, 2010
 * @see
 */
public class GeneLocationTags {

    private final String GENELOCATIONFLAGS = "geneLocation";
    private final String GENELOCATIONTYPEFLAGS = "geneLocationType";
    private final String NAMEFLAGS = "name";
    private final String TYPEFLAGS = "type";
    private final String STATUSFLAGS = "status";

    public String getGENELOCATIONFLAGS() {
        return GENELOCATIONFLAGS;
    }

    public String getGENELOCATIONTYPEFLAGS() {
        return GENELOCATIONTYPEFLAGS;
    }

    public String getNAMEFLAGS() {
        return NAMEFLAGS;
    }

    public String getSTATUSFLAGS() {
        return STATUSFLAGS;
    }

    public String getTYPEFLAGS() {
        return TYPEFLAGS;
    }
}
