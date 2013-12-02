package org.jbiowhparser.datasets.protein.xml;

import java.util.ArrayList;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.protein.xml.tags.DBReferenceTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.xml.sax.Attributes;

/**
 * This Class handled the dbReference tgs on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Sep 30, 2010
 * @see
 */
public class DBReference extends DBReferenceTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private long ProteinWID = 0;
    PropertyType propertyType = null;
    private ArrayList<PropertyType> propertyTypes = null;
    private String type = null;
    private String id = null;
    private String evidence = null;
    private String key = null;
    private final String GO = "Go";
    private final String REFSEQ = "RefSeq";
    private final String GENEID = "GeneId";
    private final String PUBMED = "PubMed";
    private final String KEGG = "KEGG";
    private final String EC = "EC";
    private final String BIOCYC = "BioCyc";
    private final String PDB = "PDB";
    private final String DIP = "DIP";
    private final String MINT = "MINT";
    private final String DRUGBANK = "DrugBank";
    private final String PFAM = "Pfam";
    private final String INTACT = "IntAct";

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     */
    public DBReference() {
        open = false;
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param qname
     * @param depth XML depth
     * @param print
     */
    public void endElement(String qname, int depth, Boolean print) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getPROPERTYFLAGS())) {
                    propertyType.open = false;
                    propertyTypes.add(propertyType);
                }
            }
        }
        if (qname.equals(getDBREFERENCEFLAGS())) {
            open = false;

            if (print) {
                switch (type) {
                    case GO:
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINGOTEMP, ProteinWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINGOTEMP, id, "\n");
                        break;
                    case REFSEQ:
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINREFSEQTEMP, ProteinWID, "\t");
                        if (id.indexOf(".") != -1) {
                            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINREFSEQTEMP, id.substring(0, id.indexOf(".")), "\n");
                        } else {
                            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINREFSEQTEMP, id, "\n");
                        }
                        break;
                    case GENEID:
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINGENETEMP, ProteinWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINGENETEMP, id, "\n");
                        break;
                    case PUBMED:
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINPMIDTEMP, ProteinWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINPMIDTEMP, id, "\n");
                        break;
                    case KEGG:
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINKEGGTEMP, ProteinWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINKEGGTEMP, id, "\n");
                        break;
                    case EC:
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINECTEMP, ProteinWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINECTEMP, id, "\n");
                        break;
                    case BIOCYC:
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINBIOCYCTEMP, ProteinWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINBIOCYCTEMP, id, "\n");
                        break;
                    case PDB:
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINPDBTEMP, ProteinWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINPDBTEMP, id, "\n");
                        break;
                    case INTACT:
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEININTACTTEMP, ProteinWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEININTACTTEMP, id, "\n");
                        break;
                    case DIP:
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINDIPTEMP, ProteinWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINDIPTEMP, id, "\n");
                        break;
                    case MINT:
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINMINTTEMP, ProteinWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINMINTTEMP, id, "\n");
                        break;
                    case DRUGBANK:
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINDRUGBANKTEMP, ProteinWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINDRUGBANKTEMP, id, "\n");
                        break;
                    case PFAM:
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINPFAMTEMP, ProteinWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINPFAMTEMP, id, "\n");
                        break;
                    default:
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINDBREFERENCE, WID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINDBREFERENCE, ProteinWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINDBREFERENCE, type, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINDBREFERENCE, id, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINDBREFERENCE, evidence, "\n");
                        if (!propertyTypes.isEmpty()) {
                            for (PropertyType p : propertyTypes) {
                                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINDBREFERENCEPROPERTY, WID, "\t");
                                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINDBREFERENCEPROPERTY, p.type, "\t");
                                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINDBREFERENCEPROPERTY, p.value, "\n");
                            }
                            propertyTypes.clear();
                        }
                        break;
                }
            }
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param qname
     * @param depth
     * @param attributes
     * @param WID
     */
    public void startElement(String qname, int depth, Attributes attributes, long WID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getPROPERTYFLAGS())) {
                    propertyType = new PropertyType();
                    propertyType.type = attributes.getValue(getTYPEFLAGS());
                    propertyType.value = attributes.getValue(getVALUEFLAGS());
                }
            }
        }
        if (qname.equals(getDBREFERENCEFLAGS())) {
            this.depth = depth;
            this.ProteinWID = WID;
            open = true;

            propertyType = null;
            propertyTypes = new ArrayList<>();

            type = attributes.getValue(getTYPEFLAGS());

            if (!type.equals(GO)
                    && !type.equals(REFSEQ)
                    && !type.equals(GENEID)
                    && !type.equals(PUBMED)
                    && !type.equals(KEGG)
                    && !type.equals(EC)
                    && !type.equals(BIOCYC)
                    && !type.equals(PDB)
                    && !type.equals(DIP)
                    && !type.equals(MINT)
                    && !type.equals(DRUGBANK)
                    && !type.equals(PFAM)
                    && !type.equals(INTACT)) {
                this.WID = WIDFactory.getInstance().getWid();
                WIDFactory.getInstance().increaseWid();
            }

            id = attributes.getValue(getIDFLAGS());
            evidence = attributes.getValue(getEVIDENCEFLAGS());
            key = attributes.getValue(getKEYFLAGS());
        }
    }

    private class PropertyType {

        private boolean open = false;
        private String type = null;
        private String value = null;

        public PropertyType() {
            open = true;
        }

        public boolean isOpen() {
            return open;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @return
     */
    public boolean isOpen() {
        return open;
    }

    /**
     *
     * @param open
     */
    public void setOpen(boolean open) {
        this.open = open;
    }
}
