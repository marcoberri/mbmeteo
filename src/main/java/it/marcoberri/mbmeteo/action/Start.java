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
import it.marcoberri.mbmeteo.helper.ConfigurationHelper;
import it.marcoberri.mbmeteo.helper.MongoConnectionHelper;
import it.marcoberri.mbmeteo.model.MapReduceHistoryMinMax;
import it.marcoberri.mbmeteo.model.Meteolog;
import it.marcoberri.mbmeteo.utils.DateTimeUtil;
import it.marcoberri.mbmeteo.utils.Log4j;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
public class Start extends HttpServlet {

    /**
     *
     */
    protected final org.apache.log4j.Logger log = Log4j.getLogger("mbmeteo", ConfigurationHelper.prop.getProperty("log.path"), Log4j.ROTATE_DAILY);

    /**
     *
     */
    @Override
    public void destroy() {
        MongoConnectionHelper.ds.getMongo().close();
    }

    /**
     *
     * @param cfg
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init(ServletConfig cfg) throws javax.servlet.ServletException {

        log.debug("start : " + this.getClass().getName());

        super.init(cfg);

        final ServletContext application = getServletConfig().getServletContext();

        final Datastore ds = MongoConnectionHelper.ds;

        //Get History Values
        final MapReduceHistoryMinMax datesHisotry = ds.find(MapReduceHistoryMinMax.class).get();
        if (datesHisotry != null) {
            application.setAttribute("history", datesHisotry);
        }

        final List<Date> datesResult = ds.getDB().getCollection("Meteolog").distinct("time");

        application.setAttribute("totRecord", ds.find(Meteolog.class).countAll());
        
        if (datesResult == null || datesResult.isEmpty()) {
            application.setAttribute("from", DateTimeUtil.getDate("yyyy-MM-dd", "1970-01-01"));
            application.setAttribute("to", DateTimeUtil.getDate("yyyy-MM-dd", "2030-01-01"));
            application.setAttribute("lastUpdate", DateTimeUtil.getDate("yyyy-MM-dd", "1970-01-01"));
            application.setAttribute("activeFrom", new Date());
            
            return;
        }


        final TreeMap<String, Date> dates = new TreeMap<String, Date>();
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        for (Date d : datesResult) {
            try {
                dates.put(df.format(d), d);
            } catch (Exception e) {
                log.error("error on data : " + d, e);
                continue;
            }
        }

        final Date to = (Date) dates.lastEntry().getValue();
        
        final Calendar cal = Calendar.getInstance();
        cal.setTime(to);
        cal.add(Calendar.DAY_OF_WEEK, -7);
        final Date from = cal.getTime(); 

        
        application.setAttribute("from", from);
        application.setAttribute("to", to);
        application.setAttribute("lastUpdate", to);
        application.setAttribute("activeFrom", (Date) dates.firstEntry().getValue());

    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Start Servlet";
    }// </editor-fold>
}
