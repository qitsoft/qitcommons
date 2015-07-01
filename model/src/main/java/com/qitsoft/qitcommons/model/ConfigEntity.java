package com.qitsoft.qitcommons.model;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "config")
public class ConfigEntity {

    @Id
    private String key;

    @Temporal(TemporalType.TIMESTAMP)
    @Version
    @Column(nullable = false)
    private Date modifiedDate;

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

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
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
