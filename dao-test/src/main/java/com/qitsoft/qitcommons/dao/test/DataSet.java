package com.qitsoft.qitcommons.dao.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation to specify the datasets to execute before the test. Each dataset should be without expension and
 * be placed in the "dataSets" folder.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DataSet {

    /**
     * The list of datasets to execute without expension.
     * @return
     */
    String[] value();
}
