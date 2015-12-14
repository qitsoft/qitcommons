package com.qitsoft.qitcommons.test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import java.util.stream.Stream;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.mockito.Mockito;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Unit test helper
 */
public class UnitTestHelper {

    /**
     * The helper method which allows injecting the components into service classes.
     *
     * @param service the service where the to inject the component.
     * @param component the component to inject.
     * @param genericTypes the generic types used to narrow the injectable field.
     * @throws IllegalAccessException when the field could not be set.
     */
    public static void inject(Object service, Object component, Class... genericTypes) throws IllegalAccessException {
        inject(service, service.getClass(), component, genericTypes);
    }

    public static void testProperties(Object object, String... exclude) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
        String[] excludedPropertyNames = Arrays.copyOf(exclude, exclude.length);
        Arrays.sort(excludedPropertyNames);

        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            if (Arrays.binarySearch(excludedPropertyNames, propertyDescriptor.getName()) >= 0) {
                continue;
            }

            if (propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null) {
                Object sampleValue = getSampleValue(propertyDescriptor.getPropertyType());
                propertyDescriptor.getWriteMethod().invoke(object, sampleValue);
                Object result = propertyDescriptor.getReadMethod().invoke(object);

                if (propertyDescriptor.getPropertyType().isPrimitive() || propertyDescriptor.getPropertyType().isArray()) {
                    assertThat(propertyDescriptor.getName(), result, Matchers.equalTo(sampleValue));
                } else {
                    assertThat(propertyDescriptor.getName(), result, Matchers.sameInstance(sampleValue));
                }
            }
        }
    }

    /**
     * The helper method allowing in DSL mode to validate the method and its annotations. To use it you just need to
     * specify the class to validate, then the method(s) and the specify the assertions:
     * <pre>
     * UnitTestHelper.test(EvaluationProfileServiceImpl.class)
     *     .method("methodName", String.class, String.class, Pojo.class)
     *         .assertMethodOrClassAnnotated(Validate.class)
     *         .assertParamAnnotated(0, NotBlank.class)
     *         .assertParamAnnotated(1, NotBlank.class)
     *         .assertParamAnnotated(2, NotNull.class)
     *         .throwsException(BSIPPException.class);
     * </pre>
     *
     * @param klass the DSL object to validate the method.
     * @return raises an assertion error on checks  failures.
     */
    public static ClassTester test(Class klass) {
        return new ClassTester(klass);
    }

    public static <T> T getSampleValue(Class<T> propertyType) {
        if (Boolean.TYPE == propertyType || Boolean.class == propertyType) {
            return (T) Boolean.TRUE;
        } else if (Byte.TYPE == propertyType || Byte.class == propertyType) {
            return (T) Byte.valueOf((byte) Math.round(Math.random() * 120));
        } else if (Character.TYPE == propertyType || Character.class ==propertyType) {
            return (T) Character.valueOf((char) Math.round(Math.random() * 120));
        } else if (Double.TYPE == propertyType || Double.class ==propertyType) {
            return (T) Double.valueOf(Math.random() * 1000);
        } else if (Float.TYPE == propertyType || Float.class == propertyType) {
            return (T) Float.valueOf((float) Math.random() * 1000);
        } else if (Integer.TYPE == propertyType || Integer.class == propertyType) {
            return (T) Integer.valueOf((int) Math.round(Math.random() * 25000));
        } else if (Long.TYPE == propertyType || Long.class == propertyType) {
            return (T) Long.valueOf((long) Math.round(Math.random() * 2500000l));
        } else if (Short.TYPE == propertyType || Short.class == propertyType) {
            return (T) Short.valueOf((short) Math.round(Math.random() * 120));
        } else if (String.class == propertyType) {
            return (T) String.valueOf("sample String value " + UUID.randomUUID().toString() + ".");
        } else if (UUID.class.isAssignableFrom(propertyType)) {
            return (T) UUID.randomUUID();
        } else if (Locale.class.isAssignableFrom(propertyType)) {
            return (T) Locale.CANADA_FRENCH;
        } else if (Date.class.isAssignableFrom(propertyType)) {
            int minusSeconds = (int) Math.round(Math.random() * 60 * 60 * 24 * 31 * 12);
            return (T) Date.from(LocalDateTime.now().minusSeconds(minusSeconds).atZone(ZoneId.systemDefault()).toInstant());
        } else if (propertyType.isEnum()) {
            if (propertyType.getEnumConstants().length > 0) {
                return propertyType.getEnumConstants()[(int) Math.round(Math.random() * (propertyType.getEnumConstants().length - 1))];
            } else {
                return null;
            }
        } else if (propertyType.isArray()) {
            int len = (int) Math.round(Math.random() * 10) + 1;
            Class componentType = propertyType.getComponentType();
            Object array = Array.newInstance(componentType, len);
            for (int i = 0; i < len; i++) {
                Array.set(array, i, getSampleValue(componentType));
            }
            return (T) array;
        } else {
            return Mockito.mock(propertyType);
        }
    }

    public static Matcher<Date> between(final Date start, final Date end) {
        return new BaseMatcher<Date>() {
            @Override
            public boolean matches(Object item) {
                return Matchers.allOf(Matchers.greaterThanOrEqualTo(start), Matchers.lessThanOrEqualTo(end)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("between ").appendValue(start).appendText(" and ").appendValue(end).appendText(".");
            }
        };
    }

    public static Matcher<Date> between(final Instant start, final Instant end) {
        return new BaseMatcher<Date>() {
            @Override
            public boolean matches(Object item) {
                return Matchers.allOf(
                        Matchers.greaterThanOrEqualTo(Date.from(start)),
                        Matchers.lessThanOrEqualTo(Date.from(end)))
                        .matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("between ").appendValue(start).appendText(" and ").appendValue(end).appendText(".");
            }
        };
    }

    public static Matcher<Collection> ofSize(int size) {
        return new BaseMatcher<Collection>() {
            @Override
            public boolean matches(Object item) {
                return ((Collection)item).size() == size;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("collection of size ").appendValue(size).appendText(".");
            }
        };
    }

    public static LogTester testLogger(String name) {
        return new LogTester(name);
    }

    /**
     * The test helper class used to declare in DSL mode the annotation validation checks on methods.
     */
    public static class ClassTester {

        /**
         * The service class to check.
         */
        private final Class klass;

        /**
         * Constructs the helper for the specified service class.
         *
         * @param klass the service class to check.
         */
        public ClassTester(Class klass) {
            this.klass = klass;
        }

        /**
         * Specify the method which to validate.
         *
         * @param name           the name of method to validate.
         * @param parameterTypes the parameter types of the method.
         * @return the method tester.
         * @throws RuntimeException (NoSuchMethodException) when no method found with this signature.
         */
        public MethodTester method(String name, Class... parameterTypes) {
            try {
                return new MethodTester(this, klass.getMethod(name, parameterTypes));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Specify the field which to validate.
         *
         * @param name the name of field to validate.
         * @return the field tester.
         * @throws RuntimeException (NoSuchFieldException) when no field found declared in the class
         *                                                to test.
         */
        public FieldTester field(String name) {
            try {
                return new FieldTester(this, klass.getDeclaredField(name));
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Assert that the service class is annotated with specified annotations.
         *
         * @param annotationClasses the annotations to check for,
         * @return the service class tester for chaining.
         */
        public ClassTester assertAnnotated(Class... annotationClasses) {
            for (Class annotationClass : annotationClasses) {
                Assert.assertNotNull(
                        MessageFormat.format("The class {0} is not annotated with {1}.", klass.getName(), annotationClass.getName()),
                        klass.getAnnotation(annotationClass));
            }

            return this;
        }
    }

    /**
     * Method tester to annotations.
     */
    public static class MethodTester implements DescriptiveTester {

        /**
         * The service class tester for the service where the method is declared.
         */
        private final ClassTester classTester;

        /**
         * The method to test.
         */
        private final Method method;

        /**
         * Creates the instance of method tester.
         *
         * @param classTester the service class tester.
         * @param method      the method to test.
         */
        public MethodTester(ClassTester classTester, Method method) {
            this.classTester = classTester;
            this.method = method;
        }

        /**
         * Assert if the method is annotated with the specified annotations.
         *
         * @param annotationClasses the annotations to check for,
         * @return the method tester for chaining.
         */
        public MethodTester assertAnnotated(Class... annotationClasses) {
            for (Class annotationClass : annotationClasses) {
                Assert.assertNotNull(
                        MessageFormat.format("The method {0} is not annotated with {1}.", method.toString(), annotationClass.getName()),
                        method.getAnnotation(annotationClass));
            }

            return this;
        }

        /**
         * Assert if the parameter is annotated with the specified annotations.
         *
         * @param paramIndex        the parameter to check. The first parameter has index 0.
         * @param annotationClasses the annotations to check for,
         * @return the method tester for chaining.
         */
        public MethodTester assertParamAnnotated(int paramIndex, Class... annotationClasses) {
            for (Class annotationClass : annotationClasses) {
                assertTrue(MessageFormat.format("The method''s {0} parameter {1} is not annotated with {2}.",
                                method.toString(), paramIndex, annotationClass.getName()),
                        Stream.of(method.getParameterAnnotations()[paramIndex])
                                .filter(x -> Matchers.instanceOf(annotationClass).matches(x))
                                .findFirst().isPresent());
            }

            return this;
        }


        /**
         * Assert if the method or the service class is annotated with the specified annotation.
         *
         * @param annotationClass the annotation to check for,
         * @return the method tester for chaining.
         */
        public MethodTester assertMethodOrClassAnnotated(Class<? extends Annotation> annotationClass) {
            Annotation annotation = method.getAnnotation(annotationClass);
            if (annotation == null) {
                annotation = classTester.klass.getAnnotation(annotationClass);
                if (annotation == null) {
                    Assert.fail(MessageFormat.format("Neither the method {0} nor the class {1} are annotated with {2}.",
                            method.toString(), classTester.klass.getName(), annotationClass.getName()));
                }
            }
            return this;
        }

        /**
         * Assert if the method has been declared the specified exception in throws clause.
         *
         * @param exceptions the exceptions to check for.
         * @return the method tester for chaining.
         */
        public MethodTester throwsException(Class... exceptions) {
            for (Class exception : exceptions) {
                assertTrue(MessageFormat.format("The method {0} do not throws the exception {1}.", method.toString(), exception.getName()),
                        Stream.of(method.getExceptionTypes()).filter(x -> exception.isAssignableFrom(x)).findFirst().isPresent()
                );
            }
            return this;
        }

        /**
         * Tests annotation for method's parameters.
         *
         * @param index           the parameter index.
         * @param annotationClass the annotation class
         * @return the annotation tester
         */
        public <A extends Annotation> AnnotationTester<MethodTester, A> paramAnnotation(int index, Class<A> annotationClass) {
            A[] annotations = (A[]) method.getParameterAnnotations()[index];
            for (A annotation : annotations) {
                if (annotationClass.isAssignableFrom(annotation.annotationType())) {
                    return new AnnotationTester<>(this, annotation);
                }
            }

            throw new AssertionError(MessageFormat.format("The method''s {0} parameter {1} is not annotated with {2}.",
                    method.toString(), index, annotationClass.getName()));
        }

        /**
         * Tests annotation for method.
         *
         * @param annotationClass the annotation class
         * @return the annotation tester
         */
        public <A extends Annotation> AnnotationTester<MethodTester, A> annotation(Class<A> annotationClass) {
            A annotation = method.getAnnotation(annotationClass);
            if (annotation == null) {
                throw new AssertionError(MessageFormat.format("The method {0} is not annotated with {1}.", method.toString(), annotationClass.getName()));
            }
            return new AnnotationTester<>(this, annotation);
        }

        /**
         * Used in chains to start another method checking.
         *
         * @return the service class tester.
         */
        public ClassTester and() {
            return classTester;
        }

        @Override
        public String getDescription() {
            return "method " + method.toString();
        }
    }

    /**
     * Field tester for annotations.
     */
    public static class FieldTester implements DescriptiveTester {

        /**
         * The service class tester for the service where the method is declared.
         */
        private ClassTester classTester;

        /**
         * The field to test.
         */
        private Field field;

        /**
         * Creates the instance of field tester.
         *
         * @param classTester   the class tester.
         * @param declaredField the field to test.
         */
        public FieldTester(ClassTester classTester, Field declaredField) {
            this.classTester = classTester;
            this.field = declaredField;
        }

        /**
         * Assert if the field is annotated with the specified annotations.
         *
         * @param annotationClasses the annotations to check for,
         * @return the field tester for chaining.
         */
        public FieldTester assertAnnotated(Class... annotationClasses) {
            for (Class annotationClass : annotationClasses) {
                Assert.assertNotNull(
                        MessageFormat.format("The field {0} is not annotated with {1}.", field.toString(), annotationClass.getName()),
                                field.getAnnotation(annotationClass));
            }

            return this;
        }

        public <A extends Annotation> AnnotationTester<FieldTester, A> annotation(Class<A> annotationClass) {
            A annotation = field.getAnnotation(annotationClass);
            if (annotation == null) {
                throw new AssertionError(MessageFormat.format("The field {0} is not annotated with {1}.", field.toString(), annotationClass.getName()));
            }
            return new AnnotationTester<>(this, annotation);
        }

        /**
         * Used in chains to start another field checking.
         *
         * @return the service class tester.
         */
        public ClassTester and() {
            return classTester;
        }

        @Override
        public String getDescription() {
            return "field " + field.toString();
        }
    }

    public static class AnnotationTester<T extends DescriptiveTester, A extends Annotation> {

        private T parent;

        private A annotation;

        public AnnotationTester(T parent, A annotation) {
            this.parent = parent;
            this.annotation = annotation;
        }

        /**
         * Used in chains to start another field checking.
         *
         * @return the service class tester.
         */
        public T and() {
            return parent;
        }

        public AnnotationTester<T, A> that(String field, Matcher<Object> matcher) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Object value = annotation.annotationType().getMethod(field).invoke(annotation);
            assertThat(MessageFormat.format("The annotation''s {0} field <value> of {1}:", annotation.annotationType().getName(), parent.getDescription()),
                    value, matcher);
            return this;
        }

        public AnnotationTester<T, A> that(Function<A, ? extends Object> field, Matcher<? extends Object> matcher) {
            assertThat(MessageFormat.format("The annotation''s {0} field got by lambda of {1}:", annotation.annotationType().getName(), parent.getDescription()),
                    field.apply(annotation), (Matcher<? super Object>) matcher);
            return this;
        }
    }

    public interface DescriptiveTester {

        String getDescription();

    }

    public static class LogTester {

        private Logger logger;

        private OutputStream logCapturingStream = new ByteArrayOutputStream();

        private StreamHandler customLogHandler;

        public LogTester(String name) {
            logger = Logger.getLogger(name);
            Handler[] handlers = logger.getParent().getHandlers();
            customLogHandler = new StreamHandler(logCapturingStream, handlers[0].getFormatter());
            logger.addHandler(customLogHandler);
        }

        public String getLog() {
            customLogHandler.flush();
            return logCapturingStream.toString();
        }
    }

    /**
     * The helper method which allows injecting the components into service classes.
     *
     * @param service the service where the to inject the component.
     * @param serviceType the type of the service whose fields should be injected.
     * @param component the component to inject.
     * @param genericTypes the generic types used to narrow the injectable field.
     * @throws IllegalAccessException when the field could not be set.
     */
    private static void inject(Object service, Class serviceType, Object component, Class... genericTypes) throws IllegalAccessException {
        if (serviceType == Object.class) {
            return;
        }

        for (Field field : serviceType.getDeclaredFields()) {
            if (field.getType().isAssignableFrom(component.getClass())) {
                if (field.getGenericType() != null && field.getGenericType() instanceof ParameterizedType) {
                    Type[] typeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                    if (Arrays.equals(typeArguments, genericTypes)) {
                        field.setAccessible(true);
                        field.set(service, component);
                        return;
                    }
                } else {
                    field.setAccessible(true);
                    field.set(service, component);
                    return;
                }
            }
        }

        inject(service, serviceType.getSuperclass(), component, genericTypes);
    }

}
