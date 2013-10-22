package org.jbiowhparser.datasets.protein.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.protein.xml.tags.EntryTags;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Entry tag on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Sep 29, 2010
 * @see
 */
public class Entry extends EntryTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private String aVersion = null;
    private String aModified = null;
    private String aDataset = null;
    private String aCreated = null;
    private ArrayList<String> name = null;
    private ArrayList<String> accession = null;
    private ProteinType proteintype = null;
    private Gene gene = null;
    private DBReference dbReference = null;
    private Organism organism = null;
    private Reference reference = null;
    private GeneLocation geneLocation = null;
    private Comment comment = null;
    private String proteinExistence = null;
    private Keyword keyword = null;
    private Feature feature = null;
    private Sequence sequence = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Entry() {
        open = false;
        WID = WIDFactory.getInstance().getWid();

        proteintype = new ProteinType();
        gene = new Gene();
        dbReference = new DBReference();
        organism = new Organism();
        reference = new Reference();
        geneLocation = new GeneLocation();
        comment = new Comment();
        keyword = new Keyword();
        feature = new Feature();
        sequence = new Sequence();
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (proteintype.isOpen()) {
                    proteintype.endElement(qname, depth);
                }
                if (gene.isOpen()) {
                    gene.endElement(qname, depth);
                }
                if (dbReference.isOpen()) {
                    dbReference.endElement(qname, depth, open);
                }
                if (organism.isOpen()) {
                    organism.endElement(qname, depth);
                }
                if (reference.isOpen()) {
                    reference.endElement(qname, depth);
                }
                if (geneLocation.isOpen()) {
                    geneLocation.endElement(qname, depth);
                }
                if (comment.isOpen()) {
                    comment.endElement(qname, depth);
                }
                if (keyword.isOpen()) {
                    keyword.endElement(qname, depth);
                }
                if (feature.isOpen()) {
                    feature.endElement(qname, depth);
                }
                if (sequence.isOpen()) {
                    sequence.endElement(qname, depth);
                }
            }
        }
        if (qname.equals(getENTRY())) {
            int count;
            open = false;

            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEIN, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEIN, aVersion, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEIN, aModified, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEIN, aCreated, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEIN, aDataset, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEIN, proteinExistence, "\t");
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEIN, sequence.getLength(), "\t");
            sequence.setLength(null);
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEIN, sequence.getMass(), "\t");
            sequence.setMass(null);
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEIN, sequence.getChecksum(), "\t");
            sequence.setChecksum(null);
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEIN, sequence.getModified(), "\t");
            sequence.setModified(null);
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEIN, sequence.getVersion(), "\t");
            sequence.setVersion(null);
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEIN, sequence.getPrecursor(), "\t");
            sequence.setPrecursor(null);
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEIN, sequence.getFragment(), "\t");
            sequence.setFragment(null);
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEIN, sequence.getSeq(), "\t");
            sequence.setSeq(null);
            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEIN, DataSetPersistence.getInstance().getDataset().getWid(), "\n");

            count = 1;
            for (Iterator<String> it = name.iterator(); it.hasNext();) {
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINNAME, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINNAME, it.next(), "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINNAME, count, "\n");
                count++;
            }
            name.clear();

            count = 1;
            for (Iterator<String> it = accession.iterator(); it.hasNext();) {
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINACCESSIONNUMBER, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINACCESSIONNUMBER, it.next(), "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINACCESSIONNUMBER, count, "\n");
                count++;
            }
            accession.clear();
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     */
    public void startElement(String qname, int depth, Attributes attributes) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getPROTEINEXISTENCEFLAGS())) {
                    proteinExistence = attributes.getValue(getTYPEFLAGS());
                }
                if (qname.equals(proteintype.getPROTEINFLAGS())) {
                    proteintype.setOpen(true);
                }
                if (qname.equals(gene.getGENEFLAGS())) {
                    gene.setOpen(true);
                }
                if (qname.equals(dbReference.getDBREFERENCEFLAGS())) {
                    dbReference.setOpen(true);
                }
                if (qname.equals(organism.getORGANISMFLAGS()) || qname.equals(organism.getORGANISMHOSTFLAGS())) {
                    organism.setOpen(true);
                }
                if (qname.equals(reference.getREFERENCEFLAGS())) {
                    reference.setOpen(true);
                }
                if (qname.equals(geneLocation.getGENELOCATIONFLAGS())) {
                    geneLocation.setOpen(true);
                }
                if (qname.equals(comment.getCOMMENTFLAGS())) {
                    comment.setOpen(true);
                }
                if (qname.equals(keyword.getKEYWORDFLAGS())) {
                    keyword.setOpen(true);
                }
                if (qname.equals(feature.getFEATUREFLAGS())) {
                    feature.setOpen(true);
                }
                if (qname.equals(sequence.getSEQUENCEFLAGS())) {
                    sequence.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (proteintype.isOpen()) {
                    proteintype.startElement(qname, depth, attributes, WID);
                }
                if (gene.isOpen()) {
                    gene.startElement(qname, depth, attributes, WID);
                }
                if (dbReference.isOpen()) {
                    dbReference.startElement(qname, depth, attributes, WID);
                }
                if (organism.isOpen()) {
                    organism.startElement(qname, depth, attributes, WID);
                }
                if (reference.isOpen()) {
                    reference.startElement(qname, depth, attributes, WID);
                }
                if (geneLocation.isOpen()) {
                    geneLocation.startElement(qname, depth, attributes, WID);
                }
                if (comment.isOpen()) {
                    comment.startElement(qname, depth, attributes, WID);
                }
                if (keyword.isOpen()) {
                    keyword.startElement(qname, depth, attributes, WID);
                }
                if (feature.isOpen()) {
                    feature.startElement(qname, depth, attributes, WID);
                }
                if (sequence.isOpen()) {
                    sequence.startElement(qname, depth, attributes);
                }
            }

        }
        if (qname.equals(getENTRY())) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();

            aVersion = attributes.getValue(getVERSION());
            aModified = attributes.getValue(getMODIFIED());
            aDataset = attributes.getValue(getDATASET());
            aCreated = attributes.getValue(getCREATED());

            accession = new ArrayList<>();
            name = new ArrayList<>();
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
            if (depth >= this.depth + 1) {
                if (proteintype.isOpen()) {
                    proteintype.characters(tagname, qname, depth);
                }
                if (gene.isOpen()) {
                    gene.characters(tagname, qname, depth);
                }
                if (geneLocation.isOpen()) {
                    geneLocation.characters(tagname, qname, depth);
                }
                if (comment.isOpen()) {
                    comment.characters(tagname, qname, depth);
                }
                if (keyword.isOpen()) {
                    keyword.characters(tagname, qname, depth);
                }
                if (feature.isOpen()) {
                    feature.characters(tagname, qname, depth);
                }
                if (sequence.isOpen()) {
                    sequence.characters(tagname, qname, depth);
                }
            }
            if (depth == this.depth + 1) {
                if (tagname.equals(getNAME())) {
                    name.add(qname);
                }
                if (tagname.equals(getACCESSION())) {
                    accession.add(qname);
                }
            }
        }
    }
}
