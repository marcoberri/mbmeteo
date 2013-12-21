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
import it.marcoberri.mbmeteo.helper.ChartEnumMinMaxHelper;
import it.marcoberri.mbmeteo.helper.MongoConnectionHelper;
import it.marcoberri.mbmeteo.model.Cache;
import it.marcoberri.mbmeteo.model.MapReduceMinMax;
import it.marcoberri.mbmeteo.utils.DateTimeUtil;
import it.marcoberri.mbmeteo.utils.Default;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
public class GetMinOrMax extends Base {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.debug("start : " + this.getClass().getName());

        final HashMap<String, String> params = getParams(request.getParameterMap());
        final Integer dimy = Default.toInteger(params.get("dimy"), 600);
        final Integer dimx = Default.toInteger(params.get("dimx"), 800);
        final String from = Default.toString(params.get("from") + " 00:00:00", "1970-01-01 00:00:00");
        final String to = Default.toString(params.get("to") + " 23:59:00", "2030-01-01 23:59:00");
        final String field = Default.toString(params.get("field"), "outdoorTemperature");
        final String period = Default.toString(params.get("period"), "day");
        final String type = Default.toString(params.get("type"), "min");


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


        final Query q = ds.createQuery(MapReduceMinMax.class).disableValidation();
        final Date dFrom = DateTimeUtil.getDate("yyyy-MM-dd hh:mm:ss", from);
        final Date dTo = DateTimeUtil.getDate("yyyy-MM-dd hh:mm:ss", to);

        final String formatIn = getFormatIn(period);
        final String formatOut = getFormatOut(period);

        final List<Date> datesIn = getRangeDate(dFrom, dTo);
        final HashSet<String> datesInString = new HashSet<String>();

        for (Date d : datesIn) {
            datesInString.add(DateTimeUtil.dateFormat(formatIn, d));
        }

        if (datesIn != null && !datesIn.isEmpty()) {
            q.filter("_id in", datesInString);
        }
        q.order("_id");

        final List<MapReduceMinMax> mapReduceResult = q.asList();

        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ChartEnumMinMaxHelper chartEnum = ChartEnumMinMaxHelper.getByFieldAndType(field, type);

        for (MapReduceMinMax m : mapReduceResult) {
            try {
                final Date tmpDate = DateTimeUtil.getDate(formatIn, m.getId().toString());

                if (tmpDate == null) {
                    continue;
                }

                final Method method = m.getClass().getMethod(chartEnum.getMethod());
                final Number n = (Number) method.invoke(m);
                dataset.addValue(n, chartEnum.getType(), DateTimeUtil.dateFormat(formatOut, tmpDate));


            } catch (IllegalAccessException ex) {
                log.error(ex);
            } catch (IllegalArgumentException ex) {
                log.error(ex);
            } catch (InvocationTargetException ex) {
                log.error(ex);
            } catch (NoSuchMethodException ex) {
                log.error(ex);
            } catch (SecurityException ex) {
                log.error(ex);
            }
        }

        final JFreeChart chart = ChartFactory.createBarChart(
                chartEnum.getTitle(), // chart title
                "", // domain axis label
                chartEnum.getUm(), // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                false, // include legend
                true, // tooltips?
                false // URLs?
                );

        final CategoryPlot xyPlot = (CategoryPlot) chart.getPlot();
        final CategoryAxis domain = xyPlot.getDomainAxis();

         if (field.toUpperCase().indexOf("PRESSURE") != -1) {
              xyPlot.getRangeAxis().setRange(chartPressureMin, chartPressureMax);
        } else {
            xyPlot.getRangeAxis().setAutoRange(true);
        }

        domain.setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);

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
            boolean delete = f.delete();
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


}
