package com.qitsoft.qitcommons.test;

import java.text.MessageFormat;
import java.util.HashMap;

import org.hamcrest.Matcher;
import org.junit.Test;

import static com.qitsoft.qitcommons.test.ObjectMatcher.objectMatcher;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ObjectMatcherTest {

    @Test
    public void testWhereStatementSuccess() {
        ObjectMatcher<ClassUnderTest> objectMatcher = objectMatcher(ClassUnderTest.class)
                .where(ClassUnderTest::getField1, equalTo("field1-value"))
                .where(ClassUnderTest::getField2, equalTo("field2-value"));

        ClassUnderTest classUnderTest = new ClassUnderTest();
        classUnderTest.setField1("field1-value");
        classUnderTest.setField2("field2-value");

        assertThat(classUnderTest, objectMatcher);
    }

    @Test
    public void testWhereStatementFailedWithoutMarker() {
        ObjectMatcher<ClassUnderTest> objectMatcher = objectMatcher(ClassUnderTest.class)
                .where(ClassUnderTest::getField1, equalTo("field1-value"))
                .where(ClassUnderTest::getField2, equalTo("field2-value"));

        ClassUnderTest classUnderTest = new ClassUnderTest();
        classUnderTest.setField1("field1-value");
        classUnderTest.setField2("field2-value-failed");

        try {
            assertThat(classUnderTest, objectMatcher);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("\nExpected: unknown field \"field2-value\" \n     but: was <{0}>", classUnderTest.toString())));
        }
    }

    @Test
    public void testWhereStatementFailedWithMarker() {
        ObjectMatcher<ClassUnderTest> objectMatcher = objectMatcher(ClassUnderTest.class)
                .where("field1", ClassUnderTest::getField1, equalTo("field1-value"))
                .where("field2", ClassUnderTest::getField2, equalTo("field2-value"));

        ClassUnderTest classUnderTest = new ClassUnderTest();
        classUnderTest.setField1("field1-value");
        classUnderTest.setField2("field2-value-failed");

        try {
            assertThat(classUnderTest, objectMatcher);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("\nExpected: field field2 \"field2-value\" \n     but: was <{0}>", classUnderTest.toString())));
        }
    }

    @Test
    public void testWhereStatementFailedMultipleFields() {
        ObjectMatcher<ClassUnderTest> objectMatcher = objectMatcher(ClassUnderTest.class)
                .where("field1", ClassUnderTest::getField1, equalTo("field1-value"))
                .where("field2", ClassUnderTest::getField2, equalTo("field2-value"));

        ClassUnderTest classUnderTest = new ClassUnderTest();
        classUnderTest.setField1("field1-value-failed");
        classUnderTest.setField2("field2-value-failed");

        try {
            assertThat(classUnderTest, objectMatcher);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("\nExpected: field field1 \"field1-value\" \n     but: was <{0}>", classUnderTest.toString())));
        }
    }

    @Test
    public void testPassNullObject() {
        try {
            assertThat(null, objectMatcher(ClassUnderTest.class));
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo("\nExpected:  not null \n     but: was null"));
        }
    }

    @Test
    public void testPassObjectOfOtherType() {
        Object actual = (Object) new HashMap<>();
        try {
            assertThat(actual, (Matcher) objectMatcher(ClassUnderTest.class));
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("\nExpected:  an instance of {0} \n     but: was <{1}>", ClassUnderTest.class.getName(), actual.toString())));
        }
    }

    private static class ClassUnderTest {

        private String field1;

        private String field2;

        private String field3;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }

        public String getField3() {
            return field3;
        }

        public void setField3(String field3) {
            this.field3 = field3;
        }
    }

}