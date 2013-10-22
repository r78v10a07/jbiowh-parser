package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML EntreSet tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 20, 2010
 * @see
 */
public class EntrySetTags {

    private final String ENTRYSETFLAGS = "entrySet";
    private final String LEVELFLAGS = "level";
    private final String MINORVERSIONFLAGS = "minorVersion";
    private final String VERSIONFLAGS = "version";

    public String getENTRYSETFLAGS() {
        return ENTRYSETFLAGS;
    }

    public String getLEVELFLAGS() {
        return LEVELFLAGS;
    }

    public String getMINORVERSIONFLAGS() {
        return MINORVERSIONFLAGS;
    }

    public String getVERSIONFLAGS() {
        return VERSIONFLAGS;
    }
}
