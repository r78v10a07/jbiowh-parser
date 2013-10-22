package org.jbiowhparser.datasets.protein.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This Class is the default handler for Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 29, 2010
 * @see
 */
public class UniprotDefaultHandler extends DefaultHandler {

    private Uniprot uniprot = null;
    private String tagname = null;
    private int depth = 0;

    public UniprotDefaultHandler(Uniprot uniprot) {
        this.uniprot = uniprot;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);
        if (!s.trim().equals("")) {
            uniprot.getEntry().characters(tagname, s, depth);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        uniprot.getEntry().endElement(qName, depth);
        depth--;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        depth++;
        tagname = qName;
        uniprot.getEntry().startElement(qName, depth, attributes);
    }
}
