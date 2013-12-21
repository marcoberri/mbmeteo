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
package it.marcoberri.mbmeteo.helper;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
public enum ChartEnumHelper {

    /**
     *
     */
    outdoorTemperature("outdoorTemperature", "Temperature", "°C", "getOutdoorTemperature"),
    /**
     *
     */
    outdoorHumidity("outdoorHumidity", "Humidity", "%","getOutdoorHumidity"),
    /**
     *
     */
    absolutePressure("absolutePressure", "Absolute Pressure", "Hpa","getAbsolutePressure"),
    /**
     *
     */
    wind("wind", "Wind", "m/s","getWind"),
    /**
     *
     */
    gust("gust", "Gust", "m/s","getGust"),
    /**
     *
     */
    relativePressure("relativePressure", "Relative Pressure", "Hpa","getRelativePressure"),
    /**
     *
     */
    dewpoint("dewpoint", "Dewpoint", "°C","getDewpoint"),
    /**
     *
     */
    windchill("windChill", "Wind Chill", "°C","getWindChill"),
    /**
     *
     */
    hourRainfall("hourRainfall", "Hour Rainfall", "mm","getHourRainfall"),
    /**
     *
     */
    dayRainfall("dayRainfall", "Day Rainfall", "mm","getDayRainfall"),
    /**
     *
     */
    weekRainfall("weekRainfall", "Week Rainfall", "mm","getWeekRainfall"),
    /**
     *
     */
    monthRainfall("monthRainfall", "Month Rainfall", "mm","getMonthRainfall"),
    /**
     *
     */
    totalRainfall("totalRainfall", "Total Rainfall", "mm","getTotalRainfall"),
    /**
     *
     */
    windLevel("windLevel", "Wind Level", "btf","getWindLevel"),
    /**
     *
     */
    gustLevel("gustLevel", "Gust Level", "btf","getGustLevel");
    
    
    private String name;
    private String title;
    private String um;
    private String method;

    /**
     *
     * @param name
     * @param title
     * @param um
     */
    private ChartEnumHelper(String name, String title, String um, String method) {
        this.name = name;
        this.title = title;
        this.um = um;
        this.method = method;

    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[");
        s.append(getName());
        s.append(",");
        s.append(getTitle());
        s.append(",");
        s.append(getUm());
        s.append("]");
        return s.toString();
    }

    /**
     *
     * @param name
     * @return
     */
    public static ChartEnumHelper getByName(String name) {
        for (ChartEnumHelper c : ChartEnumHelper.class.getEnumConstants()) {
            if (!c.getName().equals(name)) {
                continue;
            }
            return c;
        }
        return null;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the um
     */
    public String getUm() {
        return um;
    }

    /**
     * @param um the um to set
     */
    public void setUm(String um) {
        this.um = um;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }
}
