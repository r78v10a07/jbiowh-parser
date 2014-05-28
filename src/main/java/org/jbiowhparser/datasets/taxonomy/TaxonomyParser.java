package org.jbiowhparser.datasets.taxonomy;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhparser.JBioWHParser;
import org.jbiowhparser.ParserFactory;
import org.jbiowhparser.datasets.taxonomy.links.TaxonomyLinks;
import org.jbiowhparser.utils.FTPFacade;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.taxonomy.TaxonomyTables;

/**
 * This Class is the Taxonomy Parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-05-29 11:24:54 +0200
 * (Wed, 29 May 2013) $ $LastChangedRevision: 591 $
 *
 * @since Jun 18, 2011
 */
public class TaxonomyParser extends ParserFactory implements JBioWHParser {

    final static int BUFFER = 2048;

    /**
     * Run the Taxonomy Parser
     *
     * @throws java.sql.SQLException
     */
    @Override
    public void runLoader() throws SQLException {
        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();

        ParseFiles.getInstance().start(TaxonomyTables.getInstance().getTables(), DataSetPersistence.getInstance().getTempdir());

        if (DataSetPersistence.getInstance().isonlineFTP()) {
            FTPFacade ftp = new FTPFacade(DataSetPersistence.getInstance().getOnlineSite(),
                    DataSetPersistence.getInstance().getOnlineUser(),
                    DataSetPersistence.getInstance().getOnlinePasswd());

            try {
                int index = DataSetPersistence.getInstance().getOnlinePath().lastIndexOf("/");
                try (OutputStream tmpFile = new FileOutputStream(DataSetPersistence.getInstance().getTempdir() + DataSetPersistence.getInstance().getOnlinePath().substring(index + 1))) {
                    ftp.getFtpClient().retrieveFile(DataSetPersistence.getInstance().getOnlinePath(), tmpFile);
                }
                InputStream in = new FileInputStream(DataSetPersistence.getInstance().getTempdir() + DataSetPersistence.getInstance().getOnlinePath().substring(index + 1));
                ZipArchiveInputStream archiveInput = (ZipArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream("zip", in);
                ZipArchiveEntry entry;
                while ((entry = archiveInput.getNextZipEntry()) != null) {
                    if (!entry.isDirectory()) {
                        int count;
                        byte data[] = new byte[BUFFER];
                        try (FileOutputStream fos = new FileOutputStream(DataSetPersistence.getInstance().getTempdir() + entry.getName()); BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER)) {
                            while ((count = archiveInput.read(data, 0, BUFFER)) != -1) {
                                dest.write(data, 0, count);
                            }
                        }
                    }
                }
                DataSetPersistence.getInstance().setDirectory(DataSetPersistence.getInstance().getTempdir());
                delete = true;
            } catch (IOException | ArchiveException ex) {
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
                DataSetPersistence.getInstance().getDataset().setStatus("Error");
                DataSetPersistence.getInstance().updateDataSet();
                WIDFactory.getInstance().updateWIDTable();
                System.exit(-1);
            }
            ftp.close();
        }

        insertDivision();
        insertGencode();
        insertNodes();
        insertNames();
        insertCitations();

        if (DataSetPersistence.getInstance().isRunlinks()) {
            TaxonomyLinks.getInstance().runLink();
        }

        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
        ParseFiles.getInstance().end();

        cleanTmpDir();
    }

    /**
     * Insert the Division data storage into the division.dmp file into the
     * Division Table. After insertion the DivisionTemp Table is truncated.
     */
    private void insertDivision() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + TaxonomyTables.getInstance().TAXONOMYDIVISION);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().TAXONOMYDIVISION);
        }

        VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + TaxonomyTables.getInstance().DIVISIONTEMP);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().DIVISIONTEMP);

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + TaxonomyTables.DIVISIONFILE + " file");

        whdbmsFactory.loadTSVFile(TaxonomyTables.getInstance().DIVISIONTEMP, DataSetPersistence.getInstance().getDirectory() + TaxonomyTables.DIVISIONFILE,
                " FIELDS TERMINATED BY '|' ENCLOSED BY '\\t' ESCAPED BY '\\\\'");

        whdbmsFactory.executeUpdate("ALTER TABLE " + TaxonomyTables.getInstance().TAXONOMYDIVISION + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());

        whdbmsFactory.executeUpdate("INSERT INTO "
                + TaxonomyTables.getInstance().TAXONOMYDIVISION
                + " (id,Code,Name,Comment)"
                + "select id,Code,Name,Comment"
                + " from "
                + TaxonomyTables.getInstance().DIVISIONTEMP);

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + TaxonomyTables.getInstance().TAXONOMYDIVISION, "WID"));

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().DIVISIONTEMP);
    }

    /**
     * Insert the GenCode data storage into the gencode.dmp file into the
     * GenCode Table. After insertion the GenCodeTemp Table is truncate.
     */
    private void insertGencode() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + TaxonomyTables.getInstance().TAXONOMYGENCODE);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().TAXONOMYGENCODE);
        }

        VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + TaxonomyTables.getInstance().GENCODETEMP);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().GENCODETEMP);

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + TaxonomyTables.GENCODEFILE + " file");

        whdbmsFactory.loadTSVFile(TaxonomyTables.getInstance().GENCODETEMP, DataSetPersistence.getInstance().getDirectory() + TaxonomyTables.GENCODEFILE,
                " FIELDS TERMINATED BY '|' ENCLOSED BY '\\t' ESCAPED BY '\\\\'");

        whdbmsFactory.executeUpdate("ALTER TABLE " + TaxonomyTables.getInstance().TAXONOMYGENCODE + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());

        whdbmsFactory.executeUpdate("INSERT INTO "
                + TaxonomyTables.getInstance().TAXONOMYGENCODE
                + "(GenCodeId,Abbreviation,Name,Code,Start) "
                + "select GenCodeId,Abbreviation,Name,Code,Start"
                + " from "
                + TaxonomyTables.getInstance().GENCODETEMP);

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + TaxonomyTables.getInstance().TAXONOMYGENCODE, "WID"));

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().GENCODETEMP);
    }

    /**
     * Insert the Taxonomy data storage into the nodes.dmp file into the
     * Taxonomy Table. After insertion the Nodes Table is truncated
     */
    private void insertNodes() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + TaxonomyTables.getInstance().TAXONOMY);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().TAXONOMY);
        }

        VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + TaxonomyTables.getInstance().NODES);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().NODES);

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + TaxonomyTables.NODESFILE + " file");

        whdbmsFactory.loadTSVFile(TaxonomyTables.getInstance().NODES, DataSetPersistence.getInstance().getDirectory() + TaxonomyTables.NODESFILE,
                " FIELDS TERMINATED BY '|' ENCLOSED BY '\\t' ESCAPED BY '\\\\'");

        whdbmsFactory.executeUpdate("ALTER TABLE " + TaxonomyTables.getInstance().TAXONOMY + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());

        whdbmsFactory.executeUpdate("INSERT INTO "
                + TaxonomyTables.getInstance().TAXONOMY
                + "(TaxId,ParentTaxId,Rank,EMBLCode,TaxonomyDivision_WID,"
                + "InheritedDivision,TaxonomyGenCode_WID,InheritedGencode,"
                + "TaxonomyMCGenCode_WID,InheritedMCGencode,DataSetWID) "
                + "select n.tax_id,n.parent,n.rank_name,n.embl,d.WID,"
                + "n.inherited_div_flag,g.WID,n.inherited_GC,g1.WID,"
                + "n.inherited_MGC,"
                + DataSetPersistence.getInstance().getDataset().getWid()
                + " from "
                + TaxonomyTables.getInstance().NODES
                + " n inner join "
                + TaxonomyTables.getInstance().TAXONOMYDIVISION
                + " d on d.id = n.division inner join "
                + TaxonomyTables.getInstance().TAXONOMYGENCODE
                + " g on g.GenCodeId = n.genetic inner join "
                + TaxonomyTables.getInstance().TAXONOMYGENCODE
                + " g1 on g1.GenCodeId = n.mitochondrial");

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + TaxonomyTables.getInstance().TAXONOMY, "WID"));

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().NODES);
    }

    /**
     * Insert the data storage into the division.dmp file into the Division
     * Table. After insertion the DivisionTemp Table is drooped.
     */
    private void insertNames() throws SQLException {

        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + TaxonomyTables.getInstance().TAXONOMYSYNONYMNAMECLASS);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().TAXONOMYSYNONYMNAMECLASS);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + TaxonomyTables.getInstance().TAXONOMYSYNONYM);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().TAXONOMYSYNONYM);
        }

        VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + TaxonomyTables.getInstance().NAMES);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().NAMES);

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + TaxonomyTables.NAMESFILE + " file");

        whdbmsFactory.loadTSVFile(TaxonomyTables.getInstance().NAMES, DataSetPersistence.getInstance().getDirectory() + TaxonomyTables.NAMESFILE,
                " FIELDS TERMINATED BY '|' ENCLOSED BY '\\t' ESCAPED BY '\\\\'");

        whdbmsFactory.executeUpdate("ALTER TABLE " + TaxonomyTables.getInstance().TAXONOMYSYNONYMNAMECLASS + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());

        whdbmsFactory.executeUpdate("INSERT INTO "
                + TaxonomyTables.getInstance().TAXONOMYSYNONYMNAMECLASS
                + "(NameClass) "
                + "select name_class"
                + " from "
                + TaxonomyTables.getInstance().NAMES
                + " group by name_class");

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + TaxonomyTables.getInstance().TAXONOMYSYNONYMNAMECLASS, "WID"));

        whdbmsFactory.executeUpdate("INSERT IGNORE INTO "
                + TaxonomyTables.getInstance().TAXONOMYSYNONYM
                + "(Taxonomy_WID,Synonym,TaxonomySynonymNameClass_WID) "
                + "select t.WID,n.name_txt,m.WID"
                + " from "
                + TaxonomyTables.getInstance().NAMES
                + " n inner join "
                + TaxonomyTables.getInstance().TAXONOMY
                + " t on t.TaxId = n.tax_id inner join "
                + TaxonomyTables.getInstance().TAXONOMYSYNONYMNAMECLASS
                + " m on m.NameClass = n.name_class");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().NAMES);
    }

    /**
     * Insert the Citations data storage into the citations.dmp file into the
     * After insertion the GenCodeTemp Table is drooped.
     */
    private void insertCitations() throws SQLException {

        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        if (DataSetPersistence.getInstance().isDroptables()) {
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + TaxonomyTables.getInstance().TAXONOMYUNPARSECITATION);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().TAXONOMYUNPARSECITATION);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + TaxonomyTables.getInstance().TAXONOMY_HAS_TAXONOMYUNPARSECITATION);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().TAXONOMY_HAS_TAXONOMYUNPARSECITATION);
            VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + TaxonomyTables.getInstance().TAXONOMYPMID);
            whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().TAXONOMYPMID);
        }

        VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + TaxonomyTables.getInstance().NCBICITATIONTEMP);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().NCBICITATIONTEMP);
        VerbLogger.getInstance().log(this.getClass(), "Truncating table: " + TaxonomyTables.getInstance().CITTAX);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().CITTAX);

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + TaxonomyTables.CITATIONSFILE + " file");

        whdbmsFactory.loadTSVFile(TaxonomyTables.getInstance().NCBICITATIONTEMP, DataSetPersistence.getInstance().getDirectory() + TaxonomyTables.CITATIONSFILE,
                " FIELDS TERMINATED BY '|' ENCLOSED BY '\\t' ESCAPED BY '\\\\'");

        splitCitId();

        whdbmsFactory.executeUpdate("ALTER TABLE "
                + TaxonomyTables.getInstance().TAXONOMYUNPARSECITATION
                + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());
        whdbmsFactory.executeUpdate("insert into "
                + TaxonomyTables.getInstance().TAXONOMYUNPARSECITATION
                + " (CitId,CitKey,URL,Text)"
                + "select cit_id,cit_key,url,text"
                + " from "
                + TaxonomyTables.getInstance().NCBICITATIONTEMP
                + " where medline_id = 0");

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + TaxonomyTables.getInstance().TAXONOMYUNPARSECITATION, "WID"));

        whdbmsFactory.executeUpdate("insert ignore into "
                + TaxonomyTables.getInstance().TAXONOMY_HAS_TAXONOMYUNPARSECITATION
                + " (Taxonomy_WID, TaxonomyUnParseCitation_WID) "
                + "select t.WID,u.WID"
                + " from "
                + TaxonomyTables.getInstance().TAXONOMY
                + " t inner join "
                + TaxonomyTables.getInstance().CITTAX
                + " c on "
                + "c.tax_id = t.TaxId inner join "
                + TaxonomyTables.getInstance().TAXONOMYUNPARSECITATION
                + " u on "
                + "u.CitId = c.cit_id");

        whdbmsFactory.executeUpdate("insert ignore into "
                + TaxonomyTables.getInstance().TAXONOMYPMID
                + "(Taxonomy_WID,PMID) "
                + "select t.WID,n.medline_id"
                + " from "
                + TaxonomyTables.getInstance().TAXONOMY
                + " t inner join "
                + TaxonomyTables.getInstance().CITTAX
                + " c on "
                + "c.tax_id = t.TaxId inner join "
                + TaxonomyTables.getInstance().NCBICITATIONTEMP
                + " n on "
                + "n.cit_id = c.cit_id where n.medline_id != 0");

        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().NCBICITATIONTEMP);
        whdbmsFactory.executeUpdate("TRUNCATE TABLE " + TaxonomyTables.getInstance().CITTAX);

    }

    /**
     * This metho split the taxid_list field included into the citations.dmp
     * file into a string array repeating the citation id
     *
     * @param whdbms the WH DBMS manager
     * @param dataset the WH DataSet manager
     * @throws WHDBMSNotFoundException
     *
     */
    private void splitCitId() {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();

        whdbmsFactory.splitField(ParseFiles.getInstance().getPrintWriterFromName(TaxonomyTables.getInstance().CITTAX),
                "select cit_id,taxid_list from "
                + TaxonomyTables.getInstance().NCBICITATIONTEMP
                + " where taxid_list!= ''",
                "cit_id", "taxid_list", " ");

        VerbLogger.getInstance().log(this.getClass(), "Inserting the " + TaxonomyTables.getInstance().CITTAX + " file");
        ParseFiles.getInstance().getPrintWriterFromName(TaxonomyTables.getInstance().CITTAX).close();
        whdbmsFactory.loadTSVFile(TaxonomyTables.getInstance().CITTAX, ParseFiles.getInstance().getFileAbsolutName(TaxonomyTables.getInstance().CITTAX));
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(TaxonomyTables.getInstance().getTables());
    }
}
