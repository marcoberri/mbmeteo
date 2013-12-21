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
package it.marcoberri.mbmeteo.model;

import com.github.jmkgreen.morphia.annotations.CappedAt;
import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.utils.IndexDirection;
import com.google.gson.Gson;
import java.util.Date;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
@Entity(cap =
@CappedAt(1))
public class MapReduceHistoryMinMax {

    @Id
    @Indexed(value = IndexDirection.ASC, name = "id", unique = true, dropDups = true)
    private String id;
    @Embedded
    private Value value;

    /**
     *
     * @return
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the value
     */
    public Value getValue() {
        return value;
    }

    /**
     *
     * @return
     */
    public Double getOTMin() {
        return value.getOutdoorTemperature().getMin();
    }

    /**
     *
     * @return
     */
    public Date getOTMinTime() {
        return value.getOutdoorTemperature().getMintime();
    }

    /**
     *
     * @return
     */
    public Double getOTMax() {
        return value.getOutdoorTemperature().getMax();
    }

    /**
     *
     * @return
     */
    public Date getOTMaxTime() {
        return value.getOutdoorTemperature().getMaxtime();
    }

    /**
     *
     * @return
     */
    public Double getOHMin() {
        return value.getOutdoorHumidity().getMin();
    }

    /**
     *
     * @return
     */
    public Date getOHMinTime() {
        return value.getOutdoorHumidity().getMintime();
    }

    /**
     *
     * @return
     */
    public Double getOHMax() {
        return value.getOutdoorHumidity().getMax();
    }

    /**
     *
     * @return
     */
    public Date getOHMaxTime() {
        return value.getOutdoorHumidity().getMaxtime();
    }

    /**
     *
     * @return
     */
    public Double getAPMin() {
        return value.getAbsolutePressure().getMin();
    }

    /**
     *
     * @return
     */
    public Date getAPMinTime() {
        return value.getAbsolutePressure().getMintime();
    }

    /**
     *
     * @return
     */
    public Double getAPMax() {
        return value.getAbsolutePressure().getMax();
    }

    /**
     *
     * @return
     */
    public Date getAPMaxTime() {
        return value.getAbsolutePressure().getMaxtime();
    }

    /**
     *
     * @return
     */
    public Double getRPMin() {
        return value.getRelativePressure().getMin();
    }

    /**
     *
     * @return
     */
    public Date getRPMinTime() {
        return value.getRelativePressure().getMintime();
    }

    /**
     *
     * @return
     */
    public Double getRPMax() {
        return value.getRelativePressure().getMax();
    }

    /**
     *
     * @return
     */
    public Date getRPMaxTime() {
        return value.getRelativePressure().getMaxtime();
    }

    /**
     *
     * @return
     */
    public Double getWindMin() {
        return value.getWind().getMin();
    }

    /**
     *
     * @return
     */
    public Date getWindMinTime() {
        return value.getWind().getMintime();
    }

    /**
     *
     * @return
     */
    public Double getWindMax() {
        return value.getWind().getMax();
    }

    /**
     *
     * @return
     */
    public Date getWindMaxTime() {
        return value.getWind().getMaxtime();
    }

    /**
     *
     * @return
     */
    public Double getWindChillMin() {
        return value.getWindChill().getMin();
    }

    /**
     *
     * @return
     */
    public Date getWindChillMinTime() {
        return value.getWindChill().getMintime();
    }

    /**
     *
     * @return
     */
    public Double getWindChillMax() {
        return value.getWindChill().getMax();
    }

    /**
     *
     * @return
     */
    public Date getWindChillMaxTime() {
        return value.getWindChill().getMaxtime();
    }

    /**
     *
     * @return
     */
    public Double getGustMin() {
        return value.getGust().getMin();
    }

    /**
     *
     * @return
     */
    public Date getGustMinTime() {
        return value.getGust().getMintime();
    }

    /**
     *
     * @return
     */
    public Double getGustMax() {
        return value.getGust().getMax();
    }

    /**
     *
     * @return
     */
    public Date getGustMaxTime() {
        return value.getGust().getMaxtime();
    }

    /**
     *
     * @return
     */
    public Double getDewpointMin() {
        return value.getDewpoint().getMin();
    }

    /**
     *
     * @return
     */
    public Date getDewpointMinTime() {
        return value.getDewpoint().getMintime();
    }

    /**
     *
     * @return
     */
    public Double getDewpointMax() {
        return value.getDewpoint().getMax();
    }

    /**
     *
     * @return
     */
    public Date getDewpointMaxTime() {
        return value.getDewpoint().getMaxtime();
    }

    /**
     * @param value the value to set
     */
    public void setValue(Value value) {
        this.value = value;
    }

    static class Value {

        private MinAndMax outdoorTemperature;
        private MinAndMax outdoorHumidity;
        private MinAndMax absolutePressure;
        private MinAndMax relativePressure;
        private MinAndMax wind;
        private MinAndMax gust;
        private MinAndMax dewpoint;
        private MinAndMax windChill;
        private MinAndMax hourRainfall;
        private MinAndMax dayRainfall;
        private MinAndMax monthRainfall;
        private MinAndMax totalRainfall;
        private MinAndMax windLevel;
        private MinAndMax gustLevel;

        /**
         * @return the outdoorTemperature
         */
        public MinAndMax getOutdoorTemperature() {
            return outdoorTemperature;
        }

        /**
         * @param outdoorTemperature the outdoorTemperature to set
         */
        public void setOutdoorTemperature(MinAndMax outdoorTemperature) {
            this.outdoorTemperature = outdoorTemperature;
        }

        /**
         * @return the outdoorHumidity
         */
        public MinAndMax getOutdoorHumidity() {
            return outdoorHumidity;
        }

        /**
         * @param outdoorHumidity the outdoorHumidity to set
         */
        public void setOutdoorHumidity(MinAndMax outdoorHumidity) {
            this.outdoorHumidity = outdoorHumidity;
        }

        /**
         * @return the absolutePressure
         */
        public MinAndMax getAbsolutePressure() {
            return absolutePressure;
        }

        /**
         * @param absolutePressure the absolutePressure to set
         */
        public void setAbsolutePressure(MinAndMax absolutePressure) {
            this.absolutePressure = absolutePressure;
        }

        /**
         * @return the relativePressure
         */
        public MinAndMax getRelativePressure() {
            return relativePressure;
        }

        /**
         * @param relativePressure the relativePressure to set
         */
        public void setRelativePressure(MinAndMax relativePressure) {
            this.relativePressure = relativePressure;
        }

        /**
         * @return the wind
         */
        public MinAndMax getWind() {
            return wind;
        }

        /**
         * @param wind the wind to set
         */
        public void setWind(MinAndMax wind) {
            this.wind = wind;
        }

        /**
         * @return the gust
         */
        public MinAndMax getGust() {
            return gust;
        }

        /**
         * @param gust the gust to set
         */
        public void setGust(MinAndMax gust) {
            this.gust = gust;
        }

        /**
         * @return the dewpoint
         */
        public MinAndMax getDewpoint() {
            return dewpoint;
        }

        /**
         * @param dewpoint the dewpoint to set
         */
        public void setDewpoint(MinAndMax dewpoint) {
            this.dewpoint = dewpoint;
        }

        /**
         * @return the windchill
         */
        public MinAndMax getWindChill() {
            return windChill;
        }

        /**
         * @param windchill the windchill to set
         */
        public void setWindChill(MinAndMax windChill) {
            this.windChill = windChill;
        }

        /**
         * @return the hourRainfall
         */
        public MinAndMax getHourRainfall() {
            return hourRainfall;
        }

        /**
         * @param hourRainfall the hourRainfall to set
         */
        public void setHourRainfall(MinAndMax hourRainfall) {
            this.hourRainfall = hourRainfall;
        }

        /**
         * @return the dayRainfall
         */
        public MinAndMax getDayRainfall() {
            return dayRainfall;
        }

        /**
         * @param dayRainfall the dayRainfall to set
         */
        public void setDayRainfall(MinAndMax dayRainfall) {
            this.dayRainfall = dayRainfall;
        }

        /**
         * @return the monthRainfall
         */
        public MinAndMax getMonthRainfall() {
            return monthRainfall;
        }

        /**
         * @param monthRainfall the monthRainfall to set
         */
        public void setMonthRainfall(MinAndMax monthRainfall) {
            this.monthRainfall = monthRainfall;
        }

        /**
         * @return the totalRainfall
         */
        public MinAndMax getTotalRainfall() {
            return totalRainfall;
        }

        /**
         * @param totalRainfall the totalRainfall to set
         */
        public void setTotalRainfall(MinAndMax totalRainfall) {
            this.totalRainfall = totalRainfall;
        }

        /**
         * @return the windLevel
         */
        public MinAndMax getWindLevel() {
            return windLevel;
        }

        /**
         * @param windLevel the windLevel to set
         */
        public void setWindLevel(MinAndMax windLevel) {
            this.windLevel = windLevel;
        }

        /**
         * @return the gustLevel
         */
        public MinAndMax getGustLevel() {
            return gustLevel;
        }

        /**
         * @param gustLevel the gustLevel to set
         */
        public void setGustLevel(MinAndMax gustLevel) {
            this.gustLevel = gustLevel;
        }
    }

    static class MinAndMax {

        private Double min;
        private Date mintime;
        private Double max;
        private Date maxtime;

        /**
         * @return the min
         */
        public Double getMin() {
            return min;
        }

        /**
         * @param min the min to set
         */
        public void setMin(Double min) {
            this.min = min;
        }

        /**
         * @return the max
         */
        public Double getMax() {
            return max;
        }

        /**
         * @param max the max to set
         */
        public void setMax(Double max) {
            this.max = max;
        }

        /**
         * @return the mintime
         */
        public Date getMintime() {
            return mintime;
        }

        /**
         * @param mintime the mintime to set
         */
        public void setMintime(Date mintime) {
            this.mintime = mintime;
        }

        /**
         * @return the maxtime
         */
        public Date getMaxtime() {
            return maxtime;
        }

        /**
         * @param maxtime the maxtime to set
         */
        public void setMaxtime(Date maxtime) {
            this.maxtime = maxtime;
        }
    }
}
