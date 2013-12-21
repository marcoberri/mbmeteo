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
import java.util.zip.GZIPInputStream;

/**
 * Input/Output utilities.
 *
 * @author Erich Roncarolo
 * @version 0.1 - 2004-09-07
 */
public class IOUtil {

    private static Properties mimeTypes = null;

    /**
     *
     * @param s
     */
    public static final void close(InputStream s) {
        if (s != null) {
            try {
                s.close();
            } catch (Exception x) {
            }
        }
    }//close()

    /**
     *
     * @param s
     */
    public static final void close(OutputStream s) {
        if (s != null) {
            try {
                s.close();
            } catch (Exception x) {
            }
        }
    }//close()

    /**
     *
     * @param r
     */
    public static final void close(Reader r) {
        if (r != null) {
            try {
                r.close();
            } catch (Exception x) {
            }
        }
    }//close()

    /**
     *
     * @param w
     */
    public static final void close(Writer w) {
        if (w != null) {
            try {
                w.close();
            } catch (Exception x) {
            }
        }
    }//close()

    /**
     * Returns the canonical path of a file or null if an exception occours or the file is out of root
     *
     * @param path file to check
     * @param root file's root
     * @return the canonical path
     */
    public static final String canonicalPath(String path, String root) {
        if (StringUtil.isEmpty(path)) {
            return null;
        }


        path = path.trim();
        root = root.trim();
        path = path.replace('\\', '/');

        try {
            //check the root
            File froot = new File(root);
            froot = froot.getCanonicalFile();

            //add the root if needed
            File file = new File(path);
            if (!file.isAbsolute()) {
                if (froot == null) {
                    return null;
                }
                file = new File(froot, path);
                path = file.getPath().replace('\\', '/');
                //path = root +"/"+ path;
                //file = new File(path);
            }//if

            //get the canonical path
            String canonical = file.getCanonicalPath();
            if (!StringUtil.isEmpty(root) && !path.startsWith(root)) {
                //log
                return null;
            }//if
            if (canonical == null) {
                return null;
            }

            //from '\' to '/' and append '/' if is a dir
            canonical = canonical.replace('\\', '/');
            file = new File(canonical);
            if (file.isDirectory() && !canonical.endsWith("/")) {
                canonical += File.separator;
            }

            return canonical;
        } catch (Exception x) {
            //log
            return null;
        }
    }//canonicalPath()

    /**
     * Returns the canonical path of a file or null if an exception occours
     *
     * @param path file to check
     * @return the canonical path
     */
    public static final String canonicalPath(String path) {
        return canonicalPath(path, null);
    }//canonicalPath()

    /**
     * Returns the absolute path of a file checking it against MicroKernel.getEnv("root").
     * Like absolutePath(path,null)
     *
     * @param path file to check
     * @return the absolute path
     */
    public static final String absolutePath(String path) {
        return absolutePath(path, null);
    }//absolutePath()

    /**
     * Returns the absolute path of a file checking it against 'root'
     *
     * @param path file to check
     * @param root the root: must be absolute (if null then MicroKernel.getEnv("root") will be used)
     * @return the absolute path or null if root is not absolute or path is absolute and doesn't start with root
     */
    public static final String absolutePath(String path, String root) {


        String r = normalizedDirectory(root);
        if (StringUtil.isEmpty(path)) {
            return r;
        }

        File fr = new File(r);
        if (!fr.isAbsolute()) {
            return null;
        }


        String p = normalizedPath(path);
        File fp = new File(p);
        if (fp.isAbsolute()) {
            if (p.startsWith(r)) {
                return p;
            }
            return null;
        } else {
            return joinPaths(r, p);
        }
    }//absolutePath()

    /**
     * Returns the absolute path of a directory checking it against MicroKernel.getEnv("root").
     * Like absoluteDirectory(path,null)
     *
     * @param path file to check
     * @return the absolute path
     */
    public static final String absoluteDirectory(String path) {
        return absoluteDirectory(path, null);
    }//absoluteDirectory()

    /**
     * Returns the absolute path of a directory checking it against 'root'
     * Calls {@link absolutePath(String,String)} and than appends a trailing "/"
     *
     * @param path file to check
     * @param root the root: must be absolute
     * @return the absolute path or null if root is not absolute or path is absolute and doesn't start with root
     */
    public static final String absoluteDirectory(String path, String root) {
        String dir = absolutePath(path, root);
        if (dir == null) {
            return null;
        }
        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }
        return dir;
    }//absoluteDirectory()

    /**
     * Returns the normalized path of a file.
     * A normalized path is a path where path separator is "/"
     * and no redundant elements (like "." and "..") are present.<br/>
     * <strong>Note:</strong>This methods has been not tested on Win nor Mac
     * systems and probably can have some problems with relative paths starting
     * with a drive letter (eg: c:this\is\a\relative\path).
     *
     * @param path file to check
     * @return the normalized path
     */
    public static final String normalizedPath(String path) {
        if (StringUtil.isEmpty(path)) {
            return path;
        }

        path = path.replace('\\', '/');
        boolean absolute = (new File(path)).isAbsolute();

        // split path in its components
        Stack stack = new Stack();
        for (File file = new File(path.trim().replace('\\', '/')); file != null; file = file.getParentFile()) {
            stack.push(file);
        }

        // find the root, if any
        File first = (File) stack.peek();
        File root = null;
        if (absolute && first != null) {
            File[] roots = File.listRoots();
            for (int i = 0; roots != null && i < roots.length; i++) {
                if (first.equals(roots[i])) {
                    root = (File) stack.pop();
                    break;
                }//if
            }//for
        }//if

        // normalize the path removing redundant elements (. and ..)
        ArrayList list = new ArrayList();
        while (!stack.empty()) {
            File file = (File) stack.pop();
            String name = file.getName();
            if (name.equals(".")) {
                continue;
            } else if (name.equals("..")) {
                int i = list.size() - 1;
                if (i >= 0) {
                    list.remove(i);
                }
            } else {
                list.add(name);
            }//if-else
        }//while

        if (list.isEmpty()) {
            if (root != null) {
                return root.getPath().replace('\\', '/');
            }
            return null;
        }//if

        // build the file
        File file = new File((String) list.get(0));
        for (int i = 1, n = list.size(); i < n; i++) {
            file = new File(file, (String) list.get(i));
        }

        // prepend the root, if any
        if (root != null) {
            file = new File(root, file.getPath());
        }

        // if specified path is absolute, then normalized path must be absolute
        if (absolute && !file.isAbsolute()) {
            file = new File(File.separator, file.getPath());
        }

        // get normalized path as String and converts "\" -> "/"
        String normalized = file.getPath();
        normalized = normalized.replace('\\', '/');

        return normalized;
    }//normalizedPath()

    /**
     * Returns the normalized path of a directory.
     * Calls {@link normalizedPath(String)} and than appends a trailing "/"
     *
     * @param path dir to normalize
     * @return the normalized path
     */
    public static final String normalizedDirectory(String path) {
        String dir = normalizedPath(path);
        if (dir == null) {
            return null;
        }
        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }
        return dir;
    }//normalizedDirectory()

    /**
     * Finds the relative path
     * @param path 
     * @param root
     * @return
     */
    public static final String relativePath(String path, String root) {
        if (StringUtil.isEmpty(path)) {
            return path;
        }

        path = path.replace('\\', '/');

        root = root.replace('\\', '/');

        //path = path.trim();
        //root = root.trim();
        boolean path_trailing_slash = path.endsWith(File.separator);
        path = normalizedPath(path);
        if (path_trailing_slash) {
            path += File.separator;
        }
        root = normalizedDirectory(root);

        if (StringUtil.isEmpty(root) || StringUtil.isEmpty(path)) {
            return path;
        }

        String rel_path = path;
        if (rel_path.startsWith(root)) {
            rel_path = StringUtil.substring(rel_path, root.length()).trim();
        }
        if (rel_path.startsWith(File.separator)) {
            rel_path = rel_path.substring(1);
        }

        return rel_path;
    }//relativePath()

    /**
     * Finds the relative path
     * @param path
     * @return
     */
    public static final String relativePath(String path) {
        if (StringUtil.isEmpty(path)) {
            return path;
        }
        return relativePath(path, null);
    }//relativePath()

    /**
     * Finds the parent path
     * @param path
     * @return
     */
    public static final String parentPath(String path) {
        if (StringUtil.isEmpty(path)) {
            return path;
        }

        return normalizedDirectory(Default.notEmpty((new File(path)).getParent(), "/"));
    }//parentPath()

    /**
     * Joins 2 paths and normalize the result.
     *
     * @param left the left part
     * @param right the right part
     * @return "left/right"
     */
    public static final String joinPaths(String left, String right) {
        if (StringUtil.isEmpty(left)) {
            return normalizedPath(right);
        }
        if (StringUtil.isEmpty(right)) {
            return normalizedPath(left);
        }
        File f = new File(left, right);
        String path = f.getPath();
        return normalizedPath(path);
    }//joinPaths()

    /**
     * Finds a file
     * starting from a relative path (parent), a file path array (files)
     * without exiting from a root.
     * @param parent
     * @param files
     * @param root
     * @return
     */
    public static final String findFile(String parent, String files[], String root) {

        if (files == null || files.length < 1) {
            return null;
        }

        root = normalizedDirectory(root);
        parent = normalizedDirectory(parent);

        String p = null;
        File f = null;
        do {
            p = parent;
            if (p != null) {
                f = new File(p);
                parent = f.getParent();
                f = new File(root, p);
            } else {
                f = new File(root);
            }
            for (int i = 0; i < files.length; i++) {
                String file = normalizedPath(files[i]);
                File g = new File(f, file);
                if (g.exists()) {
                    return relativePath(g.getPath().replace('\\', '/'), root);
                }
            }//if
        } while (p != null);

        return null;
    }//findFile()

    /**
     * Finds a file
     * starting from a relative path (parent), a file path array (files)
     * without exiting from Tnes root.
     * @param parent 
     * @param files
     * @return
     */
    public static final String findFile(String parent, String[] files) {
        return findFile(parent, files, null);
    }//findFile()

    /**
     * Finds a file
     * starting from a relative path (parent), a file path (file)
     * without exiting from a root.
     * @param parent 
     * @param file
     * @param root
     * @return
     */
    public static final String findFile(String parent, String file, String root) {
        if (StringUtil.isEmpty(file)) {
            return null;
        }
        String f[] = new String[1];
        f[0] = file;
        return findFile(parent, f, root);
    }//findFile()

    /**
     * Finds a file
     * starting from a relative path (parent), a resource path (file)
     * without exiting from Tnes root.
     * @param parent 
     * @param file
     * @return
     */
    public static final String findFile(String parent, String file) {
        return findFile(parent, file, null);
    }//findFile()

    /**
     * Gets a Reader from a file
     * @param filePath the relative path
     * @param encoding the encoding
     * @return a Reader
     */
    public static final Reader getReader(String filePath, String encoding) {


        try {

            filePath = relativePath(filePath);
            String absPath = absolutePath(filePath);
            return new InputStreamReader(new FileInputStream(absPath), encoding);

        } catch (Exception e) {
            return null;
        }
    }//getReader()

    /**
     * Gets a Reader from a file
     * @param filePath the relative path
     * @return a Reader
     */
    public static final Reader getReader(String filePath) {
        return getReader(filePath, "ISO-8859-1");
    }//getReader()

    /**
     * Reads a reader putting all its lines in an array.
     *
     * @param reader Reader to read
     * @return an array of String containing reader's lines
     */
    public static final String[] readLines(Reader reader) {
        return readLines(reader, 0, 0);
    }//readLines()

    /**
     * Reads a reader putting nLines lines in an array.
     *
     * @param reader Reader to read
     * @param nLines number of lines to read, if nLines <= 0 then read until end of reader
     * @return an array of String containing reader's lines
     */
    public static final String[] readLines(Reader reader, int nLines) {
        return readLines(reader, 0, nLines);
    }//readLines()

    /**
     * Reads a reader starting from startLine and putting nLines lines in an array.
     *
     * @param reader Reader to read
     * @param startLine line to start, if startLine < 0 then startLine = 0
     * @param nLines number of lines to read, if nLines <= 0 then read until end of reader
     * @return an array of String containing reader's lines
     */
    public static final String[] readLines(Reader reader, int startLine, int nLines) {
        if (reader == null) {
            return null;
        }
        if (startLine < 0) {
            startLine = 0;
        }
        try {
            BufferedReader buf = new BufferedReader(reader);
            ArrayList lines = new ArrayList();
            int i = 0;
            for (String s = null; (nLines <= 0 || i < nLines) && (s = buf.readLine()) != null; i++) {
                if (i < startLine) {
                    continue;
                }
                lines.add(s);
            }
            return (String[]) lines.toArray(new String[i]);
        } catch (Exception x) {
            return null;
        }
    }//readLines()

    /**
     * Check if file exist
     *
     * @param file
     * @return return tru if a file exist
     */
    public static final boolean checkFileExist(String file) {
        File f = new File(file);
        return f.exists();
    }//checkFileExist()

    /**
     * Esegue una dir su disco in base alla regex
     *
     * @param regex String a con espressione regolare
     * @param dir la cartella su cui eseguire la dir
     * @return Un Vettore di File
     */
    public static final File[] dirFile(final String regex, File dir) {

        if (dir == null) {
            return null;
        }

        return dir.listFiles(
                new FilenameFilter() {

                    public boolean accept(File dir, String filename) {
                        if (regex == null) {
                            return true;
                        }
                        return filename.toLowerCase().matches(regex);
                    }//accept()
                }//FilenameFilter
                );

    }//dirFile()

    /**
     *
     * @param is
     * @param os
     */
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1) {
                    break;
                }
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    /**
     *
     * @param inFilePath
     * @param outFilePath
     * @return
     */
    public static String unzip(String inFilePath, String outFilePath) {

        try {

            GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(inFilePath));

            //  String outFilePath = inFilePath.replace(".gz", "");
            OutputStream out = new FileOutputStream(outFilePath);

            byte[] buf = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            gzipInputStream.close();
            out.close();

            new File(inFilePath).delete();

            return outFilePath;
        } catch (Exception e) {
            return null;
        }
    }
}//IOUtil
