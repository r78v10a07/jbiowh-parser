package org.jbiowhparser.datasets.protein.links;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;
import org.jbiowhpersistence.datasets.protein.ProteinTables;

/**
 * This Class populates the Protein_has_GenePTT relationship table
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Mar 13, 2013
 */
public class ProteinGenePTTLink {

    private static ProteinGenePTTLink singleton;

    private ProteinGenePTTLink() {
    }

    /**
     * Return a ProteinGenePTTLink instance
     *
     * @return a ProteinGenePTTLink instance
     */
    public static synchronized ProteinGenePTTLink getInstance() {
        if (singleton == null) {
            singleton = new ProteinGenePTTLink();
        }
        return singleton;
    }

    /**
     * Create the Protein-Gene relationship table
     *
     * @throws SQLException
     */
    public void runLink() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        VerbLogger.getInstance().log(this.getClass(), "Populating table: " + ProteinTables.PROTEIN_HAS_GENEPTT);

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + ProteinTables.PROTEIN_HAS_GENEPTT);
        whdbmsFactory.disableKeys(ProteinTables.PROTEIN_HAS_GENEPTT);

        whdbmsFactory.executeUpdate("insert into "
                + ProteinTables.PROTEIN_HAS_GENEPTT
                + " (Protein_WID,GenePTT_ProteinGi) "
                + " select pg.Protein_WID,gptt.GenePTT_ProteinGi from "
                + GeneTables.GENEINFO_HAS_GENEPTT
                + " gptt inner join "
                + ProteinTables.PROTEIN_HAS_GENEINFO
                + " pg on pg.GeneInfo_WID = gptt.GeneInfo_WID"
                + " group by pg.Protein_WID,gptt.GenePTT_ProteinGi");
        whdbmsFactory.enableKeys(ProteinTables.PROTEIN_HAS_GENEPTT);
    }
}
