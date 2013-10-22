package org.jbiowhparser.datasets.gene.genbank.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import org.jbiowhcore.logger.VerbLogger;
import org.jbiowhpersistence.datasets.gene.genebank.entities.GeneBank;

/**
 * This class is used to retrieve GenBank data from the NCBI web site
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2013-06-05 15:04:58 +0200
 * (Wed, 05 Jun 2013) $ $LastChangedRevision: 633 $
 *
 * @since Jun 4, 2013
 */
public class GeneBankOnline extends GeneBankFlatParser {

    /**
     * Retrieve a Nucleotide entry from the NCBI
     *
     * @param gi the nucleotide Gi
     * @return a GeneBank object or null if the nucleotide does not exist
     */
    public GeneBank getEntryByGi(Integer gi) {
        GeneBank entry = null;

        String url = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&id="
                + gi + "&retmode=text&rettype=gb";

        do {
            try {
                URL ncbi = new URL(url);
                URLConnection yc = ncbi.openConnection();

                try (BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()))) {
                    entry = readGeneBankEntry(in, "onLine");
                } catch (ParseException ex) {
                    VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                    VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                    VerbLogger.getInstance().log(this.getClass(), ex.getLocalizedMessage());
                    VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
                    break;
                } catch (UnknownHostException ex) {
                    VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                    VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                    VerbLogger.getInstance().log(this.getClass(), ex.getLocalizedMessage());
                    VerbLogger.getInstance().log(this.getClass(), "Waiting 1 sec for UnknownHostException");
                    VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex1) {
                        Thread.currentThread().interrupt();
                    }
                } catch (IOException ex) {
                    VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                    VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                    VerbLogger.getInstance().log(this.getClass(), ex.getLocalizedMessage());
                    VerbLogger.getInstance().log(this.getClass(), "Waiting 1 sec for IOException");
                    VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex1) {
                        Thread.currentThread().interrupt();
                    }
                } catch (Exception ex) {
                    VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                    VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                    VerbLogger.getInstance().log(this.getClass(), ex.getLocalizedMessage());
                    VerbLogger.getInstance().log(this.getClass(), "Waiting 1 sec for Exception");
                    VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex1) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (MalformedURLException ex) {
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                VerbLogger.getInstance().log(this.getClass(), ex.getLocalizedMessage());
                VerbLogger.getInstance().log(this.getClass(), "Waiting 1 sec for MalformedURLException");
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
                try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex1) {
                        Thread.currentThread().interrupt();
                    }
            } catch (UnknownHostException ex) {
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                VerbLogger.getInstance().log(this.getClass(), ex.getLocalizedMessage());
                VerbLogger.getInstance().log(this.getClass(), "Waiting 1 sec for UnknownHostException");
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex1) {
                    Thread.currentThread().interrupt();
                }
            } catch (IOException ex) {
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().ERROR);
                VerbLogger.getInstance().log(this.getClass(), ex.getMessage());
                VerbLogger.getInstance().log(this.getClass(), ex.getLocalizedMessage());
                VerbLogger.getInstance().log(this.getClass(), "Waiting 1 sec for IOException");
                VerbLogger.getInstance().setLevel(VerbLogger.getInstance().getInitialLevel());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex1) {
                    Thread.currentThread().interrupt();
                }
            }
        } while (entry == null);

        return entry;
    }

    /**
     * Retrieve a Nucleotide entry from the NCBI
     *
     * @param gi the nucleotide Gi
     * @return a GeneBank object or null if the nucleotide does not exist
     */
    public GeneBank getEntryByGi(String gi) {
        return getEntryByGi(Integer.parseInt(gi));
    }

    /**
     * Retrieve a list of Nucleotide entry from the NCBI. The list keep the Gi
     * order in the input list. If a Gi does not have a nucleotide a null is
     * inserted into that position in the linked list.
     *
     * @param gis the nucleotide Gi list
     * @return a of GeneBank object
     */
    public List<GeneBank> getEntryByGi(List<Integer> gis) {
        List<GeneBank> entries = new LinkedList();

        for (Integer gi : gis) {
            entries.add(getEntryByGi(gi));
        }

        return entries;
    }
}
