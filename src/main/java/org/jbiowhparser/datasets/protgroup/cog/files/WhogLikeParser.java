package org.jbiowhparser.datasets.protgroup.cog.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import javax.persistence.NoResultException;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protgroup.cog.controller.COGFuncClassJpaController;
import org.jbiowhpersistence.datasets.protgroup.cog.controller.COGOrthologousGroupJpaController;
import org.jbiowhpersistence.datasets.protgroup.cog.entities.COGFuncClass;
import org.jbiowhpersistence.datasets.protgroup.cog.entities.COGMember;
import org.jbiowhpersistence.datasets.protgroup.cog.entities.COGOrthologousGroup;
import org.jbiowhpersistence.utils.entitymanager.JBioWHPersistence;

/**
 * This class is the Whog Like Parser
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Dec 19, 2013
 */
public class WhogLikeParser {

    public void run(String fileName) {
        File file = new File(DataSetPersistence.getInstance().getDirectory() + "/" + fileName);
        InputStream in;

        VerbLogger.getInstance().log(this.getClass(), "Parsing file: " + file.getName());
        if (file.exists() && file.isFile()) {
            try {
                if (file.getCanonicalPath().endsWith(".gz")) {
                    in = new GZIPInputStream(new FileInputStream(file));
                } else {
                    in = new FileInputStream(file);
                }
                HashMap parm = new HashMap();
                String line = null;
                Pattern p1 = Pattern.compile("\\[(\\w+)\\] (\\w+) (.*)");
                Pattern p2 = Pattern.compile("(\\w+): +(\\w+.+)");
                Matcher m1 = p1.matcher("");
                Matcher m2 = p2.matcher("");
                COGOrthologousGroup cog = null;
                COGOrthologousGroupJpaController cCtrl = new COGOrthologousGroupJpaController(JBioWHPersistence.getInstance().getWHEntityManager());
                COGFuncClassJpaController fclassCtrl = new COGFuncClassJpaController(JBioWHPersistence.getInstance().getWHEntityManager());
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                    try {
                        while ((line = reader.readLine()) != null) {
                            m1.reset(line);
                            if (m1.find()) {
                                if (cog != null) {
                                    cCtrl.create(cog);
                                }
                                cog = new COGOrthologousGroup(WIDFactory.getInstance().getWid());
                                WIDFactory.getInstance().increaseWid();
                                cog.setId(m1.group(2));
                                cog.setGroupFunction(m1.group(3));
                                cog.setGeneInfo(null);
                                cog.setTaxonomy(null);
                                cog.setProtein(null);
                                cog.setCogMember(new HashSet<COGMember>());
                                cog.setDataSetWID(DataSetPersistence.getInstance().getDataset().getWid());
                                cog.setCogFuncClass(new HashSet<COGFuncClass>());
                                for (char c : m1.group(1).trim().toCharArray()) {
                                    parm.put("letter", c);
                                    try {
                                        COGFuncClass fClass = (COGFuncClass) fclassCtrl.useNamedQuerySingleResult("COGFuncClass.findByLetter", parm);
                                        cog.getCogFuncClass().add(fClass);
                                    } catch (NoResultException ex) {
                                    }
                                }
                            } else {
                                m2.reset(line.trim());
                                if (m2.find()) {
                                    if (cog != null) {
                                        String[] field = m2.group(2).split(" +");
                                        for (String f : field) {
                                            cog.getCogMember().add(new COGMember(m2.group(1).trim(), f));
                                        }
                                    }
                                }
                            }

                        }
                        if (cog != null) {
                            cCtrl.create(cog);
                        }
                    } catch (IllegalStateException ex) {
                        VerbLogger.getInstance().log(this.getClass(), "Problem on line: " + line);
                        VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                        VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
                        DataSetPersistence.getInstance().getDataset().setStatus("Error");
                        DataSetPersistence.getInstance().updateDataSet();
                        WIDFactory.getInstance().updateWIDTable();
                        System.exit(-1);
                    } catch (Exception ex) {
                        VerbLogger.getInstance().log(this.getClass(), "Problem inserting COGOrthologousGroup entity");
                        VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                        VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                        DataSetPersistence.getInstance().getDataset().setChangeDate(new Date());
                        DataSetPersistence.getInstance().getDataset().setStatus("Error");
                        DataSetPersistence.getInstance().updateDataSet();
                        WIDFactory.getInstance().updateWIDTable();
                        System.exit(-1);
                    }
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
        }
    }
}
