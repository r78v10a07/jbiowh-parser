package org.jbiowhparser.datasets.disease.omim.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.jbiowhpersistence.datasets.disease.omim.entities.OMIMTI;

/**
 * This Class is the OMIM TI field parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2012-10-03 22:11:05 +0200
 * (Wed, 03 Oct 2012) $ $LastChangedRevision: 270 $
 *
 * @since Jul 16, 2012
 */
public class OMIMTIParser extends OMIMBasicParser {

    /**
     * Get the OMIMTI object parsing the OMIM txt file
     *
     * @param reader the OMIM BufferedReader
     * @return a set with the OMIMTI data
     * @throws IOException
     */
    public Set<OMIMTI> parse(BufferedReader reader) throws IOException {
        Set<OMIMTI> tis = new HashSet<>();
        for (String a : getPatterList(reader, ";;")) {
            tis.add(new OMIMTI(a));
        }
        return tis;
    }
}
