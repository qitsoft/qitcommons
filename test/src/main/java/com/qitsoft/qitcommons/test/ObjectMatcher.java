package com.qitsoft.qitcommons.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

/**
 * The matcher used to check the POJO field by field.
 * @param <T> the type of object to check.
 */
public class ObjectMatcher<T> extends UnifiedMatcher<T> {

    /**
     * The type of object to check.
     */
    private Class<T> type;

    /**
     * The list of checks.
     */
    private List<WhereItem<T, ?>> whereItems = new ArrayList<>();

    /**
     * Creates the object matcher for specific type.
     * @param type the type of object.
     */
    public ObjectMatcher(Class<T> type) {
        this.type = type;
    }

    /**
     * Creates the object matcher for specific type.
     * @param type the type of checked object.
     * @param <T> the type of checked object.
     * @return the object matcher.
     */
    public static <T> ObjectMatcher<T> objectMatcher(Class<T> type) {
        return new ObjectMatcher<>(type);
    }

    /**
     * Checks the field by getter lambda with passed matcher.
     * @param getter the field getter
     * @param matcher the matcher to check the field.
     * @param <R> the field type
     * @return reference to this object matcher for chaining.
     */
    public <R> ObjectMatcher<T> where(Function<T, R> getter, Matcher<R> matcher) {
        return where(null, getter, matcher);
    }

    /**
     * Checks the field by getter lambda with passed matcher with passed field name (marker).
     * @param marker the field name (marker) which will be in the description of the assertion error.
     * @param getter the getter lambda of the field.
     * @param matcher the matcher to check the field.
     * @param <R> the type of the field.
     * @return reference to this object matcher for chaining.
     */
    public <R> ObjectMatcher<T> where(String marker, Function<T, R> getter, Matcher<R> matcher) {
        whereItems.add(new WhereItem(marker, getter, matcher));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void matches(T item, Description description) {
        if (!match(item, Matchers.notNullValue(), "") || !match(item, Matchers.instanceOf(type), "")) {
            return;
        }

        for (WhereItem whereItem : whereItems) {
            if (!match(whereItem.getter.apply(item), whereItem.matcher, whereItem.marker == null ? "unknown field" : "field " + whereItem.marker)) {
                return;
            }
        }
    }

    /**
     * The structure with information about checks of the field.
     * @param <T> the type of object to check.
     * @param <R> the type of the field to check.
     */
    private class WhereItem<T, R> {

        private String marker;

        private Function<T, R> getter;

        private Matcher<R> matcher;

        public WhereItem(String marker, Function<T, R> getter, Matcher<R> matcher) {
            this.marker = marker;
            this.getter = getter;
            this.matcher = matcher;
        }
    }

}
