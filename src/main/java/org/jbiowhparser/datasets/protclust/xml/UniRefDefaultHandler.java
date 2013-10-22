package org.jbiowhparser.datasets.protclust.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This Class is
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Aug 19, 2011
 */
public class UniRefDefaultHandler extends DefaultHandler {

    private UniRef UniRef = null;
    private String tagname = null;
    private int depth = 0;

    public UniRefDefaultHandler(UniRef UniRef) {
        this.UniRef = UniRef;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);
        if (!s.trim().equals("")) {
            UniRef.getUniref().characters(tagname, s, depth);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        UniRef.getUniref().endElement(qName, depth);
        depth--;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        depth++;
        tagname = qName;
        UniRef.getUniref().startElement(qName, depth, attributes);
    }
}
