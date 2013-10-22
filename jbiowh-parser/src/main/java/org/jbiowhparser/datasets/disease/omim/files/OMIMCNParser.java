package org.jbiowhparser.datasets.disease.omim.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jbiowhparser.datasets.disease.omim.files.tags.OMIMTabs;
import org.jbiowhpersistence.datasets.disease.omim.entities.OMIMCN;
import org.jbiowhpersistence.datasets.disease.omim.entities.OMIMCNPK;

/**
 * This Class is the OMIM CN field parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Jul 19, 2012
 */
public class OMIMCNParser extends OMIMTabs {
    
    /**
     * Get the OMIMCN object parsing the OMIM txt file
     *
     * @param reader the OMIM BufferedReader
     * @param WID the OMIM entry WID
     * @return a map with the OMIMCN data
     * @throws IOException
     */
    public Map<OMIMCNPK, OMIMCN> parse(BufferedReader reader, long WID) throws IOException {
        Map<OMIMCNPK, OMIMCN> cns = new HashMap<>();
        String line;

        while ((line = reader.readLine()) != null) {
            if (isField(line) || isRecord(line)) {
                reader.reset();
                break;
            }
            if (!line.isEmpty()) {
                OMIMCN omimED = new OMIMCN(WID, line);
                cns.put(omimED.getOMIMCNPK(), omimED);
            }

            reader.mark(1000);
        }

        return cns;
    }
}
