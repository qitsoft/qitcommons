package com.qitsoft.qitcommons.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * The base entity class where the id is uuid.
 */
@MappedSuperclass
public abstract class AbstractUniqueStringEntity extends AbstractEntity<String> {

    /**
     * The UUID id of entity.
     */
    @Id
    @GeneratedValue(generator = "UniqueStringGenerator")
    @GenericGenerator(name = "UniqueStringGenerator", strategy = "com.qitsoft.qitcommons.hibernate.UniqueStringGenerator")
    private String id;

    /**
     * {@inheritDoc}
     */
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public void setId(String id) {
        this.id = id;
    }
}
