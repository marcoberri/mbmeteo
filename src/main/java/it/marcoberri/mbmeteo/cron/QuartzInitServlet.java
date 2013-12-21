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
package it.marcoberri.mbmeteo.cron;

import it.marcoberri.mbmeteo.cron.action.QuartzClearCache;
import it.marcoberri.mbmeteo.cron.action.QuartzImportLogEasyWeather;
import it.marcoberri.mbmeteo.cron.action.QuartzImportLogPywws;
import it.marcoberri.mbmeteo.cron.action.QuartzMapReduce;
import it.marcoberri.mbmeteo.helper.ConfigurationHelper;
import it.marcoberri.mbmeteo.utils.Default;
import it.marcoberri.mbmeteo.utils.Log4j;
import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
public class QuartzInitServlet extends HttpServlet {

    /**
     *
     */
    public static final String QUARTZ_FACTORY_KEY = "org.quartz.impl.StdSchedulerFactory.KEY";
    private boolean performShutdown = true;
    private Scheduler scheduler = null;
    /**
     *
     */
    protected ServletContext application;
    /**
     *
     */
    protected final static org.apache.log4j.Logger log = Log4j.getLogger("mbmeteo", ConfigurationHelper.prop.getProperty("log.path"), Log4j.ROTATE_DAILY);

    /**
     *
     * @param cfg
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init(ServletConfig cfg) throws javax.servlet.ServletException {
        try {
            super.init(cfg);
            this.application = cfg.getServletContext();

            StdSchedulerFactory schedulerFactory = null;
            final String shutdownPref = cfg.getInitParameter("shutdown-on-unload");

            if (shutdownPref != null) {
                performShutdown = Boolean.valueOf(shutdownPref).booleanValue();
            }

            final Properties prop = new Properties();
            prop.setProperty("org.quartz.scheduler.instanceName", "OsQuarz_mbmeteo");
            prop.setProperty("org.quartz.scheduler.instanceId", "" + System.nanoTime());
            prop.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
            prop.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
            prop.setProperty("org.quartz.threadPool.threadCount", "1");
            log.debug("prop:" + prop);

            try {
                schedulerFactory = new StdSchedulerFactory(prop);
            } catch (SchedulerException ex) {
                log.error(ex);

                return;
            }

            scheduler = schedulerFactory.getScheduler();
            try {

                final String cronImportEW = Default.toString(ConfigurationHelper.prop.getProperty("import.loggerEasyWeather.cron"), "0 0/5 * * * ?");
                final JobDetail jobImport = new JobDetail("job_check_log", "group_check_log", QuartzImportLogEasyWeather.class);
                final Trigger triggerImport = new CronTrigger("trigger_check_log", "group_check_log", "job_check_log", "group_check_log", cronImportEW);
                scheduler.scheduleJob(jobImport, triggerImport);


                final String cronImportPW = Default.toString(ConfigurationHelper.prop.getProperty("import.loggerPywws.cron"), "0 0/5 * * * ?");
                final JobDetail jobImport2 = new JobDetail("job_check_log_pw", "group_check_log_pw", QuartzImportLogPywws.class);
                final Trigger triggerImport2 = new CronTrigger("trigger_check_log_pw", "group_check_log_pw", "job_check_log_pw", "group_check_log_pw", cronImportPW);
                scheduler.scheduleJob(jobImport2, triggerImport2);

                final String cronReduce = Default.toString(ConfigurationHelper.prop.getProperty("action.reduce.cron"), "0 0/5 * * * ?");
                final JobDetail jobReduce = new JobDetail("job_reduce", "group_reduce", QuartzMapReduce.class);
                final Trigger triggerReduce = new CronTrigger("trigger_reduce", "group_reduce", "job_reduce", "group_reduce", cronReduce);
                scheduler.scheduleJob(jobReduce, triggerReduce);


                final String cronCleanCache = Default.toString(ConfigurationHelper.prop.getProperty("action.clearcache.cron"), "0 0 23 1 * ?");
                final JobDetail jobCleanCache = new JobDetail("job_CleanCache", "group_CleanCache", QuartzClearCache.class);
                final Trigger triggerCleanCache = new CronTrigger("trigger_CleanCache", "group_CleanCache", "job_CleanCache", "group_CleanCache", cronCleanCache);
                scheduler.scheduleJob(jobCleanCache, triggerCleanCache);


            } catch (ParseException ex) {
                log.error("Fatal config Job", ex);
                return;
            }

            scheduler.start();
            String factoryKey = QUARTZ_FACTORY_KEY;
            application.setAttribute(factoryKey, schedulerFactory);
        } catch (SchedulerException ex) {
            log.error("Fatal conf Job", ex);
        }





    }

    /**
     *
     */
    @Override
    public void destroy() {

        if (!performShutdown) {
            return;
        }

        if (scheduler != null) {
            try {
                scheduler.shutdown();
            } catch (SchedulerException ex) {

                log.error(ex);
            }
        }
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Init Quartz on Start UP";
    }// </editor-fold>
}
