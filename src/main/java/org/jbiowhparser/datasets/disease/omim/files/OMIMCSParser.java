package org.jbiowhparser.datasets.disease.omim.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.jbiowhparser.datasets.disease.omim.files.tags.OMIMTabs;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.disease.omim.entities.OMIMCS;
import org.jbiowhpersistence.datasets.disease.omim.entities.OMIMCSData;

/**
 * This Class is the OMIM CS field parser
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Jul 17, 2012
 */
public class OMIMCSParser extends OMIMTabs {

    /**
     * Get the OMIMCS object parsing the OMIM txt file
     *
     * @param reader the OMIM BufferedReader
     * @param WID the OMIM entry WID
     * @return a map with the OMIMCS data
     * @throws IOException
     */
    public Set<OMIMCS> parse(BufferedReader reader, long WID) throws IOException {
        Set<OMIMCS> css = new HashSet<>();
        String line;
        StringBuilder builder = new StringBuilder();
        OMIMCS omimCS = null;
        
        while ((line = reader.readLine()) != null) {
            if (isField(line) || isRecord(line)) {
                reader.reset();
                break;
            }
            if (line.isEmpty()) {
                if (omimCS != null) {
                    Set<OMIMCSData> cssData = new HashSet<>();
                    for (String a : builder.toString().split(";")) {
                        OMIMCSData data = new OMIMCSData(WIDFactory.getInstance().getWid(), omimCS.getWid(), a.trim());
                        cssData.add(data);
                        WIDFactory.getInstance().increaseWid();
                    }
                    omimCS.setOmimCSDatas(cssData);
                    css.add(omimCS);
                    omimCS = null;
                }
            } else {
                if (line.matches("^\\w+\\:")) {
                    if (omimCS != null) {
                        Set<OMIMCSData> cssData = new HashSet<>();
                        for (String a : builder.toString().split(";")) {
                            OMIMCSData data = new OMIMCSData(WIDFactory.getInstance().getWid(), omimCS.getWid(), a.trim());
                            cssData.add(data);
                            WIDFactory.getInstance().increaseWid();
                        }
                        omimCS.setOmimCSDatas(cssData);
                        css.add(omimCS);
                    }
                    omimCS = new OMIMCS(WIDFactory.getInstance().getWid(), WID, line.replace(":", "").trim());
                    WIDFactory.getInstance().increaseWid();
                    builder = new StringBuilder();
                } else {
                    builder.append(line.trim());
                }
            }

            reader.mark(1000);
        }

        if (omimCS != null) {
            Set<OMIMCSData> cssData = new HashSet<>();
            for (String a : builder.toString().split(";")) {
                OMIMCSData data = new OMIMCSData(WIDFactory.getInstance().getWid(), omimCS.getWid(), a.trim());
                cssData.add(data);
                WIDFactory.getInstance().increaseWid();
            }
            omimCS.setOmimCSDatas(cssData);
            css.add(omimCS);
        }

        return css;
    }
}
