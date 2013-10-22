package org.jbiowhparser.datasets.disease.omim.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jbiowhpersistence.datasets.disease.omim.entities.OMIMTI;
import org.jbiowhpersistence.datasets.disease.omim.entities.OMIMTIPK;

/**
 * This Class is the OMIM TI field parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Jul 16, 2012
 */
public class OMIMTIParser extends OMIMBasicParser {

    /**
     * Get the OMIMTI object parsing the OMIM txt file
     *
     * @param reader the OMIM BufferedReader
     * @param WID the OMIM entry WID
     * @return a map with the OMIMTI data
     * @throws IOException
     */
    public Map<OMIMTIPK, OMIMTI> parse(BufferedReader reader, long WID) throws IOException {
        Map<OMIMTIPK, OMIMTI> tis = new HashMap<>();
        for (String a : getPatterList(reader, ";;")) {
            OMIMTI ti = new OMIMTI(WID, a);
            tis.put(ti.getOMIMTIPK(), ti);
        }
        return tis;
    }
}
