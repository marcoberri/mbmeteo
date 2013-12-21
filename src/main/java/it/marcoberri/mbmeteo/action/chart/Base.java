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

import com.github.jmkgreen.morphia.Datastore;
import it.marcoberri.mbmeteo.helper.ConfigurationHelper;
import it.marcoberri.mbmeteo.helper.MongoConnectionHelper;
import it.marcoberri.mbmeteo.utils.Default;
import it.marcoberri.mbmeteo.utils.Log4j;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
public class Base extends HttpServlet {

    final org.apache.log4j.Logger log = Log4j.getLogger("mbmeteo", ConfigurationHelper.prop.getProperty("log.path"), Log4j.ROTATE_DAILY);
    final boolean cacheReadEnable = Default.toBoolean(ConfigurationHelper.prop.getProperty("chart.cache.read"), Boolean.FALSE);
    final boolean cacheWriteEnable = Default.toBoolean(ConfigurationHelper.prop.getProperty("chart.cache.write"), Boolean.FALSE);
    final int chartPressureMin = Default.toInt(ConfigurationHelper.prop.getProperty("chart.pressure.min"),800);
    final int chartPressureMax = Default.toInt(ConfigurationHelper.prop.getProperty("chart.pressure.max"),1200);
    final Datastore ds = MongoConnectionHelper.ds;

    /**
     *
     * @param from
     * @param to
     * @return
     */
    protected List<Date> getRangeDate(Date from, Date to) {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(from);

        while (calendar.getTime().before(to)) {
            Date resultado = calendar.getTime();
            dates.add(resultado);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    /**
     *
     * @param p
     * @return
     */
    protected HashMap<String, String> getParams(Map<String, Object> p) {
        final HashMap<String, String> param = new HashMap<String, String>();
        for (String k : p.keySet()) {
            param.put(k, ((String[]) p.get(k))[0]);
        }
        return param;
    }

    /**
     *
     * @param params
     * @return
     */
    protected String getCacheKey(HashMap<String, String> params) {
        try {
            final StringBuilder s = new StringBuilder();
            for (String k : params.keySet()) {
                s.append(k);
                s.append(params.get(k));
            }

            return DigestUtils.shaHex(s.toString());
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    /**
     *
     * @param period
     * @return
     */
    protected String getFormatIn(String period) {

        final String formatIn = "yyyy-MM-dd";


        if (period.equals("month")) {
            return "yyyy-MM";

        } else if (period.equals("year")) {
            return "yyyy";

        }

        return formatIn;
    }

    /**
     *
     * @param period
     * @return
     */
    protected String getFormatOut(String period) {


        final String formatOut = "dd-MM-yyyy";

        if (period.equals("month")) {

            return "MM-yyyy";
        } else if (period.equals("year")) {

            return "yyyy";
        }

        return formatOut;
    }
    
     

}
