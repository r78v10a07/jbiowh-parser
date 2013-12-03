package org.jbiowhparser.datasets.disease.omim.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.jbiowhparser.datasets.disease.omim.files.tags.OMIMTabs;
import org.jbiowhpersistence.datasets.disease.omim.entities.OMIMRF;

/**
 * This Class is the OMIM RF field parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2012-11-08 14:37:19 +0100
 * (Thu, 08 Nov 2012) $ $LastChangedRevision: 322 $
 *
 * @since Jul 17, 2012
 */
public class OMIMRFParser extends OMIMTabs {

    /**
     * Get the OMIMRF object parsing the OMIM txt file
     *
     * @param reader the OMIM BufferedReader
     * @return a set with the OMIMRF data
     * @throws IOException
     */
    public Set<OMIMRF> parse(BufferedReader reader) throws IOException {
        Set<OMIMRF> rfs = new HashSet<>();
        String line;
        StringBuilder builder = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            if (isField(line) || isRecord(line)) {
                reader.reset();
                break;
            }
            if (line.isEmpty()) {
                if (!builder.toString().isEmpty()) {
                    OMIMRF omimRF = new OMIMRF(builder.toString());
                    rfs.add(omimRF);
                    builder = new StringBuilder();
                }
            } else {
                builder.append(line).append(" ");
            }

            reader.mark(1000);
        }

        if (!builder.toString().isEmpty()) {
            OMIMRF omimRF = new OMIMRF(builder.toString());
            rfs.add(omimRF);
        }

        return rfs;
    }
}
