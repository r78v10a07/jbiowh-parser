package org.jbiowhparser.datasets.pathway.kegg.kgml;

import java.util.List;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.pathway.kegg.kgml.xml.Entry;
import org.jbiowhparser.datasets.pathway.kegg.kgml.xml.Graphics;
import org.jbiowhparser.datasets.pathway.kegg.kgml.xml.Pathway;
import org.jbiowhparser.datasets.pathway.kegg.kgml.xml.Product;
import org.jbiowhparser.datasets.pathway.kegg.kgml.xml.Reaction;
import org.jbiowhparser.datasets.pathway.kegg.kgml.xml.Relation;
import org.jbiowhparser.datasets.pathway.kegg.kgml.xml.Substrate;
import org.jbiowhparser.datasets.pathway.kegg.kgml.xml.Subtype;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.pathway.kegg.KEGGTables;

/**
 * This class is print on the TSV file forma to be inserted on the DBMS
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Nov 16, 2011
 */
public class PathwayPrintOnTSVFile {

    private static PathwayPrintOnTSVFile singleton;

    private PathwayPrintOnTSVFile() {
    }

    /**
     * Return a PathwayPrintOnTSVFile
     *
     * @return a PathwayPrintOnTSVFile
     */
    public static synchronized PathwayPrintOnTSVFile getInstance() {
        if (singleton == null) {
            singleton = new PathwayPrintOnTSVFile();
        }
        return singleton;
    }

    /**
     * Print on TSV file the Pathway object loaded from a KGML file
     *
     * @param pathway
     */
    public void printPathwayOnTSVFile(Pathway pathway) {
        long WID = WIDFactory.getInstance().getWid();
        WIDFactory.getInstance().increaseWid();

        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAY, WID, "\t");
        if (pathway.getName().contains(":")) {
            ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAY, pathway.getName().split(":")[1], "\t");
        } else {
            ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAY, pathway.getName(), "\t");
        }
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAY, pathway.getOrg(), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAY, pathway.getNumber(), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAY, pathway.getTitle(), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAY, pathway.getImage(), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAY, pathway.getLink(), "\t");
        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAY, DataSetPersistence.getInstance().getDataset().getWid(), "\n");

        printEntryOnTSVFile(pathway.getEntry(), WID);
        printRelationOnTSVFile(pathway.getRelation(), WID);
        printReactionOnTSVFile(pathway.getReaction(), WID);
    }

    private void printReactionOnTSVFile(List<Reaction> reactions, long WID) {
        if (reactions != null) {
            for (Reaction reaction : reactions) {
                long entryWID = WIDFactory.getInstance().getWid();
                WIDFactory.getInstance().increaseWid();

                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYREACTION, entryWID, "\t");
                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYREACTION, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYREACTION, reaction.getId(), "\t");
                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYREACTION, reaction.getName(), "\t");
                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYREACTION, reaction.getType(), "\n");

                if (reaction.getSubstrate() != null) {
                    for (Substrate substrate : reaction.getSubstrate()) {

                        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYREACTIONSUBSTRATE, entryWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYREACTIONSUBSTRATE, substrate.getId(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYREACTIONSUBSTRATE, substrate.getName(), "\n");
                    }
                }

                if (reaction.getProduct() != null) {
                    for (Product product : reaction.getProduct()) {

                        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYREACTIONPRODUCT, entryWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYREACTIONPRODUCT, product.getId(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYREACTIONPRODUCT, product.getName(), "\n");
                    }
                }
            }
        }
    }

    private void printRelationOnTSVFile(List<Relation> relations, long WID) {
        if (relations != null) {
            for (Relation relation : relations) {
                long entryWID = WIDFactory.getInstance().getWid();
                WIDFactory.getInstance().increaseWid();

                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYRELATION, entryWID, "\t");
                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYRELATION, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYRELATION, relation.getEntry1(), "\t");
                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYRELATION, relation.getEntry2(), "\t");
                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYRELATION, relation.getType(), "\t");
                for (Subtype subtype : relation.getSubtype()) {
                    ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYRELATIONSUBTYPE, entryWID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYRELATIONSUBTYPE, subtype.getName(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYRELATIONSUBTYPE, subtype.getValue(), "\n");
                }

            }
        }
    }

    private void printEntryOnTSVFile(List<Entry> entries, long WID) {
        if (entries != null) {
            for (Entry entry : entries) {
                long entryWID = WIDFactory.getInstance().getWid();
                WIDFactory.getInstance().increaseWid();

                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRY, entryWID, "\t");
                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRY, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRY, entry.getId(), "\t");
                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRY, entry.getName(), "\t");
                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRY, entry.getType(), "\t");
                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRY, entry.getReaction(), "\t");
                ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRY, entry.getLink(), "\n");
                for (Graphics graph : entry.getGraphics()) {
                    long graphWID = WIDFactory.getInstance().getWid();
                    WIDFactory.getInstance().increaseWid();

                    ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRYGRAPHIC, graphWID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRYGRAPHIC, entryWID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRYGRAPHIC, graph.getName(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRYGRAPHIC, graph.getFgcolor(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRYGRAPHIC, graph.getBgcolor(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRYGRAPHIC, graph.getType(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRYGRAPHIC, graph.getX(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRYGRAPHIC, graph.getY(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRYGRAPHIC, graph.getCoords(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRYGRAPHIC, graph.getWidth(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRYGRAPHIC, graph.getHeight(), "\n");
                }

                for (String name : entry.getName().split(" ")) {
                    String table = null;
                    switch (entry.getType()) {
                        case "enzyme":
                            table = KEGGTables.getInstance().KEGGPATHWAYENTRYENZYME;
                            break;
                        case "compound":
                            table = KEGGTables.getInstance().KEGGPATHWAYENTRYCOMPOUND;
                            break;
                        case "gene":
                            table = KEGGTables.getInstance().KEGGPATHWAYENTRYGENE;
                            break;
                        case "ortholog":
                            table = KEGGTables.getInstance().KEGGPATHWAYENTRYORTHOLOGY;
                            break;
                    }
                    if (table != null) {
                        ParseFiles.getInstance().printOnTSVFile(table, WID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(table, name.substring(name.indexOf(":") + 1), "\n");
                    }
                }

                if (entry.getReaction() != null) {
                    for (String name : entry.getReaction().split(" ")) {
                        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRYREACTION, WID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(KEGGTables.getInstance().KEGGPATHWAYENTRYREACTION, name.substring(name.indexOf(":") + 1), "\n");
                    }
                }
            }
        }
    }
}
