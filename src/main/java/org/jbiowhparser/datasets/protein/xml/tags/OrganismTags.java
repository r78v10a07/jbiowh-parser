package org.jbiowhparser.datasets.protein.xml.tags;

/**
 * This Class storage the XML Organism tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 30, 2010
 * @see
 */
public class OrganismTags extends DBReferenceTags {

    private final String ORGANISMFLAGS = "organism";
    private final String ORGANISMHOSTFLAGS = "organismHost";
    private final String ORGANISMTYPEFLAGS = "organismType";
    private final String NAMEFLAGS = "name";

    public String getNAMEFLAGS() {
        return NAMEFLAGS;
    }

    public String getORGANISMFLAGS() {
        return ORGANISMFLAGS;
    }

    public String getORGANISMHOSTFLAGS() {
        return ORGANISMHOSTFLAGS;
    }

    public String getORGANISMTYPEFLAGS() {
        return ORGANISMTYPEFLAGS;
    }
}
