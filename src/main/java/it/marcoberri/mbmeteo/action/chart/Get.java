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
package it.marcoberri.mbmeteo.action.chart;

import com.github.jmkgreen.morphia.query.Query;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import it.marcoberri.mbmeteo.helper.ChartEnumHelper;
import it.marcoberri.mbmeteo.helper.MongoConnectionHelper;
import it.marcoberri.mbmeteo.model.Cache;
import it.marcoberri.mbmeteo.model.Meteolog;
import it.marcoberri.mbmeteo.utils.DateTimeUtil;
import it.marcoberri.mbmeteo.utils.Default;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
public class Get extends Base {

    /**
     *
     */
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

        final HashMap<String, String> params = getParams(request.getParameterMap());
        final Integer dimy = Default.toInteger(params.get("dimy"), 600);
        final Integer dimx = Default.toInteger(params.get("dimx"), 800);
        final String from = Default.toString(params.get("from") + " 00:00:00", "1970-01-01 00:00:00");
        final String to = Default.toString(params.get("to") + " 23:59:00", "2030-01-01 23:59:00");
        final String field = Default.toString(params.get("field"), "outdoorTemperature");

        request.getSession().setAttribute("from", params.get("from"));
        request.getSession().setAttribute("to", params.get("to"));

        final String cacheKey = getCacheKey(params);

        if (cacheReadEnable) {
            final Query q = ds.find(Cache.class);
            q.filter("cacheKey", cacheKey).filter("servletName", this.getClass().getName());

            final Cache c = (Cache) q.get();

            if (c == null) {
                log.info("cacheKey:" + cacheKey + " on servletName: " + this.getClass().getName() + " not found");
            }

            if (c != null) {
                log.debug("get file from cache id: " + c.getGridId());
                final GridFSDBFile imageForOutput = MongoConnectionHelper.getGridFS().findOne(new ObjectId(c.getGridId()));
                if (imageForOutput != null) {

                    ds.save(c);

                    try {
                        response.setHeader("Content-Length", "" + imageForOutput.getLength());
                        response.setHeader("Content-Disposition", "inline; filename=\"" + imageForOutput.getFilename() + "\"");
                        final OutputStream out = response.getOutputStream();
                        final InputStream in = imageForOutput.getInputStream();
                        final byte[] content = new byte[(int) imageForOutput.getLength()];
                        in.read(content);
                        out.write(content);
                        in.close();
                        out.close();
                        return;
                    } catch (Exception e) {
                        log.error(e);
                    }

                } else {
                    log.error("file not in db");
                }
            }
        }

        final String titleChart = ChartEnumHelper.getByName(field).getTitle();
        final String umChart = ChartEnumHelper.getByName(field).getUm();
        final Query q = ds.createQuery(Meteolog.class);
        final Date dFrom = DateTimeUtil.getDate("yyyy-MM-dd hh:mm:ss", from);
        final Date dTo = DateTimeUtil.getDate("yyyy-MM-dd hh:mm:ss", to);

        q.disableValidation().filter("time >=", dFrom).filter("time <=", dTo);

        final List<Meteolog> meteoLogList = q.asList();
        final TimeSeries series = new TimeSeries(umChart);

        for (Meteolog m : meteoLogList) {
            final Millisecond t = new Millisecond(m.getTime());
            try {
                //violenza di una reflection
                final Method method = m.getClass().getMethod(ChartEnumHelper.getByName(field).getMethod());
                final Number n = (Number) method.invoke(m);
                series.add(t, n);
            } catch (NoSuchMethodException ex) {
                log.error(ex);
            } catch (InvocationTargetException ex) {
                log.error(ex);
            } catch (IllegalAccessException ex) {
                log.error(ex);
            } catch (SecurityException ex) {
                log.error(ex);
            }

        }



        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        final JFreeChart chart = ChartFactory.createTimeSeriesChart(titleChart, "", umChart, dataset, false, false, false);
        final XYPlot plot = (XYPlot) chart.getPlot();
        final DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("dd-MM-yyyy HH:mm"));
        axis.setVerticalTickLabels(true);
      
        if (field.toUpperCase().indexOf("PRESSURE") != -1) {
            plot.getRangeAxis().setRange(chartPressureMin, chartPressureMax);
        }


        final File f = File.createTempFile("mbmeteo", ".jpg");
        ChartUtilities.saveChartAsJPEG(f, chart, dimx, dimy);

        try {

            if (cacheWriteEnable) {

                final GridFSInputFile gfsFile = MongoConnectionHelper.getGridFS().createFile(f);
                gfsFile.setFilename(f.getName());
                gfsFile.save();

                final Cache c = new Cache();
                c.setServletName(this.getClass().getName());
                c.setCacheKey(cacheKey);
                c.setGridId(gfsFile.getId().toString());

                ds.save(c);

            }

            response.setContentType("image/jpeg");
            response.setHeader("Content-Length", "" + f.length());
            response.setHeader("Content-Disposition", "inline; filename=\"" + f.getName() + "\"");
            final OutputStream out = response.getOutputStream();
            final FileInputStream in = new FileInputStream(f.toString());
            final int size = in.available();
            final byte[] content = new byte[size];
            in.read(content);
            out.write(content);
            in.close();
            out.close();
        } catch (Exception e) {
            log.error(e);
        } finally {
            final boolean delete = f.delete();
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
