package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML BioSourceType Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 21, 2010
 * @see
 */
public class BioSourceTypeTags {

    private final String CELLTYPEFLAGS = "cellType";
    private final String COMPARTMENTFLAGS = "compartment";
    private final String NCBITAXIDFLAGS = "ncbiTaxId";
    private final String TISSUEFLAGS = "tissue";

    public String getCELLTYPEFLAGS() {
        return CELLTYPEFLAGS;
    }

    public String getCOMPARTMENTFLAGS() {
        return COMPARTMENTFLAGS;
    }

    public String getNCBITAXIDFLAGS() {
        return NCBITAXIDFLAGS;
    }

    public String getTISSUEFLAGS() {
        return TISSUEFLAGS;
    }
}
