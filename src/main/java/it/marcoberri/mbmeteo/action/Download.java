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
import it.marcoberri.mbmeteo.helper.ConfigurationHelper;
import it.marcoberri.mbmeteo.helper.MongoConnectionHelper;
import it.marcoberri.mbmeteo.model.Meteolog;
import it.marcoberri.mbmeteo.utils.Log4j;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
public class Download extends HttpServlet {

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

        final Datastore ds = MongoConnectionHelper.ds;
        final List<Meteolog> meteoLogList = ds.find(Meteolog.class).asList();

        final Map params = request.getParameterMap();

        String format = "csv";
        if (params.containsKey("format")) {
            final String s[] = (String[]) params.get("format");
            format = s[0];
        }


        if (format.equalsIgnoreCase("csv")) {
            // toJson

            String bigCsv = "time;absPressure;T;Wind dir;gust;gust level;DewPoint;\n";

            for (Meteolog m : meteoLogList) {

                bigCsv += m.getTime() + ";";
                bigCsv += m.getAbsolutePressure() + ";";
                bigCsv += m.getOutdoorTemperature() + ";";
                bigCsv += m.getDirection() + ";";
                bigCsv += m.getGust() + ";";
                bigCsv += m.getGustLevel() + ";";
                bigCsv += m.getDewpoint() + ";";
                bigCsv += "\n;";
                
                //etc..etc..
            }
            PrintWriter out = response.getWriter();
            response.setContentType("application/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"result.csv\"");

            out.println(bigCsv);

        } else if (format.equalsIgnoreCase("json")) {

            Gson gson = new Gson();
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setHeader("Content-Disposition", "attachment; filename=\"result.json\"");
            out.println(gson.toJson(meteoLogList));
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
        return "Download Data in Excel Format";
    }// </editor-fold>
}
