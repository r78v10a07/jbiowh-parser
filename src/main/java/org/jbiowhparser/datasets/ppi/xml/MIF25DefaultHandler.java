package org.jbiowhparser.datasets.ppi.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This Class is the default handler for MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 20, 2010
 * @see
 */
public class MIF25DefaultHandler extends DefaultHandler {

    private MIF25 mif25 = null;
    private String tagname = null;
    private int depth = 0;

    public MIF25DefaultHandler(MIF25 mif25) {
        this.mif25 = mif25;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);
        if (!s.trim().equals("")) {
            mif25.getEntrySet().characters(tagname, s, depth);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        mif25.getEntrySet().endElement(qName, depth);
        depth--;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        depth++;
        tagname = qName;
        mif25.getEntrySet().startElement(qName, depth, attributes);
    }
}
