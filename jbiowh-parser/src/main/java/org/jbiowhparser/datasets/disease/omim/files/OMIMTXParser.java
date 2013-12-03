package org.jbiowhparser.datasets.disease.omim.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.jbiowhparser.datasets.disease.omim.files.tags.OMIMTabs;
import org.jbiowhpersistence.datasets.disease.omim.entities.OMIMTX;

/**
 * This Class is the OMIM TX field parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2012-10-03 22:11:05 +0200
 * (Wed, 03 Oct 2012) $ $LastChangedRevision: 270 $
 *
 * @since Jul 16, 2012
 */
public class OMIMTXParser extends OMIMTabs {

    private String tx = null;

    /**
     * Get the OMIM TX first line before any TAGS
     *
     * @return the OMIM TX first line before any TAGS
     */
    public String getTx() {
        return tx;
    }

    /**
     * Get the OMIMTX object parsing the OMIM txt file
     *
     * @param reader the OMIM BufferedReader
     * @return a map with the OMIMTX data
     * @throws IOException
     */
    public Set<OMIMTX> parse(BufferedReader reader) throws IOException {
        Set<OMIMTX> txs = new HashSet<>();
        String line;
        StringBuilder builder = new StringBuilder();
        OMIMTX omimTX = null;

        while ((line = reader.readLine()) != null) {
            if (isField(line) || isRecord(line)) {
                reader.reset();
                break;
            }
            if (line.isEmpty()) {
                builder.append("\n");
            } else if (line.toUpperCase().equals(line)
                    && line.matches("^\\D+")
                    && !line.contains("(")
                    && !line.contains(")")
                    && !line.contains(".")
                    && !line.contains("-")) {
                if (omimTX != null) {
                    omimTX.setTx(builder.toString());
                    txs.add(omimTX);
                } else if (!builder.toString().isEmpty()) {
                    tx = builder.toString();
                }
                omimTX = new OMIMTX(line);
                builder = new StringBuilder();
            } else {
                builder.append(line).append("\n");
            }

            reader.mark(1000);
        }

        if (tx == null && !builder.toString().isEmpty()) {
            tx = builder.toString();
        }

        if (omimTX != null) {
            omimTX.setTx(builder.toString());
            txs.add(omimTX);
        }

        return txs;
    }
}
