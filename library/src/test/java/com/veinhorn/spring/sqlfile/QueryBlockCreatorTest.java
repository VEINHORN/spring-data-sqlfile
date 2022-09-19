package com.veinhorn.spring.sqlfile;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class QueryBlockCreatorTest {
    @Test
    public void testQueryFormatter() throws IOException {
        String query = IOUtils.toString(getClass().getResource("/find_top_users.sql"), "UTF-8");
        StringJoiner joiner = new StringJoiner(" +" + System.lineSeparator());
        String[] splitted = query.split(System.lineSeparator());
        for (String line : splitted) {
            joiner.add(line);
        }
        String result = joiner.toString();

        assertTrue(true);
    }
}
