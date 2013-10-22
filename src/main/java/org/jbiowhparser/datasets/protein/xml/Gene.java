package org.jbiowhparser.datasets.protein.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.protein.xml.tags.GeneTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.xml.sax.Attributes;

/**
 * This Class handled the Gene on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Sep 30, 2010
 * @see
 */
public class Gene extends GeneTags {

    private int depth = 0;
    private boolean open = false;
    private long proteinWID = 0;
    private GeneNameType geneNameType = null;
    private ArrayList<GeneNameType> geneNameTypes = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Gene() {
        open = false;

        geneNameTypes = new ArrayList<>();
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getNAMEFLAGS())) {
                    geneNameType.open = false;
                    geneNameTypes.add(geneNameType);
                }
            }
        }
        if (qname.equals(getGENEFLAGS())) {
            int count;
            open = false;

            count = 1;
            for (Iterator<GeneNameType> it = geneNameTypes.iterator(); it.hasNext();) {
                geneNameType = it.next();
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINGENENAME, WIDFactory.getInstance().getWid(), "\t");
                WIDFactory.getInstance().increaseWid();
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINGENENAME, proteinWID, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINGENENAME, geneNameType.type, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINGENENAME, geneNameType.name, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINGENENAME, geneNameType.evidence, "\n");
                count++;
            }
            geneNameTypes.clear();
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     */
    public void startElement(String qname, int depth, Attributes attributes, long proteinWID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getNAMEFLAGS())) {
                    geneNameType = new GeneNameType();
                    geneNameType.type = attributes.getValue(getTYPEFLAGS());
                    geneNameType.evidence = attributes.getValue(getEVIDENCE());
                }
            }
        }
        if (qname.equals(getGENEFLAGS())) {
            this.depth = depth;
            this.proteinWID = proteinWID;
            open = true;

            geneNameTypes.clear();
            geneNameType = null;
        }
    }

    /**
     *
     * @param tagname
     * @param name
     * @param depth
     */
    public void characters(String tagname, String qname, int depth) {
        if (open) {
            if (depth == this.depth + 1) {
                if (tagname.equals(getNAMEFLAGS())) {
                    geneNameType.name = qname;
                }
            }
        }
    }

    private class GeneNameType {

        private boolean open = false;
        private String name = null;
        private String type = null;
        private String evidence = null;

        public GeneNameType() {
            open = false;
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
