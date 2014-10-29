package org.jbiowhparser.datasets.drug.drugbank.utility;

import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.AtcCodeType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.BrandType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.CalculatedPropertyType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.CarrierType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.CategoryType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.DosageType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.DrugInteractionType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.DrugType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.DrugbankDrugIdType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.EnzymeType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.ExperimentalPropertyType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.ExternalIdentifierType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.ExternalLinkType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.GroupType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.ManufacturerType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.MixtureType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.PackagerType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.PatentType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.PolypeptideType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.PriceType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.SequenceListType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.SynonymType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.TargetType;
import org.jbiowhparser.datasets.drug.drugbank.jaxb.TransporterType;
import org.jbiowhpersistence.datasets.DataSetPersistence;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;
import org.jbiowhpersistence.datasets.drug.drugbank.DrugBankTables;

/**
 * This Class is an utility to print the DrugBank data
 *
 * $Author:$ $LastChangedDate:$ $LastChangedRevision:$
 *
 * @since Oct 24, 2014
 */
public class DrugBankPrint {

    public void print(DrugType d) {
        long WID = WIDFactory.getInstance().getWid();
        WIDFactory.getInstance().increaseWid();

        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, WID, "\t");
        for (DrugbankDrugIdType t : d.getDrugbankId()) {
            if (t.isPrimary()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, t.getValue(), "\t");
            } else {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKSECONDACCESSIONNUMBERS, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKSECONDACCESSIONNUMBERS, t.getValue(), "\n");
            }
        }
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getName(), "\t");
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getDescription(), "\t");
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getCasNumber(), "\t");
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getSynthesisReference(), "\t");
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getIndication(), "\t");
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getPharmacodynamics(), "\t");
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getMechanismOfAction(), "\t");
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getToxicity(), "\t");
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getAbsorption(), "\t");
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getHalfLife(), "\t");
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getProteinBinding(), "\t");
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getRouteOfElimination(), "\t");
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getVolumeOfDistribution(), "\t");
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getClearance(), "\t");
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getType(), "\t");
        if (d.getUpdated().toString().length() > 19) {
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getUpdated().toString().substring(0, 19), "\t");
        } else {
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getUpdated().toString(), "\t");
        }
        if (d.getCreated().toString().length() > 19) {
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getCreated().toString().substring(0, 19), "\t");
        } else {
            ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, d.getCreated().toString(), "\t");
        }
        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANK, DataSetPersistence.getInstance().getDataset().getWid(), "\n");

        if (d.getBrands() != null) {
            for (BrandType b : d.getBrands().getBrand()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKBRANDS, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKBRANDS, b.getValue(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKBRANDS, b.getCompany(), "\n");
            }
        }

        if (d.getMixtures() != null) {
            for (MixtureType b : d.getMixtures().getMixture()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKMIXTURES, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKMIXTURES, b.getName(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKMIXTURES, b.getIngredients(), "\n");
            }
        }

        if (d.getSynonyms() != null) {
            for (SynonymType b : d.getSynonyms().getSynonym()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKSYNONYMS, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKSYNONYMS, b.getValue(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKSYNONYMS, b.getLanguage(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKSYNONYMS, b.getCoder(), "\n");
            }
        }

        if (d.getCalculatedProperties() != null) {
            for (CalculatedPropertyType b : d.getCalculatedProperties().getProperty()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCALCULATEDPROPERTIES, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCALCULATEDPROPERTIES, b.getKind().value(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCALCULATEDPROPERTIES, b.getValue(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCALCULATEDPROPERTIES, b.getSource().value(), "\n");
            }
        }

        if (d.getPackagers() != null) {
            for (PackagerType b : d.getPackagers().getPackager()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPACKAGERS, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPACKAGERS, b.getName(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPACKAGERS, b.getUrl(), "\n");
            }
        }

        if (d.getManufacturers() != null) {
            for (ManufacturerType b : d.getManufacturers().getManufacturer()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKMANUFACTURERS, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKMANUFACTURERS, b.getValue(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKMANUFACTURERS, b.isGeneric(), "\n");
            }
        }

        if (d.getPrices() != null) {
            for (PriceType b : d.getPrices().getPrice()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPRICES, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPRICES, b.getDescription(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPRICES, b.getCost().getValue(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPRICES, b.getCost().getCurrency(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPRICES, b.getUnit(), "\n");
            }
        }

        if (d.getFoodInteractions() != null) {
            for (String b : d.getFoodInteractions().getFoodInteraction()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKFOODINTERACTIONS, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKFOODINTERACTIONS, b, "\n");
            }
        }

        if (d.getDrugInteractions() != null) {
            for (DrugInteractionType b : d.getDrugInteractions().getDrugInteraction()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKDRUGINTERACTIONSTEMP, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKDRUGINTERACTIONSTEMP, b.getDrugbankId().getValue(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKDRUGINTERACTIONSTEMP, b.getDescription(), "\n");
            }
        }

        if (d.getCategories() != null) {
            for (CategoryType b : d.getCategories().getCategory()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCATEGORIESTEMP, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCATEGORIESTEMP, b.getCategory(), "\n");
            }
        }

        (new DrugBankSplitReference()).split(d.getGeneralReferences(), WID, DrugBankTables.getInstance().DRUGBANKGENERALREF);

        if (d.getGroups() != null) {
            for (GroupType b : d.getGroups().getGroup()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKGROUP, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKGROUP, b.value(), "\n");
            }
        }

        if (d.getDosages() != null) {
            for (DosageType b : d.getDosages().getDosage()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKDOSAGES, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKDOSAGES, b.getForm(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKDOSAGES, b.getRoute(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKDOSAGES, b.getStrength(), "\n");
            }
        }

        if (d.getAtcCodes() != null) {
            for (AtcCodeType b : d.getAtcCodes().getAtcCode()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKATCCODES, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKATCCODES, b.getCode(), "\n");
            }
        }

        if (d.getSequences() != null) {
            for (SequenceListType.Sequence b : d.getSequences().getSequence()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPROTEINSEQUENCES, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPROTEINSEQUENCES, b.getFormat(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPROTEINSEQUENCES, b.getValue(), "\n");
            }
        }

        if (d.getAhfsCodes() != null) {
            for (String b : d.getAhfsCodes().getAhfsCode()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKAHFSCODES, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKAHFSCODES, b, "\n");
            }
        }

        if (d.getExperimentalProperties() != null) {
            for (ExperimentalPropertyType b : d.getExperimentalProperties().getProperty()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKEXPERIMENTALPROPERTIES, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKEXPERIMENTALPROPERTIES, b.getKind().value(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKEXPERIMENTALPROPERTIES, b.getValue(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKEXPERIMENTALPROPERTIES, b.getSource(), "\n");
            }
        }

        if (d.getExternalIdentifiers() != null) {
            for (ExternalIdentifierType b : d.getExternalIdentifiers().getExternalIdentifier()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKEXTERNALIDENTIFIERS, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKEXTERNALIDENTIFIERS, b.getResource().value(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKEXTERNALIDENTIFIERS, b.getIdentifier(), "\n");
            }
        }

        if (d.getAffectedOrganisms() != null) {
            for (String b : d.getAffectedOrganisms().getAffectedOrganism()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKAFFECTEDORGANISMS, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKAFFECTEDORGANISMS, b, "\n");
            }
        }

        if (d.getExternalLinks() != null) {
            for (ExternalLinkType b : d.getExternalLinks().getExternalLink()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKEXTERNALLINKS, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKEXTERNALLINKS, b.getResource().value(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKEXTERNALLINKS, b.getUrl(), "\n");
            }
        }

        if (d.getPatents() != null) {
            for (PatentType b : d.getPatents().getPatent()) {
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPATENTSTEMP, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPATENTSTEMP, b.getNumber(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPATENTSTEMP, b.getCountry(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPATENTSTEMP, b.getApproved(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKPATENTSTEMP, b.getExpires(), "\n");
            }
        }

        if (d.getTargets() != null) {
            for (TargetType b : d.getTargets().getTarget()) {
                long inWID = WIDFactory.getInstance().getWid();
                WIDFactory.getInstance().increaseWid();

                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTARGETS, inWID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTARGETS, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTARGETS, b.getId(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTARGETS, b.getName(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTARGETS, b.getKnownAction().value(), "\n");
                if (b.getPolypeptide() != null && !b.getPolypeptide().isEmpty()) {
                    for (PolypeptideType c : b.getPolypeptide()) {
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTARGETSPOLYPEPTIDE, inWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTARGETSPOLYPEPTIDE, c.getId(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTARGETSPOLYPEPTIDE, c.getName(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTARGETSPOLYPEPTIDE, c.getSource(), "\n");
                    }
                }
                if (b.getActions() != null) {
                    for (String a : b.getActions().getAction()) {
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTARGETSACTIONS, inWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTARGETSACTIONS, a, "\n");
                    }
                }

                (new DrugBankSplitReference()).split(b.getReferences(), inWID, DrugBankTables.getInstance().DRUGBANKTARGETSREF);
            }
        }

        if (d.getEnzymes() != null) {
            for (EnzymeType b : d.getEnzymes().getEnzyme()) {
                long inWID = WIDFactory.getInstance().getWid();
                WIDFactory.getInstance().increaseWid();

                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKENZYMES, inWID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKENZYMES, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKENZYMES, b.getId(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKENZYMES, b.getPosition(), "\n");
                if (b.getPolypeptide() != null && !b.getPolypeptide().isEmpty()) {
                    for (PolypeptideType c : b.getPolypeptide()) {
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKENZYMESPOLYPEPTIDE, inWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKENZYMESPOLYPEPTIDE, c.getId(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKENZYMESPOLYPEPTIDE, c.getName(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKENZYMESPOLYPEPTIDE, c.getSource(), "\n");
                    }
                }
                if (b.getActions() != null) {
                    for (String a : b.getActions().getAction()) {
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKENZYMESACTIONS, inWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKENZYMESACTIONS, a, "\n");
                    }
                }

                (new DrugBankSplitReference()).split(b.getReferences(), inWID, DrugBankTables.getInstance().DRUGBANKENZYMESREF);
            }
        }

        if (d.getTransporters() != null) {
            for (TransporterType b : d.getTransporters().getTransporter()) {
                long inWID = WIDFactory.getInstance().getWid();
                WIDFactory.getInstance().increaseWid();

                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTRANSPORTERS, inWID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTRANSPORTERS, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTRANSPORTERS, b.getId(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTRANSPORTERS, b.getPosition(), "\n");
                if (b.getPolypeptide() != null && !b.getPolypeptide().isEmpty()) {
                    for (PolypeptideType c : b.getPolypeptide()) {
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTRANSPORTERSPOLYPEPTIDE, inWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTRANSPORTERSPOLYPEPTIDE, c.getId(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTRANSPORTERSPOLYPEPTIDE, c.getName(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTRANSPORTERSPOLYPEPTIDE, c.getSource(), "\n");
                    }
                }
                if (b.getActions() != null) {
                    for (String a : b.getActions().getAction()) {
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTRANSPORTERSACTIONS, inWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKTRANSPORTERSACTIONS, a, "\n");
                    }
                }

                (new DrugBankSplitReference()).split(b.getReferences(), inWID, DrugBankTables.getInstance().DRUGBANKTRANSPORTERSREF);
            }
        }

        if (d.getCarriers() != null) {
            for (CarrierType b : d.getCarriers().getCarrier()) {
                long inWID = WIDFactory.getInstance().getWid();
                WIDFactory.getInstance().increaseWid();

                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCARRIERS, inWID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCARRIERS, WID, "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCARRIERS, b.getId(), "\t");
                ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCARRIERS, b.getPosition(), "\n");
                if (b.getPolypeptide() != null && !b.getPolypeptide().isEmpty()) {
                    for (PolypeptideType c : b.getPolypeptide()) {
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCARRIERSPOLYPEPTIDE, inWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCARRIERSPOLYPEPTIDE, c.getId(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCARRIERSPOLYPEPTIDE, c.getName(), "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCARRIERSPOLYPEPTIDE, c.getSource(), "\n");
                    }
                }
                if (b.getActions() != null) {
                    for (String a : b.getActions().getAction()) {
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCARRIERSACTIONS, inWID, "\t");
                        ParseFiles.getInstance().printOnTSVFile(DrugBankTables.getInstance().DRUGBANKCARRIERSACTIONS, a, "\n");
                    }
                }

                (new DrugBankSplitReference()).split(b.getReferences(), inWID, DrugBankTables.getInstance().DRUGBANKCARRIERSREF);
            }
        }
    }
}
