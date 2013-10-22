package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML ExperimentList Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 21, 2010
 * @see
 */
public class ExperimentListTags {

    private final String EXPERIMENTLISTFLAGS = "experimentList";
    private final String EXPERIMENTREFFLAGS = "experimentRef";

    public String getEXPERIMENTLISTFLAGS() {
        return EXPERIMENTLISTFLAGS;
    }

    public String getEXPERIMENTREFFLAGS() {
        return EXPERIMENTREFFLAGS;
    }
}
