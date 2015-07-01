package com.qitsoft.qitcommons.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "config")
public class ConfigEntity extends AbstractEntity<String> {

    @Id
    private String key;

    @Column(length = 4000)
    private String value;

    @Lob
    @Type(type="org.hibernate.type.StringClobType")
    private String text;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getId() {
        return getKey();
    }

    @Override
    public void setId(String id) {
        setKey(id);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
