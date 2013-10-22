package org.jbiowhparser.datasets.ontology.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.ontology.xml.tags.GOHeaderTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.ontology.OntologyTables;

/**
 * This Class handled the HEADER XML Tag on the GO Ontology XML OBO file
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Sep 23, 2010
 * @see
 */
public class GOHeader extends GOHeaderTags {

    private int depth = 0;
    private boolean open = false;
    private ArrayList<Subsetdef> subsets = null;
    private Subsetdef subset = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     */
    public GOHeader() {
        open = false;
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElementGOHeader(String name, int depth) {
        if (open && depth > this.depth) {
            if (name.equals(getSUBSETDEF())) {
                subset.open = false;
                subsets.add(subset);
            }
        }
        if (name.equals(getHEADER())) {
            open = false;

            for (Iterator<Subsetdef> it = subsets.iterator(); it.hasNext();) {
                subset = it.next();
                ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYSUBSET, WIDFactory.getInstance().getWid(), "\t");
                ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYSUBSET, subset.id, "\t");
                ParseFiles.getInstance().printOnTSVFile(OntologyTables.getInstance().ONTOLOGYSUBSET, subset.name, "\n");
                WIDFactory.getInstance().increaseWid();
            }
            subsets.clear();
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     */
    public void startElementGOHeader(String name, int depth) {
        if (name.equals(getHEADER())) {
            this.depth = depth;
            open = true;

            subsets = new ArrayList<>();
        }
        if (open && depth > this.depth) {
            if (name.equals(getSUBSETDEF())) {
                subset = new Subsetdef();
                subset.open = true;
            }
        }
    }

    /**
     *
     * @param tagname
     * @param name
     * @param depth
     */
    public void charactersGOHeader(String tagname, String name, int depth) {
        if (open && depth > this.depth) {
            if (subset != null) {
                if (subset.open) {
                    if (tagname.equals(getID())) {
                        subset.id = name;
                    }
                    if (tagname.equals(getNAME())) {
                        subset.name = name;
                    }
                }
            }
        }
    }

    /**
     * This internal class storage the Subsetdef data
     */
    private class Subsetdef {

        private boolean open = false;
        private String name = null;
        private String id = null;
    }
}
