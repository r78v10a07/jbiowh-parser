package org.jbiowhparser.datasets.disease.omim.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jbiowhparser.datasets.disease.omim.files.tags.OMIMTabs;
import org.jbiowhpersistence.datasets.disease.omim.entities.OMIMED;
import org.jbiowhpersistence.datasets.disease.omim.entities.OMIMEDPK;

/**
 * This Class is the OMIM ED field parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Jul 18, 2012
 */
public class OMIMEDParser extends OMIMTabs {

    /**
     * Get the OMIMED object parsing the OMIM txt file
     *
     * @param reader the OMIM BufferedReader
     * @param WID the OMIM entry WID
     * @return a map with the OMIMED data
     * @throws IOException
     */
    public Map<OMIMEDPK, OMIMED> parse(BufferedReader reader, long WID) throws IOException {
        Map<OMIMEDPK, OMIMED> eds = new HashMap<>();
        String line;

        while ((line = reader.readLine()) != null) {
            if (isField(line) || isRecord(line)) {
                reader.reset();
                break;
            }
            if (!line.isEmpty()) {
                OMIMED omimED = new OMIMED(WID, line);
                eds.put(omimED.getOMIMEDPK(), omimED);
            }

            reader.mark(1000);
        }

        return eds;
    }
}
