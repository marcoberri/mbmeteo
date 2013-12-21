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
public enum ChartEnumMinMaxHelper {

    /**
     *
     */
    outdoorTemperatureMin("outdoorTemperature", "Min", "°C", "getOTMin", "min"),
    /**
     *
     */
    outdoorTemperatureMax("outdoorTemperature", "Max", "°C", "getOTMax", "max"),
    /**
     *
     */
    outdoorHumidityMin("outdoorHumidity", "Min", "%", "getOHMin", "min"),
    /**
     *
     */
    outdoorHumidityMax("outdoorHumidity", "Max", "%", "getOHMax", "max"),
    /**
     *
     */
    relativePressureMin("relativePressure", "Min", "Hpa", "getRPMin", "min"),
    /**
     *
     */
    relativePressureMax("relativePressure", "Max", "Hpa", "getRPMax", "max"),
    /**
     *
     */
    absolutePressureMin("absolutePressure", "Min", "Hpa", "getAPMin", "min"),
    /**
     *
     */
    absolutePressureMax("absolutePressure", "Max", "Hpa", "getAPMax", "max"),
    /**
     *
     */
    windMin("wind", "Max", "m/s", "getWindMin", "min"),
    /**
     *
     */
    windMax("wind", "Min", "m/s", "getWindMax", "max"),
    /**
     *
     */
    windChillMin("windChill", "Max", "m/s", "getWindChillMin", "min"),
    /**
     *
     */
    windChillMax("windChill", "Min", "m/s", "getWindChillMax", "max"),
    /**
     *
     */
    gustdMin("gust", "Max", "m/s", "getGustMin", "min"),
    /**
     *
     */
    gustMax("gust", "Min", "m/s", "getGustMax", "max"),
    /**
     *
     */
    dewpointMin("dewpoint", "Min", "°C", "getDewpointMin", "min"),
    /**
     *
     */
    dewpointMax("dewpoint", "Max", "°C", "getDewpointMax", "max");
    private String name;
    private String title;
    private String um;
    private String method;
    private String type;

    /**
     *
     * @param name
     * @param title
     * @param um
     */
    private ChartEnumMinMaxHelper(String name, String title, String um, String method, String type) {
        this.name = name;
        this.title = title;
        this.um = um;
        this.method = method;
        this.type = type;

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
        s.append(",");
        s.append(getType());
        s.append("]");
        return s.toString();
    }

    /**
     *
     * @param name
     * @param type
     * @return
     */
    public static ChartEnumMinMaxHelper getByFieldAndType(String name, String type) {

        for (ChartEnumMinMaxHelper c : ChartEnumMinMaxHelper.class.getEnumConstants()) {
            if (c.getName().equals(name) && c.getType().equals(type)) {
                return c;
            }
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

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}
