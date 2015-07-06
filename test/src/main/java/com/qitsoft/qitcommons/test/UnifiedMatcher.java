package com.qitsoft.qitcommons.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

/**
 * The base matcher class where the checking and description building is in one method. You should override the
 * method {@link #matches(Object, Description)}. In this method if you add the description then the checking is
 * considered failed and the description you added will be shown.
 *
 * @param <T> the type of object to check.
 */
public abstract class UnifiedMatcher<T> extends BaseMatcher<T> {

    /**
     * The field which holds the decription.
     */
    private ThreadLocal<PostponedDescription> descriptionLocal = new ThreadLocal<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(Object item) {
        PostponedDescription description = new PostponedDescription();
        descriptionLocal.set(description);
        matches((T) item, description);
        return description.descriptionItems.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void describeTo(Description description) {
        descriptionLocal.get().descriptionItems.forEach(x -> x.accept(description));
    }

    /**
     * Checks the object and setting the description. If the item do not matches the criteria then simply add the
     * description to the passed parameter.
     *
     * @param item the item to check.
     * @param description the description.
     */
    protected abstract void matches(T item, Description description);

    /**
     * Match the item by passed matcher and setting the description using consumer.
     *
     * @param value the value to check.
     * @param matcher the matcher to apply for checks,
     * @param describe the consumer of description.
     * @param <T> the type of value.
     * @return true if the value matches the matcher, otherwise false.
     */
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

    /**
     * Match the item by passed matcher and setting the simple description consisting only from passed message.
     * @param value the value to check
     * @param matcher the matcher to apply.
     * @param message the message to use as description.
     * @param <T> the type of value.
     * @return true if the value matches, otherwise false.
     */
    protected <T> boolean match(T value, Matcher<T> matcher, String message) {
        return match(value, matcher, x -> x.appendText(message));
    }

    /**
     * Class used to build postponed description. It persists the passed description and when is needed it will replay it.
     */
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
