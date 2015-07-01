package com.qitsoft.qitcommons.model;

import java.util.Date;

import com.qitsoft.qitcommons.test.UnitTestHelper;
import org.junit.Test;

import static com.qitsoft.qitcommons.test.UnitTestHelper.between;
import static org.junit.Assert.assertThat;

public class EntitiesTest {

    @Test
    public void testAbstractUuidEntity() throws Exception {
        UnitTestHelper.testProperties(new AbstractUuidEntity() {});
    }

    @Test
    public void testAbstractUuidEntityDefaultValues() {
        Date before = new Date();
        AbstractUuidEntity abstractUuidEntity = new AbstractUuidEntity() {};
        Date after = new Date();

        assertThat(abstractUuidEntity.getCreatedDate(), between(before, after));
    }

    @Test
    public void testAbstractIntEntity() throws Exception {
        UnitTestHelper.testProperties(new AbstractIntEntity() {});
    }

    @Test
    public void testAbstractIntEntityDefaultValues() {
        Date before = new Date();
        AbstractIntEntity abstractIntEntity = new AbstractIntEntity() {};
        Date after = new Date();

        assertThat(abstractIntEntity.getCreatedDate(), between(before, after));
    }

    @Test
    public void testConfigEntity() throws Exception {
        UnitTestHelper.testProperties(new ConfigEntity());
    }

}