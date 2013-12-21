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

import it.marcoberri.mbmeteo.helper.ChartEnumMinMaxHelper;
import it.marcoberri.mbmeteo.model.Meteolog;
import it.marcoberri.mbmeteo.utils.DateTimeUtil;
import it.marcoberri.mbmeteo.utils.Default;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
public class GetLastTBar extends Base {

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


        Meteolog meteolog = ds.find(Meteolog.class).order("-time").limit(1).get();

        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(meteolog.getOutdoorTemperature(), ChartEnumMinMaxHelper.outdoorTemperatureMin.getUm(), DateTimeUtil.dateFormat("yyyy-MM-dd hh:mm:ss",meteolog.getTime()));

        final JFreeChart chart = ChartFactory.createBarChart(
                "T°", // chart title
                "", // domain axis label
                "", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                false, // include legend
                false, // tooltips?
                false // URLs?
                );


        final CategoryPlot xyPlot = (CategoryPlot) chart.getPlot();


        xyPlot.getRangeAxis().setRange(0, meteolog.getOutdoorTemperature() + 2);
        final File f = File.createTempFile("mbmeteo", ".jpg");
        ChartUtilities.saveChartAsJPEG(f, chart, dimx, dimy);

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
