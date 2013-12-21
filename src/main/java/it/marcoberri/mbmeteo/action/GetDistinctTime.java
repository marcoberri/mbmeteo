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

import com.github.jmkgreen.morphia.Datastore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.marcoberri.mbmeteo.helper.ConfigurationHelper;
import it.marcoberri.mbmeteo.helper.MongoConnectionHelper;
import it.marcoberri.mbmeteo.utils.Log4j;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
public class GetDistinctTime extends HttpServlet {

    /**
     *
     */
    protected final org.apache.log4j.Logger log = Log4j.getLogger("mbmeteo", ConfigurationHelper.prop.getProperty("log.path"), Log4j.ROTATE_DAILY);

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        log.debug("start : " + this.getClass().getName());

        final PrintWriter out = response.getWriter();

        try {
            final Datastore ds = MongoConnectionHelper.ds;
            final List<Date> datesResult = ds.getDB().getCollection("Meteolog").distinct("time");
            final TreeMap<String, Date> dates = new TreeMap<String, Date>();

            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            for (Date d : datesResult) {
                try {
                    dates.put(df.format(d), d);
                } catch (Exception e) {
                    log.error("error on data : " + d,e);
                    continue;
                }
            }
            final GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("dd-MM-yyyy");
            final Gson gson = gsonBuilder.create();

            response.setContentType("application/json");
            response.setHeader("Content-Disposition", "attachment; filename=\"result.json\"");
            out.println(gson.toJson(dates));

        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
