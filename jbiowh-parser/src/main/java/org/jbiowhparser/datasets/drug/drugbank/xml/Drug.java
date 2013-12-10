package org.jbiowhparser.datasets.drug.drugbank.xml;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.drug.drugbank.utility.DrugBankSplitReference;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.AHFSCodesTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.ATCCodesTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.AffectedOrganismsTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.BrandsTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.CalculatedPropertiesTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.CarriersTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.CategoriesTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.DosagesTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.DrugInteractionsTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.DrugTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.EnzymesTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.ExperimentalPropertiesTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.ExternalIdentifiersTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.ExternalLinksTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.FoodInteractionsTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.GroupsTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.ManufacturersTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.MixturesTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.PackagersTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.PatentsTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.PricesTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.PropertyTypeTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.ProteinSequencesTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.SecondaryAccessionNumbersTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.SynonymsTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.TargetsTags;
import org.jbiowhparser.datasets.drug.drugbank.xml.tags.TransportersTags;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.drug.drugbank.DrugBankTables;
import org.xml.sax.Attributes;

/**
 * This class is the Drug XMl parser
 *
 * $Author: r78v10a07@gmail.com $ $LastChangedDate: 2012-11-08 14:37:19 +0100
 * (Thu, 08 Nov 2012) $ $LastChangedRevision: 322 $
 *
 * @since Sep 9, 2011
 */
public class Drug {

    private int depth = 0;
    private boolean open = false;
    private long WID = 0;
    private String id = null;
    private String name = null;
    private String description = null;
    private String casNumber = null;
    private String generalRef = null;
    private String synthesisReference = null;
    private String indication = null;
    private String pharmacology = null;
    private String mechanismOfAction = null;
    private String toxicity = null;
    private String biotransformation = null;
    private String absorption = null;
    private String halfLife = null;
    private String proteinBinding = null;
    private String routeOfElimination = null;
    private String volumeOfDistribution = null;
    private String clearance = null;
    private String type = null;
    private String updated = null;
    private String created = null;
    private String version = null;
    private TwoFieldXMLParser secondaryAccessionNumbers = null;
    private TwoFieldXMLParser groups = null;
    private Taxonomy taxonomy = null;
    private TwoFieldXMLParser synonyms = null;
    private TwoFieldXMLParser brands = null;
    private XMLIntermedParser mixtures = null;
    private XMLIntermedParser packagers = null;
    private TwoFieldXMLParser manufacturers = null;
    private XMLIntermedParser prices = null;
    private TwoFieldXMLParser categories = null;
    private TwoFieldXMLParser affectedOrganisms = null;
    private XMLIntermedParser dosages = null;
    private TwoFieldXMLParser atcCodes = null;
    private TwoFieldXMLParser ahfsCodes = null;
    private XMLIntermedParser patents = null;
    private TwoFieldXMLParser foodInteractions = null;
    private XMLIntermedParser drugInteractions = null;
    private XMLIntermedParser proteinSequences = null;
    private XMLIntermedParser calculated = null;
    private XMLIntermedParser experimental = null;
    private XMLIntermedParser externalIdentifiers = null;
    private XMLIntermedParser externalLinks = null;
    private BondType targets = null;
    private BondType enzymes = null;
    private BondType transporters = null;
    private BondType carriers = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     */
    public Drug() {
        open = false;

        secondaryAccessionNumbers = new TwoFieldXMLParser(DrugBankTables.getInstance().DRUGBANKSECONDACCESSIONNUMBERS,
                SecondaryAccessionNumbersTags.getInstance().SECONDARYACCESSIONNUMBERS,
                SecondaryAccessionNumbersTags.getInstance().SECONDARYACCESSIONNUMBER);

        groups = new TwoFieldXMLParser(DrugBankTables.getInstance().DRUGBANKGROUP,
                GroupsTags.getInstance().GROUPS, GroupsTags.getInstance().GROUP);

        taxonomy = new Taxonomy();

        synonyms = new TwoFieldXMLParser(DrugBankTables.getInstance().DRUGBANKSYNONYMS,
                SynonymsTags.getInstance().SYNONYMS, SynonymsTags.getInstance().SYNONYM);

        brands = new TwoFieldXMLParser(DrugBankTables.getInstance().DRUGBANKBRANDS,
                BrandsTags.getInstance().BRANDS, BrandsTags.getInstance().BRAND);

        mixtures = new XMLIntermedParser(DrugBankTables.getInstance().DRUGBANKMIXTURES,
                MixturesTags.getInstance().MIXTURES,
                new String[]{MixturesTags.getInstance().MIXTURE, MixturesTags.getInstance().NAME, MixturesTags.getInstance().INGREDIENTS});

        packagers = new XMLIntermedParser(DrugBankTables.getInstance().DRUGBANKPACKAGERS,
                PackagersTags.getInstance().PACKAGERS,
                new String[]{PackagersTags.getInstance().PACKAGER, PackagersTags.getInstance().NAME, PackagersTags.getInstance().URL});

        manufacturers = new TwoFieldXMLParser(DrugBankTables.getInstance().DRUGBANKMANUFACTURERS,
                ManufacturersTags.getInstance().MANUFACTURERS, ManufacturersTags.getInstance().MANUFACTURER, ManufacturersTags.getInstance().GENERIC);

        prices = new XMLIntermedParser(DrugBankTables.getInstance().DRUGBANKPRICES,
                PricesTags.getInstance().PRICES,
                new String[]{PricesTags.getInstance().PRICE, PricesTags.getInstance().DESCRIPTION, PricesTags.getInstance().COST, PricesTags.getInstance().UNIT});

        categories = new TwoFieldXMLParser(DrugBankTables.getInstance().DRUGBANKCATEGORIESTEMP,
                CategoriesTags.getInstance().CATEGORIES, CategoriesTags.getInstance().CATEGORY);

        affectedOrganisms = new TwoFieldXMLParser(DrugBankTables.getInstance().DRUGBANKAFFECTEDORGANISMS,
                AffectedOrganismsTags.getInstance().AFFECTEDORGANISMS, AffectedOrganismsTags.getInstance().AFFECTEDORGANISM);

        dosages = new XMLIntermedParser(DrugBankTables.getInstance().DRUGBANKDOSAGES,
                DosagesTags.getInstance().DOSAGES,
                new String[]{DosagesTags.getInstance().DOSAGE, DosagesTags.getInstance().FORM, DosagesTags.getInstance().ROUTE, DosagesTags.getInstance().STRENGTH});

        atcCodes = new TwoFieldXMLParser(DrugBankTables.getInstance().DRUGBANKATCCODES,
                ATCCodesTags.getInstance().ATCCODES, ATCCodesTags.getInstance().ATCCODE);

        ahfsCodes = new TwoFieldXMLParser(DrugBankTables.getInstance().DRUGBANKAHFSCODES,
                AHFSCodesTags.getInstance().AHFSCODES, AHFSCodesTags.getInstance().AHFSCODE);

        patents = new XMLIntermedParser(DrugBankTables.getInstance().DRUGBANKPATENTSTEMP,
                PatentsTags.getInstance().PATENTS,
                new String[]{PatentsTags.getInstance().PATENT, PatentsTags.getInstance().NUMBER,
                    PatentsTags.getInstance().COUNTRY, PatentsTags.getInstance().APPROVED, PatentsTags.getInstance().EXPIRES});

        foodInteractions = new TwoFieldXMLParser(DrugBankTables.getInstance().DRUGBANKFOODINTERACTIONS,
                FoodInteractionsTags.getInstance().FOODINTERACTIONS, FoodInteractionsTags.getInstance().FOODINTERACTION);

        drugInteractions = new XMLIntermedParser(DrugBankTables.getInstance().DRUGBANKDRUGINTERACTIONSTEMP,
                DrugInteractionsTags.getInstance().DRUGINTERACTIONS,
                new String[]{DrugInteractionsTags.getInstance().DRUGINTERACTION, DrugInteractionsTags.getInstance().DRUG, DrugInteractionsTags.getInstance().DESCRIPTION});

        proteinSequences = new XMLIntermedParser(DrugBankTables.getInstance().DRUGBANKPROTEINSEQUENCES,
                ProteinSequencesTags.getInstance().PROTEINSEQUENCES,
                new String[]{ProteinSequencesTags.getInstance().PROTEINSEQUENCE, ProteinSequencesTags.getInstance().HEADER, ProteinSequencesTags.getInstance().CHAIN});

        calculated = new XMLIntermedParser(DrugBankTables.getInstance().DRUGBANKCALCULATEDPROPERTIES,
                CalculatedPropertiesTags.getInstance().CALCULATEDPROPERTIES,
                new String[]{PropertyTypeTags.getInstance().PROPERTY, PropertyTypeTags.getInstance().KIND, PropertyTypeTags.getInstance().VALUE, PropertyTypeTags.getInstance().SOURCE});

        experimental = new XMLIntermedParser(DrugBankTables.getInstance().DRUGBANKEXPERIMENTALPROPERTIES,
                ExperimentalPropertiesTags.getInstance().EXPERIMENTALPROPERTIES,
                new String[]{PropertyTypeTags.getInstance().PROPERTY, PropertyTypeTags.getInstance().KIND, PropertyTypeTags.getInstance().VALUE, PropertyTypeTags.getInstance().SOURCE});

        externalIdentifiers = new XMLIntermedParser(DrugBankTables.getInstance().DRUGBANKEXTERNALIDENTIFIERS,
                ExternalIdentifiersTags.getInstance().EXTERNALIDENTIFIERS,
                new String[]{ExternalIdentifiersTags.getInstance().EXTERNALIDENTIFIER, ExternalIdentifiersTags.getInstance().RESOURCE, ExternalIdentifiersTags.getInstance().IDENTIFIER});

        externalLinks = new XMLIntermedParser(DrugBankTables.getInstance().DRUGBANKEXTERNALLINKS,
                ExternalLinksTags.getInstance().EXTERNALLINKS,
                new String[]{ExternalLinksTags.getInstance().EXTERNALLINK, ExternalLinksTags.getInstance().RESOURCE, ExternalLinksTags.getInstance().URL});

        targets = new BondType(TargetsTags.getInstance().TARGET, DrugBankTables.getInstance().DRUGBANKTARGETS,
                DrugBankTables.getInstance().DRUGBANKTARGETSREF,
                DrugBankTables.getInstance().DRUGBANKTARGETSACTIONS, true);

        enzymes = new BondType(EnzymesTags.getInstance().ENZYME, DrugBankTables.getInstance().DRUGBANKENZYMES,
                DrugBankTables.getInstance().DRUGBANKENZYMESREF,
                DrugBankTables.getInstance().DRUGBANKENZYMESACTIONS, false);

        transporters = new BondType(TransportersTags.getInstance().TRANSPORTER, DrugBankTables.getInstance().DRUGBANKTRANSPORTERS,
                DrugBankTables.getInstance().DRUGBANKTRANSPORTERSREF,
                DrugBankTables.getInstance().DRUGBANKTRANSPORTERSACTIONS, false);

        carriers = new BondType(CarriersTags.getInstance().CARRIER, DrugBankTables.getInstance().DRUGBANKCARRIERS,
                DrugBankTables.getInstance().DRUGBANKCARRIERSREF,
                DrugBankTables.getInstance().DRUGBANKCARRIERSACTIONS, false);
    }

    /**
     * This is the endElement method for the Header on GO
     *
     * @param qname
     * @param depth XML depth
     */
    public void endElement(String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (secondaryAccessionNumbers.isOpen()) {
                    secondaryAccessionNumbers.endElement(qname, depth, false);
                }
                if (groups.isOpen()) {
                    groups.endElement(qname, depth, false);
                }
                if (taxonomy.isOpen()) {
                    taxonomy.endElement(qname, depth);
                }
                if (synonyms.isOpen()) {
                    synonyms.endElement(qname, depth, false);
                }
                if (brands.isOpen()) {
                    brands.endElement(qname, depth, false);
                }
                if (mixtures.isOpen()) {
                    mixtures.endElement(qname, depth, false);
                }
                if (packagers.isOpen()) {
                    packagers.endElement(qname, depth, false);
                }
                if (manufacturers.isOpen()) {
                    manufacturers.endElement(qname, depth, false);
                }
                if (prices.isOpen()) {
                    prices.endElement(qname, depth, false);
                }
                if (categories.isOpen()) {
                    categories.endElement(qname, depth, false);
                }
                if (affectedOrganisms.isOpen()) {
                    affectedOrganisms.endElement(qname, depth, false);
                }
                if (dosages.isOpen()) {
                    dosages.endElement(qname, depth, false);
                }
                if (atcCodes.isOpen()) {
                    atcCodes.endElement(qname, depth, false);
                }
                if (ahfsCodes.isOpen()) {
                    ahfsCodes.endElement(qname, depth, false);
                }
                if (patents.isOpen()) {
                    patents.endElement(qname, depth, false);
                }
                if (foodInteractions.isOpen()) {
                    foodInteractions.endElement(qname, depth, false);
                }
                if (drugInteractions.isOpen()) {
                    drugInteractions.endElement(qname, depth, false);
                }
                if (proteinSequences.isOpen()) {
                    proteinSequences.endElement(qname, depth, false);
                }
                if (calculated.isOpen()) {
                    calculated.endElement(qname, depth, false);
                }
                if (experimental.isOpen()) {
                    experimental.endElement(qname, depth, false);
                }
                if (externalIdentifiers.isOpen()) {
                    externalIdentifiers.endElement(qname, depth, false);
                }
                if (externalLinks.isOpen()) {
                    externalLinks.endElement(qname, depth, false);
                }
                if (targets.isOpen()) {
                    targets.endElement(qname, depth);
                }
                if (enzymes.isOpen()) {
                    enzymes.endElement(qname, depth);
                }
                if (transporters.isOpen()) {
                    transporters.endElement(qname, depth);
                }
                if (carriers.isOpen()) {
                    carriers.endElement(qname, depth);
                }
            }
        }

        if (qname.equals(DrugTags.getInstance().DRUG) && depth == this.depth) {
            open = false;
            String tmp;

            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, WID, "\t");
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, id, "\t");
            tmp = null;
            if (name != null) {
                tmp = name.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
            }
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, tmp, "\t");
            tmp = null;
            if (description != null) {
                tmp = description.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
            }
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, tmp, "\t");
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, casNumber, "\t");
            tmp = null;
            if (synthesisReference != null) {
                tmp = synthesisReference.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
            }
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, tmp, "\t");
            tmp = null;
            if (indication != null) {
                tmp = indication.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
            }
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, tmp, "\t");
            tmp = null;
            if (pharmacology != null) {
                tmp = pharmacology.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
            }
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, tmp, "\t");
            tmp = null;
            if (mechanismOfAction != null) {
                tmp = mechanismOfAction.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
            }
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, tmp, "\t");
            tmp = null;
            if (toxicity != null) {
                tmp = toxicity.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
            }
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, tmp, "\t");
            tmp = null;
            if (biotransformation != null) {
                tmp = biotransformation.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
            }
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, tmp, "\t");
            tmp = null;
            if (absorption != null) {
                tmp = absorption.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
            }
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, tmp, "\t");
            tmp = null;
            if (halfLife != null) {
                tmp = halfLife.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
            }
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, tmp, "\t");
            tmp = null;
            if (proteinBinding != null) {
                tmp = proteinBinding.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
            }
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, tmp, "\t");
            tmp = null;
            if (routeOfElimination != null) {
                tmp = routeOfElimination.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
            }
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, tmp, "\t");
            tmp = null;
            if (volumeOfDistribution != null) {
                tmp = volumeOfDistribution.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
            }
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, tmp, "\t");
            tmp = null;
            if (clearance != null) {
                tmp = clearance.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
            }
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, tmp, "\t");
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, type, "\t");
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, version, "\t");
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, updated.substring(0, 19), "\t");
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, created.substring(0, 19), "\t");
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, DataSetPersistence.getInstance().getDataset().getWid(), "\n");

            (new DrugBankSplitReference()).split(generalRef, WID, DrugBankTables.getInstance().DRUGBANKGENERALREF);
        }
    }

    /**
     * This is the method for the Header on GO
     *
     * @param qname
     * @param depth
     * @param attributes
     */
    public void startElement(String qname, int depth, Attributes attributes) {
        if (open) {
            if (depth == this.depth + 1) {
                if (qname.equals(SecondaryAccessionNumbersTags.getInstance().SECONDARYACCESSIONNUMBERS)) {
                    secondaryAccessionNumbers.setOpen(true);
                }
                if (qname.equals(GroupsTags.getInstance().GROUPS)) {
                    groups.setOpen(true);
                }
                if (qname.equals(taxonomy.TAXONOMY)) {
                    taxonomy.setOpen(true);
                }
                if (qname.equals(SynonymsTags.getInstance().SYNONYMS)) {
                    synonyms.setOpen(true);
                }
                if (qname.equals(BrandsTags.getInstance().BRANDS)) {
                    brands.setOpen(true);
                }
                if (qname.equals(MixturesTags.getInstance().MIXTURES)) {
                    mixtures.setOpen(true);
                }
                if (qname.equals(PackagersTags.getInstance().PACKAGERS)) {
                    packagers.setOpen(true);
                }
                if (qname.equals(ManufacturersTags.getInstance().MANUFACTURERS)) {
                    manufacturers.setOpen(true);
                }
                if (qname.equals(PricesTags.getInstance().PRICES)) {
                    prices.setOpen(true);
                }
                if (qname.equals(CategoriesTags.getInstance().CATEGORIES)) {
                    categories.setOpen(true);
                }
                if (qname.equals(AffectedOrganismsTags.getInstance().AFFECTEDORGANISMS)) {
                    affectedOrganisms.setOpen(true);
                }
                if (qname.equals(DosagesTags.getInstance().DOSAGES)) {
                    dosages.setOpen(true);
                }
                if (qname.equals(ATCCodesTags.getInstance().ATCCODES)) {
                    atcCodes.setOpen(true);
                }
                if (qname.equals(AHFSCodesTags.getInstance().AHFSCODES)) {
                    ahfsCodes.setOpen(true);
                }
                if (qname.equals(PatentsTags.getInstance().PATENTS)) {
                    patents.setOpen(true);
                }
                if (qname.equals(FoodInteractionsTags.getInstance().FOODINTERACTIONS)) {
                    foodInteractions.setOpen(true);
                }
                if (qname.equals(DrugInteractionsTags.getInstance().DRUGINTERACTIONS)) {
                    drugInteractions.setOpen(true);
                }
                if (qname.equals(ProteinSequencesTags.getInstance().PROTEINSEQUENCES)) {
                    proteinSequences.setOpen(true);
                }
                if (qname.equals(CalculatedPropertiesTags.getInstance().CALCULATEDPROPERTIES)) {
                    calculated.setOpen(true);
                }
                if (qname.equals(ExperimentalPropertiesTags.getInstance().EXPERIMENTALPROPERTIES)) {
                    experimental.setOpen(true);
                }
                if (qname.equals(ExternalIdentifiersTags.getInstance().EXTERNALIDENTIFIERS)) {
                    externalIdentifiers.setOpen(true);
                }
                if (qname.equals(ExternalLinksTags.getInstance().EXTERNALLINKS)) {
                    externalLinks.setOpen(true);
                }
            }
            if (depth == this.depth + 2) {
                if (qname.equals(TargetsTags.getInstance().TARGET)) {
                    targets.setOpen(true);
                }
                if (qname.equals(EnzymesTags.getInstance().ENZYME)) {
                    enzymes.setOpen(true);
                }
                if (qname.equals(TransportersTags.getInstance().TRANSPORTER)) {
                    transporters.setOpen(true);
                }
                if (qname.equals(CarriersTags.getInstance().CARRIER)) {
                    carriers.setOpen(true);
                }
            }
            if (depth >= this.depth + 1) {
                if (secondaryAccessionNumbers.isOpen()) {
                    secondaryAccessionNumbers.startElement(qname, depth, attributes, WID);
                }
                if (groups.isOpen()) {
                    groups.startElement(qname, depth, attributes, WID);
                }
                if (taxonomy.isOpen()) {
                    taxonomy.startElement(qname, depth, attributes, WID);
                }
                if (synonyms.isOpen()) {
                    synonyms.startElement(qname, depth, attributes, WID);
                }
                if (brands.isOpen()) {
                    brands.startElement(qname, depth, attributes, WID);
                }
                if (mixtures.isOpen()) {
                    mixtures.startElement(qname, depth, attributes, WID);
                }
                if (packagers.isOpen()) {
                    packagers.startElement(qname, depth, attributes, WID);
                }
                if (manufacturers.isOpen()) {
                    manufacturers.startElement(qname, depth, attributes, WID);
                }
                if (prices.isOpen()) {
                    prices.startElement(qname, depth, attributes, WID);
                }
                if (categories.isOpen()) {
                    categories.startElement(qname, depth, attributes, WID);
                }
                if (affectedOrganisms.isOpen()) {
                    affectedOrganisms.startElement(qname, depth, attributes, WID);
                }
                if (dosages.isOpen()) {
                    dosages.startElement(qname, depth, attributes, WID);
                }
                if (atcCodes.isOpen()) {
                    atcCodes.startElement(qname, depth, attributes, WID);
                }
                if (ahfsCodes.isOpen()) {
                    ahfsCodes.startElement(qname, depth, attributes, WID);
                }
                if (patents.isOpen()) {
                    patents.startElement(qname, depth, attributes, WID);
                }
                if (foodInteractions.isOpen()) {
                    foodInteractions.startElement(qname, depth, attributes, WID);
                }
                if (drugInteractions.isOpen()) {
                    drugInteractions.startElement(qname, depth, attributes, WID);
                }
                if (proteinSequences.isOpen()) {
                    proteinSequences.startElement(qname, depth, attributes, WID);
                }
                if (calculated.isOpen()) {
                    calculated.startElement(qname, depth, attributes, WID);
                }
                if (experimental.isOpen()) {
                    experimental.startElement(qname, depth, attributes, WID);
                }
                if (externalIdentifiers.isOpen()) {
                    externalIdentifiers.startElement(qname, depth, attributes, WID);
                }
                if (externalLinks.isOpen()) {
                    externalLinks.startElement(qname, depth, attributes, WID);
                }
                if (targets.isOpen()) {
                    targets.startElement(qname, depth, attributes, WID);
                }
                if (enzymes.isOpen()) {
                    enzymes.startElement(qname, depth, attributes, WID);
                }
                if (transporters.isOpen()) {
                    transporters.startElement(qname, depth, attributes, WID);
                }
                if (carriers.isOpen()) {
                    carriers.startElement(qname, depth, attributes, WID);
                }
            }
        }

        if (qname.equals(DrugTags.getInstance().DRUG) && depth == 2) {
            this.depth = depth;
            open = true;

            WID = WIDFactory.getInstance().getWid();
            WIDFactory.getInstance().increaseWid();

            id = null;
            name = null;
            description = null;
            casNumber = null;
            generalRef = null;
            synthesisReference = null;
            indication = null;
            pharmacology = null;
            mechanismOfAction = null;
            toxicity = null;
            biotransformation = null;
            absorption = null;
            halfLife = null;
            proteinBinding = null;
            routeOfElimination = null;
            volumeOfDistribution = null;
            clearance = null;

            type = attributes.getValue(DrugTags.getInstance().TYPE);
            version = attributes.getValue(DrugTags.getInstance().VERSION);
            updated = attributes.getValue(DrugTags.getInstance().UPDATED);
            created = attributes.getValue(DrugTags.getInstance().CREATED);
        }
    }

    /**
     *
     * @param tagname
     * @param qname
     * @param depth
     */
    public void characters(String tagname, String qname, int depth) {
        if (open) {
            if (depth >= this.depth + 1) {
                if (secondaryAccessionNumbers.isOpen()) {
                    secondaryAccessionNumbers.characters(tagname, qname, depth);
                }
                if (groups.isOpen()) {
                    groups.characters(tagname, qname, depth);
                }
                if (taxonomy.isOpen()) {
                    taxonomy.characters(tagname, qname, depth);
                }
                if (synonyms.isOpen()) {
                    synonyms.characters(tagname, qname, depth);
                }
                if (brands.isOpen()) {
                    brands.characters(tagname, qname, depth);
                }
                if (mixtures.isOpen()) {
                    mixtures.characters(tagname, qname, depth);
                }
                if (packagers.isOpen()) {
                    packagers.characters(tagname, qname, depth);
                }
                if (manufacturers.isOpen()) {
                    manufacturers.characters(tagname, qname, depth);
                }
                if (prices.isOpen()) {
                    prices.characters(tagname, qname, depth);
                }
                if (categories.isOpen()) {
                    categories.characters(tagname, qname, depth);
                }
                if (affectedOrganisms.isOpen()) {
                    affectedOrganisms.characters(tagname, qname, depth);
                }
                if (dosages.isOpen()) {
                    dosages.characters(tagname, qname, depth);
                }
                if (atcCodes.isOpen()) {
                    atcCodes.characters(tagname, qname, depth);
                }
                if (ahfsCodes.isOpen()) {
                    ahfsCodes.characters(tagname, qname, depth);
                }
                if (patents.isOpen()) {
                    patents.characters(tagname, qname, depth);
                }
                if (foodInteractions.isOpen()) {
                    foodInteractions.characters(tagname, qname, depth);
                }
                if (drugInteractions.isOpen()) {
                    drugInteractions.characters(tagname, qname, depth);
                }
                if (proteinSequences.isOpen()) {
                    proteinSequences.characters(tagname, qname, depth);
                }
                if (calculated.isOpen()) {
                    calculated.characters(tagname, qname, depth);
                }
                if (experimental.isOpen()) {
                    experimental.characters(tagname, qname, depth);
                }
                if (externalIdentifiers.isOpen()) {
                    externalIdentifiers.characters(tagname, qname, depth);
                }
                if (externalLinks.isOpen()) {
                    externalLinks.characters(tagname, qname, depth);
                }
                if (targets.isOpen()) {
                    targets.characters(tagname, qname, depth);
                }
                if (enzymes.isOpen()) {
                    enzymes.characters(tagname, qname, depth);
                }
                if (transporters.isOpen()) {
                    transporters.characters(tagname, qname, depth);
                }
                if (carriers.isOpen()) {
                    carriers.characters(tagname, qname, depth);
                }
            }

            if (depth == this.depth + 1) {

                if (tagname.equals(DrugTags.getInstance().DRUGBANK_ID)) {
                    id = qname;
                }
                if (tagname.equals(DrugTags.getInstance().NAME)) {
                    name = copyQName(qname, name);
                }
                if (tagname.equals(DrugTags.getInstance().DESCRIPTION)) {
                    description = copyQName(qname, description);
                }
                if (tagname.equals(DrugTags.getInstance().CASNUMBER)) {
                    casNumber = copyQName(qname, casNumber);
                }
                if (tagname.equals(DrugTags.getInstance().GENERALREFERENCES)) {
                    generalRef = copyQName(qname, generalRef);
                }
                if (tagname.equals(DrugTags.getInstance().SYNTHESISREFERENCE)) {
                    synthesisReference = copyQName(qname, synthesisReference);
                }
                if (tagname.equals(DrugTags.getInstance().INDICATION)) {
                    indication = copyQName(qname, indication);
                }
                if (tagname.equals(DrugTags.getInstance().PHARMACOLOGY)) {
                    pharmacology = copyQName(qname, pharmacology);
                }
                if (tagname.equals(DrugTags.getInstance().MECHANISMOFACTION)) {
                    mechanismOfAction = copyQName(qname, mechanismOfAction);
                }
                if (tagname.equals(DrugTags.getInstance().TOXICITY)) {
                    toxicity = copyQName(qname, toxicity);
                }
                if (tagname.equals(DrugTags.getInstance().BIOTRANSFORMATION)) {
                    biotransformation = copyQName(qname, biotransformation);
                }
                if (tagname.equals(DrugTags.getInstance().ABSORPTION)) {
                    absorption = copyQName(qname, absorption);
                }
                if (tagname.equals(DrugTags.getInstance().HALFLIFE)) {
                    halfLife = copyQName(qname, halfLife);
                }
                if (tagname.equals(DrugTags.getInstance().PROTEINBINDING)) {
                    proteinBinding = copyQName(qname, proteinBinding);
                }
                if (tagname.equals(DrugTags.getInstance().ROUTEOFELIMINATION)) {
                    routeOfElimination = copyQName(qname, routeOfElimination);
                }
                if (tagname.equals(DrugTags.getInstance().VOLUMEOFDISTRIBUTION)) {
                    volumeOfDistribution = copyQName(qname, volumeOfDistribution);
                }
                if (tagname.equals(DrugTags.getInstance().CLEARANCE)) {
                    clearance = copyQName(qname, clearance);
                }
            }
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    private String copyQName(String qname, String value) {
        if (qname == null) {
            return null;
        }
        String result;
        if (value == null) {
            result = qname;
        } else {
            result = value.concat(qname);
        }
        return result;
    }
}
