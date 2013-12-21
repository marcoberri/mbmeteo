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

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.PrePersist;
import com.github.jmkgreen.morphia.utils.IndexDirection;
import com.google.gson.Gson;
import java.util.Date;
import org.bson.types.ObjectId;

/**
 *
 * @author Marco Berri <marcoberri@gmail.com>
 */
@Entity("Cache")
public class Cache {

    @Id
    private ObjectId id;
    @Indexed(value = IndexDirection.ASC)
    private String cacheKey;
    @Indexed(value = IndexDirection.ASC)
    private String servletName;
    private Date create;
    private Date lastRead;
    private String gridId;

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
     * @return the key
     */
    public String getCacheKey() {
        return cacheKey;
    }

    /**
     *
     * @return
     */
    public String getServletName() {
        return servletName;
    }

    /**
     * @param cacheKey 
     */
    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    /**
     *
     * @param servletName
     */
    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    /**
     * @return the create
     */
    public Date getCreate() {
        return create;
    }

    /**
     * @param create the create to set
     */
    public void setCreate(Date create) {
        this.create = create;
    }

    /**
     * @return the lastRead
     */
    public Date getLastRead() {
        return lastRead;
    }

    /**
     * @param lastRead the lastRead to set
     */
    public void setLastRead(Date lastRead) {
        this.lastRead = lastRead;
    }

    @PrePersist
    void prePersist() {
        setLastRead(new Date());
    }

    /**
     * @return the gridId
     */
    public String getGridId() {
        return gridId;
    }

    /**
     * @param gridId the gridId to set
     */
    public void setGridId(String gridId) {
        this.gridId = gridId;
    }
}
