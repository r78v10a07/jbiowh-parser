package org.jbiowhparser.datasets.protgroup.ncbiprotclust.files;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protgroup.ncbiprotclust.controller.ProtClustJpaController;
import org.jbiowhpersistence.datasets.protgroup.ncbiprotclust.entities.ProtClust;
import org.jbiowhpersistence.datasets.protgroup.ncbiprotclust.entities.ProtClustEC;
import org.jbiowhpersistence.datasets.protgroup.ncbiprotclust.entities.ProtClustPMID;
import org.jbiowhpersistence.datasets.protgroup.ncbiprotclust.entities.ProtClustProteins;
import org.jbiowhpersistence.datasets.protgroup.ncbiprotclust.entities.ProtClustXRef;
import org.jbiowhpersistence.utils.entitymanager.JBioWHPersistence;

/**
 * This class is the NCBI BCp file format parser
 *
 * $Author$ $LastChangedDate$ $LastChangedRevision$
 *
 * @since Jan 13, 2014
 */
public class NCBIBCPParser {

    public final String ENDENTRY = "////";
    public final String COG_GROUP = "COG_GROUP";
    public final String DEFINITION = "DEFINITION";
    public final String EC_NUMBER = "EC_NUMBER";
    public final String ENTRY = "ENTRY";
    public final String LOCUS = "LOCUS";
    public final String PMID = "PMID";
    public final String PROTEINS = "PROTEINS";
    public final String REL = "REL";
    public final String RELATIVES = "RELATIVES";
    public final String STATUS = "STATUS";
    public final String XREF = "XREF";

    public void parser(InputStream in) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            ProtClustJpaController instance = new ProtClustJpaController(JBioWHPersistence.getInstance().getWHEntityManager());
            Pattern p = Pattern.compile("\\w+\\s+(.+)");
            Matcher m = p.matcher("");
            ProtClust pclust = null;
            while ((line = reader.readLine()) != null) {
                if (line.equals(ENDENTRY)) {
                    instance.create(pclust);
                } else if (line.matches("^\\w+\\s*.*")) {
                    m.reset(line);
                    m.find();
                    String[] fields = line.split("[\\s\t]+");
                    switch (fields[0]) {
                        case ENTRY:
                            pclust = new ProtClust(WIDFactory.getInstance().getWid());
                            WIDFactory.getInstance().increaseWid();
                            pclust.setEntry(fields[1]);
                            pclust.setDataSetWID(DataSetPersistence.getInstance().getDataset().getWid());
                            pclust.setProtClustXRef(new HashSet<ProtClustXRef>());
                            pclust.setProtClustEC(new HashSet<ProtClustEC>());
                            pclust.setProtClustPMID(new HashSet<ProtClustPMID>());
                            pclust.setProtClustProtein(new HashSet<ProtClustProteins>());
                            break;
                        case DEFINITION:
                            if (pclust != null) {
                                pclust.setDefinition(m.group(1));
                            }
                            break;
                        case LOCUS:
                            if (pclust != null && !m.group(1).toLowerCase().equals("none")) {
                                pclust.setLocus(m.group(1));
                            }
                            break;
                        case STATUS:
                            if (pclust != null) {
                                pclust.setStatus(m.group(1));
                            }
                            break;
                        case XREF:
                            if (pclust != null) {
                                pclust.getProtClustXRef().add(new ProtClustXRef(m.group(1)));
                            }
                            break;
                        case PMID:
                            if (pclust != null) {
                                pclust.getProtClustPMID().add(new ProtClustPMID(Long.valueOf(m.group(1))));
                            }
                            break;
                        case EC_NUMBER:
                            if (pclust != null) {
                                pclust.getProtClustEC().add(new ProtClustEC(m.group(1)));
                            }
                            break;
                        case PROTEINS:
                            if (pclust != null) {
                                while ((line = reader.readLine()) != null) {
                                    if (line.equals(ENDENTRY)) {
                                        reader.reset();
                                        break;
                                    }
                                    Pattern p1 = Pattern.compile("(\\d+)[ \t]+([a-zA-Z_0-9\\.\\(\\)<>\\^,: \\-/']+)[ \t]+(\\d+)[ \t]+([a-zA-Z_0-9\\.\\(\\)<>\\^,;: \\-\\+/`'=\\*\\[\\]#\\\\\\?]*) \\[[a-zA-Z_0-9\\.\\(\\)<>\\^,: \\-/'\\+]+\\]");
                                    Matcher m1 = p1.matcher(line.trim());
                                    m1.find();
                                    ProtClustProteins prot = new ProtClustProteins();
                                    if (m1.group(1) != null && !m1.group(1).isEmpty()) {
                                        prot.setGeneGi(Long.valueOf(m1.group(1)));
                                    }
                                    if (m1.group(2) != null && !m1.group(2).isEmpty()) {
                                        prot.setLocusName(m1.group(2));
                                    }
                                    if (m1.group(3) != null && !m1.group(3).isEmpty() && !m1.group(3).equals("0")) {
                                        prot.setProteinGi(BigInteger.valueOf(Long.valueOf(m1.group(3))));
                                    }
                                    if (m1.group(4) != null && !m1.group(4).isEmpty()) {
                                        prot.setProteinName(m1.group(4));
                                    }
                                    pclust.getProtClustProtein().add(prot);
                                    reader.mark(1000000);
                                }
                            }
                            break;
                    }
                }
                reader.mark(1000000);
            }
        } catch (Exception ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
            VerbLogger.getInstance().log(this.getClass(), "Error: " + ex.toString());
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
            ex.printStackTrace(System.out);
            System.exit(1);
        }
    }
}
