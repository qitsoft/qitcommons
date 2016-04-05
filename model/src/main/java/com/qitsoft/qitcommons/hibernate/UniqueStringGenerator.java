package com.qitsoft.qitcommons.hibernate;

import java.io.Serializable;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;

public class UniqueStringGenerator implements IdentifierGenerator, Configurable {

    @Override
    public void configure(Type type, Properties params, Dialect d) throws MappingException {
    }

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()).replaceAll("=+", "");
    }
}
