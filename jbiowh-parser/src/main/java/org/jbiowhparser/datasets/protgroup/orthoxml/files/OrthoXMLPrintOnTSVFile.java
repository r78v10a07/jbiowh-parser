package org.jbiowhparser.datasets.protgroup.orthoxml.files;

import javax.xml.bind.JAXBElement;
import org.jbiowhcore.utility.fileformats.orthoxml.Database;
import org.jbiowhcore.utility.fileformats.orthoxml.Gene;
import org.jbiowhcore.utility.fileformats.orthoxml.GeneRef;
import org.jbiowhcore.utility.fileformats.orthoxml.Genes;
import org.jbiowhcore.utility.fileformats.orthoxml.Group;
import org.jbiowhcore.utility.fileformats.orthoxml.Property;
import org.jbiowhcore.utility.fileformats.orthoxml.Score;
import org.jbiowhcore.utility.fileformats.orthoxml.ScoreDef;
import org.jbiowhcore.utility.fileformats.orthoxml.Scores;
import org.jbiowhcore.utility.fileformats.orthoxml.Species;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protgroup.orthoxml.OrthoXMLTables;

/**
 * This Class is to print into the TSV files of the OrthoXML module
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Jan 8, 2014
 */
public class OrthoXMLPrintOnTSVFile {

    private static OrthoXMLPrintOnTSVFile singleton;

    private OrthoXMLPrintOnTSVFile() {
    }

    /**
     * Return a OrthoXMLPrintOnTSVFile instance
     *
     * @return a OrthoXMLPrintOnTSVFile instance
     */
    public static synchronized OrthoXMLPrintOnTSVFile getInstance() {
        if (singleton == null) {
            singleton = new OrthoXMLPrintOnTSVFile();
        }
        return singleton;
    }

    /**
     * Print in the TSV file the OrthoXML attributes
     *
     * @param origin
     * @param originVersion
     * @param version
     * @return
     */
    public long printOrthoXMLOnTSVFile(String origin, String originVersion, String version) {
        long WID = WIDFactory.getInstance().getWid();
        WIDFactory.getInstance().increaseWid();

        ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXML, WID, "\t");
        ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXML, origin, "\t");
        ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXML, originVersion, "\t");
        ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXML, version, "\t");
        ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXML, DataSetPersistence.getInstance().getDataset().getWid(), "\n");

        return WID;
    }

    /**
     * Print in the TSV file the Species object
     *
     * @param orthoXML_WID
     * @param species
     */
    public void printSpeciesOnTSVFile(long orthoXML_WID, Species species) {
        if (species != null) {
            long species_WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();

            ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLSPECIE, species_WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLSPECIE, species.getNCBITaxId(), "\t");
            ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLSPECIE, species.getName(), "\t");
            ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLSPECIE, orthoXML_WID, "\n");

            if (species.getDatabase() != null) {
                for (Database d : species.getDatabase()) {
                    long database_WID = WIDFactory.getInstance().getWid();
                    WIDFactory.getInstance().increaseWid();

                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLSPECIESDATABASE, database_WID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLSPECIESDATABASE, d.getGeneLink(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLSPECIESDATABASE, d.getName(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLSPECIESDATABASE, d.getProtLink(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLSPECIESDATABASE, d.getTranscriptLink(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLSPECIESDATABASE, d.getVersion(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLSPECIESDATABASE, species_WID, "\n");

                    if (d.getGenes() != null) {
                        Genes gs = d.getGenes();
                        if (gs.getGene() != null) {
                            for (Gene g : gs.getGene()) {
                                ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLSPECIESDATABASEGENE, g.getId(), "\t");
                                ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLSPECIESDATABASEGENE, g.getGeneId(), "\t");
                                ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLSPECIESDATABASEGENE, g.getProtId(), "\t");
                                ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLSPECIESDATABASEGENE, g.getTranscriptId(), "\t");
                                ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLSPECIESDATABASEGENE, database_WID, "\n");
                            }
                        }
                    }

                }
            }
        }
    }

    /**
     * Print in the TSV file the Scores object
     *
     * @param orthoXML_WID
     * @param scores
     */
    public void printScoresOnTSVFile(long orthoXML_WID, Scores scores) {
        if (scores != null) {
            if (scores.getScoreDef() != null) {
                for (ScoreDef s : scores.getScoreDef()) {
                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLSCORE, s.getId(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLSCORE, s.getDesc(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLSCORE, orthoXML_WID, "\n");
                }
            }
        }
    }

    /**
     *
     * Print in the TSV file the Group object
     *
     * @param orthoXML_WID
     * @param group
     * @param isOrtholog
     */
    public void printGroupOnTSVFile(long orthoXML_WID, Group group, int isOrtholog) {
        if (group != null) {
            long group_WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();

            ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLGROUP, group_WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLGROUP, group.getId(), "\t");
            ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLGROUP, isOrtholog, "\t");
            ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLGROUP, orthoXML_WID, "\n");

            if (group.getGeneRefOrParalogGroupOrOrthologGroup() != null) {
                for (JAXBElement<?> d : group.getGeneRefOrParalogGroupOrOrthologGroup()) {
                    if (d.getValue() instanceof GeneRef) {
                        GeneRef g = (GeneRef) d.getValue();

                        long geneRef_WID = WIDFactory.getInstance().getWid();
                        WIDFactory.getInstance().increaseWid();

                        ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLGROUPGENEREF, geneRef_WID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLGROUPGENEREF, g.getId(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.getInstance().ORTHOXMLGROUPGENEREF, group_WID, "\n");

                        if (g.getScore() != null) {
                            for (Score s : g.getScore()) {
                                ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLGROUPGENEREFSCORE, s.getId(), "\t");
                                ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLGROUPGENEREFSCORE, s.getValue(), "\t");
                                ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLGROUPGENEREFSCORE, geneRef_WID, "\n");
                            }
                        }
                    }
                }
            }

            if (group.getProperty() != null) {
                for (Property p : group.getProperty()) {
                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLGROUPPROPERTY, p.getName(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLGROUPPROPERTY, p.getValue(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLGROUPPROPERTY, group_WID, "\n");
                }
            }

            if (group.getScore() != null) {
                for (Score s : group.getScore()) {
                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLGROUPSCORE, s.getId(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLGROUPSCORE, s.getValue(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(OrthoXMLTables.ORTHOXMLGROUPSCORE, group_WID, "\n");
                }
            }

        }
    }
}
