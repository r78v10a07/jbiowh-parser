package org.jbiowhparser.datasets.ontology.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This Class acts as DefaultHandler for the Ontology SaX Parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 23, 2010
 * @see
 */
public class GOOntologyDefaultHandler extends DefaultHandler {

    private GO go = null;
    private String tagname = null;
    private int depth = 0;

    public GOOntologyDefaultHandler(GO go) {
        this.go = go;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);
        if (!s.trim().equals("")) {
            go.getHeader().charactersGOHeader(getTagname(), s, depth);
            go.getTerm().charactersGOTerm(getTagname(), s, depth);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        go.getHeader().endElementGOHeader(qName, depth);
        go.getTerm().endElementGOTerm(qName, depth);
        depth--;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        depth++;
        setTagname(qName);
        go.getHeader().startElementGOHeader(qName, depth);
        go.getTerm().startElementGOTerm(qName, depth, attributes);
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }
}
