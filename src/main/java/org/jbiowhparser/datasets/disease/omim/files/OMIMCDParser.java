package org.jbiowhparser.datasets.disease.omim.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.jbiowhparser.datasets.disease.omim.files.tags.OMIMTabs;
import org.jbiowhpersistence.datasets.disease.omim.entities.OMIMCD;

/**
 * This Class is the OMIM CD field parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2012-10-03 22:11:05 +0200
 * (Wed, 03 Oct 2012) $ $LastChangedRevision: 270 $
 *
 * @since Jul 18, 2012
 */
public class OMIMCDParser extends OMIMTabs {

    /**
     * Get the OMIMCD object parsing the OMIM txt file
     *
     * @param reader the OMIM BufferedReader
     * @return a map with the OMIMCD data
     * @throws IOException
     */
    public Set<OMIMCD> parse(BufferedReader reader) throws IOException {
        Set<OMIMCD> cds = new HashSet<>();
        String line;

        while ((line = reader.readLine()) != null) {
            if (isField(line) || isRecord(line)) {
                reader.reset();
                break;
            }
            if (!line.isEmpty()) {
                cds.add(new OMIMCD(line));
            }

            reader.mark(1000);
        }

        return cds;
    }
}
