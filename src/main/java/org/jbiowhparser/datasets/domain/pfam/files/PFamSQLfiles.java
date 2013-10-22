package org.jbiowhparser.datasets.domain.pfam.files;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.fileformats.sql.JBioWHSQLParser;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhparser.datasets.domain.pfam.links.PFamLinks;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.domain.pfam.PFamTables;

/**
 * This class load the Pfam SQL database into the JBioWH relational schema
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 515 $
 *
 * @since Oct 24, 2012
 */
public class PFamSQLfiles {

    private final String PFAMA_REG_FULL_INSIGNIFICANT = "pfamA_reg_full_insignificant";
    private final String PFAMA_REG_FULL_SIGNIFICANT = "pfamA_reg_full_significant";
    private final String PFAMA_REG_SEED = "pfamA_reg_seed";
    private final String PFAMSEQ = "pfamseq";
    private final String CONTEXT_PFAM_REGIONS = "context_pfam_regions";
    private final String PFAMB = "pfamB";
    private final String PFAMB_REG = "pfamB_reg";
    private final String OTHER_REG = "other_reg";
    private final String PFAMSEQ_DISULPHIDE = "pfamseq_disulphide";
    private final String MARKUP_KEY = "markup_key";
    private final String PFAMSEQ_MARKUP = "pfamseq_markup";
    private final String ARCHITECTURE = "architecture";
    private final String PFAMA_ARCHITECTURE = "pfamA_architecture";
    private final String PFAMA_DATABASE_LINKS = "pfamA_database_links";
    private final String INTERPRO = "interpro";
    private final String LITERATURE_REFERENCES = "literature_references";
    private final String PFAMA_LITERATURE_REFERENCES = "pfamA_literature_references";
    private final String PFAMA_INTERACTIONS = "pfamA_interactions";
    private final String CLANS = "clans";
    private final String CLAN_MEMBERSHIP = "clan_membership";
    private final String CLAN_ARCHITECTURE = "clan_architecture";
    private final String CLAN_LIT_REFS = "clan_lit_refs";
    private final String CLAN_DATABASE_LINKS = "clan_database_links";
    private final String NESTED_LOCATIONS = "nested_locations";
    //private final String NCBI_PFAMA_REG = "ncbi_pfamA_reg";
    private final String PDB = "pdb";
    private final String PDB_PFAMA_REG = "pdb_pfamA_reg";
    private final String PDB_RESIDUE_DATA = "pdb_residue_data";
    private final String COMPLETE_PROTEOMES = "complete_proteomes";
    private final String PROTEOME_PFAMSEQ = "proteome_pfamseq";
    private final String PROTEOME_REGIONS = "proteome_regions";

    public void loadData() {
        try {
            WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
            loadPFAMData(PFamTables.getInstance().PFAMA_ORIG);
            loadPFAMData(PFAMA_REG_FULL_INSIGNIFICANT);
            loadPFAMData(PFAMA_REG_FULL_SIGNIFICANT);
            loadPFAMData(PFAMA_REG_SEED);
            loadPFAMData(PFAMSEQ);
            loadPFAMData(CONTEXT_PFAM_REGIONS);
            loadPFAMData(PFAMB);
            loadPFAMData(PFAMB_REG);
            loadPFAMData(OTHER_REG);
            loadPFAMData(PFAMSEQ_DISULPHIDE);
            loadPFAMData(MARKUP_KEY);
            loadPFAMData(PFAMSEQ_MARKUP);
            loadPFAMData(ARCHITECTURE);
            loadPFAMData(PFAMA_ARCHITECTURE);
            loadPFAMData(PFamTables.getInstance().GENE_ONTOLOGY);
            loadPFAMData(PFAMA_DATABASE_LINKS);
            loadPFAMData(INTERPRO);
            loadPFAMData(LITERATURE_REFERENCES);
            loadPFAMData(PFAMA_LITERATURE_REFERENCES);
            loadPFAMData(PFAMA_INTERACTIONS);
            loadPFAMData(CLANS);
            loadPFAMData(CLAN_MEMBERSHIP);
            loadPFAMData(CLAN_ARCHITECTURE);
            loadPFAMData(CLAN_LIT_REFS);
            loadPFAMData(CLAN_DATABASE_LINKS);
            loadPFAMData(NESTED_LOCATIONS);
            loadPFAMData(PFamTables.getInstance().PFAMA_NCBI);
            //loadPFAMData(NCBI_PFAMA_REG);
            loadPFAMData(PDB);
            loadPFAMData(PDB_PFAMA_REG);
            loadPFAMData(PDB_RESIDUE_DATA);
            loadPFAMData(COMPLETE_PROTEOMES);
            loadPFAMData(PROTEOME_PFAMSEQ);
            loadPFAMData(PROTEOME_REGIONS);

            new PFamSQLReader().loadSQLScript(whdbmsFactory);

            if (DataSetPersistence.getInstance().isRunlinks()) {
                PFamLinks.getInstance().runLink();
            }

            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMA_REG_FULL_INSIGNIFICANT);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMA_REG_FULL_SIGNIFICANT);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMA_REG_SEED);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMSEQ);
            whdbmsFactory.executeUpdate("DROP TABLE " + CONTEXT_PFAM_REGIONS);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMB);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMB_REG);
            whdbmsFactory.executeUpdate("DROP TABLE " + OTHER_REG);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMSEQ_DISULPHIDE);
            whdbmsFactory.executeUpdate("DROP TABLE " + MARKUP_KEY);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMSEQ_MARKUP);
            whdbmsFactory.executeUpdate("DROP TABLE " + ARCHITECTURE);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMA_ARCHITECTURE);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMA_DATABASE_LINKS);
            whdbmsFactory.executeUpdate("DROP TABLE " + INTERPRO);
            whdbmsFactory.executeUpdate("DROP TABLE " + LITERATURE_REFERENCES);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMA_LITERATURE_REFERENCES);
            whdbmsFactory.executeUpdate("DROP TABLE " + PFAMA_INTERACTIONS);
            whdbmsFactory.executeUpdate("DROP TABLE " + CLANS);
            whdbmsFactory.executeUpdate("DROP TABLE " + CLAN_MEMBERSHIP);
            whdbmsFactory.executeUpdate("DROP TABLE " + CLAN_ARCHITECTURE);
            whdbmsFactory.executeUpdate("DROP TABLE " + CLAN_LIT_REFS);
            whdbmsFactory.executeUpdate("DROP TABLE " + CLAN_DATABASE_LINKS);
            whdbmsFactory.executeUpdate("DROP TABLE " + NESTED_LOCATIONS);
            whdbmsFactory.executeUpdate("DROP TABLE " + PDB);
            whdbmsFactory.executeUpdate("DROP TABLE " + PDB_PFAMA_REG);
            whdbmsFactory.executeUpdate("DROP TABLE " + PDB_RESIDUE_DATA);
            whdbmsFactory.executeUpdate("DROP TABLE " + COMPLETE_PROTEOMES);
            whdbmsFactory.executeUpdate("DROP TABLE " + PROTEOME_PFAMSEQ);
            whdbmsFactory.executeUpdate("DROP TABLE " + PROTEOME_REGIONS);
        } catch (SQLException ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
        }
    }

    private void loadPFAMData(String fileName) {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();
        List<String> lineList = JBioWHSQLParser.getInstance().getAllSQL(new File(DataSetPersistence.getInstance().getDirectory() + fileName + ".sql"));
        for (String line : lineList) {
            try {
                if (line.toUpperCase().contains("DROP") || line.toUpperCase().contains("CREATE")) {
                    whdbmsFactory.executeUpdate(line);
                }
            } catch (SQLException ex) {
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
            }
        }
        whdbmsFactory.loadTSVFile(fileName, DataSetPersistence.getInstance().getDirectory() + fileName + ".txt", "LINES TERMINATED BY '\\n';");
    }
}
