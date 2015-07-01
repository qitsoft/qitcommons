package com.qitsoft.qitcommons.dao;

import java.io.Serializable;
import java.util.UUID;

import com.mysema.query.jpa.hibernate.HibernateDeleteClause;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.jpa.hibernate.HibernateSubQuery;
import com.mysema.query.jpa.hibernate.HibernateUpdateClause;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.StringPath;
import com.qitsoft.qitcommons.model.AbstractEntity;
import com.qitsoft.qitcommons.model.AbstractUuidEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DaoHelperTest {

    public static final UUID ID = UUID.randomUUID();

    public static final AbstractUuidEntity USER = new AbstractUuidEntity() {};

    @InjectMocks
    @Spy
    private DaoHelper daoHelper = new DaoHelper();

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    private EntityPath<String> entity = new EntityPathBase<>(String.class, "entity");

    @Before
    public void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testSessionReturnsCurrent() {
        assertThat(daoHelper.session(), sameInstance(session));
        verify(sessionFactory).getCurrentSession();
        verifyNoMoreInteractions(sessionFactory);
    }

    @Test
    public void testGet() {
        when(session.get(any(Class.class), any(Serializable.class))).thenReturn(USER);

        AbstractEntity result = daoHelper.get(AbstractEntity.class, ID);

        verify(session).get(eq(AbstractEntity.class), eq(ID));
        assertThat(result, sameInstance(USER));
        verifyNoMoreInteractions(session);
    }

    @Test
    public void testSave() {
        AbstractEntity persistedUser = new AbstractUuidEntity(){};
        when(session.merge(anyObject())).thenReturn(persistedUser);

        AbstractEntity result = daoHelper.save(USER);

        assertThat(result, sameInstance(persistedUser));
        verify(session).merge(eq(USER));
        verifyNoMoreInteractions(session);
    }

    @Test
    public void testSelect() {
        HibernateQuery result = daoHelper.select();

        assertThat(result, notNullValue());
        verify(sessionFactory).getCurrentSession();
        verifyNoMoreInteractions(sessionFactory);
    }

    @Test
    public void testSubselect() {
        HibernateSubQuery result = daoHelper.subselect();

        assertThat(result, notNullValue());
        verifyNoMoreInteractions(sessionFactory);
    }

    @Test
    public void testRefresh() {
        daoHelper.refresh(USER);

        verify(session).refresh(eq(USER));
    }

    @Test
    public void testDeleteClause() {
        HibernateDeleteClause result = daoHelper.delete(entity);

        assertThat(result, notNullValue());
        verify(sessionFactory).getCurrentSession();
        verifyNoMoreInteractions(sessionFactory);
    }

    @Test
    public void testDelete() {
        daoHelper.delete(USER);

        verify(sessionFactory).getCurrentSession();
        verifyNoMoreInteractions(sessionFactory);
        verify(session).delete(same(USER));
    }

    @Test
    public void testUpdate() {
        HibernateUpdateClause result = daoHelper.update(entity);

        assertThat(result, notNullValue());
        verify(sessionFactory).getCurrentSession();
        verifyNoMoreInteractions(sessionFactory);
    }

    @Test
    public void testMergeFields() {
        StringPath path1 = mock(StringPath.class);
        StringPath path2 = mock(StringPath.class);

        StringExpression[] expressions = new StringExpression[3];
        for (int i = 0; i < expressions.length; i++) {
            expressions[i] = mock(StringExpression.class);
        }

        when(path1.concat(eq(" "))).thenReturn(expressions[0]);
        when(expressions[0].concat(eq(path2))).thenReturn(expressions[1]);
        when(expressions[1].toLowerCase()).thenReturn(expressions[2]);

        StringExpression expression = daoHelper.mergeFields(path1, path2);

        assertThat(expression, sameInstance(expressions[2]));

        verify(path1).concat(eq(" "));
        verify(expressions[0]).concat(eq(path2));
        verify(expressions[1]).toLowerCase();
    }

    @Test
    public void testMergeFieldsOnePath() {
        StringPath path = mock(StringPath.class);

        StringExpression returnExpression = mock(StringExpression.class);

        when(path.toLowerCase()).thenReturn(returnExpression);

        StringExpression expression = daoHelper.mergeFields(path);

        assertThat(expression, sameInstance(returnExpression));

        verify(path).toLowerCase();
    }

    @Test
    public void testFilter() {
        StringExpression expression1 = mock(StringExpression.class);
        StringExpression expression2 = mock(StringExpression.class);
        StringExpression expression = mock(StringExpression.class);

        doReturn(expression).when(daoHelper).mergeFields(eq(expression1), eq(expression2));

        BooleanExpression[] expressions = new BooleanExpression[]{
                mock(BooleanExpression.class),
                mock(BooleanExpression.class)
        };
        BooleanExpression returnExpression = mock(BooleanExpression.class);

        when(expression.like(anyString())).thenReturn(expressions[0]).thenReturn(expressions[1]);
        when(expressions[0].and(eq(expressions[1]))).thenReturn(returnExpression);

        BooleanExpression result = daoHelper.filter(new String[]{"a", "b"}, expression1, expression2);

        assertThat(result, notNullValue());
        verify(expression).like(eq("%a%"));
        verify(expression).like(eq("%b%"));
        verify(expressions[0]).and(eq(expressions[1]));
        verify(daoHelper).mergeFields(eq(expression1), eq(expression2));
    }
}