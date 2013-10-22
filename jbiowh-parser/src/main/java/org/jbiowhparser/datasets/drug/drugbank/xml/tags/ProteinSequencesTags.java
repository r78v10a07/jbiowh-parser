package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the ProteinSequences XMl Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 11, 2011
 */
public class ProteinSequencesTags extends SequenceTypeTags {

    public final String PROTEINSEQUENCES = "protein-sequences";
    public final String PROTEINSEQUENCE = "protein-sequence";
    private static ProteinSequencesTags singleton;

    private ProteinSequencesTags() {
    }

    /**
     * Return a ProteinSequencesTags
     *
     * @return a ProteinSequencesTags
     */
    public static synchronized ProteinSequencesTags getInstance() {
        if (singleton == null) {
            singleton = new ProteinSequencesTags();
        }
        return singleton;
    }
}
