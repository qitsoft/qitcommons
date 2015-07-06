package com.qitsoft.qitcommons.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * The base entity class with integral (long) id.
 */
@MappedSuperclass
public abstract class AbstractIntEntity extends AbstractEntity<Long> {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * {@inheritDoc}
     */
    public Long getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public void setId(Long id) {
        this.id = id;
    }
}
