package com.qitsoft.qitcommons.model;

import java.util.Locale;

public class LocalizedSearchFilter<T> extends SearchFilter<T> {

    private Locale locale;

    public LocalizedSearchFilter() {
        this(Locale.getDefault());
    }

    public LocalizedSearchFilter(T query, int page, int pageSize) {
        this(query, Locale.getDefault(), page, pageSize);
    }

    public LocalizedSearchFilter(T query) {
        this(query, Locale.getDefault());
    }

    public LocalizedSearchFilter(Locale locale) {
        this.locale = locale;
    }

    public LocalizedSearchFilter(T query, Locale locale, int page, int pageSize) {
        super(query, page, pageSize);
        this.locale = locale;
    }

    public LocalizedSearchFilter(T query, Locale locale) {
        super(query);
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
