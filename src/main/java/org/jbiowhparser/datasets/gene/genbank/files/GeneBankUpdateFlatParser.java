package org.jbiowhparser.datasets.gene.genbank.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.zip.GZIPInputStream;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhcore.utility.utils.ExploreDirectory;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.gene.gene.controller.GeneInfoJpaController;
import org.jbiowhpersistence.datasets.gene.gene.entities.GeneInfo;
import org.jbiowhpersistence.datasets.gene.genebank.controller.GeneBankCDSJpaController;
import org.jbiowhpersistence.datasets.gene.genebank.controller.GeneBankJpaController;
import org.jbiowhpersistence.datasets.gene.genebank.entities.GeneBank;
import org.jbiowhpersistence.datasets.gene.genebank.entities.GeneBankCDS;
import org.jbiowhpersistence.utils.entitymanager.JBioWHPersistence;

/**
 * This class is
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-05-29 11:24:54 +0200
 * (Wed, 29 May 2013) $ $LastChangedRevision: 591 $
 *
 * @since May 13, 2013
 */
public class GeneBankUpdateFlatParser extends GeneBankFlatParser {

    private GeneBankJpaController controller;
    private GeneInfoJpaController gController;
    private GeneBankCDSJpaController cController;
    private HashMap parm;
    private int count;

    @Override
    public void load() throws SQLException {
        File dir = new File(DataSetPersistence.getInstance().getDirectory());

        controller = new GeneBankJpaController(JBioWHPersistence.getInstance().getWHEntityManager());
        gController = new GeneInfoJpaController(JBioWHPersistence.getInstance().getWHEntityManager());
        cController = new GeneBankCDSJpaController(JBioWHPersistence.getInstance().getWHEntityManager());
        parm = new HashMap();

        count = 0;

        long startTime = System.currentTimeMillis();
        if (dir.isDirectory()) {
            List<File> files = ExploreDirectory.getInstance().extractFilesPathFromDir(dir);
            int i = 0;
            for (File file : files) {
                try {
                    InputStream in = null;
                    VerbLogger.getInstance().log(this.getClass(), "File: " + (i++) + " of: " + files.size());
                    if (file.isFile() && file.getCanonicalPath().endsWith(".flat.gz")) {
                        in = new GZIPInputStream(new FileInputStream(file));
                    } else if (file.isFile() && file.getCanonicalPath().endsWith(".flat")) {
                        in = new FileInputStream(file);
                    }
                    if (in != null) {
                        VerbLogger.getInstance().log(this.getClass(), "Parsing file " + i + " " + file.getCanonicalPath());
                        parser(in, file.getName());
                    }
                    {
                        long gTime = System.currentTimeMillis();
                        System.runFinalization();
                        System.gc();
                        VerbLogger.getInstance().log(this.getClass(), "Running garbage collector in ... "
                                + String.format("%.2f", ((float) ((long) (System.currentTimeMillis() - gTime) / 1000))) + " s");
                    }
                } catch (IOException ex) {
                    VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                    VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                    DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
                    DataSetPersistence.getInstance().getDataset().setStatus("Error");
                    DataSetPersistence.getInstance().updateDataSet();
                    WIDFactory.getInstance().updateWIDTable();
                    System.exit(-1);
                }

                String strTime = String.format("%.2f", ((float) ((((long) (System.currentTimeMillis() - startTime) / 1000)) / i) * (files.size() - i) / 3600.0));
                VerbLogger.getInstance().log(this.getClass(), "Estimated left time: " + strTime + " h");
            }
        }
    }

    private void parser(InputStream in, String fileName) {

        try {
            parm.clear();
            parm.put("fileName", fileName);
            if (!((Long) controller.useNamedQuerySingleResult("GeneBank.countByFileName", parm)).equals(0L)) {
                return;
            }
        } catch (NoResultException ex) {
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            GeneBank geneBank;

            while ((geneBank = readGeneBankEntry(reader, fileName)) != null) {
                count++;
                VerbLogger.getInstance().log(this.getClass(), count + " " + geneBank.getGi() + " CDSs: " + geneBank.getGeneBankCDS().size() + " " + fileName);
                parm.clear();
                parm.put("locusName", geneBank.getLocusName());
                try {
                    GeneBank fromDB = (GeneBank) controller.useNamedQuerySingleResult("GeneBank.findByLocusName", parm);
                    geneBank.setWid(fromDB.getWid());

                    EntityManager em = cController.getEntityManager();
                    try {
                        em.getTransaction().begin();
                        for (GeneBankCDS c : fromDB.getGeneBankCDS()) {
                            c = em.getReference(GeneBankCDS.class, c.getWid());
                            c.getWid();
                            GeneBank IntoCDS = c.getGeneBank();
                            if (IntoCDS != null) {
                                IntoCDS.getGeneBankCDS().remove(c);
                                IntoCDS = em.merge(IntoCDS);
                                c.setGeneBank(null);
                            }
                            c.setGeneInfo(null);
                            em.remove(c);
                        }
                        em.getTransaction().commit();
                    } finally {
                        if (em != null) {
                            em.close();
                        }
                    }
                    for (GeneBankCDS c : geneBank.getGeneBankCDS()) {
                        parm.clear();
                        parm.put("proteinGi", (long) c.getProteinGi());
                        c.setGeneBankWID(fromDB.getWid());
                        List<GeneInfo> geneInfos = gController.useNamedQuery("GeneInfo.findByProteinGi", parm);
                        if (!geneInfos.isEmpty()) {
                            c.getGeneInfo().addAll(geneInfos);
                        }
                        cController.create(c);
                    }
                    geneBank.setGeneBankCDS(new HashSet<GeneBankCDS>());
                    controller.edit(geneBank);
                } catch (NoResultException ex) {
                    for (GeneBankCDS c : geneBank.getGeneBankCDS()) {
                        parm.clear();
                        parm.put("proteinGi", (long) c.getProteinGi());
                        List<GeneInfo> geneInfos = gController.useNamedQuery("GeneInfo.findByProteinGi", parm);
                        if (!geneInfos.isEmpty()) {
                            c.getGeneInfo().addAll(geneInfos);
                        }
                        cController.create(c);
                    }
                    controller.create(geneBank);

                }
                if (count % 100 == 0) {
                    System.runFinalization();
                    System.gc();
                }
            }
        } catch (Exception ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
            DataSetPersistence.getInstance().getDataset().setStatus("Error");
            DataSetPersistence.getInstance().updateDataSet();
            WIDFactory.getInstance().updateWIDTable();
            System.exit(-1);
        }

    }
}
