/**
 * Copyright 2011 Marco Berri - marcoberri@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package it.marcoberri.mbmeteo.action;

import it.marcoberri.mbmeteo.action.UploadFile.ExecuteImport;
import it.marcoberri.mbmeteo.helper.ConfigurationHelper;
import it.marcoberri.mbmeteo.utils.Log4j;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author berri
 */
public class UploadFile extends HttpServlet {

    private static final int THRESHOLD_SIZE = 1024 * 1024 * 3; // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
    /**
     *
     */
    private final org.apache.log4j.Logger log = Log4j.getLogger("mbmeteo", ConfigurationHelper.prop.getProperty("log.path"), Log4j.ROTATE_DAILY);

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // checks if the request actually contains upload file
        if (!ServletFileUpload.isMultipartContent(request)) {
            return;
        }

        // configures some settings
        final DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(THRESHOLD_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        final ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(REQUEST_SIZE);

        // constructs the directory path to store upload file
        final String uploadPath = ConfigurationHelper.prop.getProperty("import.loggerEasyWeather.filepath");

        final File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            FileUtils.forceMkdir(uploadDir);
        }

        try {
            // parses the request's content to extract file data
            final List formItems = upload.parseRequest(request);
            Iterator iter = formItems.iterator();

            // iterates over form's fields
            while (iter.hasNext()) {
                final FileItem item = (FileItem) iter.next();
                // processes only fields that are not form fields
                if (item.isFormField()) {
                    continue;
                }

                final String fileName = new File(item.getName()).getName();
                final String filePath = uploadPath + File.separator + fileName;
                final File storeFile = new File(filePath);
                item.write(storeFile);
            }
            request.setAttribute("message", "Upload has been done successfully!");
        } catch (Exception ex) {
            request.setAttribute("message", "There was an error: " + ex.getMessage());
        }

        ExecuteImport i = new ExecuteImport();
        Thread t = new Thread(i);
        t.start();

    }

    @Override
    public String getServletInfo() {
        return "Upload Log for import data";
    }

    class ExecuteImport implements Runnable {

        private final org.apache.log4j.Logger log = Log4j.getLogger("mbmeteo", ConfigurationHelper.prop.getProperty("log.path"), Log4j.ROTATE_DAILY);

        @Override
        public void run() {

            Commons.importLogEasyWeather(log);
            Commons.mapReduce(log);
            Commons.deleteCache(log);

        }
    }

}
