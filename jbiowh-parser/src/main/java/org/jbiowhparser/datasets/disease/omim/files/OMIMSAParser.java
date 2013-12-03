package org.jbiowhparser.datasets.disease.omim.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.jbiowhparser.datasets.disease.omim.files.tags.OMIMTabs;
import org.jbiowhpersistence.datasets.disease.omim.entities.OMIMSA;

/**
 * This Class is the OMIM SA field parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2012-11-08 14:37:19 +0100
 * (Thu, 08 Nov 2012) $ $LastChangedRevision: 322 $
 *
 * @since Jul 19, 2012
 */
public class OMIMSAParser extends OMIMTabs {

    /**
     * Get the OMIMSA object parsing the OMIM txt file
     *
     * @param reader the OMIM BufferedReader
     * @return a map with the OMIMSA data
     * @throws IOException
     */
    public Set<OMIMSA> parse(BufferedReader reader) throws IOException {
        Set<OMIMSA> sas = new HashSet<>();
        String line;
        StringBuilder builder = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            if (isField(line) || isRecord(line)) {
                reader.reset();
                break;
            }
            if (!line.isEmpty()) {
                builder.append(line).append(" ");
            }

            reader.mark(1000);
        }

        if (!builder.toString().isEmpty()) {
            OMIMSA omimCD = new OMIMSA(builder.toString());
            sas.add(omimCD);
        }
        return sas;
    }
}
