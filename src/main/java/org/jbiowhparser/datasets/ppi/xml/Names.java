package org.jbiowhparser.datasets.ppi.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ppi.xml.tags.NamesTags;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.xml.sax.Attributes;

/**
 * This Class handled the XML Names Tags on MIF25
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 20, 2010
 * @see
 */
public class Names extends NamesTags {

    private int depth = 0;
    private boolean open = false;
    private String shortLabel = null;
    private String fullName = null;
    private Alias alia = null;
    private ArrayList<Alias> alias = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param dataset
     */
    public Names() {
        open = false;

        alias = new ArrayList<>();
    }

    /**
     * This is the endElement method
     *
     * @param qname the tags name
     * @param depth XML depth
     * @param aliasTables file name to be printed the alias data
     */
    public void endElement(String qname, int depth, long OtherWID, String aliasTables) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getALIASFLAGS())) {
                    alias.add(alia);
                }
            }
        }
        if (qname.equals(getNAMESFLAGS())) {
            open = false;

            if (!alias.isEmpty() && aliasTables != null) {
                for (Iterator<Alias> it = alias.iterator(); it.hasNext();) {
                    Alias al = it.next();

                    ParseFiles.getInstance().printOnTSVFile(aliasTables, WIDFactory.getInstance().getWid(), "\t");
                    WIDFactory.getInstance().increaseWid();
                    ParseFiles.getInstance().printOnTSVFile(aliasTables, OtherWID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(aliasTables, al.alias, "\t");
                    ParseFiles.getInstance().printOnTSVFile(aliasTables, al.typeAc, "\t");
                    ParseFiles.getInstance().printOnTSVFile(aliasTables, al.types, "\t");
                    ParseFiles.getInstance().printOnTSVFile(aliasTables, DataSetPersistence.getInstance().getDataset().getWid(), "\n");
                }
                alias.clear();
            }
        }
    }

    /**
     * This is the startElement method
     *
     * @param qname the tags name
     * @param depth the depth on the XML file
     * @param attributes tag's attributes
     */
    public void startElement(String qname, int depth, Attributes attributes) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getALIASFLAGS())) {
                    alia = new Alias();
                    alia.typeAc = attributes.getValue(getTYPEACFLAGS());
                    alia.types = attributes.getValue(getTYPEFLAGS());
                }
            }
        }
        if (qname.equals(getNAMESFLAGS())) {
            this.depth = depth;
            open = true;

            shortLabel = null;
            fullName = null;
            alia = null;
            alias.clear();
        }
    }

    /**
     * This is the characters method
     *
     * @param tagname the tags name
     * @param qname text value
     * @param depth the depth on the XML file
     */
    public void characters(String tagname, String qname, int depth) {
        if (open) {
            if (depth == this.depth + 1) {
                if (tagname.equals(getSHORTLABELFLAGS())) {
                    shortLabel = qname;
                }
                if (tagname.equals(getFULLNAMEFLAGS())) {
                    fullName = qname;
                }
                if (tagname.equals(getALIASFLAGS())) {
                    alia.alias = qname;
                }
            }
        }
    }

    /**
     * Get the open value
     *
     * @return open
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Set the open value
     *
     * @param open
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * Get the fullName value
     *
     * @return fullName
     */
    public String getFullName() {
        if (fullName != null) {
            return fullName.replace("\t", " ");
        }
        return fullName;
    }

    /**
     * Get the shortLabel value
     *
     * @return shortLabel
     */
    public String getShortLabel() {
        if (shortLabel != null) {
            return shortLabel.replace("\t", " ");
        }
        return shortLabel;
    }

    private class Alias {

        private String alias = null;
        private String types = null;
        private String typeAc = null;
    }
}
