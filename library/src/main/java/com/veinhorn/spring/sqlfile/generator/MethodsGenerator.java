package com.veinhorn.spring.sqlfile.generator;

import com.squareup.javapoet.MethodSpec;
import com.veinhorn.spring.sqlfile.QueryRetriever;
import com.veinhorn.spring.sqlfile.SqlFromResource;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Now, it not only generates methods, but also retrieves info from internal Java objects
public class MethodsGenerator {
    private QueryRetriever queryRetriever;

    public MethodsGenerator(QueryRetriever queryRetriever) {
        this.queryRetriever = queryRetriever;
    }

    public List<MethodSpec> getMethods(Element annotatedElement) {
        return annotatedElement
                .getEnclosedElements()
                .stream()
                .map(method -> {
                    Annotation annotation = ((Element) method).getAnnotation(SqlFromResource.class);
                    String queryPath = ((SqlFromResource) annotation).path();

                    System.out.println("method = " + method.getSimpleName().toString());
                    System.out.println("sql file path = " + queryPath);

                    ExecutableElement executableElement = ((ExecutableElement) ((Element) method));
                    List<? extends VariableElement> m = executableElement.getParameters();

                    return newMethod(method, queryPath, m);
                })
                .collect(Collectors.toList());
    }

    private MethodSpec newMethod(Element method,
                                 String queryPath,
                                 List<? extends VariableElement> m) {
        List<? extends AnnotationMirror> methodAnnotations = ((Element) method).getAnnotationMirrors();

        methodAnnotations.forEach(a -> {
            System.out.println(a.getAnnotationType().toString());
            a.getElementValues().keySet().forEach(r -> {
                System.out.println(r.toString());
                System.out.println(a.getElementValues().get(r));
            });
        });

        try {
            return new MethodGenerator(
                    ((Element) method).getSimpleName().toString(),
                    queryRetriever.retrieve(queryPath),
                    ((Element) method).asType().toString(),
                    getParamTypes(method),
                    getParamNames(m),
                    methodAnnotations
            ).generate();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private List<String> getParamTypes(Element method) {
        ExecutableType executableType = (ExecutableType) ((ExecutableElement) method).asType();
        List<? extends TypeMirror> types = executableType.getParameterTypes();

        if (types.isEmpty()) return new ArrayList<>();

        return types
                .stream()
                .map(p -> {
                    String paramType = ((TypeMirror) p).toString();
                    System.out.println(paramType);
                    return paramType;
                })
                .collect(Collectors.toList());
    }

    private List<String> getParamNames(List<? extends VariableElement> m) {
        if (m.isEmpty()) return new ArrayList<>();

        return m
                .stream()
                .map(p -> {
                    String paramName = ((VariableElement) p).getSimpleName().toString();
                    System.out.println(paramName);
                    return paramName;
                })
                .collect(Collectors.toList());
    }
}
