package org.jbiowhparser.datasets.protein.xml;

import org.jbiowhparser.datasets.protein.xml.tags.EvidencedStringTypeTags;

/**
 * This Class handled the EvidencedStringType on Uniprot
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-10-03 22:11:05 +0200 (Wed, 03 Oct 2012) $
 * $LastChangedRevision: 270 $
 * @since Sep 30, 2010
 * @see
 */
public class EvidencedStringType extends EvidencedStringTypeTags {

    private boolean open = false;
    private String evidence = null;
    private String status = null;
    private String data = null;

    /**
     * This constructor initialize the WH file manager and the WH DataSet
     * manager
     *
     * @param files the WH file manager
     * @param whdataset the WH DataSet manager
     */
    public EvidencedStringType() {
        open = false;
        evidence = null;
        status = null;
        data = null;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
