package org.jbiowhparser.datasets.drug.drugbank.xml;

import java.util.HashMap;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.drug.drugbank.utility.DrugBankSplitReference;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.IdentifiersTypeTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.PFamsTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.PartnerTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.SynonymsTags;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.drug.drugbank.DrugBankTables;
import org.xml.sax.Attributes;

/**
 * This class is the Partner XML parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Sep 9, 2011
 */
public class Partner extends PartnerTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private HashMap values = null;
    private String id = null;
    private String references = null;
    private XMLIntermedParser externalIdentifier = null;
    private TwoFieldXMLParser synonyms = null;
    private XMLIntermedParser pfams = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     */
    public Partner() {
        open = false;
        values = new HashMap();
        externalIdentifier = new XMLIntermedParser(DrugBankTables.getInstance().DRUGBANKPARTNEREXTERNALIDENTIFIERS,
                IdentifiersTypeTags.getInstance().EXTERNALIDENTIFIERS,
                new String[]{IdentifiersTypeTags.getInstance().EXTERNALIDENTIFIER, IdentifiersTypeTags.getInstance().RESOURCE, IdentifiersTypeTags.getInstance().IDENTIFIER});

        synonyms = new TwoFieldXMLParser(DrugBankTables.getInstance().DRUGBANKPARTNERSYNONYMS,
                SynonymsTags.getInstance().SYNONYMS, SynonymsTags.getInstance().SYNONYM);

        pfams = new XMLIntermedParser(DrugBankTables.getInstance().DRUGBANKPARTNERPFAM,
                PFamsTags.getInstance().PFAMS,
                new String[]{PFamsTags.getInstance().PFAM, PFamsTags.getInstance().IDENTIFIER, PFamsTags.getInstance().NAME});
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param qname
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (externalIdentifier.isOpen()) {
                    externalIdentifier.endElement(qname, depth, true);
                }
                if (synonyms.isOpen()) {
                    synonyms.endElement(qname, depth, true);
                }
                if (pfams.isOpen()) {
                    pfams.endElement(qname, depth, true);
                }
            }
        }
        if (qname.equals(PARTNER) && depth == this.depth) {
            open = false;

            if (!values.isEmpty()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPARTNERS, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPARTNERS, id, "\t");
                for (String TAGS1 : TAGS) {
                    String value = (String) values.get(TAGS1);
                    ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPARTNERS, value, "\t");
                }
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPARTNERS, DataSetPersistence.getInstance().getDataset().getWid(), "\n");

                (new DrugBankSplitReference()).split(references, WID, DrugBankTables.getInstance().DRUGBANKPARTNERREF);
            }
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param qname
     * @param depth
     * @param attributes
     */
    public void startElement(String qname, int depth, Attributes attributes) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(IdentifiersTypeTags.getInstance().EXTERNALIDENTIFIERS)) {
                    externalIdentifier.setOpen(true);
                }
                if (qname.equals(SynonymsTags.getInstance().SYNONYMS)) {
                    synonyms.setOpen(true);
                }
                if (qname.equals(PFamsTags.getInstance().PFAMS)) {
                    pfams.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (externalIdentifier.isOpen()) {
                    externalIdentifier.startElement(qname, depth, attributes, WID);
                }
                if (synonyms.isOpen()) {
                    synonyms.startElement(qname, depth, attributes, WID);
                }
                if (pfams.isOpen()) {
                    pfams.startElement(qname, depth, attributes, WID);
                }
            }
        }

        if (qname.equals(PARTNER) && depth == 3) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();

            values.clear();
            id = attributes.getValue(ID);
            references = null;
        }
    }

    /**
     *
     * @param tagname
     * @param qname
     * @param depth
     */
    public void characters(String tagname, String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (externalIdentifier.isOpen()) {
                    externalIdentifier.characters(tagname, qname, depth);
                }
                if (synonyms.isOpen()) {
                    synonyms.characters(tagname, qname, depth);
                }
                if (pfams.isOpen()) {
                    pfams.characters(tagname, qname, depth);
                }
            }
            if (depth == this.depth + 1) {
                for (String TAGS1 : TAGS) {
                    if (tagname.equals(TAGS1)) {
                        values.put(TAGS1, copyQName(qname, (String) values.get(TAGS1)));
                    }
                }
                if (tagname.equals(REFERENCES)) {
                    references = copyQName(qname, references);
                }
            }
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    private String copyQName(String qname, String value) {
        String result;
        if (value == null) {
            result = qname;
        } else {
            result = value.concat(qname);
        }
        return result;
    }
}
