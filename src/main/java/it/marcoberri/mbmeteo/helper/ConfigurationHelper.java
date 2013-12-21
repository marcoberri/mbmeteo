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

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
public final class ConfigurationHelper {

    private static final ConfigurationHelper INSTANCE = new ConfigurationHelper();
    /**
     *
     */
    public static Properties prop;

    private ConfigurationHelper() {
        try {
            final URL main = getClass().getProtectionDomain().getCodeSource().getLocation();
            final String path = URLDecoder.decode(main.getPath(), "utf-8");
            final String webInfFolderPosition = new File(path).getPath();
            final String webInfFolder = webInfFolderPosition.substring(0, webInfFolderPosition.indexOf("classes"));
            prop = new Properties();
            prop.load(FileUtils.openInputStream(new File(webInfFolder + File.separator + "config.properties")));
        } catch (Exception e) {
            throw new RuntimeException("Error initializing mongo db", e);
        }
    }

    /**
     *
     * @return
     */
    public static ConfigurationHelper instance() {
        return INSTANCE;
    }

    public String getProperty(String key) {
        return this.prop.getProperty(key);
    }

    public String getProperty(String key, String def) {
        return this.prop.getProperty(key, def);
    }
}
