package com.qitsoft.qitcommons.dao.test;

import java.util.UUID;

import com.qitsoft.qitcommons.model.AbstractIntEntity;
import com.qitsoft.qitcommons.model.AbstractUuidEntity;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class DaoTestUtilsTest {

    public static final UUID UUID_ID = UUID.randomUUID();

    public static final long LONG_ID = 125l;

    @Test
    public void testWithIdForAbstractUuidEntityByString() {
        AbstractUuidEntity abstractUuidEntity = new AbstractUuidEntity() {};
        abstractUuidEntity.setId(UUID_ID);

        assertThat(abstractUuidEntity, DaoTestUtils.withId(UUID_ID.toString()));
    }

    @Test
    public void testWithIdForAbstractUuidEntityByStringFailed() {
        AbstractUuidEntity abstractUuidEntity = new AbstractUuidEntity() {};
        abstractUuidEntity.setId(UUID_ID);

        assertThat(DaoTestUtils.withId("dsdasasd").matches(abstractUuidEntity), equalTo(false));
    }

    @Test
    public void testWithIdForAbstractUuidEntityByStringNullId() {
        AbstractUuidEntity abstractUuidEntity = new AbstractUuidEntity() {};

        Matcher<? super AbstractUuidEntity> matcher = DaoTestUtils.withId(UUID_ID.toString());
        assertThat(matcher.matches(abstractUuidEntity), equalTo(false));
        StringDescription description = new StringDescription();
        matcher.describeTo(description);
        assertThat(description.toString(), equalTo("entity with id \"" + UUID_ID.toString() + "\"."));
    }

    @Test
    public void testWithIdForAbstractUuidEntityByUuid() {
        AbstractUuidEntity abstractUuidEntity = new AbstractUuidEntity() {};
        abstractUuidEntity.setId(UUID_ID);

        assertThat(abstractUuidEntity, DaoTestUtils.withId(UUID_ID));
    }

    @Test
    public void testWithIdForAbstractUuidEntityByUuidFailed() {
        AbstractUuidEntity abstractUuidEntity = new AbstractUuidEntity() {};
        abstractUuidEntity.setId(UUID_ID);

        assertThat(DaoTestUtils.withId(UUID.randomUUID()).matches(abstractUuidEntity), equalTo(false));
    }

    @Test
    public void testWithIdForAbstractUuidEntityByUuidNullId() {
        AbstractUuidEntity abstractUuidEntity = new AbstractUuidEntity() {};

        assertThat(DaoTestUtils.withId(UUID_ID).matches(abstractUuidEntity), equalTo(false));

        Matcher<? super AbstractUuidEntity> matcher = DaoTestUtils.withId(UUID_ID);
        assertThat(matcher.matches(abstractUuidEntity), equalTo(false));
        StringDescription description = new StringDescription();
        matcher.describeTo(description);
        assertThat(description.toString(), equalTo("entity with id \"" + UUID_ID.toString() + "\"."));
    }

    @Test
    public void testWithIdForAbstractintEntity() {
        AbstractIntEntity abstractIntEntity = new AbstractIntEntity() {};
        abstractIntEntity.setId(LONG_ID);

        assertThat(abstractIntEntity, DaoTestUtils.withId(LONG_ID));
    }

    @Test
    public void testWithIdForAbstractUuidEntityByIntFailed() {
        AbstractIntEntity abstractIntEntity = new AbstractIntEntity() {};
        abstractIntEntity.setId(LONG_ID);

        assertThat(DaoTestUtils.withId(LONG_ID + 15).matches(abstractIntEntity), equalTo(false));
    }

    @Test
    public void testWithIdForAbstractIntEntityNullId() {
        AbstractIntEntity abstractIntEntity = new AbstractIntEntity() {};

        assertThat(DaoTestUtils.withId(LONG_ID).matches(abstractIntEntity), equalTo(false));

        Matcher<? super AbstractIntEntity> matcher = DaoTestUtils.withId(LONG_ID);
        assertThat(matcher.matches(abstractIntEntity), equalTo(false));
        StringDescription description = new StringDescription();
        matcher.describeTo(description);
        assertThat(description.toString(), equalTo("entity with id <" + LONG_ID + "L>."));
    }

}