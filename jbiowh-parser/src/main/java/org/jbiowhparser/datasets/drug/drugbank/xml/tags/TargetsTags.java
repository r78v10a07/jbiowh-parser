package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the Targets XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 12, 2011
 */
public class TargetsTags {

    public final String TARGETS = "targets";
    public final String TARGET = "target";
    private static TargetsTags singleton;

    private TargetsTags() {
    }

    /**
     * Return a TargetsTags
     *
     * @return a TargetsTags
     */
    public static synchronized TargetsTags getInstance() {
        if (singleton == null) {
            singleton = new TargetsTags();
        }
        return singleton;
    }
}
