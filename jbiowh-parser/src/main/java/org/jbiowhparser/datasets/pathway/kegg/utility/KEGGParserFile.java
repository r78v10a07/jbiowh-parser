package org.jbiowhparser.datasets.pathway.kegg.utility;

import java.io.*;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhparser.datasets.pathway.kegg.gene.KEGGGene;
import org.jbiowhparser.datasets.pathway.kegg.ligand.KEGGCompound;
import org.jbiowhparser.datasets.pathway.kegg.ligand.KEGGEnzyme;
import org.jbiowhparser.datasets.pathway.kegg.ligand.KEGGGlycan;
import org.jbiowhparser.datasets.pathway.kegg.ligand.KEGGRPair;
import org.jbiowhparser.datasets.pathway.kegg.ligand.KEGGReaction;
/**
 * This class is the KEGG Parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2013-03-19 09:38:47 +0100 (Tue, 19 Mar 2013) $
 * $LastChangedRevision: 396 $
 * @since Oct 29, 2011
 */
public class KEGGParserFile extends KEGGParserFieldTags {

    private String keggFileName = null;
    private String keggFileType = null;
    private String line = null;
    private BufferedReader inBuffer = null;

    /**
     * Create the KEGGParserFile object from the file name
     *
     * @param keggFileType
     * @param keggFile
     * @throws FileNotFoundException
     */
    public KEGGParserFile(String keggFileType, String keggFile) throws FileNotFoundException {
        this.keggFileType = keggFileType;
        this.keggFileName = keggFile;
        this.inBuffer = new BufferedReader(new FileReader(keggFile));
    }

    /**
     * Create the KEGGParserFile object from the file object
     *
     * @param keggFileType
     * @param keggFile
     * @throws FileNotFoundException
     */
    public KEGGParserFile(String keggFileType, File keggFile) throws FileNotFoundException {
        this.keggFileType = keggFileType;
        this.keggFileName = keggFile.getAbsolutePath();
        this.inBuffer = new BufferedReader(new FileReader(keggFile));
    }

    /**
     * Read the KEG file and return the first entry. The file descriptor is
     * placed on the next entry start offset
     *
     * @return a KEGGParserFactory object with the read KEGG entry
     */
    public KEGGParserFactory readKEGGEntryFile() {
        KEGGParserFactory entry = null;
        switch (keggFileType) {
            case "reaction":
                entry = new KEGGReaction();
                break;
            case "enzyme":
                entry = new KEGGEnzyme();
                break;
            case "compound":
                entry = new KEGGCompound();
                break;
            case "glycan":
                entry = new KEGGGlycan();
                break;
            case "rpair":
                entry = new KEGGRPair();
                break;
            case "gene":
                entry = new KEGGGene();
                break;
        }

        if (entry == null) {
            return null;
        }

        boolean flags = false;
        try {
            String field = null;
            String value = null;
            while ((line = inBuffer.readLine()) != null) {
                if (line.startsWith("///")) {
                    flags = true;
                    entry.getFieldValue().put(field, value);
                    break;
                }
                if (!line.startsWith(" ")) {
                    line = line.replaceAll("\\s+", " ");
                    if (field != null) {
                        entry.getFieldValue().put(field, value);
                    }

                    if ((field = line.split("\\s")[0]).equals(ENTRY)) {
                        value = line.replace(ENTRY, "").replaceAll("\\s+", " ");
                    } else {
                        value = getDefault();
                    }
                } else {
                    value = value.concat("\n").concat(getDefault());
                }
            }
        } catch (IOException ex) {
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
            VerbLogger.getInstance().log(this.getClass(), "KEGGParserFile: " + ex.getMessage());
            VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
        }

        if (!flags) {
            return null;
        }

        return entry;
    }

    private String getDefault() {
        String data[] = line.split(" ", 2);
        if (data.length != 2) {
            return "";
        }
        return data[1].trim();
    }

    /**
     * Get the KEGG file name
     *
     * @return s String object with the KEGG file name
     */
    public String getKeggFileName() {
        return keggFileName;
    }
}
