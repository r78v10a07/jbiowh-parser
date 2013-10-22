package org.jbiowhparser.datasets.disease.omim.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jbiowhparser.datasets.disease.omim.files.tags.OMIMTabs;

/**
 * This Class is the OMIM basic parser methods
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Jul 16, 2012
 */
public class OMIMBasicParser extends OMIMTabs {

    /**
     * Get a list with all fields into a OMIM field
     *
     * @param reader the OMIM BufferedReader
     * @param pattern the patter to separate the fields
     * @return a list with all fields into a OMIM field
     * @throws IOException
     */
    public List<String> getPatterList(BufferedReader reader, String pattern) throws IOException {
        String line;
        List<String> lines = new ArrayList();
        StringBuilder builder = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            if (isField(line) || isRecord(line)) {
                reader.reset();
                break;
            }
            builder.append(line);
            reader.mark(1000);
        }
        lines.addAll(Arrays.asList(builder.toString().split(pattern)));

        return lines;
    }
}
