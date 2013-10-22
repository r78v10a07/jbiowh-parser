package org.jbiowhparser.datasets.drug.drugbank.xml;

/**
 * This class is is the XML start point for DrugBank
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 9, 2011
 */
public class Drugs {

    private Drug drug = null;
    private Partner partner = null;

    public Drugs() {
        drug = new Drug();
        partner = new Partner();
    }

    public Drug getDrug() {
        return drug;
    }

    public Partner getPartner() {
        return partner;
    }
}
