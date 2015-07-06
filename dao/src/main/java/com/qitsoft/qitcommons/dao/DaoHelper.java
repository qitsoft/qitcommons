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

/**
 * The helper for dao classes.
 * @param <T> the base entity class.
 */
public class DaoHelper<T extends AbstractEntity> {

    /**
     * The Hibernate session factory.
     */
    private SessionFactory sessionFactory;

    /**
     * Save the entity.
     * @param obj the entity to save.
     * @return the saved entity.
     */
    @SuppressWarnings("unchecked")
    public T save(T obj) {
        return (T) session().merge(obj);
    }

    /**
     * Refreshes the entity.
     * @param obj the entity to refresh.
     */
    public void refresh(T obj) {
        session().refresh(obj);
    }

    /**
     * Builds the expression which concatenates other expressions by space in lower case.
     * @param expressions the expression to concatenate.
     * @return the concatenated expression.
     */
    public StringExpression concat(StringExpression... expressions) {
        StringExpression expression = expressions[0];

        for (int i = 1; i < expressions.length; i++) {
            expression = expression.concat(" ");
            expression = expression.concat(expressions[i]);
        }

        expression = expression.toLowerCase();
        return expression;
    }

    /**
     * Builds the expression which filters the string expressions by query terms.
     * @param query the query by which to build the filtered expression.
     * @param expressions the string expressions which to filter.
     * @return the filtered expression.
     */
    public BooleanExpression filter(String[] query, StringExpression... expressions) {
        List<BooleanExpression> booleanExpressions = new ArrayList<>();
        StringExpression mergedExpression = concat(expressions);
        for (String q : query) {
            booleanExpressions.add(mergedExpression.like("%" + q + "%"));
        }
        return BooleanExpression.allOf(Iterables.toArray(booleanExpressions, BooleanExpression.class));
    }

    /**
     * Create the subselect of QueryDSL.
     * @return the QueryDSL subselect statement.
     */
    public HibernateSubQuery subselect() {
        return new HibernateSubQuery();
    }

    /**
     * Create the select of QueryDSL.
     * @return the QueryDSL select statement.
     */
    public HibernateQuery select() {
        return new HibernateQuery(session());
    }

    /**
     * Creates the delete clause of QueryDSL.
     * @param entityPath the entity where to delete the data.
     * @return the QueryDSL delete statement.
     */
    public HibernateDeleteClause delete(EntityPath<?> entityPath) {
        return new HibernateDeleteClause(session(), entityPath);
    }

    /**
     * Creates the update clause of QueryDSL.
     * @param entityPath the entity where to update data.
     * @return the QueryDSL update statement.
     */
    public HibernateUpdateClause update(EntityPath<?> entityPath) {
        return new HibernateUpdateClause(session(), entityPath);
    }

    /**
     * Deletes the entity.
     * @param obj the entity to delete.
     */
    public void delete(T obj) {
        session().delete(obj);
    }

    /**
     * Returns the current hibernate session.
     * @return the hibernate session.
     */
    public Session session() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Returns the entity by type and id.
     * @param entityType the entity type.
     * @param id the id of the entity.
     * @return the found entity or null.
     */
    @SuppressWarnings("unchecked")
    public T get(Class<T> entityType, Serializable id) {
        return (T) session().get(entityType, id);
    }

    /**
     * Getter of hibernate session factory.
     * @return the hibernate session factory.
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Setter of hibernate session factory.
     * @param sessionFactory the hibernate session factory.
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
