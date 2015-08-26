package com.qitsoft.qitcommons.model;

import java.util.Locale;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class LocalizedSearchFilterTest {

    public static final String QUERY = "QUERY";

    @Test
    public void testDefaultConstructor() {
        LocalizedSearchFilter<String> filter = new LocalizedSearchFilter<>();

        assertThat(filter.getQuery(), nullValue());
        assertThat(filter.getPage(), equalTo(0));
        assertThat(filter.getPageSize(), equalTo(0));
        assertThat(filter.getLocale(), equalTo(Locale.getDefault()));
    }

    @Test
    public void testNonPagedConstructor() {
        LocalizedSearchFilter<String> filter = new LocalizedSearchFilter<>(QUERY);

        assertThat(filter.getQuery(), equalTo(QUERY));
        assertThat(filter.getPage(), equalTo(0));
        assertThat(filter.getPageSize(), equalTo(0));
        assertThat(filter.getLocale(), equalTo(Locale.getDefault()));
    }

    @Test
    public void testPagedConstructor() {
        LocalizedSearchFilter<String> filter = new LocalizedSearchFilter<>(QUERY, 24, 35);

        assertThat(filter.getQuery(), equalTo(QUERY));
        assertThat(filter.getPage(), equalTo(24));
        assertThat(filter.getPageSize(), equalTo(35));
        assertThat(filter.getLocale(), equalTo(Locale.getDefault()));
    }

    @Test
    public void testLocalizedDefaultConstructor() {
        LocalizedSearchFilter<String> filter = new LocalizedSearchFilter<>(Locale.CHINESE);

        assertThat(filter.getQuery(), nullValue());
        assertThat(filter.getPage(), equalTo(0));
        assertThat(filter.getPageSize(), equalTo(0));
        assertThat(filter.getLocale(), equalTo(Locale.CHINESE));
    }

    @Test
    public void testLocalizedNonPagedConstructor() {
        LocalizedSearchFilter<String> filter = new LocalizedSearchFilter<>(QUERY, Locale.CHINESE);

        assertThat(filter.getQuery(), equalTo(QUERY));
        assertThat(filter.getPage(), equalTo(0));
        assertThat(filter.getPageSize(), equalTo(0));
        assertThat(filter.getLocale(), equalTo(Locale.CHINESE));
    }

    @Test
    public void testLocalizedPagedConstructor() {
        LocalizedSearchFilter<String> filter = new LocalizedSearchFilter<>(QUERY, Locale.CHINESE, 24, 35);

        assertThat(filter.getQuery(), equalTo(QUERY));
        assertThat(filter.getPage(), equalTo(24));
        assertThat(filter.getPageSize(), equalTo(35));
        assertThat(filter.getLocale(), equalTo(Locale.CHINESE));
    }

}