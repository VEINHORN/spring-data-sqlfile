package com.veinhorn.spring.sqlfile;

import com.squareup.javapoet.CodeBlock;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class QueryBlockCreator {
    private String rawQuery;

    public QueryBlockCreator(String rawQuery) {
        this.rawQuery = rawQuery;
    }

    public CodeBlock create() {
        List<String> queryLines = Arrays
                .stream(rawQuery.split(System.lineSeparator()))
                .map(line -> "\"" + line + "\"")
                .collect(Collectors.toList());
        StringJoiner joiner = new StringJoiner(" +\n");
        for (String line : queryLines) {
            joiner.add(line);
        }
        return codeBlock(joiner.toString());
    }

    private CodeBlock codeBlock(String query) {
        return CodeBlock
                .builder()
                .add(query)
                .build();
    }
}
