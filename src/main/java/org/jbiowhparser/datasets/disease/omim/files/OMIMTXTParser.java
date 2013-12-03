package org.jbiowhparser.datasets.disease.omim.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhparser.datasets.disease.omim.files.tags.OMIMTabs;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.disease.omim.OMIMTables;
import org.jbiowhpersistence.datasets.disease.omim.controller.OMIMJpaController;
import org.jbiowhpersistence.datasets.disease.omim.entities.OMIM;
import org.jbiowhpersistence.utils.controller.exceptions.PreexistingEntityException;
import org.jbiowhpersistence.utils.entitymanager.JBioWHPersistence;

/**
 * This Class is the parser for the OMIM omim file
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-03-19 09:38:47 +0100
 * (Tue, 19 Mar 2013) $ $LastChangedRevision: 396 $
 *
 * @since Jul 16, 2012
 */
public class OMIMTXTParser extends OMIMTabs {

    public void loader() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(DataSetPersistence.getInstance().getDirectory() + OMIMTables.getInstance().OMIMFILE)))) {
            String line;
            OMIM entry = null;
            OMIMJpaController instance = new OMIMJpaController(JBioWHPersistence.getInstance().getWHEntityManager());
            int count = 0;

            while ((line = reader.readLine()) != null) {
                if (isRecord(line)) {
                    if (entry != null) {
                        count++;
                        VerbLogger.getInstance().log(this.getClass(),
                                "Inserting the OMIM entity: " + count
                                + " with ID: " + entry.getOmimId()
                                + " and WID: " + entry.getWid());
                        try {
                            entry.setDataSetWID(DataSetPersistence.getInstance().getDataset().getWid());
                            instance.create(entry);
                        } catch (PreexistingEntityException ex) {
                            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                            VerbLogger.getInstance().log(this.getClass(),
                                    ex.getMessage());
                            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
                            ex.printStackTrace(System.out);
                            System.exit(1);
                        } catch (Exception ex) {
                            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                            VerbLogger.getInstance().log(this.getClass(),
                                    ex.getMessage());

                            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
                            System.exit(1);
                        }
                    }
                    entry = new OMIM(WIDFactory.getInstance().getWid());
                    WIDFactory.getInstance().increaseWid();
                }
                if (entry != null) {
                    if (isNO(line)) {
                        entry.setOmimId(new Long(reader.readLine()));
                    }
                    if (isTI(line)) {
                        entry.setOmimTI((new OMIMTIParser()).parse(reader));
                    }
                    if (isTX(line)) {
                        OMIMTXParser txs = new OMIMTXParser();
                        entry.setOmimTX(txs.parse(reader));
                        entry.setTx(txs.getTx());
                    }
                    if (isRF(line)) {
                        entry.setOmimRF((new OMIMRFParser()).parse(reader));
                    }
                    if (isCS(line)) {
                        entry.setOmimCS((new OMIMCSParser()).parse(reader, entry.getWid()));
                    }
                    if (isCD(line)) {
                        entry.setOmimCD((new OMIMCDParser()).parse(reader));
                    }
                    if (isED(line)) {
                        entry.setOmimED((new OMIMEDParser()).parse(reader));
                    }
                    if (isAV(line)) {
                        entry.setOmimAV((new OMIMAVParser()).parse(reader));
                    }
                    if (isSA(line)) {
                        entry.setOmimSA((new OMIMSAParser()).parse(reader));
                    }
                    if (isCN(line)) {
                        entry.setOmimCN((new OMIMCNParser()).parse(reader));
                    }
                }
            }
        } catch (IOException ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            VerbLogger.getInstance().log(this.getClass(), "Error: " + ex.toString());
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
            System.exit(1);
        }
    }
}
