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
package it.marcoberri.mbmeteo.utils;

import org.apache.log4j.*;
import org.apache.log4j.spi.*;

/**
 * @author Marco Berri marcoberri@gmail.com
 */
public class Log4j extends Logger {

    /**
     *
     */
    public static final String ROTATE_DAILY = "daily";
    /**
     *
     */
    public static final String ROTATE_ROLLING = "rolling";
    /**
     *
     */
    public static final String MAX_FILE_SIZE = "20MB";

    static class AccessLoggerFactory implements LoggerFactory {


        public Logger makeNewLoggerInstance(String name, Level level) {
            Logger logger = new Log4j(name);

            if(level == null)
                logger.setLevel(Level.DEBUG);
            else
                logger.setLevel(level);
            return logger;
        }//makeNewLoggerInstance()

        public Logger makeNewLoggerInstance(String name) {
            return  makeNewLoggerInstance(name,null);
        }//makeNewLoggerInstance()

    }//AccessLoggerFactory

    /**
     *
     */
    protected static AccessLoggerFactory factory = new AccessLoggerFactory();

    /**
     * Salva il logger
     *
     * @param name nome del log
     * @param path la path del log
     * @param pattern la sintassi del log default "%m%n"
     * @param encoding encoding del file di log default "UTF-8"
     * @param appString la stringa di sintassi per log4j vedi default = "'.'yyyy-MM-dd" vedi: http://logging.apache.org/log4j/docs/api/org/apache/log4j/DailyRollingFileAppender.html
     * @param type
     * @return
     */
    public static Logger getLogger(String name, String path, String pattern, String encoding, String appString, String type) {

        //if (path == null)
        //  path = ""IOUtil.absolutePath("logs");

        if (pattern == null) {
            pattern = "[%d] %-5p - %m%n";
        }

        if (encoding == null) {
            encoding = "UTF-8";
        }

        if (name == null) {
            return null;
        }

        String app_name = name.toLowerCase().replaceAll("[^a-z0-1]+", "_");

        Logger logger = Logger.getLogger(app_name, factory);

        if (logger == null) {
            return null;
        }



        if (logger.getAppender(app_name) == null) {
            try {

                if (appString == null) {
                    appString = "'.'yyyy-MM-dd";
                }

                if (type == null || type.equals(ROTATE_ROLLING)) {

                    //RollingFileAppender fa = new RollingFileAppender(new PatternLayout(pattern), IOUtil.joinPaths(path,name+".log"));
                    RollingFileAppender fa = new RollingFileAppender(new PatternLayout(pattern), IOUtil.normalizedPath(path + name + ".log"));
                    fa.setMaxFileSize(MAX_FILE_SIZE);
                    fa.setMaxBackupIndex(1000000);
                    fa.setLayout(new PatternLayout(pattern));
                    fa.setName(app_name);
                    fa.setEncoding(encoding);
                    fa.activateOptions();
                    logger.addAppender(fa);


                } else if (type.equals(ROTATE_DAILY)) {
                    DailyRollingFileAppender fa = new DailyRollingFileAppender(new PatternLayout(pattern), path + name + ".log", appString);
                    fa.setName(app_name);
                    fa.setEncoding(encoding);
                    fa.setLayout(new PatternLayout(pattern));
                    fa.activateOptions();
                    logger.addAppender(fa);

              
                } else {
                    return null;
                }//if()


            } catch (Exception x) {
                Logger log = Logger.getLogger(Log4j.class);
                log.error("", x);
            }
        }//if
        return logger;

    }//getLogger()

    /**
     *
     * @param name
     * @param path
     * @param type
     * @return
     */
    public static Logger getLogger(String name, String path, String type) {
        return getLogger(name, path, null, null, null, type);
    }//getLogger()

    /**
     *
     * @param name
     * @param type
     * @return
     */
    public static Logger getLogger(String name, String type) {
        return getLogger(name, null, null, null, null, type);
    }//getLogger()

    /**
     *
     * @param name
     */
    protected Log4j(String name) {
        super(name);
    }//AccessLogger()

    /**
     *
     * @param name
     * @return
     */
    public static Logger getLogger(String name) {
        return Log4j.getLogger(name, "/var/log/log/", ROTATE_DAILY);
    }

    /**
     *
     * @param path
     * @param pattern
     * @param encoding
     * @param clazz
     * @param type
     * @return
     */
    public static Logger getLoggerClass(String path, String pattern, String encoding, String clazz, String type) {

        Logger logger = Logger.getLogger(clazz);

        if (logger == null) {
            return null;
        }

        if (pattern == null) {
            pattern = "[%d] %-5p - %m%n";
        }




        if (logger.getAppender(clazz) == null) {
            try {

                if (type == null || type.equals(ROTATE_ROLLING)) {

                    //RollingFileAppender fa = new RollingFileAppender(new PatternLayout(pattern), IOUtil.joinPaths(path,name+".log"));
                    RollingFileAppender fa = new RollingFileAppender(new PatternLayout(pattern), IOUtil.normalizedPath(path));
                    fa.setMaxFileSize(MAX_FILE_SIZE);
                    fa.setMaxBackupIndex(1000000);
                    fa.setLayout(new PatternLayout(pattern));
                    fa.setName(clazz);
                    fa.setEncoding(encoding);
                    fa.activateOptions();
                    logger.addAppender(fa);


                } else if (type.equals(ROTATE_DAILY)) {
                    DailyRollingFileAppender fa = new DailyRollingFileAppender(new PatternLayout(pattern), path, "'.'yyyy-MM-dd");
                    fa.setName(clazz);
                    fa.setEncoding(encoding);
                    fa.setLayout(new PatternLayout(pattern));
                    fa.activateOptions();
                    logger.addAppender(fa);
                } else {
                    return null;
                }//if()


            } catch (Exception x) {
                Logger log = Logger.getLogger(Log4j.class);
                log.error("", x);
            }
        }//if
        return logger;

    }//getLogger()

    /**
     * 
     * @param path
     * @param clazz
     * @return
     */
    public static Logger getLoggerClass(String path, String clazz) {
        return Log4j.getLoggerClass(path, null, "UTF8", clazz, ROTATE_DAILY);
    }
}//Log4j

