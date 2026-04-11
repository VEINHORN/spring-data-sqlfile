package com.veinhorn.spring.sqlfile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to specify that a SQL query for a repository method should be loaded from an external file.
 * The external file should be located in the resources folder of the project.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface SqlFromResource {
    /**
     * Specifies the path to the SQL file relative to the resources directory.
     * <p>
     * Example: {@code @SqlFromResource(path = "sql/find_all_users.sql")}
     *
     * @return the path to the SQL file
     */
    String path();

    /**
     * Specifies the path to an optional count SQL file relative to the resources directory.
     * This is typically used for pagination queries where a separate count query is required.
     * <p>
     * Example: {@code @SqlFromResource(path = "sql/find_all_users.sql", countQueryPath = "sql/count_all_users.sql")}
     *
     * @return the path to the count SQL file, or an empty string if not specified
     */
    String countQueryPath() default "";
}
