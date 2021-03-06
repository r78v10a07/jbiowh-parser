package org.jbiowhparser.datasets.gene.genbank;

import java.sql.SQLException;
import java.util.Date;
import org.jbiowhparser.JBioWHParser;
import org.jbiowhparser.ParserFactory;
import org.jbiowhparser.datasets.gene.genbank.files.GeneBankUpdateFlatParser;
import org.jbiowhparser.datasets.gene.genbank.links.GeneBankLinks;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.gene.genebank.GeneBankTables;

/**
 * This class is
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-05-29 11:24:54 +0200
 * (Wed, 29 May 2013) $ $LastChangedRevision: 591 $
 *
 * @since May 13, 2013
 */
public class GeneBankUpdateParser extends ParserFactory implements JBioWHParser {

    @Override
    public void runLoader() throws SQLException {
        DataSetPersistence.getInstance().insertDataSet();
        WIDFactory.getInstance().getWIDFromDataBase();

        new GeneBankUpdateFlatParser().load();

        if (DataSetPersistence.getInstance().isRunlinks()) {
            GeneBankLinks.getInstance().runLink();
        }

        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
        DataSetPersistence.getInstance().getDataset().setStatus("Inserted");
        DataSetPersistence.getInstance().updateDataSet();
        WIDFactory.getInstance().updateWIDTable();
    }

    @Override
    public void runCleaner() throws SQLException {
        clean(GeneBankTables.getInstance().getTables());
    }
}
