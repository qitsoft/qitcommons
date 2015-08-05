package com.qitsoft.qitcommons.model;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SearchFilterTest {

    public static final String QUERY = "QUERY";

    @Test
    public void testNonPagedConstructor() {
        SearchFilter<String> filter = new SearchFilter<>(QUERY);

        assertThat(filter.getQuery(), equalTo(QUERY));
        assertThat(filter.getPage(), equalTo(0));
        assertThat(filter.getPageSize(), equalTo(0));
    }

    @Test
    public void testPagedConstructor() {
        SearchFilter<String> filter = new SearchFilter<>(QUERY, 24, 35);

        assertThat(filter.getQuery(), equalTo(QUERY));
        assertThat(filter.getPage(), equalTo(24));
        assertThat(filter.getPageSize(), equalTo(35));
    }

}