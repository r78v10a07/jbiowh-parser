package org.jbiowhparser.datasets.protein.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.protein.xml.tags.OrganismTags;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Organism tags on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 30, 2010
 * @see
 */
public class Organism extends OrganismTags {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    DBReference dbReference = null;
    private ArrayList<DBReference> dbReferences = null;
    private int host = 0;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public Organism() {
        open = false;

        dbReferences = new ArrayList<>();
        dbReference = new DBReference();
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                dbReference.endElement(qname, depth, false);
                if (qname.equals(getDBREFERENCEFLAGS())) {
                    dbReferences.add(dbReference);
                }
            }
        }
        if (qname.equals(getORGANISMFLAGS()) || qname.equals(getORGANISMHOSTFLAGS())) {
            open = false;

            if (!dbReferences.isEmpty()) {
                for (Iterator<DBReference> it = dbReferences.iterator(); it.hasNext();) {
                    dbReference = it.next();
                    if (dbReference.getType().equals("NCBI Taxonomy")) {
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINTAXID, WID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINTAXID, dbReference.getId(), "\t");
                        if (host == 0) {
                            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINTAXID, 0, "\n");
                        } else {
                            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINTAXID, 1, "\n");
                        }

                    }
                }
                dbReferences.clear();
            }
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     */
    public void startElement(String qname, int depth, Attributes attributes, long WID) {
        if (open) {
            if (depth >= this.depth + 1) {
                dbReference.startElement(qname, depth, attributes, this.WID);
            }
        }
        if (qname.equals(getORGANISMFLAGS()) || qname.equals(getORGANISMHOSTFLAGS())) {
            this.depth = depth;
            this.WID = WID;
            open = true;

            host = 0;
            dbReferences.clear();
            if (qname.equals(getORGANISMHOSTFLAGS())) {
                host = 1;
            }
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
