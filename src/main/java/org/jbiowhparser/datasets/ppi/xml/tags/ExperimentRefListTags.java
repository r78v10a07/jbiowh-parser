package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML ExperimentRefList Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 21, 2010
 * @see
 */
public class ExperimentRefListTags {

    private final String EXPERIMENTREFLISTFLAGS = "experimentRefList";
    private final String EXPERIMENTREFFLAGS = "experimentRef";

    public String getEXPERIMENTREFFLAGS() {
        return EXPERIMENTREFFLAGS;
    }

    public String getEXPERIMENTREFLISTFLAGS() {
        return EXPERIMENTREFLISTFLAGS;
    }
}
