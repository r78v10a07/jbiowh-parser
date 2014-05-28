package org.jbiowhparser.datasets.protgroup.cog.files;

import java.sql.SQLException;
import org.jbiowhdbms.dbms.JBioWHDBMSSingleton;
import org.jbiowhdbms.dbms.JBioWHDBMS;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protgroup.cog.COGTables;

/**
 * This class is to parser the eggNOG flat files
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Jan 8, 2014
 */
public class EggNOGParserFiles {

    public void parserFiles() throws SQLException {
        JBioWHDBMS whdbmsFactory = JBioWHDBMSSingleton.getInstance().getWhdbmsFactory();
        String descrip = DataSetPersistence.getInstance().getDirectory() + "/NOG.description.txt";
        String funcat = DataSetPersistence.getInstance().getDirectory() + "/NOG.funccat.txt";
        String members = DataSetPersistence.getInstance().getDirectory() + "/NOG.members.txt";

        whdbmsFactory.executeUpdate("ALTER TABLE " + COGTables.getInstance().COGORTHOLOGOUSGROUP + " AUTO_INCREMENT=" + WIDFactory.getInstance().getWid());

        whdbmsFactory.loadTSVFile(COGTables.getInstance().COGORTHOLOGOUSGROUP, descrip,
                "(Id,GroupFunction,DataSet_WID)"
                + " set "
                + "DataSet_WID=" + DataSetPersistence.getInstance().getDataset().getWid());

        WIDFactory.getInstance().setWid(whdbmsFactory.getLongColumnLabel("select MAX(WID) + 1 as WID from "
                + COGTables.getInstance().COGORTHOLOGOUSGROUP, "WID"));

        whdbmsFactory.execute("DROP TABLE IF EXISTS `NOGMember`");
        whdbmsFactory.execute("CREATE TABLE `NOGMember` ("
                + "`COG` VARCHAR(10) NOT NULL,"
                + "`Id` VARCHAR(100) NOT NULL,"
                + "INDEX `cog_index` (`COG` ASC),"
                + "INDEX `id_index` (`Id` ASC)) "
                + "ENGINE = MyISAM "
                + "DEFAULT CHARACTER SET = utf8 "
                + "COLLATE = utf8_bin");
        whdbmsFactory.loadTSVFile("NOGMember", members, "  IGNORE 1 LINES");

        whdbmsFactory.execute("DROP TABLE IF EXISTS `NOGFuncat`");
        whdbmsFactory.execute("CREATE TABLE `NOGFuncat` ("
                + "`COG` VARCHAR(10) NOT NULL,"
                + "`FunCat` VARCHAR(10) NOT NULL,"
                + "PRIMARY KEY (`COG`)) "
                + "ENGINE = MyISAM "
                + "DEFAULT CHARACTER SET = utf8 "
                + "COLLATE = utf8_bin");

        whdbmsFactory.loadTSVFile("NOGFuncat", funcat);

        whdbmsFactory.execute("INSERT INTO "
                + COGTables.COGORTHOLOGOUSGROUP_HAS_COGFUNCCLASS
                + " (COGOrthologousGroup_WID,COGFuncClass_WID) "
                + "SELECT g.WID,c.WID FROM "
                + COGTables.getInstance().COGFUNCCLASS
                + " c INNER JOIN "
                + " NOGFuncat f ON INSTR(f.Funcat,c.Letter) != 0 INNER JOIN "
                + COGTables.getInstance().COGORTHOLOGOUSGROUP
                + " g ON f.COG = g.Id");

        whdbmsFactory.execute("INSERT INTO "
                + COGTables.getInstance().COGMEMBER
                + " (COGOrthologousGroup_WID,Organism,Id) "
                + "SELECT g.WID,SUBSTRING(m.Id,1,INSTR(m.Id,'.') - 1),SUBSTRING(m.Id,INSTR(m.Id,'.') + 1) FROM "
                + COGTables.getInstance().COGORTHOLOGOUSGROUP
                + " g INNER JOIN "
                + " NOGMember m ON m.COG = g.Id ");

        whdbmsFactory.execute("INSERT INTO "
                + COGTables.getInstance().COGMEMBER_TAXID
                + " (Organism,TaxId) "
                + "SELECT Organism,CAST(Organism AS UNSIGNED) FROM "
                + COGTables.getInstance().COGMEMBER
                + "  group by Organism");
    }
}
