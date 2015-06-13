package com.qitsoft.messender;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.logging.Logger;

import com.qitsoft.messender.UnitTestHelper.AnnotationTester;
import com.qitsoft.messender.UnitTestHelper.ClassTester;
import com.qitsoft.messender.UnitTestHelper.FieldTester;
import com.qitsoft.messender.UnitTestHelper.LogTester;
import com.qitsoft.messender.UnitTestHelper.MethodTester;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.verification.VerificationMode;

import static com.qitsoft.messender.UnitTestHelper.between;
import static com.qitsoft.messender.UnitTestHelper.ofSize;
import static com.qitsoft.messender.UnitTestHelper.test;
import static com.qitsoft.messender.UnitTestHelper.testLogger;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UnitTestHelperTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testPojoProperties() throws Exception {
        SimplePojo pojo = spy(new SimplePojo());
        UnitTestHelper.testProperties(pojo, "callbacks");

        assertProperty(pojo, Byte.class, SimplePojo::setByteProp, SimplePojo::getByteProp);
        assertProperty(pojo, Boolean.class, SimplePojo::setBooleanProp, SimplePojo::isBooleanProp);
        assertProperty(pojo, Character.class, SimplePojo::setCharProp, SimplePojo::getCharProp);
        assertProperty(pojo, Double.class, SimplePojo::setDoubleProp, SimplePojo::getDoubleProp);
        assertProperty(pojo, Float.class, SimplePojo::setFloatProp, SimplePojo::getFloatProp);
        assertProperty(pojo, Integer.class, SimplePojo::setIntProp, SimplePojo::getIntProp);
        assertProperty(pojo, Long.class, SimplePojo::setLongProp, SimplePojo::getLongProp);
        assertProperty(pojo, Short.class, SimplePojo::setShortProp, SimplePojo::getShortProp);
        assertProperty(pojo, String.class, SimplePojo::setStringProp, SimplePojo::getStringProp);
        assertProperty(pojo, Date.class, SimplePojo::setDateProp, SimplePojo::getDateProp);
        assertProperty(pojo, UUID.class, SimplePojo::setUuidProp, SimplePojo::getUuidProp);
    }

    @Test
    public void testParentPojoProperties() throws Exception {
        SimplePojo pojo = spy(new ChildSimplePojo());
        UnitTestHelper.testProperties(pojo, "callbacks");

        assertProperty(pojo, Byte.class, SimplePojo::setByteProp, SimplePojo::getByteProp);
        assertProperty(pojo, Boolean.class, SimplePojo::setBooleanProp, SimplePojo::isBooleanProp);
        assertProperty(pojo, Character.class, SimplePojo::setCharProp, SimplePojo::getCharProp);
        assertProperty(pojo, Double.class, SimplePojo::setDoubleProp, SimplePojo::getDoubleProp);
        assertProperty(pojo, Float.class, SimplePojo::setFloatProp, SimplePojo::getFloatProp);
        assertProperty(pojo, Integer.class, SimplePojo::setIntProp, SimplePojo::getIntProp);
        assertProperty(pojo, Long.class, SimplePojo::setLongProp, SimplePojo::getLongProp);
        assertProperty(pojo, Short.class, SimplePojo::setShortProp, SimplePojo::getShortProp);
        assertProperty(pojo, String.class, SimplePojo::setStringProp, SimplePojo::getStringProp);
        assertProperty(pojo, Date.class, SimplePojo::setDateProp, SimplePojo::getDateProp);
        assertProperty(pojo, UUID.class, SimplePojo::setUuidProp, SimplePojo::getUuidProp);
    }

    @Test
    public void testPojoExcludesProperties() throws Exception {
        SimplePojo pojo = spy(new ChildSimplePojo());
        UnitTestHelper.testProperties(pojo, "callbacks", "booleanProp");

        assertProperty(pojo, Byte.class, SimplePojo::setByteProp, SimplePojo::getByteProp);
        verify(pojo, never()).setBooleanProp(anyBoolean());
        assertProperty(pojo, Character.class, SimplePojo::setCharProp, SimplePojo::getCharProp);
        assertProperty(pojo, Double.class, SimplePojo::setDoubleProp, SimplePojo::getDoubleProp);
        assertProperty(pojo, Float.class, SimplePojo::setFloatProp, SimplePojo::getFloatProp);
        assertProperty(pojo, Integer.class, SimplePojo::setIntProp, SimplePojo::getIntProp);
        assertProperty(pojo, Long.class, SimplePojo::setLongProp, SimplePojo::getLongProp);
        assertProperty(pojo, Short.class, SimplePojo::setShortProp, SimplePojo::getShortProp);
        assertProperty(pojo, String.class, SimplePojo::setStringProp, SimplePojo::getStringProp);
        assertProperty(pojo, Date.class, SimplePojo::setDateProp, SimplePojo::getDateProp);
        assertProperty(pojo, UUID.class, SimplePojo::setUuidProp, SimplePojo::getUuidProp);
    }

    @Test
    public void testPojoArrayProperties() throws Exception {
        SimplePojo pojo = spy(new SimplePojo());
        UnitTestHelper.testProperties(pojo, "callbacks");

        assertProperty(pojo, byte[].class, SimplePojo::setByteArrProp, SimplePojo::getByteArrProp);
        assertProperty(pojo, boolean[].class, SimplePojo::setBooleanArrProp, SimplePojo::getBooleanArrProp);
        assertProperty(pojo, char[].class, SimplePojo::setCharArrProp, SimplePojo::getCharArrProp);
        assertProperty(pojo, double[].class, SimplePojo::setDoubleArrProp, SimplePojo::getDoubleArrProp);
        assertProperty(pojo, float[].class, SimplePojo::setFloatArrProp, SimplePojo::getFloatArrProp);
        assertProperty(pojo, int[].class, SimplePojo::setIntArrProp, SimplePojo::getIntArrProp);
        assertProperty(pojo, long[].class, SimplePojo::setLongArrProp, SimplePojo::getLongArrProp);
        assertProperty(pojo, short[].class, SimplePojo::setShortArrProp, SimplePojo::getShortArrProp);
        assertProperty(pojo, String[].class, SimplePojo::setStringArrProp, SimplePojo::getStringArrProp);
        assertProperty(pojo, Date[].class, SimplePojo::setDateArrProp, SimplePojo::getDateArrProp);
        assertProperty(pojo, UUID[].class, SimplePojo::setUuidArrProp, SimplePojo::getUuidArrProp);
    }

    @Test
    public void testPojoCollectionProperties() throws Exception {
        SimplePojo pojo = spy(new SimplePojo());
        UnitTestHelper.testProperties(pojo, "callbacks");

        assertProperty(pojo, Iterable.class, SimplePojo::setIterableProp, SimplePojo::getIterableProp);
        assertProperty(pojo, Iterator.class, SimplePojo::setIteratorProp, SimplePojo::getIteratorProp);
        assertProperty(pojo, List.class, SimplePojo::setListProp, SimplePojo::getListProp);
        assertProperty(pojo, Collection.class, SimplePojo::setCollectionProp, SimplePojo::getCollectionProp);
        assertProperty(pojo, Set.class, SimplePojo::setSetProp, SimplePojo::getSetProp);
        assertProperty(pojo, Map.class, SimplePojo::setMapProp, SimplePojo::getMapProp);
    }

    @Test
    public void testPojoIncompleteProperties() throws Exception {
        IncompletePojo pojo = spy(new IncompletePojo());
        UnitTestHelper.testProperties(pojo, "callbacks");

        verify(pojo, never()).setNoGetter(anyString());
        verify(pojo, never()).getNoSetter();
        assertThat(pojo.getNoSetter(), equalTo("the-no-setter-method"));
    }

    @Test
    public void testPojoFailedProperties() throws Exception {
        FailedFieldPojo pojo = new FailedFieldPojo();

        try {
            UnitTestHelper.testProperties(pojo, "callbacks");
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("failedSetter"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPojoFailedPropertiesGetterType() throws Exception {
        FailedGetterTypePojo pojo = new FailedGetterTypePojo();

        UnitTestHelper.testProperties(pojo, "callbacks");
    }

    @Test
    public void testPojoArrayPropertiesFailPrimitive() throws Exception {
        SimplePojo pojo = spy(new SimplePojo());

        when(pojo.getIntArrProp()).thenReturn(new int[]{52, 23, 56, 21, 848, 4984, 5141, 5, 11});

        try {
            UnitTestHelper.testProperties(pojo, "callbacks");
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("intArrProp"));
        }
    }

    @Test
    public void testPojoArrayPropertiesFailPrimitiveWithSameLength() throws Exception {
        SimplePojo pojo = spy(new SimplePojo());

        final List<int[]> data = new ArrayList<>();
        doAnswer(x -> {
            data.add((int[]) x.getArguments()[0]);
            return null;
        }).when(pojo).setIntArrProp(any(int[].class));

        when(pojo.getIntArrProp()).thenAnswer(x -> {
            int[] arr = Arrays.copyOf(data.get(0), data.get(0).length);
            Arrays.fill(arr, data.get(0)[0] - 10);
            return arr;
        });

        try {
            UnitTestHelper.testProperties(pojo, "callbacks");
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("intArrProp"));
        }
    }

    @Test
    public void testPojoArrayPropertiesFailObject() throws Exception {
        SimplePojo pojo = spy(new SimplePojo());

        when(pojo.getStringArrProp()).thenReturn(new String[]{"52", "23", "56", "21", "848", "4984", "5141", "5", "11"});

        try {
            UnitTestHelper.testProperties(pojo, "callbacks");
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("stringArrProp"));
        }
    }

    @Test
    public void testPojoArrayPropertiesFailObjectWithSameLength() throws Exception {
        SimplePojo pojo = spy(new SimplePojo());

        final List<String[]> data = new ArrayList<>();
        doAnswer(x -> {
            data.add((String[]) x.getArguments()[0]);
            return null;
        }).when(pojo).setStringArrProp(any(String[].class));

        when(pojo.getStringArrProp()).thenAnswer(x -> {
            String[] arr = Arrays.copyOf(data.get(0), data.get(0).length);
            Arrays.fill(arr, data.get(0)[0] + "10");
            return arr;
        });

        try {
            UnitTestHelper.testProperties(pojo, "callbacks");
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("stringArrProp"));
        }
    }

    @Test
    public void testPojoArrayPropertiesCopyOfArray() throws Exception {
        SimplePojo pojo = spy(new SimplePojo());

        final List<String[]> data = new ArrayList<>();
        doAnswer(x -> {
            data.add((String[]) x.getArguments()[0]);
            return null;
        }).when(pojo).setStringArrProp(any(String[].class));

        when(pojo.getStringArrProp()).thenAnswer(x -> {
            String[] arr = Arrays.copyOf(data.get(0), data.get(0).length);
            return arr;
        });

        UnitTestHelper.testProperties(pojo, "callbacks");
        assertProperty(pojo, String[].class, SimplePojo::setStringArrProp, SimplePojo::getStringArrProp);
    }

    @Test
    public void testPojoPropertiesFailPrimitive() throws Exception {
        SimplePojo pojo = spy(new SimplePojo());

        when(pojo.getIntProp()).thenReturn(4984);

        try {
            UnitTestHelper.testProperties(pojo, "callbacks");
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("intProp"));
        }
    }

    @Test
    public void testPojoPropertiesFailObject() throws Exception {
        SimplePojo pojo = spy(new SimplePojo());

        List<String> data = new ArrayList<>();
        doAnswer(x -> {
                data.add((String) x.getArguments()[0]);
                return null;
        }).when(pojo).setStringProp(anyString());
        when(pojo.getStringProp()).thenAnswer(x -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(data.get(0));
            return stringBuilder.toString();
        });

        try {
            UnitTestHelper.testProperties(pojo, "callbacks");
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), containsString("stringProp"));
        }
    }

    @Test
    public void testGetSampleValue() {
        assertGetSampleValue(Boolean.TYPE, anyOf(equalTo(true), equalTo(false)), equalTo(true));
        assertGetSampleValue(Boolean.class, anyOf(equalTo(true), equalTo(false)), equalTo(true));
        assertGetSampleValue(Byte.TYPE, lessThanOrEqualTo((byte) 120), greaterThan((byte) 100));
        assertGetSampleValue(Byte.class, lessThanOrEqualTo((byte) 120), greaterThan((byte) 100));
        assertGetSampleValue(Character.TYPE, lessThanOrEqualTo((char) 120), greaterThan((char) 100));
        assertGetSampleValue(Character.class, lessThanOrEqualTo((char) 120), greaterThan((char) 100));
        assertGetSampleValue(Double.TYPE, lessThanOrEqualTo(1000d), greaterThan(100d));
        assertGetSampleValue(Double.class, lessThanOrEqualTo(1000d), greaterThan(100d));
        assertGetSampleValue(Float.TYPE, lessThanOrEqualTo(1000f), greaterThan(100f));
        assertGetSampleValue(Float.class, lessThanOrEqualTo(1000f), greaterThan(100f));
        assertGetSampleValue(Integer.TYPE, lessThanOrEqualTo(25000), greaterThan(100));
        assertGetSampleValue(Integer.class, lessThanOrEqualTo(25000), greaterThan(100));
        assertGetSampleValue(Long.TYPE, lessThanOrEqualTo(2500000l), greaterThan(100l));
        assertGetSampleValue(Long.class, lessThanOrEqualTo(2500000l), greaterThan(100l));
        assertGetSampleValue(Short.TYPE, lessThanOrEqualTo((short) 120), greaterThan((short) 100));
        assertGetSampleValue(Short.class, lessThanOrEqualTo((short) 120), greaterThan((short) 100));

        Date afterDate = Date.from(LocalDateTime.now().minusSeconds(60 * 60 * 24 * 31 * 12).atZone(ZoneId.systemDefault()).toInstant());
        Date atLeastDate = Date.from(LocalDateTime.now().minusMonths(11).atZone(ZoneId.systemDefault()).toInstant());
        assertGetSampleValue(Date.class, allOf(between(afterDate, new Date())), lessThan(atLeastDate));
        assertGetSampleValue(NoConstants.class, nullValue(NoConstants.class), nullValue(NoConstants.class));
        assertGetSampleValue(RetentionPolicy.class, notNullValue(RetentionPolicy.class), equalTo(RetentionPolicy.RUNTIME));
        assertGetSampleValue(UUID.class, notNullValue(UUID.class), notNullValue(UUID.class));
        assertGetSampleValue(Locale.class, notNullValue(Locale.class), notNullValue(Locale.class));
        assertGetSampleValue(Integer[].class, new UnifiedMatcher<Integer[]>() {
            @Override
            protected void matches(Integer[] item, Description description) {
                if (item.length == 0) {
                    description.appendText("is not empty");
                    return;
                }
                if (item.length > 11) {
                    description.appendText("has length less than 11");
                    return;
                }
                for (Integer i : item) {
                    if (i == null) {
                        description.appendText("has not null values");
                        return;
                    }
                }
            }
        }, new UnifiedMatcher<Integer[]>() {
            @Override
            protected void matches(Integer[] item, Description description) {
                if (item.length < 10) {
                    description.appendText("length at least 10 items length.");
                }
            }
        });
        assertGetSampleValue(String.class, new UnifiedMatcher<String>() {
            @Override
            protected void matches(String item, Description description) {
                if (!item.matches("^sample String value [a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}.$")) {
                    description.appendText("matches special string pattern.");
                }
            }
        }, notNullValue(String.class));
    }

    @Test
    public void testTest() {
        ClassTester result = test(SimplePojo.class);
        assertThat(result, notNullValue());
        assertThat(result, instanceOf(ClassTester.class));
    }

    @Test
    public void testBetween() {
        LocalDateTime now = LocalDateTime.now();
        Date before = Date.from(now.minusMinutes(1).toInstant(ZoneOffset.UTC));
        Date after = Date.from(now.minusSeconds(1).toInstant(ZoneOffset.UTC));

        Matcher<Date> matcher = between(before, after);

        assertThat(matcher.matches(Date.from(now.toInstant(ZoneOffset.UTC))), equalTo(false));
        assertThat(matcher.matches(after), equalTo(true));
        assertThat(matcher.matches(before), equalTo(true));
        assertThat(matcher.matches(Date.from(now.minusMinutes(1).minusSeconds(1).toInstant(ZoneOffset.UTC))), equalTo(false));
        assertThat(matcher.matches(Date.from(now.minusSeconds(20).toInstant(ZoneOffset.UTC))), equalTo(true));
    }

    @Test
    public void testBetweenDescription() {
        LocalDateTime now = LocalDateTime.now();
        Date before = Date.from(now.minusMinutes(1).toInstant(ZoneOffset.UTC));
        Date after = Date.from(now.minusSeconds(1).toInstant(ZoneOffset.UTC));

        Matcher<Date> matcher = between(before, after);
        matcher.matches(Date.from(now.toInstant(ZoneOffset.UTC)));
        Description description = new StringDescription();
        matcher.describeTo(description);

        assertThat(description.toString(), equalTo(MessageFormat.format("between <{0}> and <{1}>.", before.toString(), after.toString())));
    }

    @Test
    public void testBetweenWIthInstant() {
        LocalDateTime now = LocalDateTime.now();
        Instant before = now.minusMinutes(1).toInstant(ZoneOffset.UTC);
        Instant after = now.minusSeconds(1).toInstant(ZoneOffset.UTC);

        Matcher<Date> matcher = between(before, after);

        assertThat(matcher.matches(Date.from(now.toInstant(ZoneOffset.UTC))), equalTo(false));
        assertThat(matcher.matches(Date.from(after)), equalTo(true));
        assertThat(matcher.matches(Date.from(before)), equalTo(true));
        assertThat(matcher.matches(Date.from(now.minusMinutes(1).minusSeconds(1).toInstant(ZoneOffset.UTC))), equalTo(false));
        assertThat(matcher.matches(Date.from(now.minusSeconds(20).toInstant(ZoneOffset.UTC))), equalTo(true));
    }

    @Test
    public void testBetweenDescriptionWithInstant() {
        LocalDateTime now = LocalDateTime.now();
        Instant before = now.minusMinutes(1).toInstant(ZoneOffset.UTC);
        Instant after = now.minusSeconds(1).toInstant(ZoneOffset.UTC);

        Matcher<Date> matcher = between(before, after);
        matcher.matches(Date.from(now.toInstant(ZoneOffset.UTC)));
        Description description = new StringDescription();
        matcher.describeTo(description);

        assertThat(description.toString(), equalTo(MessageFormat.format("between <{0}> and <{1}>.", before.toString(), after.toString())));
    }

    @Test
    public void testOfSize() {
        Matcher<Collection> matcher = ofSize(3);

        assertThat(matcher.matches(Arrays.asList("a", "b", "c")), equalTo(true));
        assertThat(matcher.matches(Arrays.asList("a", "b")), equalTo(false));
        assertThat(matcher.matches(Arrays.asList("a", "b", "c", "d")), equalTo(false));

        Description description = new StringDescription();
        matcher.describeTo(description);

        assertThat(description.toString(), equalTo("collection of size <3>."));
    }

    @Test
    public void testClassTesterMethodTesterReturns() {
        ClassTester classTester = new ClassTester(SimplePojo.class);

        assertThat(classTester.method("getIntProp"), allOf(notNullValue(), instanceOf(MethodTester.class)));
        assertThat(classTester.method("setIntProp", Integer.TYPE), allOf(notNullValue(), instanceOf(MethodTester.class)));

        try {
            classTester.method("getIntProp", Object.class);
            fail();
        } catch (RuntimeException e) {
            assertThat(e.getCause(), instanceOf(NoSuchMethodException.class));
        }
        try {
            classTester.method("setIntProp", Object.class);
            fail();
        } catch (RuntimeException e) {
            assertThat(e.getCause(), instanceOf(NoSuchMethodException.class));
        }
    }

    @Test
    public void testClassTesterFieldTesterReturns() {
        ClassTester classTester = new ClassTester(SimplePojo.class);

        assertThat(classTester.field("intProp"), allOf(notNullValue(), instanceOf(FieldTester.class)));

        try {
            classTester.field("getIntProp");
            fail();
        } catch (RuntimeException e) {
            assertThat(e.getCause(), instanceOf(NoSuchFieldException.class));
        }
    }

    @Test
    public void testClassTesterAssertAnnotated() {
        ClassTester classTester = new ClassTester(SimplePojo.class);

        assertThat(classTester.assertAnnotated(SampleAnnotation.class), sameInstance(classTester));
        assertThat(classTester.assertAnnotated(SampleAnnotation.class, AnotherSampleAnnotation.class), sameInstance(classTester));

        try {
            assertThat(classTester.assertAnnotated(Retention.class), sameInstance(classTester));
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(
                    MessageFormat.format("The class {0} is not annotated with {1}.", SimplePojo.class.getName(), Retention.class.getName())
            ));
        }

        try {
            assertThat(classTester.assertAnnotated(SampleAnnotation.class, Retention.class), sameInstance(classTester));
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(
                    MessageFormat.format("The class {0} is not annotated with {1}.", SimplePojo.class.getName(), Retention.class.getName())
            ));
        }
    }

    @Test
    public void testMethodTesterAssertAnnotated() throws NoSuchMethodException {
        MethodTester methodTester = test(SimplePojo.class).method("getIntProp");

        assertThat(methodTester.assertAnnotated(SampleAnnotation.class), sameInstance(methodTester));
        assertThat(methodTester.assertAnnotated(SampleAnnotation.class, AnotherSampleAnnotation.class), sameInstance(methodTester));

        try {
            methodTester.assertAnnotated(Retention.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The method {0} is not annotated with {1}.",
                    SimplePojo.class.getMethod("getIntProp").toString(), Retention.class.getName())));
        }

        try {
            methodTester.assertAnnotated(SampleAnnotation.class, Retention.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The method {0} is not annotated with {1}.",
                    SimplePojo.class.getMethod("getIntProp").toString(), Retention.class.getName())));
        }
    }

    @Test
    public void testMethodTesterAssertParamAnnotated() throws NoSuchMethodException {
        MethodTester methodTester = test(SimplePojo.class).method("setUuidProp", UUID.class);

        assertThat(methodTester.assertParamAnnotated(0, SampleAnnotation.class), sameInstance(methodTester));
        assertThat(methodTester.assertParamAnnotated(0, SampleAnnotation.class, AnotherSampleAnnotation.class), sameInstance(methodTester));

        try {
            methodTester.assertParamAnnotated(0, Retention.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The method''s {0} parameter 0 is not annotated with {1}.",
                    SimplePojo.class.getMethod("setUuidProp", UUID.class).toString(), Retention.class.getName())));
        }

        try {
            methodTester.assertParamAnnotated(0, SampleAnnotation.class, Retention.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The method''s {0} parameter 0 is not annotated with {1}.",
                    SimplePojo.class.getMethod("setUuidProp", UUID.class).toString(), Retention.class.getName())));
        }
    }

    @Test
    public void testMethodTesterAssertParamAnnotatedOnIndex1() throws NoSuchMethodException {
        MethodTester methodTester = test(SimplePojo.class).method("annotatedMethod", String.class, String.class);

        assertThat(methodTester.assertParamAnnotated(1, SampleAnnotation.class), sameInstance(methodTester));
        assertThat(methodTester.assertParamAnnotated(1, SampleAnnotation.class, AnotherSampleAnnotation.class), sameInstance(methodTester));

        try {
            methodTester.assertParamAnnotated(1, Retention.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The method''s {0} parameter 1 is not annotated with {1}.",
                    SimplePojo.class.getMethod("annotatedMethod", String.class, String.class).toString(), Retention.class.getName())));
        }

        try {
            methodTester.assertParamAnnotated(1, SampleAnnotation.class, Retention.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The method''s {0} parameter 1 is not annotated with {1}.",
                    SimplePojo.class.getMethod("annotatedMethod", String.class, String.class).toString(), Retention.class.getName())));
        }
    }

    @Test
    public void testMethodTesterAssertMethodOrClassAnnotatedOnMethod() throws NoSuchMethodException {
        MethodTester methodTester = test(SimplePojo.class).method("getIntProp");

        assertThat(methodTester.assertMethodOrClassAnnotated(SampleAnnotation.class), sameInstance(methodTester));
        assertThat(methodTester.assertMethodOrClassAnnotated(ThirdSampleAnnotation.class), sameInstance(methodTester));

        try {
            methodTester.assertMethodOrClassAnnotated(Retention.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("Neither the method {0} nor the class {1} are annotated with {2}.",
                            SimplePojo.class.getMethod("getIntProp").toString(), SimplePojo.class.getName(), Retention.class.getName())));
        }
    }

    @Test
    public void testMethodTesterAssertMethodOrClassAnnotatedOnClass() throws NoSuchMethodException {
        MethodTester methodTester = test(SimplePojo.class).method("getLongProp");

        assertThat(methodTester.assertMethodOrClassAnnotated(SampleAnnotation.class), sameInstance(methodTester));

        try {
            methodTester.assertMethodOrClassAnnotated(Retention.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("Neither the method {0} nor the class {1} are annotated with {2}.",
                            SimplePojo.class.getMethod("getLongProp").toString(), SimplePojo.class.getName(), Retention.class.getName())));
        }
    }

    @Test
    public void testMethodTesterThrowException() throws NoSuchMethodException {
        MethodTester methodTester = test(SimplePojo.class).method("annotatedMethod", String.class, String.class);

        assertThat(methodTester.throwsException(IOException.class), sameInstance(methodTester));
        assertThat(methodTester.throwsException(IOException.class, NumberFormatException.class), sameInstance(methodTester));

        try {
            methodTester.throwsException(SQLException.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The method {0} do not throws the exception {1}.",
                            SimplePojo.class.getMethod("annotatedMethod", String.class, String.class).toString(), SQLException.class.getName())));
        }
        try {
            methodTester.throwsException(IOException.class, SQLException.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The method {0} do not throws the exception {1}.",
                            SimplePojo.class.getMethod("annotatedMethod", String.class, String.class).toString(), SQLException.class.getName())));
        }
    }

    @Test
    public void testMethodTesterParamAnnotation() throws NoSuchMethodException {
        assertThat(test(SimplePojo.class).method("annotatedMethod", String.class, String.class)
                .paramAnnotation(0, ThirdSampleAnnotation.class), allOf(notNullValue(), instanceOf(AnnotationTester.class)));
        assertThat(test(SimplePojo.class).method("annotatedMethod", String.class, String.class)
                .paramAnnotation(1, SampleAnnotation.class), allOf(notNullValue(), instanceOf(AnnotationTester.class)));

        try {
            test(SimplePojo.class).method("annotatedMethod", String.class, String.class).paramAnnotation(0, SampleAnnotation.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The method''s {0} parameter 0 is not annotated with {1}.",
                    SimplePojo.class.getMethod("annotatedMethod", String.class, String.class).toString(), SampleAnnotation.class.getName())));
        }
        try {
            test(SimplePojo.class).method("annotatedMethod", String.class, String.class).paramAnnotation(1, ThirdSampleAnnotation.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The method''s {0} parameter 1 is not annotated with {1}.",
                    SimplePojo.class.getMethod("annotatedMethod", String.class, String.class).toString(), ThirdSampleAnnotation.class.getName())));
        }
    }

    @Test
    public void testMethodTesterAnnotation() throws NoSuchMethodException {
        assertThat(test(SimplePojo.class).method("getIntProp").annotation(SampleAnnotation.class),
                allOf(notNullValue(), instanceOf(AnnotationTester.class)));

        try {
            test(SimplePojo.class).method("getIntProp").annotation(Retention.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The method {0} is not annotated with {1}.",
                    SimplePojo.class.getMethod("getIntProp").toString(), Retention.class.getName())));
        }
    }

    @Test
    public void testMethodTesterAnd() throws NoSuchMethodException {
        ClassTester classTester = test(SimplePojo.class);
        assertThat(classTester.method("getIntProp").and(), sameInstance(classTester));
    }

    @Test
    public void testMethodTesterGetDesciption() throws NoSuchMethodException {
        assertThat(test(SimplePojo.class).method("getIntProp").getDescription(), equalTo("method " + SimplePojo.class.getMethod("getIntProp").toString()));
    }

    @Test
    public void testFieldTesterAssertAnnotated() throws NoSuchFieldException {
        FieldTester fieldTester = test(SimplePojo.class).field("intProp");

        assertThat(fieldTester.assertAnnotated(SampleAnnotation.class), sameInstance(fieldTester));
        assertThat(fieldTester.assertAnnotated(SampleAnnotation.class, AnotherSampleAnnotation.class), sameInstance(fieldTester));

        try {
            fieldTester.assertAnnotated(Retention.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The field {0} is not annotated with {1}.",
                    SimplePojo.class.getDeclaredField("intProp").toString(), Retention.class.getName())));
        }

        try {
            fieldTester.assertAnnotated(SampleAnnotation.class, Retention.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The field {0} is not annotated with {1}.",
                    SimplePojo.class.getDeclaredField("intProp").toString(), Retention.class.getName())));
        }
    }

    @Test
    public void testFieldTesterAnnotation() throws NoSuchFieldException {
        FieldTester fieldTester = test(SimplePojo.class).field("intProp");

        assertThat(fieldTester.annotation(SampleAnnotation.class), allOf(notNullValue(), instanceOf(AnnotationTester.class)));

        try {
            fieldTester.annotation(Retention.class);
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The field {0} is not annotated with {1}.",
                    SimplePojo.class.getDeclaredField("intProp").toString(), Retention.class.getName())));
        }
    }

    @Test
    public void testFieldTesterAnd() {
        ClassTester classTester = test(SimplePojo.class);
        FieldTester fieldTester = classTester.field("intProp");

        assertThat(fieldTester.and(), allOf(notNullValue(), sameInstance(classTester)));
    }

    @Test
    public void testFieldTesterGetDescription() throws NoSuchFieldException {
        FieldTester fieldTester = test(SimplePojo.class).field("intProp");

        assertThat(fieldTester.getDescription(), equalTo("field " + SimplePojo.class.getDeclaredField("intProp").toString()));
    }

    @Test
    public void testAnnotationTesterAnd() {
        FieldTester fieldTester = test(SimplePojo.class).field("intProp");
        assertThat(fieldTester.annotation(SampleAnnotation.class).and(), sameInstance(fieldTester));
    }

    @Test
    public void testAnnotationTesterThatForField() throws Exception {
        AnnotationTester<FieldTester, SampleAnnotation> annotationTester = test(SimplePojo.class).field("intProp").annotation(SampleAnnotation.class);

        assertThat(annotationTester.that("value", equalTo("the-int")), allOf(notNullValue(), sameInstance(annotationTester)));

        try {
            annotationTester.that("valueXXX", equalTo("the-int"));
            fail();
        } catch (NoSuchMethodException e) {
        }

        try {
            annotationTester.that("value", equalTo("the-wrong-int"));
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The annotation''s {0} field <value> of field {1}:\nExpected: \"the-wrong-int\"\n     but: was \"the-int\"",
                    SampleAnnotation.class.getName(), SimplePojo.class.getDeclaredField("intProp").toString())));
        }
    }

    @Test
    public void testAnnotationTesterThatForMethod() throws Exception {
        AnnotationTester<MethodTester, SampleAnnotation> annotationTester = test(SimplePojo.class).method("getIntProp").annotation(SampleAnnotation.class);

        assertThat(annotationTester.that("value", equalTo("the-int")), allOf(notNullValue(), sameInstance(annotationTester)));

        try {
            annotationTester.that("valueXXX", equalTo("the-int"));
            fail();
        } catch (NoSuchMethodException e) {
        }

        try {
            annotationTester.that("value", equalTo("the-wrong-int"));
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The annotation''s {0} field <value> of method {1}:\nExpected: \"the-wrong-int\"\n     but: was \"the-int\"",
                    SampleAnnotation.class.getName(), SimplePojo.class.getDeclaredMethod("getIntProp").toString())));
        }
    }

    @Test
    public void testAnnotationTesterThatWithLambdaForField() throws Exception {
        AnnotationTester<FieldTester, SampleAnnotation> annotationTester = test(SimplePojo.class).field("intProp").annotation(SampleAnnotation.class);

        assertThat(annotationTester.that(SampleAnnotation::value, equalTo("the-int")), allOf(notNullValue(), sameInstance(annotationTester)));

        try {
            annotationTester.that(SampleAnnotation::value, equalTo("the-wrong-int"));
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The annotation''s {0} field got by lambda of field {1}:\nExpected: \"the-wrong-int\"\n     but: was \"the-int\"",
                    SampleAnnotation.class.getName(), SimplePojo.class.getDeclaredField("intProp").toString())));
        }
    }

    @Test
    public void testAnnotationTesterThatWithLambdaForMethod() throws Exception {
        AnnotationTester<MethodTester, SampleAnnotation> annotationTester = test(SimplePojo.class).method("getIntProp").annotation(SampleAnnotation.class);

        assertThat(annotationTester.that(SampleAnnotation::value, equalTo("the-int")), allOf(notNullValue(), sameInstance(annotationTester)));

        try {
            annotationTester.that(SampleAnnotation::value, equalTo("the-wrong-int"));
            fail();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), equalTo(MessageFormat.format("The annotation''s {0} field got by lambda of method {1}:\nExpected: \"the-wrong-int\"\n     but: was \"the-int\"",
                    SampleAnnotation.class.getName(), SimplePojo.class.getDeclaredMethod("getIntProp").toString())));
        }
    }

    @Test
    public void testLogTester() {
        Logger logger = Logger.getLogger("abracadabra");
        LogTester logTester = testLogger("abracadabra");

        logger.info("the sample message");

        assertThat(logTester.getLog(), containsString("INFO"));
        assertThat(logTester.getLog(), containsString("the sample message"));
    }

    private <T> void assertGetSampleValue(Class<T> type, Matcher<T> matcher, Matcher<T> atLeastOne) {
        boolean found = false;
        for (int i=0;i<1000;i++) {
            T sampleValue = UnitTestHelper.getSampleValue(type);
            assertThat(sampleValue, matcher);
            if (atLeastOne.matches(sampleValue)) {
                found = true;
            }
        }
        if (!found) {
            Description description = new StringDescription();
            atLeastOne.describeTo(description);
            throw new AssertionError("Sould be at least one " + description.toString() + ".");
        }
    }

    private <T, R> void assertProperty(T pojo, Class<R> type, Setter<T, R> setter, Getter<T, R> getter, VerificationMode... modes) {
        ArgumentCaptor<R> propCaptor = ArgumentCaptor.forClass(type);
        setter.set(verify(pojo, modes.length > 0 ? modes[0] : times(1)), propCaptor.capture());

        assertThat(propCaptor.getValue(), notNullValue());
        assertThat(getter.get(pojo), equalTo(propCaptor.getValue()));
    }

    @FunctionalInterface
    private interface Setter<T, R> {

        void set(T obj, R value);

    }

    @FunctionalInterface
    private interface Getter<T, R> {

        R get(T obj);

    }

    public static class ChildSimplePojo extends SimplePojo {}

    @SampleAnnotation
    @AnotherSampleAnnotation
    public static class SimplePojo {

        private byte byteProp;

        private boolean booleanProp;

        private char charProp;

        private double doubleProp;

        private float floatProp;

        @SampleAnnotation(value = "the-int")
        @AnotherSampleAnnotation
        private int intProp;

        private long longProp;

        private short shortProp;

        private String stringProp;

        private Date dateProp;

        private UUID uuidProp;

        private byte[] byteArrProp;

        private boolean[] booleanArrProp;

        private char[] charArrProp;

        private double[] doubleArrProp;

        private float[] floatArrProp;

        private int[] intArrProp;

        private long[] longArrProp;

        private short[] shortArrProp;

        private String[] stringArrProp;

        private Date[] dateArrProp;

        private UUID[] uuidArrProp;

        private List listProp;

        private Iterable iterableProp;

        private Iterator iteratorProp;

        private Collection collectionProp;

        private Set setProp;

        private Map mapProp;

        public byte getByteProp() {
            return byteProp;
        }

        public void setByteProp(byte byteProp) {
            this.byteProp = byteProp;
        }

        public boolean isBooleanProp() {
            return booleanProp;
        }

        public void setBooleanProp(boolean booleanProp) {
            this.booleanProp = booleanProp;
        }

        public char getCharProp() {
            return charProp;
        }

        public void setCharProp(char charProp) {
            this.charProp = charProp;
        }

        public double getDoubleProp() {
            return doubleProp;
        }

        public void setDoubleProp(double doubleProp) {
            this.doubleProp = doubleProp;
        }

        public float getFloatProp() {
            return floatProp;
        }

        public void setFloatProp(float floatProp) {
            this.floatProp = floatProp;
        }

        @SampleAnnotation(value = "the-int")
        @AnotherSampleAnnotation
        @ThirdSampleAnnotation
        public int getIntProp() {
            return intProp;
        }

        public void setIntProp(int intProp) {
            this.intProp = intProp;
        }

        public long getLongProp() {
            return longProp;
        }

        public void setLongProp(long longProp) {
            this.longProp = longProp;
        }

        public short getShortProp() {
            return shortProp;
        }

        public void setShortProp(short shortProp) {
            this.shortProp = shortProp;
        }

        public String getStringProp() {
            return stringProp;
        }

        public void setStringProp(String stringProp) {
            this.stringProp = stringProp;
        }

        public Date getDateProp() {
            return dateProp;
        }

        public void setDateProp(Date dateProp) {
            this.dateProp = dateProp;
        }

        public UUID getUuidProp() {
            return uuidProp;
        }

        public void setUuidProp(@SampleAnnotation @AnotherSampleAnnotation UUID uuidProp) {
            this.uuidProp = uuidProp;
        }

        public byte[] getByteArrProp() {
            return byteArrProp;
        }

        public void setByteArrProp(byte[] byteArrProp) {
            this.byteArrProp = byteArrProp;
        }

        public boolean[] getBooleanArrProp() {
            return booleanArrProp;
        }

        public void setBooleanArrProp(boolean[] booleanArrProp) {
            this.booleanArrProp = booleanArrProp;
        }

        public char[] getCharArrProp() {
            return charArrProp;
        }

        public void setCharArrProp(char[] charArrProp) {
            this.charArrProp = charArrProp;
        }

        public double[] getDoubleArrProp() {
            return doubleArrProp;
        }

        public void setDoubleArrProp(double[] doubleArrProp) {
            this.doubleArrProp = doubleArrProp;
        }

        public float[] getFloatArrProp() {
            return floatArrProp;
        }

        public void setFloatArrProp(float[] floatArrProp) {
            this.floatArrProp = floatArrProp;
        }

        public int[] getIntArrProp() {
            return intArrProp;
        }

        public void setIntArrProp(int[] intArrProp) {
            this.intArrProp = intArrProp;
        }

        public long[] getLongArrProp() {
            return longArrProp;
        }

        public void setLongArrProp(long[] longArrProp) {
            this.longArrProp = longArrProp;
        }

        public short[] getShortArrProp() {
            return shortArrProp;
        }

        public void setShortArrProp(short[] shortArrProp) {
            this.shortArrProp = shortArrProp;
        }

        public String[] getStringArrProp() {
            return stringArrProp;
        }

        public void setStringArrProp(String[] stringArrProp) {
            this.stringArrProp = stringArrProp;
        }

        public Date[] getDateArrProp() {
            return dateArrProp;
        }

        public void setDateArrProp(Date[] dateArrProp) {
            this.dateArrProp = dateArrProp;
        }

        public UUID[] getUuidArrProp() {
            return uuidArrProp;
        }

        public void setUuidArrProp(UUID[] uuidArrProp) {
            this.uuidArrProp = uuidArrProp;
        }

        public List getListProp() {
            return listProp;
        }

        public void setListProp(List listProp) {
            this.listProp = listProp;
        }

        public Iterable getIterableProp() {
            return iterableProp;
        }

        public void setIterableProp(Iterable iterableProp) {
            this.iterableProp = iterableProp;
        }

        public Iterator getIteratorProp() {
            return iteratorProp;
        }

        public void setIteratorProp(Iterator iteratorProp) {
            this.iteratorProp = iteratorProp;
        }

        public Collection getCollectionProp() {
            return collectionProp;
        }

        public void setCollectionProp(Collection collectionProp) {
            this.collectionProp = collectionProp;
        }

        public Set getSetProp() {
            return setProp;
        }

        public void setSetProp(Set setProp) {
            this.setProp = setProp;
        }

        public Map getMapProp() {
            return mapProp;
        }

        public void setMapProp(Map mapProp) {
            this.mapProp = mapProp;
        }

        public void annotatedMethod(@ThirdSampleAnnotation String a, @SampleAnnotation @AnotherSampleAnnotation  String b) throws IOException, NumberFormatException {}
    }

    public static class IncompletePojo {

        private String noGetter;

        private String noSetter = "the-no-setter-method";

        public void setNoGetter(String noGetter) {
            this.noGetter = noGetter;
        }

        public String getNoSetter() {
            return noSetter;
        }
    }

    public static class FailedFieldPojo {

        public void setFailedSetter(String value) {}

        public String getFailedSetter() {
            return "failed-string";
        }
    }

    public static class FailedGetterTypePojo {

        public void setFailedSetter(String value) {}

        public Object getFailedSetter() {
            return new Date();
        }
    }

    public enum NoConstants {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
    public @interface SampleAnnotation {

        String value() default "";

        int intValue() default 0;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
    public @interface AnotherSampleAnnotation {

        String value() default "";

        int intValue() default 0;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
    public @interface ThirdSampleAnnotation {

        String value() default "";

        int intValue() default 0;
    }

}