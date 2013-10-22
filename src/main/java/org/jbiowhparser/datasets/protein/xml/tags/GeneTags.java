package org.jbiowhparser.datasets.protein.xml.tags;

/**
 * This Class storage the XML tags for gene on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 30, 2010
 * @see
 */
public class GeneTags {

    private final String TYPEFLAGS = "type";
    private final String NAMEFLAGS = "name";
    private final String GENEFLAGS = "gene";
    private final String EVIDENCE = "evidence";

    public String getGENEFLAGS() {
        return GENEFLAGS;
    }

    public String getNAMEFLAGS() {
        return NAMEFLAGS;
    }

    public String getTYPEFLAGS() {
        return TYPEFLAGS;
    }

    public String getEVIDENCE() {
        return EVIDENCE;
    }
}
