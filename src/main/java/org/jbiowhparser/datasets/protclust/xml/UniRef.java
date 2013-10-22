package org.jbiowhparser.datasets.protclust.xml;

/**
 * This Class is the UniRef XML
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Aug 19, 2011
 */
public class UniRef {

    private UniRefXML uniref = null;

    public UniRef() {
        uniref = new UniRefXML();
    }

    public UniRefXML getUniref() {
        return uniref;
    }
}
