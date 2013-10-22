package org.jbiowhparser.datasets.pathway.kegg.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jbiowhcore.utility.utils.ParseFiles;
import org.jbiowhpersistence.datasets.dataset.WIDFactory;

/**
 * This class is storage the KEGG ENTRY data
 *
 * $Author: r78v10a07@gmail.com $
 * $LastChangedDate: 2012-11-08 14:37:19 +0100 (Thu, 08 Nov 2012) $
 * $LastChangedRevision: 322 $
 * @since Oct 29, 2011
 */
public class KEGGParserEntry extends KEGGParserFieldTags {

    private HashMap<String, Object> fieldValue;

    public KEGGParserEntry() {
        fieldValue = new HashMap<>();
    }
    
    
    /**
     * Return the field HashMap where the Entry data is storage
     *
     * @return a HashMap object
     */
    public HashMap<String, Object> getFieldValue() {
        return fieldValue;
    }

    /**
     * Return a Set Object with the field tags used by the Entry
     *
     * @return a Set object
     */
    public Set<String> getFields() {
        if (fieldValue.isEmpty()) {
            return null;
        }
        return fieldValue.keySet();
    }

    /**
     * Format the Entry's field splitting the value by patter Replace the field
     * value by an ArrayList Object
     *
     * @param field The Entry's field name
     * @param patter The split patter to by used
     */
    protected void formatFieldSplit(String field, String patter) {
        ArrayList<String> result = new ArrayList<>();
        String value = (String) fieldValue.get(field);

        if (value != null) {
            String[] data = value.trim().split(patter);
            for (String data1 : data) {
                if (!data1.isEmpty()) {
                    result.add(data1.replace("\n", " ").trim());
                }
            }

            getFieldValue().put(field, result);
        }
    }

    /**
     * Format the Entry's field splitting the value by regex and extract from
     * the result the petter as a new key * Replace the field value by an
     * ArrayList Object which includes ArrayList elements with 2 elements: The
     * first element is the new key and the second element the rest of the data
     *
     * @param field The Entry's field name
     * @param pattern The split patter to by used
     * @param regex The regular expression to extract the new key
     * @param keyEnd The string considered to end the key selection
     * @param oldCharOnKey The string into the key that should be replaced by
     * the newCharOnKey
     * @param newCharOnKey The string into the key that is used to replaced by
     * the oldCharOnKey
     */
    protected void formatFieldArrayListSplit(String field, String pattern,
            String regex, String keyEnd, String oldCharOnKey, String newCharOnKey) {
        ArrayList<ArrayList> result = new ArrayList<>();
        String value = (String) fieldValue.get(field);

        if (value != null) {
            String[] data = value.trim().split(pattern);
            String s = null;
            for (String data1 : data) {
                if (data1.matches(regex)) {
                    if (s != null) {
                        ArrayList<String> inResult = new ArrayList<>();
                        String key;
                        String val;
                        if (oldCharOnKey != null) {
                            if (s.indexOf(keyEnd) > 0) {
                                key = s.substring(0, s.indexOf(keyEnd)).replace(oldCharOnKey, newCharOnKey);
                            } else {
                                key = s.replace(oldCharOnKey, newCharOnKey);
                            }
                        } else {
                            if (s.indexOf(keyEnd) > 0) {
                                key = s.substring(0, s.indexOf(keyEnd));
                            } else {
                                key = s;
                            }
                        }
                        inResult.add(key);
                        if (s.indexOf(keyEnd) > 0) {
                            val = s.substring(s.indexOf(keyEnd) + keyEnd.length());
                        } else {
                            val = null;
                        }
                        if (val != null) {
                            inResult.add(val.trim());
                        } else {
                            inResult.add(val);
                        }
                        result.add(inResult);
                        s = data1;
                    } else {
                        s = data1;
                    }
                } else {
                    s = s.concat(" ").concat(data1).replace("\n", " ");
                }
            }
            if (s != null) {
                ArrayList<String> inResult = new ArrayList<>();
                String key;
                String val;
                if (oldCharOnKey != null) {
                    if (s.indexOf(keyEnd) > 0) {
                        key = s.substring(0, s.indexOf(keyEnd)).replace(oldCharOnKey, newCharOnKey);
                    } else {
                        key = s.replace(oldCharOnKey, newCharOnKey);
                    }
                } else {
                    if (s.indexOf(keyEnd) > 0) {
                        key = s.substring(0, s.indexOf(keyEnd));
                    } else {
                        key = s;
                    }
                }
                inResult.add(key);
                if (s.indexOf(keyEnd) > 0) {
                    val = s.substring(s.indexOf(keyEnd) + keyEnd.length());
                } else {
                    val = null;
                }
                if (val != null) {
                    inResult.add(val.trim());
                } else {
                    inResult.add(val);
                }
                result.add(inResult);
            }
            getFieldValue().put(field, result);
        }
    }

    /**
     * Format the Entry's field splitting the value by a ArrayList object of
     * extracted pattern
     *
     * @param field The Entry's field name
     * @param pPrefix The pattern prefix
     * @param pSubfix The patter to be extracted
     * @param pSubject The pattern subfix
     * @param newPrefix The new prefix to be add to the extracted pattern
     * @param newSubfix The new subfix to be add to the extracted pattern
     */
    protected void extractPatternFromField(String field, String pPrefix, String pSubject, String pSubfix,
            String newPrefix, String newSubfix) {
        ArrayList<String> result = new ArrayList<>();
        String value = (String) fieldValue.get(field);
        
        if (value != null) {
            Pattern pat = Pattern.compile(pPrefix + pSubject + pSubfix);
            Matcher mat = pat.matcher(value);

            while (mat.find()) {
                String inValue = mat.group().replace(pPrefix, newPrefix).replace(pSubfix, newSubfix);
                if (inValue != null) {
                    if (!inValue.isEmpty()) {
                        result.add(inValue);
                    }
                }

            }
            
            getFieldValue().put(field, result);
        }
    }

    /**
     * Format the Entry's field replace the oldChar value
     *
     * @param field The Entry's field name
     * @param oldChar Old string to be delete
     * @param newChar New string to be inserted
     */
    protected void formatFieldReplace(String field, String oldChar, String newChar) {
        if (fieldValue.get(field) instanceof String) {
            String value = (String) fieldValue.get(field);

            if (value != null) {
                getFieldValue().put(field, value.replace(oldChar, newChar));
            }
        }
    }

    /**
     * Print into the fileName TSV file the KEGG's field data
     *
     * @param intWID
     * @param WID The Other WID value
     * @param tagName The KEGG's field tag
     * @param fileName The name of the file to print
     */
    protected void printOnTSVFileArrayListValue(boolean intWID, long WID, String tagName, String fileName) {
        Object valObject = getFieldValue().get(tagName);
        if (valObject instanceof ArrayList) {
            ArrayList value = (ArrayList) valObject;

            if (value != null) {
                for (Iterator it = value.iterator(); it.hasNext();) {
                    Object inValue = it.next();
                    if (intWID) {
                        ParseFiles.getInstance().printOnTSVFile(fileName, WIDFactory.getInstance().getWid(), "\t");
                        WIDFactory.getInstance().increaseWid();
                    }
                    ParseFiles.getInstance().printOnTSVFile(fileName, WID, "\t");
                    if (inValue instanceof ArrayList) {
                        int count = 0;
                        for (Iterator it1 = ((ArrayList) inValue).iterator(); it1.hasNext();) {
                            if (count != ((ArrayList) inValue).size() - 1) {
                                ParseFiles.getInstance().printOnTSVFile(fileName, (String) it1.next(), "\t");
                            } else {
                                ParseFiles.getInstance().printOnTSVFile(fileName, (String) it1.next(), "\n");
                            }
                            count++;
                        }
                    } else if (inValue instanceof String) {
                        ParseFiles.getInstance().printOnTSVFile(fileName, (String) inValue, "\n");
                    }
                }
            }
        }
    }
}
