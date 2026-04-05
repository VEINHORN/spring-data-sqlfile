package com.veinhorn.spring.sqlfile.experimental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface RepositoryMethods {
    RepositoryMethod[] value();
}
