package org.jbiowhparser.datasets.pathway.kegg.utility;

/**
 * This class is the KEGG Flat file field definition
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Oct 29, 2011
 */
public class KEGGParserFieldTags {

    /*
     * General field tags
     */
    public final String ENTRY = "ENTRY";
    public final String NAME = "NAME";
    public final String COMMENT = "COMMENT";
    public final String ORTHOLOGY = "ORTHOLOGY";
    public final String PATHWAY = "PATHWAY";
    /*
     * Field tags for:
     *
     * enzyme
     */
    public final String CLASS = "CLASS";
    public final String ALL_REAC = "ALL_REAC";
    public final String SYSNAME = "SYSNAME";
    public final String REACTION = "REACTION";
    public final String SUBSTRATE = "SUBSTRATE";
    public final String PRODUCT = "PRODUCT";
    public final String COFACTOR = "COFACTOR";
    public final String GENES = "GENES";
    public final String DBLINKS = "DBLINKS";
    public final String INHIBITOR = "INHIBITOR";
    public final String EFFECTOR = "EFFECTOR";
    /*
     * Field tags for:
     *
     * reaction
     */
    public final String DEFINITION = "DEFINITION";
    public final String ENZYME = "ENZYME";
    public final String EQUATION = "EQUATION";
    public final String RPAIR = "RPAIR";
    public final String REMARK = "REMARK";
    /*
     * Fields tags for:
     *
     * compound
     */
    public final String MASS = "MASS";
    public final String FORMULA = "FORMULA";
    /*
     * Fields tags for:
     *
     * glycan
     */
    public final String COMPOSITION = "COMPOSITION";
    /*
     * Fields tags for:
     *
     * rpair
     */
    public final String TYPE = "TYPE";
    public final String RCLASS = "RCLASS";
    public final String RELATEDPAIR = "RELATEDPAIR";
    public final String COMPOUND = "COMPOUND";
    /*
     * Fields tags for:
     *
     * gene
     */
    public final String POSITION = "POSITION";
    public final String POSITION1 = "POSITION1";
    public final String POSITION2 = "POSITION2";
    public final String MOTIF = "MOTIF";
    public final String STRUCTURE = "STRUCTURE";
    public final String DRUG_TARGET = "DRUG_TARGET";
    public final String DISEASE = "DISEASE";
}
