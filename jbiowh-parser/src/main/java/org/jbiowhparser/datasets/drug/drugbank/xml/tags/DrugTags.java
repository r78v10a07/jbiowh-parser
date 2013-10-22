package org.jbiowhparser.datasets.drug.drugbank.xml.tags;

/**
 * This class is the Drug XML Tags
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 9, 2011
 */
public class DrugTags {

    private static DrugTags drugTags;
    public final String DRUG = "drug";
    public final String DRUGBANK_ID = "drugbank-id";
    public final String DRUGBANKID = "drugbank-id";
    public final String NAME = "name";
    public final String DESCRIPTION = "description";
    public final String CASNUMBER = "cas-number";
    public final String GENERALREFERENCES = "general-references";
    public final String SYNTHESISREFERENCE = "synthesis-reference";
    public final String INDICATION = "indication";
    public final String PHARMACOLOGY = "pharmacology";
    public final String MECHANISMOFACTION = "mechanism-of-action";
    public final String TOXICITY = "toxicity";
    public final String BIOTRANSFORMATION = "biotransformation";
    public final String ABSORPTION = "absorption";
    public final String HALFLIFE = "half-life";
    public final String PROTEINBINDING = "protein-binding";
    public final String ROUTEOFELIMINATION = "route-of-elimination";
    public final String VOLUMEOFDISTRIBUTION = "volume-of-distribution";
    public final String CLEARANCE = "clearance";
    public final String TYPE = "type";
    public final String UPDATED = "updated";
    public final String CREATED = "created";
    public final String VERSION = "version";

    private DrugTags() {
    }

    /**
     * Return a DrugTags
     *
     * @return a DrugTags
     */
    public static synchronized DrugTags getInstance() {
        if (drugTags == null) {
            drugTags = new DrugTags();
        }
        return drugTags;
    }
}
