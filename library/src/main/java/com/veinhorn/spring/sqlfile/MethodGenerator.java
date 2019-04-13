package com.veinhorn.spring.sqlfile;

import com.squareup.javapoet.*;
import org.springframework.data.jpa.repository.Query;

import javax.lang.model.element.Modifier;

/**
 * Generates methods for Repository interface
 */
public class MethodGenerator implements Generator<MethodSpec> {
    private String methodName;
    private String sqlQuery;
    private String methodType;

    public MethodGenerator(String methodName, String sqlQuery, String methodType) {
        this.methodName = methodName;
        this.sqlQuery = sqlQuery;
        this.methodType = methodType;
    }

    @Override
    public MethodSpec generate() {
        System.out.println("method type = " + methodType);

        return MethodSpec
                .methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(getReturnType())
                .addAnnotation(getAnnotation())
                .build();
    }

    private TypeName getReturnType() {
        return new TypeRecognizer().recognize(methodType);
    }

    private AnnotationSpec getAnnotation() {
        return AnnotationSpec
                .builder(Query.class)
                .addMember("value", getQuery())
                .addMember("nativeQuery", "true")
                .build();
    }

    private String getQuery() {
        return String.format("\"%s\"", sqlQuery);
    }
}
