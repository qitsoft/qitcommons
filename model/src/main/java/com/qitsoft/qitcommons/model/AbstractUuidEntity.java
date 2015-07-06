package com.qitsoft.qitcommons.model;

import java.util.UUID;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * The base entity class where the id is uuid.
 */
@MappedSuperclass
public abstract class AbstractUuidEntity extends AbstractEntity<UUID> {

    /**
     * The UUID id of entity.
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID id;

    /**
     * {@inheritDoc}
     */
    public UUID getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public void setId(UUID id) {
        this.id = id;
    }
}
