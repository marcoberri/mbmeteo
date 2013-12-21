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
import com.github.jmkgreen.morphia.MapreduceResults;
import com.github.jmkgreen.morphia.MapreduceType;
import com.mongodb.WriteConcern;
import it.marcoberri.mbmeteo.helper.ConfigurationHelper;
import it.marcoberri.mbmeteo.helper.MongoConnectionHelper;
import it.marcoberri.mbmeteo.model.Cache;
import it.marcoberri.mbmeteo.model.MapReduceHistoryMinMax;
import it.marcoberri.mbmeteo.model.MapReduceMinMax;
import it.marcoberri.mbmeteo.model.Meteolog;
import it.marcoberri.mbmeteo.utils.Default;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
public class Commons {

    public static void importLogPywws(Logger log) {
        try {

            final File importPath = new File(ConfigurationHelper.prop.getProperty("import.loggerPywws.filepath"));
            FileUtils.forceMkdir(importPath);
            final File importPathBackup = new File(ConfigurationHelper.prop.getProperty("import.loggerPywws.filepath") + File.separator + "old" + File.separator);
            FileUtils.forceMkdir(importPathBackup);
            boolean hasHeader = Default.toBoolean(ConfigurationHelper.prop.getProperty("import.loggerPywws.hasHeader"), false);

            final String[] extension = {"csv", "txt"};
            Collection<File> files = FileUtils.listFiles(importPath, extension, false);

            if (files.isEmpty()) {
                log.debug("No file to inport: " + importPath);
                return;
            }


            for (File f : files) {
                log.debug("read file:" + f);

                final List<String> l = FileUtils.readLines(f, "UTF-8");
                log.debug("tot line:" + l.size());

            }

        } catch (Exception ex) {
            log.fatal(ex);


        }
    }

    /**
     *
     * @param log
     */
    public static void importLogEasyWeather(Logger log) {

        try {

            final File importPath = new File(ConfigurationHelper.prop.getProperty("import.loggerEasyWeather.filepath"));
            FileUtils.forceMkdir(importPath);
            final File importPathBackup = new File(ConfigurationHelper.prop.getProperty("import.loggerEasyWeather.filepath") + File.separator + "old" + File.separator);
            FileUtils.forceMkdir(importPathBackup);
            boolean hasHeader = Default.toBoolean(ConfigurationHelper.prop.getProperty("import.loggerEasyWeather.hasHeader"), true);

            final String[] extension = {"csv", "txt"};
            Collection<File> files = FileUtils.listFiles(importPath, extension, false);

            if (files.isEmpty()) {
                log.debug("No file to inport: " + importPath);
                return;
            }


            for (File f : files) {

                log.debug("read file:" + f);

                final List<String> l = FileUtils.readLines(f, "UTF-8");
                log.debug("tot line:" + l.size());


                final Datastore ds = MongoConnectionHelper.ds;

                log.debug("hasHeader: " + hasHeader);
                int lineCount = 0;
                int lineDouble = 0;
                for (String s : l) {

                    if (hasHeader) {
                        hasHeader = false;
                        continue;
                    }

                    final String[] columns = s.split(";");
                    List<Meteolog> check = ds.createQuery(Meteolog.class).field("time").equal(getDate("dd-MM-yyyy HH:mm", columns[1], log)).asList();

                    if (check != null && !check.isEmpty()) {
                        log.debug("data exist, continue");
                        lineDouble++;
                        continue;
                    }
                    
                    final Meteolog meteoLog = new Meteolog();
                    try {
                        meteoLog.setN(Integer.valueOf(columns[0]));
                    } catch (Exception e) {
                        log.error("line skipped " + lineCount, e);
                        continue;
                    }

                    try {
                        meteoLog.setTime(getDate("dd-MM-yyyy HH:mm", columns[1], log));
                    } catch (Exception e) {
                        log.error("line skipped" + lineCount, e);
                        continue;
                    }


                    try {
                        meteoLog.setInterval(Integer.valueOf(columns[2]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setIndoorHumidity(Double.valueOf(columns[3]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setIndoorTemperature(Double.valueOf(columns[4]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setOutdoorHumidity(Double.valueOf(columns[5]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setOutdoorTemperature(Double.valueOf(columns[6]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setAbsolutePressure(Double.valueOf(columns[7]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setWind(Double.valueOf(columns[8]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setGust(Double.valueOf(columns[9]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setDirection(columns[10]);
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setRelativePressure(Double.valueOf(columns[11]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setDewpoint(Double.valueOf(columns[12]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setWindChill(Double.valueOf(columns[13]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setHourRainfall(Double.valueOf(columns[14]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setDayRainfall(Double.valueOf(columns[15]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setWeekRainfall(Double.valueOf(columns[16]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setMonthRainfall(Double.valueOf(columns[17]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setTotalRainfall(Double.valueOf(columns[18]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setWindLevel(Double.valueOf(columns[19]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }


                    try {
                        meteoLog.setGustLevel(Double.valueOf(columns[20]));
                    } catch (Exception e) {
                        log.error("line " + lineCount, e);
                    }

                    ds.save(meteoLog, WriteConcern.NORMAL);
                    lineCount++;

                }

                log.debug("Tot line insert:" + lineCount);
                log.debug("Tot line scarted:" + lineDouble);

                //move file to backup dir with rename
                final File toMove = new File(importPathBackup + "/" + f.getName() + "_" + System.currentTimeMillis());

                log.debug("Move File to Backup dir" + toMove);
                f.renameTo(toMove);

            }
        } catch (IOException ex) {
            log.fatal(ex);

        }
    }

    /**
     *
     * @param format
     * @param date
     * @param log
     * @return
     */
    protected static java.util.Date getDate(String format, String date, Logger log) {
        final String s = date;

        final SimpleDateFormat sdf = new SimpleDateFormat(format);

        try {
            return new java.util.Date(sdf.parse(s).getTime());
        } catch (ParseException e) {
            log.error(e);
        }
        return null;
    }//getTimestamp()

    /*----------------- mapReduce ---------------*/
    /**
     *
     * @param log
     */
    public static void mapReduce(Logger log) {

        final String reduce = getJsFromFile("/mapreduce/minmax/reduce.txt", log);
        runMapReduceMinMax("/mapreduce/minmax/mapday.txt", reduce, log);
        runMapReduceMinMax("/mapreduce/minmax/mapmonth.txt", reduce, log);
        runMapReduceMinMax("/mapreduce/minmax/mapyear.txt", reduce, log);

        final String reduce2 = getJsFromFile("/mapreduce/historyminmax/reduce.txt", log);
        runMapReduceHisotryMinMax("/mapreduce/historyminmax/map.txt", reduce2, log);

    }

    /**
     *
     * @param file
     * @param reduce
     * @param log
     */
    protected static void runMapReduceMinMax(String file, String reduce, Logger log) {

        final Datastore ds = MongoConnectionHelper.ds;

        final String map = getJsFromFile(file, log);
        final MapreduceResults<MapReduceMinMax> mrRes = ds.mapReduce(MapreduceType.MERGE, ds.createQuery(Meteolog.class), map, reduce, null, null, MapReduceMinMax.class);

        log.debug(
                "mrRes --> count all:" + mrRes.createQuery().countAll());
        log.debug(
                "mrRes --> tot time: " + mrRes.getElapsedMillis());
    }

    /**
     *
     * @param file
     * @param reduce
     * @param log
     */
    protected static void runMapReduceHisotryMinMax(String file, String reduce, Logger log) {

        final Datastore ds = MongoConnectionHelper.ds;

        final String map = getJsFromFile(file, log);
        final MapreduceResults<MapReduceHistoryMinMax> mrRes = ds.mapReduce(MapreduceType.MERGE, ds.createQuery(Meteolog.class), map, reduce, null, null, MapReduceHistoryMinMax.class);

        log.debug("mrRes --> count all:" + mrRes.createQuery().countAll());
        log.debug("mrRes --> tot time: " + mrRes.getElapsedMillis());
    }

    /**
     *
     * @param fileName
     * @param log
     * @return
     */
    protected static String getJsFromFile(String fileName, Logger log) {
        try {
            final URL main = log.getClass().getProtectionDomain().getCodeSource().getLocation();
            final String path = URLDecoder.decode(main.getPath(), "utf-8");
            final String webInfFolderPosition = new File(path).getPath();
            final String webInfFolder = webInfFolderPosition.substring(0, webInfFolderPosition.indexOf("classes"));
            return FileUtils.readFileToString(new File(webInfFolder + File.separator + fileName));
        } catch (IOException ex) {
            log.error(ex);
        }
        return null;
    }

    /*----------------- deleteCache ---------------*/
    /**
     *
     * @param log
     */
    public static void deleteCache(Logger log) {
        final Datastore ds = MongoConnectionHelper.ds;
        ds.getCollection(Cache.class).drop();
        MongoConnectionHelper.getGridCol().drop();
    }

    public static TreeMap<String, Date> getDistinctTime() {

        final Datastore ds = MongoConnectionHelper.ds;
        final List<Date> datesResult = ds.getDB().getCollection("Meteolog").distinct("time");
        final TreeMap<String, Date> dates = new TreeMap<String, Date>();

        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        for (Date d : datesResult) {
            try {
                dates.put(df.format(d), d);
            } catch (Exception e) {

                continue;
            }
        }

        return dates;
    }
}
