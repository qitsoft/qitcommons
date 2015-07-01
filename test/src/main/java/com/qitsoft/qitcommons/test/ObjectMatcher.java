package com.qitsoft.qitcommons.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public class ObjectMatcher<T> extends UnifiedMatcher<T> {

    private Class<T> type;

    private List<WhereItem<T, ?>> whereItems = new ArrayList<>();

    public ObjectMatcher(Class<T> type) {
        this.type = type;
    }

    public static <T> ObjectMatcher<T> objectMatcher(Class<T> type) {
        return new ObjectMatcher<>(type);
    }

    public <R> ObjectMatcher<T> where(Function<T, R> getter, Matcher<R> matcher) {
        return where(null, getter, matcher);
    }

    public <R> ObjectMatcher<T> where(String marker, Function<T, R> getter, Matcher<R> matcher) {
        whereItems.add(new WhereItem(marker, getter, matcher));
        return this;
    }

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
