package org.jbiowhparser.datasets.ppi.xml.tags;

/**
 * This Class storage the XML Interactor Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 21, 2010
 * @see
 */
public class InteractorTags {

    private final String INTERACTORFLAGS = "interactor";
    private final String IDFLAGS = "id";
    private final String INTERACTORTYPEFLAGS = "interactorType";
    private final String ORGANISMFLAGS = "organism";
    private final String SEQUENCEFLAGS = "sequence";

    public String getIDFLAGS() {
        return IDFLAGS;
    }

    public String getINTERACTORFLAGS() {
        return INTERACTORFLAGS;
    }

    public String getINTERACTORTYPEFLAGS() {
        return INTERACTORTYPEFLAGS;
    }

    public String getORGANISMFLAGS() {
        return ORGANISMFLAGS;
    }

    public String getSEQUENCEFLAGS() {
        return SEQUENCEFLAGS;
    }
}
