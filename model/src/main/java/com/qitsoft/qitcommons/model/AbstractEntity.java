package com.qitsoft.qitcommons.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The abstract entity class.
 * @param <T> the id type.
 */
@MappedSuperclass
public abstract class AbstractEntity<T> implements Serializable {

    /**
     * The date when the entity was created.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdDate = new Date();

    /**
     * The date when the entity was last modified. It also is the version field.
     */
    @Version
    private Date modifiedDate;

    /**
     * Returns the id.
     * @return the id.
     */
    public abstract T getId();

    /**
     * Sets the id of the entiry.
     * @param id the id of entity.
     */
    public abstract void setId(T id);

    /**
     * Getter for create date.
     * @return the created date.
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Setter for created date.
     * @param createdDate the created date.
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Getter for modified date.
     * @return the modified date.
     */
    public Date getModifiedDate() {
        return modifiedDate;
    }

    /**
     * Setter for modified date.
     * @param modifiedDate the modified date.
     */
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

}
