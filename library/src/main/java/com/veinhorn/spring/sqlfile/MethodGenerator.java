package com.veinhorn.spring.sqlfile;

import com.squareup.javapoet.*;
import org.springframework.data.jpa.repository.Query;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Generates methods for Repository interface
 */
public class MethodGenerator implements Generator<MethodSpec> {
    private String methodName;
    private String sqlQuery;
    private String methodType;

    private List<String> paramTypes;
    private List<String> paramNames;

    public MethodGenerator(String methodName, String sqlQuery, String methodType, List<String> paramTypes, List<String> paramNames) {
        this.methodName = methodName;
        this.sqlQuery = sqlQuery;
        this.methodType = methodType;
        this.paramTypes = paramTypes;
        this.paramNames = paramNames;
    }

    @Override
    public MethodSpec generate() {
        System.out.println("method type = " + methodType);

        return MethodSpec
                .methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(getReturnType())
                .addParameters(createParameters())
                .addAnnotation(getAnnotation())
                .build();
    }

    private List<ParameterSpec> createParameters() {
        return IntStream
                .range(0, paramTypes.size())
                .mapToObj(i -> {
                    if ("int".equalsIgnoreCase(paramTypes.get(i)))
                        return ParameterSpec.builder(ClassName.INT, paramNames.get(i)).build();

                    return ParameterSpec
                            .builder(ClassName.bestGuess(paramTypes.get(i)), paramNames.get(i))
                            .build();
                })
                .collect(Collectors.toList());
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
