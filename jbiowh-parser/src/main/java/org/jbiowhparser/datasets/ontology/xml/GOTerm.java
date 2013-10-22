package org.jbiowhparser.datasets.ontology.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ontology.xml.tags.GOTermTags;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ontology.OntologyTables;
import org.xml.sax.Attributes;

/**
 * This Class is the Go Term XML Sax parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Sep 23, 2010
 * @see
 */
public class GOTerm extends GOTermTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private String id = null;
    private String name = null;
    private String nameSpace = null;
    private Def def = null;
    private XRef xRef = null;
    private ArrayList<String> subsets = null;
    private Synonym syn = null;
    private ArrayList<Synonym> syns = null;
    private String comment = null;
    private String isObsolete = "0";
    private ArrayList<String> altId = null;
    private ArrayList<Relationship> relationships = null;
    private Relationship relationship = null;
    private ArrayList<String> isa = null;
    private ArrayList<String> consider = null;
    private ArrayList<XRef> xRefAnalogs = null;
    private XRef xRefAnalog = null;
    private Boolean xRefAnalogOpen = false;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     */
    public GOTerm() {
        open = false;
        WID = WIDFactory.getInstance().getWid();
    }

    /**
     * This is the endElement method for the Term on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElementGOTerm(String name, int depth) {

        if (open && depth > this.depth) {
            if (name.equals(getDEF())) {
                def.open = false;
            }
            if (name.equals(getDBXREF())) {
                xRef.open = false;
                def.xRef.add(xRef);
            }
            if (name.equals(getSYNONYM())) {
                syn.open = false;
                syns.add(syn);
            }
            if (name.equals(getRELATIONSHIP())) {
                relationship.open = false;
                relationships.add(relationship);
            }
            if (name.equals(getXREFANALOG())) {
                xRefAnalogOpen = false;
                xRefAnalog.open = false;
                xRefAnalogs.add(xRefAnalog);
            }
        }

        if (name.equals(getTERM())) {
            open = false;

            ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGY, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGY, this.id, "\t");
            ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGY, this.name, "\t");
            ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGY, this.nameSpace, "\t");
            ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGY, def.defstr, "\t");
            ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGY, comment, "\t");
            ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGY, isObsolete, "\t");
            ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGY, DataSetPersistence.getInstance().getDataset().getWid(), "\n");

            if (def.xRef != null) {
                if (!def.xRef.isEmpty()) {
                    for (Iterator<XRef> it = def.xRef.iterator(); it.hasNext();) {
                        xRef = it.next();
                        ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDXREFTEMP, WID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDXREFTEMP, xRef.acc, "\t");
                        ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDXREFTEMP, xRef.dbName, "\t");
                        ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDXREFTEMP, xRef.name, "\t");
                        ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDXREFTEMP, getDEF(), "\n");
                    }
                    def.xRef.clear();
                }
            }

            if (!subsets.isEmpty()) {
                for (Iterator<String> it = subsets.iterator(); it.hasNext();) {
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDONTOLOGYSUBSET, WID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDONTOLOGYSUBSET, it.next(), "\n");
                }
                subsets.clear();
            }

            if (!syns.isEmpty()) {
                for (Iterator<Synonym> it = syns.iterator(); it.hasNext();) {
                    syn = it.next();
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDSYNONYMTEMP, WID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDSYNONYMTEMP, syn.text, "\t");
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDSYNONYMTEMP, syn.scope, "\n");
                }
                syns.clear();
            }

            if (!altId.isEmpty()) {
                for (Iterator<String> it = altId.iterator(); it.hasNext();) {
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYALTERNATIVEID, WID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYALTERNATIVEID, it.next(), "\n");
                }
                altId.clear();
            }

            if (!relationships.isEmpty()) {
                for (Iterator<Relationship> it = relationships.iterator(); it.hasNext();) {
                    relationship = it.next();
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDRELATIONTEMP, WID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDRELATIONTEMP, relationship.type, "\t");
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDRELATIONTEMP, relationship.to, "\n");
                }
                relationships.clear();
            }

            if (!isa.isEmpty()) {
                for (Iterator<String> it = isa.iterator(); it.hasNext();) {
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDISATEMP, WID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDISATEMP, it.next(), "\n");
                }
                isa.clear();
            }

            if (!consider.isEmpty()) {
                for (Iterator<String> it = consider.iterator(); it.hasNext();) {
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDTOCONSIDERTEMP, WID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDTOCONSIDERTEMP, it.next(), "\n");
                }
                consider.clear();
            }

            if (!xRefAnalogs.isEmpty()) {
                for (Iterator<XRef> it = xRefAnalogs.iterator(); it.hasNext();) {
                    xRefAnalog = it.next();
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDXREFTEMP, WID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDXREFTEMP, xRefAnalog.acc, "\t");
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDXREFTEMP, xRefAnalog.dbName, "\t");
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDXREFTEMP, xRefAnalog.name, "\t");
                    ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYWIDXREFTEMP, getXREFANALOG(), "\n");
                }
                xRefAnalogs.clear();
            }

            clearData();
        }

    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     * @param attributes
     */
    public void startElementGOTerm(String name, int depth, Attributes attributes) {
        if (name.equals(getTERM())) {
            clearData();
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();

            def = new Def();
            subsets = new ArrayList<>();
            syns = new ArrayList<>();
            altId = new ArrayList<>();
            relationships = new ArrayList<>();
            isa = new ArrayList<>();
            consider = new ArrayList<>();
            xRefAnalogs = new ArrayList<>();
        }
        if (open) {
            if (depth == this.depth + 1) {
                if (name.equals(getDEF())) {
                    def.open = true;
                    def.xRef = new ArrayList<>();
                }
                if (name.equals(getSYNONYM())) {
                    syn = new Synonym();
                    syn.open = true;
                    syn.scope = attributes.getValue("scope");
                }
                if (name.equals(getRELATIONSHIP())) {
                    relationship = new Relationship();
                    relationship.open = true;
                }
                if (name.equals(getXREFANALOG())) {
                    xRefAnalog = new XRef();
                    xRefAnalog.open = true;
                    xRefAnalogOpen = true;
                }
            }
            if (depth == this.depth + 2) {
                if (name.equals(getDBXREF())) {
                    xRef = new XRef();
                    xRef.open = true;
                }
            }
        }
    }

    /**
     *
     * @param tagname
     * @param name
     * @param depth
     */
    public void charactersGOTerm(String tagname, String name, int depth) {
        if (open) {
            if (depth == this.depth + 1) {
                if (tagname.equals(getID())) {
                    this.id = name;
                }
                if (tagname.equals(getNAME())) {
                    this.name = name;
                }
                if (tagname.equals(getNAMESPACE())) {
                    this.nameSpace = name;
                }
                if (tagname.equals(getSUBSET())) {
                    subsets.add(name);
                }
                if (tagname.equals(getCOMMENT())) {
                    this.comment = name;
                }
                if (tagname.equals(getISOBSOLETE())) {
                    this.isObsolete = name;
                }
                if (tagname.equals(getALTID())) {
                    altId.add(name);
                }
                if (tagname.equals(getISA())) {
                    isa.add(name);
                }
                if (tagname.equals(getCONSIDER())) {
                    consider.add(name);
                }
            } else if (depth == this.depth + 2) {
                if (tagname.equals(getDEFSTR())) {
                    def.defstr = name;
                }
                if (tagname.equals(getSYNONYMTEXT())) {
                    syn.text = name;
                }
                if (tagname.equals(getTYPE())) {
                    relationship.type = name;
                }
                if (tagname.equals(getTO())) {
                    relationship.to = name;
                }
                if (xRefAnalogOpen) {
                    if (tagname.equals(getACC())) {
                        xRefAnalog.acc = name;
                    }
                    if (tagname.equals(getDBNAME())) {
                        xRefAnalog.dbName = name;
                    }
                    if (tagname.equals(getNAME())) {
                        xRefAnalog.name = name;
                    }
                }
            } else if (depth == this.depth + 3) {
                if (tagname.equals(getACC())) {
                    xRef.acc = name;
                }
                if (tagname.equals(getDBNAME())) {
                    xRef.dbName = name;
                }
            }
        }

    }

    private void clearData() {
        depth = 0;
        open = false;
        WID = 0;
        id = null;
        name = null;
        nameSpace = null;
        def = null;
        xRef = null;
        subsets = null;
        syn = null;
        syns = null;
        comment = null;
        isObsolete = "0";
        altId = null;
        relationships = null;
        relationship = null;
        isa = null;
        consider = null;
        xRefAnalogs = null;
        xRefAnalog = null;
        xRefAnalogOpen = false;
    }

    private class Def {

        private boolean open = false;
        private String defstr = null;
        private ArrayList<XRef> xRef = null;
    }

    private class XRef {

        private boolean open = false;
        private String acc = null;
        private String dbName = null;
        private String name = null;
    }

    private class Synonym {

        private boolean open = false;
        private String scope = null;
        private String text = null;
    }

    private class Relationship {

        private boolean open = false;
        private String type = null;
        private String to = null;
    }
}
