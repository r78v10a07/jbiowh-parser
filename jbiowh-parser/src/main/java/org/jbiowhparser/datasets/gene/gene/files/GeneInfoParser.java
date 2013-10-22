package org.jbiowhparser.datasets.gene.gene.files;

import java.sql.SQLException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhdbms.dbms.WHDBMSFactory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.gene.gene.GeneTables;

/**
 * This Class is the GeneInfo parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-05-29 11:24:54 +0200 (Wed, 29 May 2013) $
 * $LastChangedRevision: 591 $
 * @since Jul 5, 2011
 */
public class GeneInfoParser {

    /**
     * This method load the data in gene_info file to its relational tables
     *
     * @throws SQLException
     */
    public void loader() throws SQLException {
        WHDBMSFactory whdbmsFactory = JBioWHDBMS.getInstance().getWhdbmsFactory();

        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENE_INFO);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENE_INFO);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENEINFO);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENEINFO);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENEINFOWIDSYNONYMSTEMP);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENEINFOWIDSYNONYMSTEMP);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENEINFOSYNONYMS);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENEINFOSYNONYMS);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENEINFOWIDDBXREFSTEMP);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENEINFOWIDDBXREFSTEMP);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + GeneTables.getInstance().GENEINFODBXREFS);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENEINFODBXREFS);
        }

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + GeneTables.getInstance().GENE_INFO + " file");

        whdbmsFactory.loadTSVFile(GeneTables.getInstance().GENE_INFO, DataSetPersistence.getInstance().getDirectory() + GeneTables.getInstance().GENE_INFO,
                "  IGNORE 1 LINES"
                + "(@TaxID,GeneID,@Symbol,@LocusTag,Synonyms,dbXrefs,@Chromosome,@MapLocation,@Description,@TypeOfGene,"
                + "@SymbolFromNomenclature,@FullNameFromNomenclatureAuthority,@NomenclatureStatus,@OtherDesignations,@ModificationDate)");

        whdbmsFactory.executeUpdate("ALTER TABLE " + GeneTables.getInstance().GENEINFO + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());

        whdbmsFactory.loadTSVFile(GeneTables.getInstance().GENEINFO, DataSetPersistence.getInstance().getDirectory() + GeneTables.getInstance().GENE_INFO,
                "  IGNORE 1 LINES"
                + "(TaxID,GeneID,@Symbol,@LocusTag,@Synonyms,@dbXrefs,@Chromosome,@MapLocation,@Description,@TypeOfGene,"
                + "@SymbolFromNomenclature,@FullNameFromNomenclatureAuthority,@NomenclatureStatus,@OtherDesignations,@ModificationDate)"
                + " set "
                + "Symbol=REPLACE(@Symbol,'-',NULL),"
                + "LocusTag=REPLACE(@LocusTag,'-',NULL),"
                + "Chromosome=REPLACE(@Chromosome,'-',NULL),"
                + "MapLocation=REPLACE(@MapLocation,'-',NULL),"
                + "Description=REPLACE(@Description,'-',NULL),"
                + "TypeOfGene=REPLACE(@TypeOfGene,'-',NULL),"
                + "SymbolFromNomenclature=REPLACE(@SymbolFromNomenclature,'-',NULL),"
                + "FullNameFromNomenclatureAuthority=REPLACE(@FullNameFromNomenclatureAuthority,'-',NULL),"
                + "NomenclatureStatus=REPLACE(@NomenclatureStatus,'-',NULL),"
                + "OtherDesignations=REPLACE(@OtherDesignations,'-',NULL),"
                + "ModificationDate=DATE(REPLACE(@ModificationDate,'-',NULL)),"
                + "DataSetWID=" + DataSetPersistence.getInstance().getDataset().getWid());

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + GeneTables.getInstance().GENEINFO, "WID"));

        whdbmsFactory.splitField(ParseFiles.getInstance().getPrintWriterFromName(GeneTables.getInstance().GENEINFOWIDSYNONYMSTEMP),
                "select GeneID,Synonyms from "
                + GeneTables.getInstance().GENE_INFO
                + " where Synonyms != '-' group by GeneID,Synonyms",
                "GeneID", "Synonyms", "\\|");

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + GeneTables.getInstance().GENEINFOWIDSYNONYMSTEMP + " file");
        ParseFiles.getInstance().getPrintWriterFromName(GeneTables.getInstance().GENEINFOWIDSYNONYMSTEMP).close();
        whdbmsFactory.loadTSVFile(GeneTables.getInstance().GENEINFOWIDSYNONYMSTEMP, ParseFiles.getInstance().getFileAbsolutName(GeneTables.getInstance().GENEINFOWIDSYNONYMSTEMP));

        whdbmsFactory.executeUpdate("insert into "
                + GeneTables.getInstance().GENEINFOSYNONYMS
                + " (GeneInfo_WID,Synonyms) "
                + "select g.WID,s.Synonyms from "
                + GeneTables.getInstance().GENEINFO
                + " g inner join "
                + GeneTables.getInstance().GENEINFOWIDSYNONYMSTEMP
                + " s on s.GeneID = g.GeneID group by g.WID,s.Synonyms");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENEINFOWIDSYNONYMSTEMP);

        whdbmsFactory.splitField(ParseFiles.getInstance().getPrintWriterFromName(GeneTables.getInstance().GENEINFOWIDDBXREFSTEMP),
                "select GeneID,dbXrefs from "
                + GeneTables.getInstance().GENE_INFO
                + " where dbXrefs != '-' group by GeneID,dbXrefs",
                "GeneID", "dbXrefs", "\\|", ":");

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + GeneTables.getInstance().GENEINFOWIDDBXREFSTEMP + " file");
        ParseFiles.getInstance().getPrintWriterFromName(GeneTables.getInstance().GENEINFOWIDDBXREFSTEMP).close();
        whdbmsFactory.loadTSVFile(GeneTables.getInstance().GENEINFOWIDDBXREFSTEMP, ParseFiles.getInstance().getFileAbsolutName(GeneTables.getInstance().GENEINFOWIDDBXREFSTEMP));

        whdbmsFactory.executeUpdate("insert into "
                + GeneTables.getInstance().GENEINFODBXREFS
                + " (GeneInfo_WID,DBName,ID) "
                + "select g.WID,s.DBName,s.ID from "
                + GeneTables.getInstance().GENEINFO
                + " g inner join "
                + GeneTables.getInstance().GENEINFOWIDDBXREFSTEMP
                + " s on s.GeneID = g.GeneID group by g.WID,s.DBName,s.ID");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + GeneTables.getInstance().GENEINFOWIDDBXREFSTEMP);
    }
}
