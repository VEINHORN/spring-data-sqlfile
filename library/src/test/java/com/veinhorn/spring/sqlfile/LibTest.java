package com.veinhorn.spring.sqlfile;

import com.squareup.javapoet.*;
import org.junit.Test;
import org.springframework.data.jpa.repository.Query;

import javax.lang.model.element.Modifier;

public class LibTest {
    @Test
    public void test() {
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        // TypeSpec.classBuilder(String.class);

        System.out.println(helloWorld.originatingElements.size());
    }

    @Test
    public void test2() {
        AnnotationSpec spec = AnnotationSpec
                .builder(Query.class)
                .addMember("value", "\"sdfsdf\"")
                .addMember("nativeQuery", "true")
                .build();

        String fullName = "com.veinhorn.spring.sqlfile.example.domain.User";


        String test = "test";
    }
}
