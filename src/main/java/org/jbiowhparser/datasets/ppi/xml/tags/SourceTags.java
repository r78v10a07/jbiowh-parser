package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML Source Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 20, 2010
 * @see
 */
public class SourceTags {

    private final String SOURCEFLAGS = "source";
    private final String RELEASEFLAGS = "release";

    public String getRELEASEDATEFLAGS() {
        return RELEASEDATEFLAGS;
    }

    public String getRELEASEFLAGS() {
        return RELEASEFLAGS;
    }

    public String getSOURCEFLAGS() {
        return SOURCEFLAGS;
    }
    private final String RELEASEDATEFLAGS = "releaseDate";
}
