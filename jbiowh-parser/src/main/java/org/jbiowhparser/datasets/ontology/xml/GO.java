package org.jbiowhparser.datasets.ontology.xml;

/**
 * This Class is the GO starting class for the SAX parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 23, 2010
 * @see
 */
public class GO {

    private GOHeader header = null;
    private GOTerm term = null;

    public GO() {
        header = new GOHeader();
        term = new GOTerm();
    }

    public GOHeader getHeader() {
        return header;
    }

    public GOTerm getTerm() {
        return term;
    }
}
