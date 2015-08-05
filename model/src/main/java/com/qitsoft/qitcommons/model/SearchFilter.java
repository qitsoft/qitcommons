package com.qitsoft.qitcommons.model;

public class SearchFilter<T> {

    private T query;

    private int page;

    private int pageSize;

    public SearchFilter() {
    }

    public SearchFilter(T query, int page, int pageSize) {
        this.query = query;
        this.page = page;
        this.pageSize = pageSize;
    }

    public SearchFilter(T query) {
        this.query = query;
        this.page = 0;
        this.pageSize = 0;
    }

    public T getQuery() {
        return query;
    }

    public void setQuery(T query) {
        this.query = query;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
