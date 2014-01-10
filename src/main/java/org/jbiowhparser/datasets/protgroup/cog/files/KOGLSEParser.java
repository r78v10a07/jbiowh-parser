package org.jbiowhparser.datasets.protgroup.cog.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
 * This class is the KOG LSE file Parser
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Dec 20, 2013
 */
public class KOGLSEParser {

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
                String tax = null;
                Pattern p1 = Pattern.compile("\\[(\\w+)\\] (\\w+) (\\w) (.*)");
                Matcher m1 = p1.matcher("");
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
                                tax = m1.group(3);
                                cog.setGroupFunction(m1.group(4));
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
                                if (cog != null && !line.isEmpty()) {
                                    String[] field = line.split(" +");
                                    for (String f : field) {
                                        cog.getCogMember().add(new COGMember(tax, f));
                                    }
                                }
                            }

                        }
                        if (cog != null) {
                            cCtrl.create(cog);
                        }
                    } catch (IllegalStateException ex) {
                        VerbLogger.getInstance().log(this.getClass(), "Problem on line: " + line);
                        ex.printStackTrace(System.err);
                    } catch (Exception ex) {
                        VerbLogger.getInstance().log(this.getClass(), "Problem inserting COGOrthologousGroup entity");
                        ex.printStackTrace(System.err);
                    }
                }
            } catch (IOException ex) {
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            }
        }
    }
}
