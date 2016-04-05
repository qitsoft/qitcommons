package com.qitsoft.qitcommons.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Type;

/**
 * The entity to store the configuration. It is based on string keys and values.
 */
@Entity
@Table(name = "config")
public class ConfigEntity {

    /**
     * The key of config value.
     */
    @Id
    private String key;

    /**
     * The valjue of config value.
     */
    @Column(length = 4000)
    private String value;

    /**
     * The value of the config key if it should hold large amount of data.
     */
    @Lob
    @Type(type="org.hibernate.type.StringClobType")
    private String text;

    /**
     * The date when the entity was last modified. It also is the version field.
     */
    @Version
    private Date modifiedDate;

    /**
     * Getter for key.
     * @return the config key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Setter for key.
     * @param key the key.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Getter for value.
     * @return the config value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter for value.
     * @param value the config value.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Getter for large value.
     * @return the large config value.
     */
    public String getText() {
        return text;
    }

    /**
     * Setter for large value.
     * @param text the large config value.
     */
    public void setText(String text) {
        this.text = text;
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
