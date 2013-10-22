package org.jbiowhparser.datasets.drug.drugbank.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is the DrugBank XML DefaultHandler
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 9, 2011
 */
public class DrugBankDefaultHandler extends DefaultHandler {

    private Drugs drugs = null;
    private String tagname = null;
    private int depth = 0;

    public DrugBankDefaultHandler(Drugs Drugs) {
        this.drugs = Drugs;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);
        if (!s.trim().equals("")) {
            drugs.getDrug().characters(getTagname(), s, depth);
            drugs.getPartner().characters(getTagname(), s, depth);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        drugs.getDrug().endElement(qName, depth);
        drugs.getPartner().endElement(qName, depth);
        depth--;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        depth++;
        setTagname(qName);
        drugs.getDrug().startElement(qName, depth, attributes);
        drugs.getPartner().startElement(qName, depth, attributes);
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }
}
