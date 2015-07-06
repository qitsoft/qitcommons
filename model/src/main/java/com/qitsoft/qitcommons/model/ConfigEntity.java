package com.qitsoft.qitcommons.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * The entity to store the configuration. It is based on string keys and values.
 */
@Entity
@Table(name = "config")
public class ConfigEntity extends AbstractEntity<String> {

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
     * Getter of key. Alias for {@link #getKey()}.
     * @return the key.
     */
    @Override
    public String getId() {
        return getKey();
    }

    /**
     * Setter for key. Alias for {@link #setKey(String)}.
     * @param id the id of entity.
     */
    @Override
    public void setId(String id) {
        setKey(id);
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
}
