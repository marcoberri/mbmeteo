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

import it.marcoberri.mbmeteo.model.Meteolog;
import it.marcoberri.mbmeteo.utils.Default;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.StandardGradientPaintTransformer;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
public class GetLastUDial extends Base {

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("start : " + this.getClass().getName());

        final HashMap<String, String> params = getParams(request.getParameterMap());
        final Integer dimy = Default.toInteger(params.get("dimy"), 600);
        final Integer dimx = Default.toInteger(params.get("dimx"), 800);


        Meteolog meteolog = ds.find(Meteolog.class).order("-time").limit(1).get();


        DefaultValueDataset dataset = new DefaultValueDataset(meteolog.getOutdoorHumidity());

        // get data for diagrams
        DialPlot plot = new DialPlot();
        plot.setInsets(RectangleInsets.ZERO_INSETS);
       //plot.setView(0.1, 0.1, 0.9, 0.9);
       // plot.set
        plot.setDataset(0, dataset);

        GradientPaint gp = new GradientPaint(new Point(),
                new Color(255, 255, 255), new Point(),
                new Color(170, 170, 220));

       
        DialBackground db = new DialBackground(gp);
        db.setGradientPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.CENTER_HORIZONTAL));
        plot.setBackground(db);


        StandardDialScale scale = new StandardDialScale();
        scale.setLowerBound(0);
        scale.setUpperBound(100);
        scale.setTickLabelOffset(0.14);
        scale.setTickLabelFont(new Font("Dialog", Font.PLAIN, 10));
        plot.addScale(0, scale);
        plot.setInsets( RectangleInsets.ZERO_INSETS);

        DialPointer needle = new DialPointer.Pointer(0);

        plot.addLayer(needle);

        JFreeChart chart1 = new JFreeChart(plot);
        chart1.setTitle("Humidity %");
       
       

        final File f = File.createTempFile("mbmeteo", ".jpg");
        ChartUtilities.saveChartAsJPEG(f, chart1, dimx, dimy);

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
