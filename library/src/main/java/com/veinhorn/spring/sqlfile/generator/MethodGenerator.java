package com.veinhorn.spring.sqlfile.generator;

import com.squareup.javapoet.*;
import com.veinhorn.spring.sqlfile.QueryBlockCreator;
import com.veinhorn.spring.sqlfile.SqlFromResource;
import com.veinhorn.spring.sqlfile.TypeRecognizer;
import org.springframework.data.jpa.repository.Query;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
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

    // method annotations from incoming Repository method
    private List<? extends AnnotationMirror> methodAnnotations;

    public MethodGenerator(String methodName, String sqlQuery, String methodType,
                           List<String> paramTypes, List<String> paramNames,
                           List<? extends AnnotationMirror> methodAnnotations) {
        this.methodName = methodName;
        this.sqlQuery = sqlQuery;
        this.methodType = methodType;
        this.paramTypes = paramTypes;
        this.paramNames = paramNames;
        this.methodAnnotations = methodAnnotations;
    }

    @Override
    public MethodSpec generate() {
        System.out.println("method type = " + methodType);

        return MethodSpec
                .methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(getReturnType())
                .addParameters(createParameters())
                .addAnnotations(createAnnotations())
                .build();
    }

    private List<AnnotationSpec> createAnnotations() {
        List<AnnotationSpec> annotations = new ArrayList<>();
        annotations.add(createQueryAnnotation());
        annotations.addAll(
                methodAnnotations
                        .stream()
                        .filter(annotation -> !annotationType(annotation).equals(SqlFromResource.class.getTypeName()))
                        .map(annotation -> {
                            AnnotationSpec.Builder annotationSpec = AnnotationSpec
                                    .builder(ClassName.bestGuess(((AnnotationMirror) annotation).getAnnotationType().toString()));

                            ((AnnotationMirror) annotation).getElementValues().keySet().forEach(key -> {
                                annotationSpec.addMember(key.toString().replaceAll("\\(\\)", ""), ((AnnotationMirror) annotation).getElementValues().get(key).toString());
                            });

                            return annotationSpec.build();
                        })
                .collect(Collectors.toList())
        );
        return annotations;
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

    private AnnotationSpec createQueryAnnotation() {
        return AnnotationSpec
                .builder(Query.class)
                .addMember("value", new QueryBlockCreator(sqlQuery).create())
                .addMember("nativeQuery", "true")
                .build();
    }

    private String annotationType(AnnotationMirror annotation) {
        return annotation.getAnnotationType().toString();
    }
}
