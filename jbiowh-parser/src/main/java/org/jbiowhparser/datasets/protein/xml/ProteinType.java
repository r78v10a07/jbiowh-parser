package org.jbiowhparser.datasets.protein.xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.protein.xml.tags.ProteinTypeTags;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.protein.ProteinTables;
import org.xml.sax.Attributes;

/**
 * This Class handled the ProteinType on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Sep 30, 2010
 * @see
 */
public class ProteinType extends ProteinTypeTags {

    private int depth = 0;
    private boolean open = false;
    private long proteinWID = 0;
    ProteinNameGroup proteinNameGroup = null;
    private EvidencedStringType shortName = null;
    private ArrayList<ProteinNameGroup> proteinNameGroups = null;
    private EvidencedStringType allergenName = null;
    private EvidencedStringType biotechName = null;
    private EvidencedStringType cdAntigenName = null;
    private ArrayList<EvidencedStringType> cdAntigenNames = null;
    private EvidencedStringType innName = null;
    private ArrayList<EvidencedStringType> innNames = null;
    private boolean domain = false;
    private boolean component = false;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public ProteinType() {
        open = false;

        proteinNameGroups = new ArrayList<>();
        cdAntigenNames = new ArrayList<>();
        innNames = new ArrayList<>();
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param name XML Tag
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getRECOMMENDEDNAMEFLAGS())
                        || qname.equals(getALTERNATIVENAMEFLAGS())
                        || qname.equals(getSUBMITTEDNAMEFLAGS())) {
                    proteinNameGroup.open = false;
                    if (domain) {
                        proteinNameGroup.domain = 1;
                    }
                    if (component) {
                        proteinNameGroup.component = 1;
                    }

                    proteinNameGroups.add(proteinNameGroup);
                }
                if (qname.equals(getCDANTIGENNAMEFLAGS())) {
                    cdAntigenName.setOpen(false);
                    cdAntigenNames.add(cdAntigenName);
                }
                if (qname.equals(getINNNAMEFLAGS())) {
                    innName.setOpen(false);
                    innNames.add(innName);
                }
            }
            if (depth == this.depth + 2) {
                if (proteinNameGroup != null) {
                    if (proteinNameGroup.open) {
                        if (qname.equals(getSHORTNAMEFLAGS())) {
                            shortName.setOpen(false);
                            proteinNameGroup.shortName.add(shortName);
                        }
                    }
                }
            }
            if (depth == this.depth) {
                if (qname.equals(getDOMAINFLAGS()) || qname.equals(getCOMPONENTFLAGS())) {
                    this.depth--;
                    domain = false;
                    component = false;
                }

            }
        }
        if (qname.equals(getPROTEINFLAGS()) && depth == this.depth) {
            open = false;

            if (!proteinNameGroups.isEmpty()) {
                for (Iterator<ProteinNameGroup> it = proteinNameGroups.iterator(); it.hasNext();) {
                    proteinNameGroup = it.next();
                    if (proteinNameGroup.fullName != null) {
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, WIDFactory.getInstance().getWid(), "\t");
                        WIDFactory.getInstance().increaseWid();
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, proteinWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, proteinNameGroup.nameGroup, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, getFULLNAMEFLAGS(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, proteinNameGroup.component, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, proteinNameGroup.domain, "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, proteinNameGroup.fullName.getData(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, proteinNameGroup.fullName.getEvidence(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, proteinNameGroup.fullName.getStatus(), "\n");
                    }

                    if (!proteinNameGroup.shortName.isEmpty()) {
                        for (Iterator<EvidencedStringType> it1 = proteinNameGroup.shortName.iterator(); it1.hasNext();) {
                            shortName = it1.next();
                            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, WIDFactory.getInstance().getWid(), "\t");
                            WIDFactory.getInstance().increaseWid();
                            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, proteinWID, "\t");
                            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, proteinNameGroup.nameGroup, "\t");
                            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, getSHORTNAMEFLAGS(), "\t");
                            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, proteinNameGroup.component, "\t");
                            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, proteinNameGroup.domain, "\t");
                            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, shortName.getData(), "\t");
                            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, shortName.getEvidence(), "\t");
                            ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, shortName.getStatus(), "\n");
                        }
                        proteinNameGroup.shortName.clear();
                    }
                }
                proteinNameGroups.clear();
            }

            if (allergenName != null) {
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, WIDFactory.getInstance().getWid(), "\t");
                WIDFactory.getInstance().increaseWid();
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, proteinWID, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, getALLERGENNAMEFLAGS(), "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, (String) null, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, 0, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, 0, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, allergenName.getData(), "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, allergenName.getEvidence(), "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, allergenName.getStatus(), "\n");
            }
            if (biotechName != null) {
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, WIDFactory.getInstance().getWid(), "\t");
                WIDFactory.getInstance().increaseWid();
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, proteinWID, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, getBIOTECHNAMEFLAGS(), "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, (String) null, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, 0, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, 0, "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, biotechName.getData(), "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, biotechName.getEvidence(), "\t");
                ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, biotechName.getStatus(), "\n");
            }
            if (!cdAntigenNames.isEmpty()) {
                for (Iterator<EvidencedStringType> it = cdAntigenNames.iterator(); it.hasNext();) {
                    shortName = it.next();
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, WIDFactory.getInstance().getWid(), "\t");
                    WIDFactory.getInstance().increaseWid();
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, proteinWID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, getCDANTIGENNAMEFLAGS(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, (String) null, "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, 0, "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, 0, "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, shortName.getData(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, shortName.getEvidence(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, shortName.getStatus(), "\n");
                }
                cdAntigenNames.clear();
            }
            if (!innNames.isEmpty()) {
                for (Iterator<EvidencedStringType> it = innNames.iterator(); it.hasNext();) {
                    shortName = it.next();
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, WIDFactory.getInstance().getWid(), "\t");
                    WIDFactory.getInstance().increaseWid();
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, proteinWID, "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, getINNNAMEFLAGS(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, (String) null, "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, 0, "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, 0, "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, shortName.getData(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, shortName.getEvidence(), "\t");
                    ParseFiles.getInstance().printOnTSVFile(ProteinTables.getInstance().PROTEINLONGNAME, shortName.getStatus(), "\n");
                }
                innNames.clear();
            }
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param name
     * @param depth
     */
    public void startElement(String qname, int depth, Attributes attributes, long proteinWID) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(getRECOMMENDEDNAMEFLAGS())
                        || qname.equals(getALTERNATIVENAMEFLAGS())
                        || qname.equals(getSUBMITTEDNAMEFLAGS())) {
                    proteinNameGroup = new ProteinNameGroup(qname);
                    shortName = new EvidencedStringType();
                }
                if (qname.equals(getDOMAINFLAGS())) {
                    domain = true;
                    this.depth = depth;
                }
                if (qname.equals(getCOMPONENTFLAGS())) {
                    component = true;
                    this.depth = depth;
                }
                if (qname.equals(getALLERGENNAMEFLAGS())) {
                    allergenName = new EvidencedStringType();
                    allergenName.setEvidence(attributes.getValue(allergenName.getEVIDENCE()));
                    allergenName.setStatus(attributes.getValue(allergenName.getSTATUS()));
                }
                if (qname.equals(getBIOTECHNAMEFLAGS())) {
                    biotechName = new EvidencedStringType();
                    biotechName.setEvidence(attributes.getValue(biotechName.getEVIDENCE()));
                    biotechName.setStatus(attributes.getValue(biotechName.getSTATUS()));
                }
                if (qname.equals(getCDANTIGENNAMEFLAGS())) {
                    cdAntigenName = new EvidencedStringType();
                    cdAntigenName.setEvidence(attributes.getValue(cdAntigenName.getEVIDENCE()));
                    cdAntigenName.setStatus(attributes.getValue(cdAntigenName.getSTATUS()));
                }
                if (qname.equals(getINNNAMEFLAGS())) {
                    innName = new EvidencedStringType();
                    innName.setEvidence(attributes.getValue(innName.getEVIDENCE()));
                    innName.setStatus(attributes.getValue(innName.getSTATUS()));
                }
            }
            if (depth == this.depth + 2) {
                if (proteinNameGroup != null) {
                    if (proteinNameGroup.open) {
                        if (qname.equals(getFULLNAMEFLAGS())) {
                            proteinNameGroup.fullName.setEvidence(attributes.getValue(proteinNameGroup.fullName.getEVIDENCE()));
                            proteinNameGroup.fullName.setStatus(attributes.getValue(proteinNameGroup.fullName.getSTATUS()));
                        }
                        if (qname.equals(getSHORTNAMEFLAGS())) {
                            shortName.setEvidence(attributes.getValue(shortName.getEVIDENCE()));
                            shortName.setStatus(attributes.getValue(shortName.getSTATUS()));
                        }
                    }
                }
            }
        }
        if (qname.equals(getPROTEINFLAGS())) {
            this.depth = depth;
            this.proteinWID = proteinWID;
            open = true;

            proteinNameGroups.clear();
            cdAntigenNames.clear();
            innNames.clear();
            shortName = null;
            proteinNameGroup = null;
            allergenName = null;
            biotechName = null;
            domain = false;
            component = false;
        }
    }

    /**
     *
     * @param tagname
     * @param name
     * @param depth
     */
    public void characters(String tagname, String qname, int depth) {
        if (open) {
            if (depth == this.depth + 1) {
                if (tagname.equals(getALLERGENNAMEFLAGS())) {
                    allergenName.setData(qname);
                }
                if (tagname.equals(getBIOTECHNAMEFLAGS())) {
                    biotechName.setData(qname);
                }
                if (tagname.equals(getCDANTIGENNAMEFLAGS())) {
                    cdAntigenName.setData(qname);
                }
                if (tagname.equals(getINNNAMEFLAGS())) {
                    innName.setData(qname);
                }
            }
            if (depth == this.depth + 2) {
                if (proteinNameGroup != null) {
                    if (proteinNameGroup.open) {
                        if (tagname.equals(getFULLNAMEFLAGS())) {
                            proteinNameGroup.fullName.setData(qname);
                        }
                        if (tagname.equals(getSHORTNAMEFLAGS())) {
                            shortName.setData(qname);
                        }
                    }
                }
            }
        }
    }

    private class ProteinNameGroup {

        private boolean open = false;
        private String nameGroup = null;
        private EvidencedStringType fullName = null;
        private ArrayList<EvidencedStringType> shortName = null;
        private int domain = 0;
        private int component = 0;

        public ProteinNameGroup(String nameGroup) {
            this.nameGroup = nameGroup;
            fullName = new EvidencedStringType();
            shortName = new ArrayList<>();
            open = true;
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
