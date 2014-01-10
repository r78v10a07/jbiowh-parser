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
import org.jbiowhpersistence.datasets.protgroup.cog.controller.COGFuncClassGroupJpaController;
import org.jbiowhpersistence.datasets.protgroup.cog.controller.COGFuncClassJpaController;
import org.jbiowhpersistence.datasets.protgroup.cog.entities.COGFuncClass;
import org.jbiowhpersistence.datasets.protgroup.cog.entities.COGFuncClassGroup;
import org.jbiowhpersistence.utils.entitymanager.JBioWHPersistence;

/**
 * This class is the COG fun.txt file parser
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Dec 19, 2013
 */
public class COGFunParser {

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
                boolean create = false;
                boolean edit = false;
                String line = null;
                HashMap parm = new HashMap();
                Pattern p1 = Pattern.compile(" \\[(\\w)\\] (.+)");
                Matcher m1 = p1.matcher("");
                COGFuncClassGroup classGroup = null;
                COGFuncClassGroupJpaController cCtrl = new COGFuncClassGroupJpaController(JBioWHPersistence.getInstance().getWHEntityManager());
                COGFuncClassJpaController classCtrl = new COGFuncClassJpaController(JBioWHPersistence.getInstance().getWHEntityManager());
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                    try {
                        while ((line = reader.readLine()) != null) {
                            m1.reset(line);
                            if (!m1.find()) {
                                if (!line.isEmpty()) {
                                    if (classGroup != null) {
                                        if (create) {
                                            cCtrl.create(classGroup);
                                            create = false;
                                        }
                                        if (edit) {
                                            cCtrl.edit(classGroup);
                                            edit = false;
                                        }
                                    }
                                    try {
                                        parm.put("name", line.trim());
                                        classGroup = (COGFuncClassGroup) cCtrl.useNamedQuerySingleResult("COGFuncClassGroup.findByName", parm);
                                        VerbLogger.getInstance().log(this.getClass(), "Class group found with WID " + classGroup.getWid() + " and Name " + classGroup.getName());
                                    } catch (NoResultException ex) {
                                        create = true;
                                        classGroup = new COGFuncClassGroup(WIDFactory.getInstance().getWid(), line.trim());
                                        WIDFactory.getInstance().increaseWid();
                                        classGroup.setCogFuncClass(new HashSet<COGFuncClass>());
                                        VerbLogger.getInstance().log(this.getClass(), "Creating a new Class group with WID " + classGroup.getWid() + " and Name " + classGroup.getName());
                                    }
                                }
                            } else {
                                if (classGroup != null) {
                                    COGFuncClass funClass;
                                    try {
                                        parm.put("name", m1.group(2).trim());
                                        funClass = (COGFuncClass) classCtrl.useNamedQuerySingleResult("COGFuncClass.findByName", parm);
                                        if (funClass.getcOGFuncClassGroupWID() != classGroup.getWid()) {
                                            throw new IllegalStateException("Function class that belong to different groups\n\t" + line);
                                        }
                                        VerbLogger.getInstance().log(this.getClass(), "Function Class found with WID " + funClass.getWid() + " and Name " + funClass.getName());
                                    } catch (NoResultException ex) {
                                        funClass = new COGFuncClass(WIDFactory.getInstance().getWid());
                                        WIDFactory.getInstance().increaseWid();
                                        funClass.setLetter(m1.group(1).trim().charAt(0));
                                        funClass.setName(m1.group(2).trim());
                                        funClass.setcOGFuncClassGroupWID(classGroup.getWid());
                                        classGroup.getCogFuncClass().add(funClass);
                                        if (!create) {
                                            edit = true;
                                        }
                                        VerbLogger.getInstance().log(this.getClass(), "Creating a new Function Class with WID " + funClass.getWid() + " and Name " + funClass.getName());
                                    }
                                }
                            }
                        }
                        if (classGroup != null) {
                            if (create) {
                                cCtrl.create(classGroup);
                            }
                            if (edit) {
                                cCtrl.edit(classGroup);
                            }
                        }
                    } catch (IllegalStateException ex) {
                        VerbLogger.getInstance().log(this.getClass(), "Problem on line: " + line);
                        ex.printStackTrace(System.err);
                    } catch (Exception ex) {
                        VerbLogger.getInstance().log(this.getClass(), "Problem inserting the COGFuncClassGroup entity");
                        ex.printStackTrace(System.err);
                    }
                }
            } catch (IOException ex) {
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            }
        }
    }
}
