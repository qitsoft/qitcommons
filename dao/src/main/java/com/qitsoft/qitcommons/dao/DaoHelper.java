package com.qitsoft.qitcommons.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Iterables;
import com.mysema.query.jpa.hibernate.HibernateDeleteClause;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.jpa.hibernate.HibernateSubQuery;
import com.mysema.query.jpa.hibernate.HibernateUpdateClause;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.StringExpression;
import com.qitsoft.qitcommons.model.AbstractEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DaoHelper<T extends AbstractEntity> {

    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public T save(T obj) {
        return (T) session().merge(obj);
    }

    public void refresh(T obj) {
        session().refresh(obj);
    }

    public StringExpression mergeFields(StringExpression... expressions) {
        StringExpression expression = expressions[0];

        for (int i = 1; i < expressions.length; i++) {
            expression = expression.concat(" ");
            expression = expression.concat(expressions[i]);
        }

        expression = expression.toLowerCase();
        return expression;
    }

    public BooleanExpression filter(String[] query, StringExpression... expressions) {
        List<BooleanExpression> booleanExpressions = new ArrayList<>();
        StringExpression mergedExpression = mergeFields(expressions);
        for (String q : query) {
            booleanExpressions.add(mergedExpression.like("%" + q + "%"));
        }
        return BooleanExpression.allOf(Iterables.toArray(booleanExpressions, BooleanExpression.class));
    }

    public HibernateSubQuery subselect() {
        return new HibernateSubQuery();
    }

    public HibernateQuery select() {
        return new HibernateQuery(session());
    }

    public HibernateDeleteClause delete(EntityPath<?> entityPath) {
        return new HibernateDeleteClause(session(), entityPath);
    }

    public HibernateUpdateClause update(EntityPath<?> entityPath) {
        return new HibernateUpdateClause(session(), entityPath);
    }

    public void delete(T obj) {
        session().delete(obj);
    }

    public Session session() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public T get(Class<T> entityType, Serializable id) {
        return (T) session().get(entityType, id);
    }

}
