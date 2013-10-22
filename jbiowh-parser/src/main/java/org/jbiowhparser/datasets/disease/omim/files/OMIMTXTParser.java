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
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-03-19 09:38:47 +0100 (Tue, 19 Mar 2013) $
 * $LastChangedRevision: 396 $
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
                if (isNO(line)) {
                    entry.setOmimId(new Long(reader.readLine()));
                }
                if (isTI(line)) {
                    entry.setOmimTIs((new OMIMTIParser()).parse(reader, entry.getWid()));
                }
                if (isTX(line)) {
                    OMIMTXParser txs = new OMIMTXParser();
                    entry.setOmimTXs(txs.parse(reader, entry.getWid()));
                    entry.setTx(txs.getTx());
                }
                if (isRF(line)) {
                    entry.setOmimRFs((new OMIMRFParser()).parse(reader, entry.getWid()));
                }
                if (isCS(line)) {
                    entry.setOmimCSs((new OMIMCSParser()).parse(reader, entry.getWid()));
                }
                if (isCD(line)) {
                    entry.setOmimCDs((new OMIMCDParser()).parse(reader, entry.getWid()));
                }
                if (isED(line)) {
                    entry.setOmimEDs((new OMIMEDParser()).parse(reader, entry.getWid()));
                }
                if (isAV(line)) {
                    entry.setOmimAVs((new OMIMAVParser()).parse(reader, entry.getWid()));
                }
                if (isSA(line)) {
                    entry.setOmimSAs((new OMIMSAParser()).parse(reader, entry.getWid()));
                }
                if (isCN(line)) {
                    entry.setOmimCNs((new OMIMCNParser()).parse(reader, entry.getWid()));
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
