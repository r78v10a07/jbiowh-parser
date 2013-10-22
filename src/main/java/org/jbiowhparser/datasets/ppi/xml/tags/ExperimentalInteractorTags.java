package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML ExperimentalInteractor Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 25, 2010
 * @see
 */
public class ExperimentalInteractorTags {

    private final String EXPERIMENTALINTERACTORFLAGS = "experimentalInteractor";
    private final String INTERACTORREFFLAGS = "interactorRef";

    public String getEXPERIMENTALINTERACTORFLAGS() {
        return EXPERIMENTALINTERACTORFLAGS;
    }

    public String getINTERACTORREFFLAGS() {
        return INTERACTORREFFLAGS;
    }
}
