package com.veinhorn.spring.sqlfile;

import com.squareup.javapoet.MethodSpec;
import com.veinhorn.spring.sqlfile.generator.MethodGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class MethodGeneratorTest {
    @Test
    public void testMethodGenerator() {
        String methodName = "findAll";
        String sqlQuery = "select * from users";
        String methodType = "()java.util.List<com.veinhorn.spring.sqlfile.example.domain.User>";

        MethodSpec methodSpec = new MethodGenerator(methodName, sqlQuery, null, methodType, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()).generate();
        System.out.println(methodSpec.toString());

        assertTrue(true);
    }

    @Test
    public void testMethodGeneratorWithCountQuery() {
        String methodName = "findAll";
        String sqlQuery = "select * from users";
        String countQuery = "select count(*) from users";
        String methodType = "()org.springframework.data.domain.Page<com.veinhorn.spring.sqlfile.example.domain.User>";

        MethodSpec methodSpec = new MethodGenerator(methodName, sqlQuery, countQuery, methodType, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()).generate();
        System.out.println(methodSpec.toString());

        assertTrue(methodSpec.toString().contains("countQuery = \"select count(*) from users\""));
    }
}
