package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the AHFSCodes XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class AHFSCodesTags {

    private static AHFSCodesTags aHFSCodesTags;
    public final String AHFSCODES = "ahfs-codes";
    public final String AHFSCODE = "ahfs-code";

    private AHFSCodesTags() {
    }

    /**
     * Return a AHFSCodesTags
     *
     * @return a AHFSCodesTags
     */
    public static synchronized AHFSCodesTags getInstance() {
        if (aHFSCodesTags == null) {
            aHFSCodesTags = new AHFSCodesTags();
        }
        return aHFSCodesTags;
    }
}
