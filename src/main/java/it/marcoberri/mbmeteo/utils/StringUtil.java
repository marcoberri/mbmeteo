/**
 *  Copyright 2011 Marco Berri - marcoberri@gmail.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 **/
package it.marcoberri.mbmeteo.utils;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Utility Class for manage String informations
 *
 * @author Erich Roncarolo
 * @version 0.1 - 2004-08-23
 */
public class StringUtil {

    /**
     * *
     * @param f nome del file in formato string
     * @return estensione del file in formato string
     */
    public static String getFileExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');
        if (i > 0 && i < f.length() - 1) {
            ext = f.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /**
     *
     * @param str
     * @param find
     * @param replaceto
     * @return String
     */
    public static String replaceAll(String str, String find, String replaceto) {

        return str.replaceAll(find, replaceto);
    }

    /**
     * Checks if a String is empty or null
     *
     *	@param s String to check
     * @return true if null or empty, false otherwise
     */
    public static boolean isEmpty(String s) {
        return (s == null || "".equals(s.trim()));
    }//isEmpty()

    /**
     * Checks if a String is empty, null or "null" (case insensitive)
     *
     *	@param s String to check
     * @return true if null, empty or "null", false otherwise
     */
    public static boolean isNullOrEmpty(String s) {
        return (isEmpty(s) || "null".equalsIgnoreCase(s.trim()));
    }//isNullOrEmpty()

    /**
     * Returns a int value from a String value
     *
     *	@param s String to check
     * @param d default value in case of exception
     * @return the int value of the string, or a default value
     */
    public static int parseInt(String s, int d) {
        try {
            return Integer.parseInt(s);
        } catch (Exception x) {
            return d;
        }
    }//parseInt()

    /**
     * Returns a long value from a String value
     *
     *	@param s String to check
     * @param d default value in case of exception
     * @return the long value of the string, or a default value
     */
    public static long parseLong(String s, long d) {
        try {
            return Long.parseLong(s);
        } catch (Exception x) {
            return d;
        }
    }//parseLong()

    /**
    /**
     * Returns a capitalized String.
     * Make the first character upper case leaving untouched the other ones
     * Eg: this is my String -> This is my String
     *
     * @param s String to capitalize
     * @return a capitalized String
     */
    public static String capitalize(String s) {
        if (isEmpty(s)) {
            return s;
        }
        if (s.length() == 1) {
            return "" + Character.toUpperCase(s.charAt(0));
        }
        return "" + Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }//capitalize()

    /**
     * Returns a String where all words are capitalized, except invariant words (case insensitive)
     * Eg: this is my string (invariant: this is my) -> This is my String (first word will be always capitalized)
     *
     * @param s String to capitalize
     * @param invariant String array of word that should not be capitalized
     * @param tokens used to tokenize
     * @param special special tokens, like '
     * @param first capitalize always first word (default true)
     * @return a capitalized String
     */
    public static String capitalizeAllWords(String s, String[] invariant, String tokens, String special, boolean first) {
        if (isEmpty(s)) {
            return s;
        }
        if (s.length() == 1) {
            return "" + Character.toUpperCase(s.charAt(0));
        }

        String t[] = StringUtil.tokenize(s, tokens);
        if (first) {
            t[0] = StringUtil.capitalize(t[0]);
        }

        for (int i = 0; i < t.length; i++) {
            boolean b = true;

            if (special != null) {
                boolean next = false;
                for (int k = 0, n = special.length(); k < n; k++) {
                    char c = special.charAt(k);
                    if (t[i].indexOf(c) != -1) {
                        next = true;
                        t[i] = StringUtil.capitalizeAllWords(t[i], invariant, "" + c, null, false);
                    }//if
                }//for
                if (next) {
                    continue;
                }
            }//if

            if (invariant != null) {
                for (int j = 0; j < invariant.length; j++) {
                    if (t[i].equalsIgnoreCase(invariant[j])) {
                        b = false;
                        break;
                    }//if
                }//for
            }//if

            if (b) {
                t[i] = StringUtil.capitalize(t[i]);
            }
        }//for

        return StringUtil.join(t, (tokens == null ? null : "" + tokens.charAt(0)));
    }//capitalizeAllWords()

    /**
     *
     * @param s
     * @param invariant
     * @return
     */
    public static String capitalizeAllWords(String s, String[] invariant) {
        return capitalizeAllWords(s, invariant, null, "'", true);
    }//capitalizeAllWords()

    /**
     *
     * @param array
     * @param separators
     * @return List
     */
    public static List splitAsList(String array, String separators) {
        List list = new ArrayList();

        if (array == null) {
            return list;
        }

        if (separators == null) {
            separators = "\\s+";
        } else {
            separators = "[" + separators + "]+";
        }

        String s[] = array.split(separators);
        if (s == null || s.length == 0) {
            return list;
        }

        for (int i = 0; i < s.length; i++) {
            if (!isEmpty(s[i])) {
                list.add(s[i].trim());
            }
        }

        return list;
    }//split()

    /**
     *
     * @param array
     * @param separators
     * @return element trimmed in array elements
     */
    public static String[] split(String array, String separators) {
        final List list = splitAsList(array, separators);
        return (String[]) list.toArray(new String[list.size()]);
    }//split()

    /**
     *
     * @param s
     * @return element toklenized in array elements
     */
    public static String[] tokenize(String s) {
        return tokenize(s, null, (char) 0, 0);
    }//tokenize()

    /**
     *
     * @param s
     * @param tok
     * @return element toklenized in array elements
     */
    public static String[] tokenize(String s, String tok) {
        return tokenize(s, tok, (char) 0, 0);
    }//tokenize()

    /**
     *
     * @param s
     * @param tok
     * @param quotes
     * @return element toklenized in array elements
     */
    public static String[] tokenize(String s, String tok, char quotes) {
        return tokenize(s, tok, quotes, 0);
    }//tokenize()

    /**
     *
     * @param s
     * @param tok
     * @param len
     * @return element toklenized in array elements
     */
    public static String[] tokenize(String s, String tok, int len) {
        return tokenize(s, tok, (char) 0, len);
    }//tokenize()

    /*
     * Se (len == 0) ritorna un vettore di lunghezza appropriata
     * Se (len > 0) ritorna un vettore esattamente uguale a len scartando i risultati in eccesso
     *  o con padding fatto di stringhe vuote (non null)
     * Se (len < 0) ritorna un vettore di lunghezza al piu' uguale a (-len) mettendo tutti i risultati eccedenti
     *  nell'ultima stringa (separati da tok.charAt(0) o ' ') e senza padding
     */
    /**
     *
     * @param s
     * @param tok
     * @param quotes
     * @param len
     * @return element toklenized in array elements
     */
    public static String[] tokenize(String s, String tok, char quotes, int len) {
        if (s == null || "".equals(s.trim())) {
            if (len > 0) {
                String t[] = new String[len];
                for (int i = 0; i < len; i++) {
                    t[i] = "";
                }
                return t;
            } else if (len < 0) {
                String t[] = new String[0];
                return t;
            }
            return null;
        }
        StringTokenizer st = null;
        if (quotes != 0) {
            String t[] = tokenize(s, "" + quotes + "", (char) 0);
            int j = (s.trim().charAt(0) == quotes ? 0 : 1);
            Vector v = new Vector();
            for (int i = 0; i < t.length; i++) {
                if (i % 2 == j) {
                    v.addElement(t[i]);
                } else {
                    String w[] = tokenize(t[i], tok);
                    if (w != null) {
                        for (int k = 0; k < w.length; k++) {
                            v.addElement(w[k]);
                        }
                    }//if
                }//if-else
            }//for
            t = new String[v.size()];
            v.copyInto(t);
            return t;
        }
        if (tok == null) {
            st = new StringTokenizer(s.trim());
        } else {
            st = new StringTokenizer(s.trim(), tok);
        }
        if (len == 0) {
            len = st.countTokens();
        }
        int length = Math.abs(len);
        String t[] = new String[length];
        int i = 0;
        for (; st.hasMoreTokens() && i < length; i++) {
            t[i] = st.nextToken().trim();
        }
        if (len > 0) {
            for (; i < length; i++) {
                t[i] = "";
            }
        } else {
            char c = ' ';
            if (tok != null) {
                c = tok.charAt(0);
            }
            for (i--; st.hasMoreTokens();) {
                t[i] += ("" + c + st.nextToken().trim());
            }
        }
        return t;
    }//tokenize()

    /**
     *
     * @param s
     * @return
     */
    public static String join(String s[]) {
        if (s == null) {
            return null;
        }
        return join(s, null, 0, s.length);
    }//join()

    /**
     *
     * @param s
     * @param tok
     * @return
     */
    public static final String join(String s[], String tok) {
        if (s == null) {
            return null;
        }
        return join(s, tok, 0, s.length);
    }//join()

    /**
     *
     * @param s
     * @param tok
     * @param start
     * @return
     */
    public static String join(String s[], String tok, int start) {
        if (s == null) {
            return null;
        }
        return join(s, tok, start, s.length);
    }//join()

    /**
     *
     * @param s
     * @param tok
     * @param start
     * @param stop
     * @return
     */
    public static String join(String s[], String tok, int start, int stop) {
        if (s == null || s.length == 0) {
            return null;
        }
        if (tok == null) {
            tok = " ";
        }
        if (start >= s.length) {
            return null;
        }
        while (start < s.length && (s[start] == null || "".equals(s[start].trim()))) {
            start++;
        }
        if (start >= s.length) {
            return null;
        }
        String r = s[start];
        stop = (stop < s.length ? stop : s.length);
        for (int i = start + 1; i < stop; i++) {
            if (s[i] != null && !"".equals(s[i].trim())) {
                r += tok + s[i];
            }
        }
        return r;
    }//join()

    /**
     *
     * @param s
     * @return
     */
    public static final String normalize(String s) {
        if (s == null) {
            return null;
        }
        return s.toLowerCase().replaceAll("[^a-z0-9\\.]+", "_");
    }//normalize()

    /**
     * Remove all the near occurrences of the specific token
     *
     *	@param s String to check
     * @param token
     * @return
     */
    public static String removeClones(String s, String token) {
        if (s == null) {
            return null;
        }
        while (s.indexOf(token + token) != -1) {
            s = s.replaceAll(token + token, token);
        }
        return s;
    }//removeClones()

    /**
     * Esegue una Trim su una Stringa
     * se s == null return null
     *
     *	@param s String to trim
     * @return La Stringa Trimmata
     */
    public static final String trim(String s) {
        if (s == null) {
            return null;
        }
        return s.trim();
    }//trim()

    /**
     * Esegue una toUpperCase su una Stringa
     * se s == null return null
     *
     *	@param s String to toUpperCase
     * @return La Stringa toUpperCase
     */
    public static final String toUpperCase(String s) {
        if (s == null) {
            return null;
        }
        return s.toUpperCase();
    }//toUpperCase()

    /**
     * Esegue una toLowerCase su una Stringa
     * se s == null return null
     *
     *	@param s String to toLowerCase
     * @return La Stringa toLowerCase
     */
    public static String toLowerCase(String s) {
        if (s == null) {
            return null;
        }
        return s.toLowerCase();
    }//toLowerCase()

    /**
     *
     * @param s
     * @param start
     * @param stop
     * @return string
     */
    public static final String substring(String s, int start, int stop) {
        if (isEmpty(s)) {
            return s;
        }
        if (start < 0 || start >= s.length()) {
            return "";
        }
        if (stop > s.length() || stop < 0) {
            stop = s.length();
        }
        return s.substring(start, stop);
    }//substring()

    /**
     *
     * @param s
     * @param start
     * @return string
     */
    public static final String substring(String s, int start) {
        return substring(s, start, -1);
    }//substring()

    /**
     *
     * @param s
     * @param stop
     * @param suf
     * @return String truncated
     */
    public static final String truncate(String s, int stop, String suf) {
        if (s == null) {
            return "";
        }
        if (s.length() == 0) {
            return "";
        } else if (s.length() <= stop) {
            return s;
        } else {
            return substring(s, 0, s.indexOf(" ", stop)) + suf;
        }
    }//truncate()

    /**
     *
     * @param data
     * @param encode
     * @return
     */
    public static final String encode(String data, String encode) {
        return encode(data, encode, null);
    }

    /**
     *
     * @param data
     * @param encode
     * @param from
     * @return
     */
    public static final String encode(String data, String encode, String from) {
        if (data == null || encode == null) {
            return null;
        }
        try {
            if (from != null) {
                return new String(data.getBytes(encode), from);
            }
            return new String(data.getBytes(encode));
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     *
     * @param data
     * @return
     */
    public static final String encode(String data) {
        return encode(data, "UTF-8");
    }

    /**
     * Fa l'escape di tutti i char > ascii(127) in &#unicode;
     * @param s 
     * @param encode
     * @return
     */
    public static String escapeNonAsciiChars(String s, String encode) {
        try {
            if (encode != null) {
                s = StringUtil.encode(s, "ISO-8859-1", encode);
            }

            StringBuffer sb = new StringBuffer(s);
            for (int i = 0; i < sb.length(); i++) {
                char c = sb.charAt(i);
                int j = (int) c;
                if (j > 127) {
                    sb.setCharAt(i, ';');
                    sb.insert(i, "&#" + j);
                    i += ("&#" + j).length();
                }
            }

            return sb.toString();

        } catch (Exception e) {
            return s;
        }
    }//escapeNonAsciiChars()

    /**
     *
     * @param s
     * @return
     */
    public static String escapeNonAsciiChars(String s) {
        return escapeNonAsciiChars(s, null);
    }//escapeNonAsciiChars()

    /**
     * da Byte a intero
     * @param sByte 
     * @return
     */
    public static final int byteToUnInt(Byte sByte) {
        int uByte = sByte.intValue();
        if (uByte <= 0) {
            uByte = uByte + 256;
        }
        return uByte;

    }//byteToUnInt()

    /**
     * Returns a byte array containing the two's-complement representation of the integer.<br>
     * The byte array will be in big-endian byte-order with a fixes length of 4
     * (the least significant byte is in the 4th element).<br>
     * <br>
     * <b>Example:</b><br>
     * <code>intToByteArray(258)</code> will return { 0, 0, 1, 2 },<br>
     * <code>BigInteger.valueOf(258).toByteArray()</code> returns { 1, 2 }.
     * @param integer The integer to be converted.
     * @return The byte array of length 4.
     */
    public static final byte[] intToByteArray(final int integer) {
        int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
        byte[] byteArray = new byte[4];

        for (int n = 0; n < byteNum; n++) {
            byteArray[3 - n] = (byte) (integer >>> (n * 8));
        }

        return (byteArray);
    }

    /**
     *
     * @param s
     * @return String urlencode with default UTF8 encoding
     */
    public static final String urlencode(String s) {
        return urlencode(s, null);
    }

    /**
     *
     * @param s
     * @param enc
     * @return String urlencoded
     */
    public static final String urlencode(String s, String enc) {
        if (enc == null) {
            enc = "UTF8";
        }
        try {
            return java.net.URLEncoder.encode(s, enc);
        } catch (Exception e) {
            return e.getMessage();
        }
    }//urlencode()

    /**
     *
     * @param a
     * @return Trim all array elements
     */
    public static String[] trimAll(String[] a) {

        if (a == null && a.length < 0) {
            return null;
        }

        String[] r = new String[a.length];

        for (int i = 0; a != null && i < a.length; i++) {
            r[i] = (a[i]).trim();
        }

        return r;
    }

    /**
     *
     * @param arr
     * @return vector
     */
    public static Vector stringToVector(String[] arr) {

        if (arr == null) {
            return null;
        }

        Vector a = new Vector();
        for (int i = 0; i < arr.length; i++) {
            a.add(arr[i].trim());
        }

        return a;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static String concat(Object a, Object b) {
        return Default.toString(a) + Default.toString(b);
    }

    /**
     *
     * @param s
     * @param alt
     * @return
     */
    public static String isEmpty(String s, String alt) {
        if (isEmpty(s)) {
            return alt;
        }
        return s;
    }

    /**
     * Create a list from a file where an element of the list is a line of the text file
     *	@param filePath Relative path
     * @return the List
     */
    public static final List getListFromFile(String filePath) {
        if (isEmpty(filePath)) {
            return null;
        }

        Reader f = IOUtil.getReader(filePath);

        return Arrays.asList(IOUtil.readLines(f));
    }//getListFromFile()

    /**
     * Traforma la sctringa capitallizzata e sostituice gli _ con ''
     *
     * @param s
     * @return La sctringa elaborata
     *
     */
    public static final String pretty(String s) {

        if (s == null) {
            return null;
        }

        return StringUtil.capitalize(s).replace('_', ' ');
    }//pretty()

    /**
     * prende la stringa "la sfiga e' con noi" e diventa "sfiga diventa con noi (La)"
     * @param title 
     * @return
     */
    public static final String reindexTitle(String title) {

        if (StringUtil.isNullOrEmpty(title)) {
            return null;
        }

        String[] illola = {"il", "lo", "la", "l", "i", "gli", "le", "del", "della", "dell'", "dei", "delle", "degli", "un", "uno", "una", "sul", "sullo", "sulla", "sull", "sui", "sugli", "sulle"};

        String result = "";


        String original = StringUtil.trim(title);

        original = original.replaceAll("&#8217;", "'");

        for (int k = 0; k < illola.length; k++) {
            Pattern p = Pattern.compile("^(" + illola[k] + ")('| )(.*)", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(original);
            boolean match = m.matches();

            if (match) {
                result = StringUtil.trim(m.group(3)) + " (" + StringUtil.capitalize(m.group(1)) + StringUtil.trim(m.group(2)) + ")";
                break;
            }//if()
        }//for()

        return result;
    }//reindexTitle()
}//StringUtil

