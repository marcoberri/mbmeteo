/**
 *  Copyright 2011 Marco Berri - marcoberri@gmail.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 **/
package it.marcoberri.mbmeteo.model;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.Reference;
import com.github.jmkgreen.morphia.utils.IndexDirection;
import com.google.gson.Gson;
import java.util.Date;
import org.bson.types.ObjectId;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
@Entity("Meteolog")
public class Meteolog {

    @Id
    private ObjectId id;
    @Reference
    private Stations station;
    private Integer n;
    @Indexed(value = IndexDirection.ASC, name = "time", unique = true, dropDups = true)
    private Date time;
    private Integer interval;
    private Double indoorHumidity;
    private Double indoorTemperature;
    private Double outdoorHumidity;
    private Double outdoorTemperature;
    private Double absolutePressure;
    private Double wind;
    private Double gust;
    private String direction;
    private Double relativePressure;
    private Double dewpoint;
    private Double windChill;
    private Double windLevel;
    private Double gustLevel;
    private Double rain;
    private Double hourRainfall;
    private Double dayRainfall;
    private Double weekRainfall;
    private Double monthRainfall;
    private Double totalRainfall;
    @Embedded
    private Datainfo datainfo;
    private boolean fixed = false;

    /**
     * @return the id
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(ObjectId id) {
        this.id = id;
    }

    /**
     * @return the station
     */
    public Stations getStation() {
        return station;
    }

    /**
     * @param station the station to set
     */
    public void setStation(Stations station) {
        this.station = station;
    }

    /**
     * @return the n
     */
    public Integer getN() {
        return n;
    }

    /**
     * @param n the n to set
     */
    public void setN(Integer n) {
        this.n = n;
    }

    /**
     * @return the time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * @return the interval
     */
    public Integer getInterval() {
        return interval;
    }

    /**
     * @param interval the interval to set
     */
    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    /**
     * @return the indoorHumidity
     */
    public Double getIndoorHumidity() {
        return indoorHumidity;
    }

    /**
     * @param indoorHumidity the indoorHumidity to set
     */
    public void setIndoorHumidity(Double indoorHumidity) {
        this.indoorHumidity = indoorHumidity;
    }

    /**
     * @return the indoorTemperature
     */
    public Double getIndoorTemperature() {
        return indoorTemperature;
    }

    /**
     * @param indoorTemperature the indoorTemperature to set
     */
    public void setIndoorTemperature(Double indoorTemperature) {
        this.indoorTemperature = indoorTemperature;
    }

    /**
     * @return the outdoorHumidity
     */
    public Double getOutdoorHumidity() {
        return outdoorHumidity;
    }

    /**
     * @param outdoorHumidity the outdoorHumidity to set
     */
    public void setOutdoorHumidity(Double outdoorHumidity) {
        this.outdoorHumidity = outdoorHumidity;
    }

    /**
     * @return the outdoorTemperature
     */
    public Double getOutdoorTemperature() {
        return outdoorTemperature;
    }

    /**
     * @param outdoorTemperature the outdoorTemperature to set
     */
    public void setOutdoorTemperature(Double outdoorTemperature) {
        this.outdoorTemperature = outdoorTemperature;
    }

    /**
     * @return the absolutePressure
     */
    public Double getAbsolutePressure() {
        return absolutePressure;
    }

    /**
     * @param absolutePressure the absolutePressure to set
     */
    public void setAbsolutePressure(Double absolutePressure) {
        this.absolutePressure = absolutePressure;
    }

    /**
     * @return the wind
     */
    public Double getWind() {
        return wind;
    }

    /**
     * @param wind the wind to set
     */
    public void setWind(Double wind) {
        this.wind = wind;
    }

    /**
     * @return the gust
     */
    public Double getGust() {
        return gust;
    }

    /**
     * @param gust the gust to set
     */
    public void setGust(Double gust) {
        this.gust = gust;
    }

    /**
     * @return the direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return the relativePressure
     */
    public Double getRelativePressure() {
        return relativePressure;
    }

    /**
     * @param relativePressure the relativePressure to set
     */
    public void setRelativePressure(Double relativePressure) {
        this.relativePressure = relativePressure;
    }

    /**
     * @return the dewpoint
     */
    public Double getDewpoint() {
        return dewpoint;
    }

    /**
     * @param dewpoint the dewpoint to set
     */
    public void setDewpoint(Double dewpoint) {
        this.dewpoint = dewpoint;
    }

    /**
     * @return the windchill
     */
    public Double getWindChill() {
        return windChill;
    }

    /**
     * @param windChill 
     */
    public void setWindChill(Double windChill) {
        this.windChill = windChill;
    }

    /**
     * @return the hourRainfall
     */
    public Double getHourRainfall() {
        return hourRainfall;
    }

    /**
     * @param hourRainfall the hourRainfall to set
     */
    public void setHourRainfall(Double hourRainfall) {
        this.hourRainfall = hourRainfall;
    }

    /**
     * @return the dayRainfall
     */
    public Double getDayRainfall() {
        return dayRainfall;
    }

    /**
     * @param dayRainfall the dayRainfall to set
     */
    public void setDayRainfall(Double dayRainfall) {
        this.dayRainfall = dayRainfall;
    }

    /**
     * @return the weekRainfall
     */
    public Double getWeekRainfall() {
        return weekRainfall;
    }

    /**
     * @param weekRainfall the weekRainfall to set
     */
    public void setWeekRainfall(Double weekRainfall) {
        this.weekRainfall = weekRainfall;
    }

    /**
     * @return the monthRainfall
     */
    public Double getMonthRainfall() {
        return monthRainfall;
    }

    /**
     * @param monthRainfall the monthRainfall to set
     */
    public void setMonthRainfall(Double monthRainfall) {
        this.monthRainfall = monthRainfall;
    }

    /**
     * @return the totalRainfall
     */
    public Double getTotalRainfall() {
        return totalRainfall;
    }

    /**
     * @param totalRainfall the totalRainfall to set
     */
    public void setTotalRainfall(Double totalRainfall) {
        this.totalRainfall = totalRainfall;
    }

    /**
     * @return the windLevel
     */
    public Double getWindLevel() {
        return windLevel;
    }

    /**
     * @param windLevel the windLevel to set
     */
    public void setWindLevel(Double windLevel) {
        this.windLevel = windLevel;
    }

    /**
     * @return the gustLevel
     */
    public Double getGustLevel() {
        return gustLevel;
    }

    /**
     * @param gustLevel the gustLevel to set
     */
    public void setGustLevel(Double gustLevel) {
        this.gustLevel = gustLevel;
    }

    /**
     * @return the datainfo
     */
    public Datainfo getDatainfo() {
        return datainfo;
    }

    /**
     * @param datainfo the datainfo to set
     */
    public void setDatainfo(Datainfo datainfo) {
        this.datainfo = datainfo;
    }

    /**
     *
     * @return
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * @return the rain
     */
    public Double getRain() {
        return rain;
    }

    /**
     * @param rain the rain to set
     */
    public void setRain(Double rain) {
        this.rain = rain;
    }

    /**
     * @return the fixed
     */
    public boolean isFixed() {
        return fixed;
    }

    /**
     * @param fixed the fixed to set
     */
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
}
