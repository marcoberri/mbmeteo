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

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import it.marcoberri.mbmeteo.model.Cache;
import it.marcoberri.mbmeteo.model.MapReduceHistoryMinMax;
import it.marcoberri.mbmeteo.model.MapReduceMinMax;
import it.marcoberri.mbmeteo.model.Meteolog;
import it.marcoberri.mbmeteo.model.News;
import it.marcoberri.mbmeteo.model.Stations;
import java.net.UnknownHostException;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
public final class MongoConnectionHelper {

    private static final MongoConnectionHelper INSTANCE = new MongoConnectionHelper();
    /**
     *
     */
    public static Datastore ds;
    private static GridFS gridFS;
    private static final String GRIDCOLLECTIONNAME = "Cache";

    private MongoConnectionHelper() {
        if (ds != null) {
            return;
        }
        try {
            final MongoClient mongoClient = new MongoClient(ConfigurationHelper.prop.getProperty("mongo.host") );
            final Morphia morphia = new Morphia();
           //morphia.mapPackage("it.marcoberri.mbmeteo.model")
            morphia.map(Meteolog.class).
                    map(News.class).
                    map(Stations.class).
                    map(Cache.class).
                    
                    map(MapReduceMinMax.class).
                    map(MapReduceHistoryMinMax.class);
            ds = morphia.createDatastore(mongoClient, ConfigurationHelper.prop.getProperty("mongo.dbname"));
            ds.ensureCaps();
            ds.ensureIndexes();
        } catch (final UnknownHostException e) {
            throw new RuntimeException("Error initializing mongo db", e);
        }
    }

    /**
     *
     * @return
     */
    public static GridFS getGridFS() {
        if (gridFS == null) {
            gridFS = new GridFS(ds.getDB(), GRIDCOLLECTIONNAME);
        }
        return gridFS;
    }

    /**
     *
     * @return
     */
    public static DBCollection getGridCol() {
        return ds.getDB().getCollection(GRIDCOLLECTIONNAME);
    }

    /**
     *
     * @return
     */
    public static MongoConnectionHelper instance() {
        return INSTANCE;
    }
}
