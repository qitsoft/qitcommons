package com.qitsoft.qitcommons.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

public abstract class UnifiedMatcher<T> extends BaseMatcher<T> {

    private ThreadLocal<PostponedDescription> descriptionLocal = new ThreadLocal<>();

    @Override
    public boolean matches(Object item) {
        PostponedDescription description = new PostponedDescription();
        descriptionLocal.set(description);
        matches((T) item, description);
        return description.descriptionItems.isEmpty();
    }

    @Override
    public void describeTo(Description description) {
        descriptionLocal.get().descriptionItems.forEach(x -> x.accept(description));
    }

    protected abstract void matches(T item, Description description);

    protected <T> boolean match(T value, Matcher<T> matcher, Consumer<Description> describe) {
        if (!matcher.matches(value)) {
            describe.accept(descriptionLocal.get());
            descriptionLocal.get().appendText(" ");
            matcher.describeTo(descriptionLocal.get());
            descriptionLocal.get().appendText(" ");
            return false;
        } else {
            return true;
        }
    }

    protected <T> boolean match(T value, Matcher<T> matcher, String message) {
        return match(value, matcher, x -> x.appendText(message));
    }

    private static class PostponedDescription implements Description {

        private List<Consumer<Description>> descriptionItems;

        public PostponedDescription() {
            this.descriptionItems = new ArrayList<>();
        }

        @Override
        public Description appendText(String text) {
            descriptionItems.add(x -> x.appendText(text));
            return this;
        }

        @Override
        public Description appendDescriptionOf(SelfDescribing value) {
            descriptionItems.add(x -> x.appendDescriptionOf(value));
            return this;
        }

        @Override
        public Description appendValue(Object value) {
            descriptionItems.add(x -> x.appendValue(value));
            return this;
        }

        @Override
        public <T> Description appendValueList(String start, String separator, String end, T... values) {
            descriptionItems.add(x -> x.appendValueList(start, separator, end, values));
            return this;
        }

        @Override
        public <T> Description appendValueList(String start, String separator, String end, Iterable<T> values) {
            descriptionItems.add(x -> x.appendValueList(start, separator, end, values));
            return this;
        }

        @Override
        public Description appendList(String start, String separator, String end, Iterable<? extends SelfDescribing> values) {
            descriptionItems.add(x -> x.appendList(start, separator, end, values));
            return this;
        }
    }
}
