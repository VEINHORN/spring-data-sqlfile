package com.veinhorn.spring.sqlfile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface SqlFromResource {
    /**
     * Path to the SQL file
     * @return
     */
    String path();
}
