package org.jbiowhparser.datasets.disease.omim.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.jbiowhparser.datasets.disease.omim.files.tags.OMIMTabs;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.disease.omim.entities.OMIMAV;

/**
 * This Class is the OMIM TX field parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Jul 19, 2012
 */
public class OMIMAVParser extends OMIMTabs {

    /**
     * Get the OMIMAV object parsing the OMIM txt file
     *
     * @param reader the OMIM BufferedReader
     * @param WID the OMIM entry WID
     * @return a set with the OMIMAV data
     * @throws IOException
     */
    public Set<OMIMAV> parse(BufferedReader reader, long WID) throws IOException {
        Set<OMIMAV> avs = new HashSet<>();
        String line;
        StringBuilder builder = new StringBuilder();
        
        while ((line = reader.readLine()) != null) {
            if (isField(line) || isRecord(line)) {
                reader.reset();
                break;
            }
            if (line.matches("^\\.\\d{4}")) {
                if (!builder.toString().isEmpty()) {
                    OMIMAV omimav = new OMIMAV(WIDFactory.getInstance().getWid(), WID, builder.toString().trim());
                    avs.add(omimav);
                    WIDFactory.getInstance().increaseWid();
                    builder = new StringBuilder();
                }
                builder.append(line).append("\n");
            } else if (!line.isEmpty()) {
                builder.append(line).append("\n");
            } else {
                builder.append("\n");
            }

            reader.mark(1000);
        }

        if (!builder.toString().isEmpty()) {
            OMIMAV omimav = new OMIMAV(WIDFactory.getInstance().getWid(), WID, builder.toString().trim());
            avs.add(omimav);
            WIDFactory.getInstance().increaseWid();
        }

        return avs;
    }
}
