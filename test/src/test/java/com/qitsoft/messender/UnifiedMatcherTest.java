package com.qitsoft.messender;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.hamcrest.internal.SelfDescribingValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.notNull;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UnifiedMatcherTest {

    @Spy
    private UnifiedMatcher unifiedMatcher = new UnifiedMatcher() {
        @Override
        protected void matches(Object item, Description description) {

        }
    };

    @Mock
    private Description description;

    @Mock
    private Matcher<String> sampleMatcher;

    private Object item = new Object();

    private List<Object> itemList1 = Arrays.asList("a1", "a2");

    private List<Object> itemList2 = Arrays.asList("b1", "b2");

    private Object[] itemArr1 = new String[]{"arr11", "arr12"};

    private Object[] itemArr2 = new String[]{"arr21", "arr22"};

    private List<SelfDescribing> selftDescribingList1 = Arrays.asList(new SelfDescribingValue<>("sd1"), new SelfDescribingValue<>("sd2"));

    private List<SelfDescribing> selftDescribingList2 = Arrays.asList(new SelfDescribingValue<>("sd3"), new SelfDescribingValue<>("sd4"));

    private SelfDescribingValue<String> sd1 = new SelfDescribingValue<>("self-describing-value1");

    private SelfDescribingValue<String> sd2 = new SelfDescribingValue<>("self-describing-value2");

    @Test
    public void testMatchesInvokesCommonMatches() {
        assertThat(unifiedMatcher.matches(item), equalTo(true));

        verify(unifiedMatcher).matches(same(item), notNull(Description.class));
        unifiedMatcher.describeTo(description);
        verifyZeroInteractions(description);
    }

    @Test
    public void testMatchesInvokesCommonMatchesAndReturnsFalse() {
        doAnswer(x -> ((Description)x.getArguments()[1]).appendText("error"))
                .when(unifiedMatcher).matches(anyObject(), any(Description.class));
        assertThat(unifiedMatcher.matches(item), equalTo(false));

        verify(unifiedMatcher).matches(same(item), notNull(Description.class));
    }

    @Test
    public void testDescribe() {
        doAnswer(x -> ((Description)x.getArguments()[1])
                .appendText("appendText1")
                .appendValue("value1")
                .appendDescriptionOf(sd1)
                .appendList("sd1-start-", "-", "-sd1-end", selftDescribingList1)
                .appendValueList("a-start-", "-", "-a-end", itemList1)
                .appendValueList("arr1-start", "-", "-arr1-end", itemArr1)
                .appendText("appendText2")
                .appendValue("value2")
                .appendDescriptionOf(sd2)
                .appendList("sd2-start-", "-", "-sd2-end", selftDescribingList2)
                .appendValueList("b-start-", "-", "-b-end", itemList2)
                .appendValueList("arr2-start", "-", "-arr2-end", itemArr2))
                .when(unifiedMatcher).matches(anyObject(), any(Description.class));

        assertThat(unifiedMatcher.matches(item), equalTo(false));
        unifiedMatcher.describeTo(description);

        InOrder descriptionOrder = inOrder(description);
        descriptionOrder.verify(description).appendText(eq("appendText1"));
        descriptionOrder.verify(description).appendValue(eq("value1"));
        descriptionOrder.verify(description).appendDescriptionOf(eq(sd1));
        descriptionOrder.verify(description).appendList(eq("sd1-start-"), eq("-"), eq("-sd1-end"), eq(selftDescribingList1));
        descriptionOrder.verify(description).appendValueList(eq("a-start-"), eq("-"), eq("-a-end"), eq(itemList1));
        descriptionOrder.verify(description).appendValueList(eq("arr1-start"), eq("-"), eq("-arr1-end"), eq(itemArr1[0]), eq(itemArr1[1]));
        descriptionOrder.verify(description).appendText(eq("appendText2"));
        descriptionOrder.verify(description).appendValue(eq("value2"));
        descriptionOrder.verify(description).appendDescriptionOf(eq(sd2));
        descriptionOrder.verify(description).appendList(eq("sd2-start-"), eq("-"), eq("-sd2-end"), eq(selftDescribingList2));
        descriptionOrder.verify(description).appendValueList(eq("b-start-"), eq("-"), eq("-b-end"), eq(itemList2));
        descriptionOrder.verify(description).appendValueList(eq("arr2-start"), eq("-"), eq("-arr2-end"), eq(itemArr2[0]), eq(itemArr2[1]));
    }

    @Test
    public void testMatchWithConsumerSuccess() {
        unifiedMatcher = new UnifiedMatcher() {
            @Override
            protected void matches(Object item, Description description) {
                match("a", equalTo("a"), (Consumer) x -> {});
            }
        };

        assertThat(unifiedMatcher.matches(item), equalTo(true));
        unifiedMatcher.describeTo(description);
        verifyZeroInteractions(description);
    }

    @Test
    public void testMatchWithConsumerFailed() {
        when(sampleMatcher.matches(anyString())).thenReturn(false);
        doAnswer(x -> ((Description)x.getArguments()[0])
                .appendText("appendText2")
                .appendValue("value2")
                .appendDescriptionOf(sd2)
                .appendList("sd2-start-", "-", "-sd2-end", selftDescribingList2)
                .appendValueList("b-start-", "-", "-b-end", itemList2)
                .appendValueList("arr2-start", "-", "-arr2-end", itemArr2)
        ).when(sampleMatcher).describeTo(any(Description.class));

        unifiedMatcher = new UnifiedMatcher() {
            @Override
            protected void matches(Object item, Description description) {
                match("a", sampleMatcher, (Consumer<Description>) x -> {
                    x.appendText("appendText1")
                            .appendValue("value1")
                            .appendDescriptionOf(sd1)
                            .appendList("sd1-start-", "-", "-sd1-end", selftDescribingList1)
                            .appendValueList("a-start-", "-", "-a-end", itemList1)
                            .appendValueList("arr1-start", "-", "-arr1-end", itemArr1);
                });
            }
        };

        assertThat(unifiedMatcher.matches(item), equalTo(false));
        unifiedMatcher.describeTo(description);

        InOrder descriptionOrder = inOrder(description, sampleMatcher);
        descriptionOrder.verify(description).appendText(eq("appendText1"));
        descriptionOrder.verify(description).appendValue(eq("value1"));
        descriptionOrder.verify(description).appendDescriptionOf(eq(sd1));
        descriptionOrder.verify(description).appendList(eq("sd1-start-"), eq("-"), eq("-sd1-end"), eq(selftDescribingList1));
        descriptionOrder.verify(description).appendValueList(eq("a-start-"), eq("-"), eq("-a-end"), eq(itemList1));
        descriptionOrder.verify(description).appendValueList(eq("arr1-start"), eq("-"), eq("-arr1-end"), eq(itemArr1[0]), eq(itemArr1[1]));
        descriptionOrder.verify(description).appendText(eq(" "));
        descriptionOrder.verify(description).appendText(eq("appendText2"));
        descriptionOrder.verify(description).appendValue(eq("value2"));
        descriptionOrder.verify(description).appendDescriptionOf(eq(sd2));
        descriptionOrder.verify(description).appendList(eq("sd2-start-"), eq("-"), eq("-sd2-end"), eq(selftDescribingList2));
        descriptionOrder.verify(description).appendValueList(eq("b-start-"), eq("-"), eq("-b-end"), eq(itemList2));
        descriptionOrder.verify(description).appendValueList(eq("arr2-start"), eq("-"), eq("-arr2-end"), eq(itemArr2[0]), eq(itemArr2[1]));
        descriptionOrder.verify(description).appendText(eq(" "));
    }

    @Test
    public void testMatchWithStringSuccess() {
        Matcher<String> matcher = equalTo("a");
        unifiedMatcher = spy(new UnifiedMatcher() {
            @Override
            protected void matches(Object item, Description description) {
                assertThat(match("a", matcher, ""), equalTo(true));
            }
        });

        assertThat(unifiedMatcher.matches(item), equalTo(true));
        unifiedMatcher.describeTo(description);
        verifyZeroInteractions(description);
        verify(unifiedMatcher).match(eq("a"), eq(matcher), notNull(Consumer.class));
    }

    @Test
    public void testMatchWithStringFailed() {
        when(sampleMatcher.matches(anyString())).thenReturn(false);
        doAnswer(x -> ((Description)x.getArguments()[0])
                        .appendText("appendText2")
                        .appendValue("value2")
                        .appendDescriptionOf(sd2)
                        .appendList("sd2-start-", "-", "-sd2-end", selftDescribingList2)
                        .appendValueList("b-start-", "-", "-b-end", itemList2)
                        .appendValueList("arr2-start", "-", "-arr2-end", itemArr2)
        ).when(sampleMatcher).describeTo(any(Description.class));

        unifiedMatcher = new UnifiedMatcher() {
            @Override
            protected void matches(Object item, Description description) {
                assertThat(match("a", sampleMatcher, "sample-text"), equalTo(false));
            }
        };

        assertThat(unifiedMatcher.matches(item), equalTo(false));
        unifiedMatcher.describeTo(description);

        InOrder descriptionOrder = inOrder(description, sampleMatcher);
        descriptionOrder.verify(description).appendText(eq("sample-text"));
        descriptionOrder.verify(description).appendText(eq(" "));
        descriptionOrder.verify(description).appendText(eq("appendText2"));
        descriptionOrder.verify(description).appendValue(eq("value2"));
        descriptionOrder.verify(description).appendDescriptionOf(eq(sd2));
        descriptionOrder.verify(description).appendList(eq("sd2-start-"), eq("-"), eq("-sd2-end"), eq(selftDescribingList2));
        descriptionOrder.verify(description).appendValueList(eq("b-start-"), eq("-"), eq("-b-end"), eq(itemList2));
        descriptionOrder.verify(description).appendValueList(eq("arr2-start"), eq("-"), eq("-arr2-end"), eq(itemArr2[0]), eq(itemArr2[1]));
        descriptionOrder.verify(description).appendText(eq(" "));
    }

    @Test
    public void testMatchWithNullStringFailed() {
        when(sampleMatcher.matches(anyString())).thenReturn(false);
        doAnswer(x -> ((Description)x.getArguments()[0])
                        .appendText("appendText2")
                        .appendValue("value2")
                        .appendDescriptionOf(sd2)
                        .appendList("sd2-start-", "-", "-sd2-end", selftDescribingList2)
                        .appendValueList("b-start-", "-", "-b-end", itemList2)
                        .appendValueList("arr2-start", "-", "-arr2-end", itemArr2)
        ).when(sampleMatcher).describeTo(any(Description.class));

        unifiedMatcher = new UnifiedMatcher() {
            @Override
            protected void matches(Object item, Description description) {
                assertThat(match("a", sampleMatcher, (String) null), equalTo(false));
            }
        };

        assertThat(unifiedMatcher.matches(item), equalTo(false));
        unifiedMatcher.describeTo(description);

        InOrder descriptionOrder = inOrder(description, sampleMatcher);
        descriptionOrder.verify(description).appendText(eq(" "));
        descriptionOrder.verify(description).appendText(eq("appendText2"));
        descriptionOrder.verify(description).appendValue(eq("value2"));
        descriptionOrder.verify(description).appendDescriptionOf(eq(sd2));
        descriptionOrder.verify(description).appendList(eq("sd2-start-"), eq("-"), eq("-sd2-end"), eq(selftDescribingList2));
        descriptionOrder.verify(description).appendValueList(eq("b-start-"), eq("-"), eq("-b-end"), eq(itemList2));
        descriptionOrder.verify(description).appendValueList(eq("arr2-start"), eq("-"), eq("-arr2-end"), eq(itemArr2[0]), eq(itemArr2[1]));
        descriptionOrder.verify(description).appendText(eq(" "));
    }

    @Test
    public void testMultipleUse() {
        doAnswer(x -> ((Description)x.getArguments()[1]).appendValue(x.getArguments()[0])).when(unifiedMatcher).matches(anyObject(), any(Description.class));

        unifiedMatcher.matches("text1");
        unifiedMatcher.matches("text2");

        StringDescription stringDescription = new StringDescription();
        unifiedMatcher.describeTo(stringDescription);
        assertThat(stringDescription.toString(), equalTo("\"text2\""));

        unifiedMatcher.matches("text3");

        stringDescription = new StringDescription();
        unifiedMatcher.describeTo(stringDescription);
        assertThat(stringDescription.toString(), equalTo("\"text3\""));
    }

    @Test
    public void testMultithreaded() throws Throwable {
        doAnswer(x -> ((Description)x.getArguments()[1]).appendValue(x.getArguments()[0])).when(unifiedMatcher).matches(anyObject(), any(Description.class));

        CyclicBarrier barrier1 = new CyclicBarrier(10);
        CyclicBarrier barrier2 = new CyclicBarrier(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Collection<Throwable> assertionsAccumulator = new ConcurrentLinkedQueue<>();

        IntStream.range(0, 10)
                .mapToObj(x -> new MultithreadedRunnable("text" + x, barrier1, barrier2, countDownLatch, equalTo("\"text" + x + "\""), assertionsAccumulator))
                .map(Thread::new).forEach(Thread::start);

        countDownLatch.await();

        if (!assertionsAccumulator.isEmpty()) {
            throw assertionsAccumulator.iterator().next();
        }
    }

    @Test
    public void testMultithreadedWithMatches() throws Throwable {
        unifiedMatcher = new UnifiedMatcher() {
            @Override
            protected void matches(Object item, Description description) {
                match(item, equalTo("a"), item + ":");
            }
        };

        CyclicBarrier barrier1 = new CyclicBarrier(10);
        CyclicBarrier barrier2 = new CyclicBarrier(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Collection<Throwable> assertionsAccumulator = new ConcurrentLinkedQueue<>();

        IntStream.range(0, 10)
                .mapToObj(x -> new MultithreadedRunnable("text" + x, barrier1, barrier2, countDownLatch, equalTo("text" + x + ": \"a\" "), assertionsAccumulator))
                .map(Thread::new).forEach(Thread::start);

        countDownLatch.await();

        if (!assertionsAccumulator.isEmpty()) {
            throw assertionsAccumulator.iterator().next();
        }
    }

    private class MultithreadedRunnable implements Runnable {

        private CyclicBarrier barrier1;

        private CyclicBarrier barrier2;

        private CountDownLatch countDownLatch;

        private String text;

        private Matcher matcher;

        private Collection<Throwable> accumulator;

        public MultithreadedRunnable(String text, CyclicBarrier barrier1, CyclicBarrier barrier2, CountDownLatch countDownLatch, Matcher matcher, Collection<Throwable> accumulator) {
            this.barrier1 = barrier1;
            this.barrier2 = barrier2;
            this.countDownLatch = countDownLatch;
            this.text = text;
            this.matcher = matcher;
            this.accumulator = accumulator;
        }

        @Override
        public void run() {
            try {
                barrier1.await();

                unifiedMatcher.matches(text);

                StringDescription description = new StringDescription();
                barrier2.await();

                unifiedMatcher.describeTo(description);

                assertThat(description.toString(), matcher);
            } catch (Throwable e) {
                accumulator.add(e);
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}