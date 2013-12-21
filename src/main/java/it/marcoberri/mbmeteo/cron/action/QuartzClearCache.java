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
package it.marcoberri.mbmeteo.cron.action;

import it.marcoberri.mbmeteo.action.Commons;
import it.marcoberri.mbmeteo.helper.ConfigurationHelper;
import it.marcoberri.mbmeteo.utils.Log4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
public class QuartzClearCache implements Job {

    /**
     *
     */
    protected static final org.apache.log4j.Logger log = Log4j.getLogger("quartz_clearcache", ConfigurationHelper.prop.getProperty("log.path"), Log4j.ROTATE_DAILY);

    /**
     *
     * @param jec
     * @throws JobExecutionException
     */
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        log.debug("start : " + this.getClass().getName());
        Commons.deleteCache(log);
        log.debug("end : " + this.getClass().getName());
    }
}
