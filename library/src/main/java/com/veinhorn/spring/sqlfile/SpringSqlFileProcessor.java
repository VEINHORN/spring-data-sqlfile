package com.veinhorn.spring.sqlfile;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import org.apache.commons.io.IOUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AutoService(Processor.class)
public class SpringSqlFileProcessor extends AbstractProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // Here we get all Repository classes
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Repository.class);

        for (Element annotatedElement : elements) {
            System.out.println("kind of element = " + annotatedElement.getKind().toString());

            System.out.println("element = " + annotatedElement.getKind().toString());
            System.out.println("element name = " + annotatedElement.getSimpleName().toString());

            // Here we check that our Repositories is interfaces
            if (annotatedElement.getKind().isInterface() && !annotatedElement.getSimpleName().toString().endsWith("Generated")) {
                // Here we create type using JavaPoet library
                System.out.println("full repository name = " + annotatedElement.toString());

                String repositoryName = annotatedElement.getSimpleName().toString() + "Generated";

                List<MethodSpec> methods = annotatedElement
                        .getEnclosedElements()
                        .stream()
                        .map(method -> {
                            Annotation annotation = ((Element) method).getAnnotation(SqlFromResource.class);

                            System.out.println("method = " + method.getSimpleName().toString());
                            String queryPath = ((SqlFromResource) annotation).path();
                            System.out.println("sql file path = " + queryPath);

                            String methodReturnType = ((Element) method).asType().toString().replaceAll("\\(.*\\)", ""); // replace parameter types
                            System.out.println("method return type = " + methodReturnType);

                            try {
                                TypeName complexType = ParameterizedTypeName.get(
                                        ClassName.get("java.util", "List"),
                                        ClassName.get("com.veinhorn.spring.sqlfile.example.domain", "User")
                                                /*Util.ClassUtil.getPackageName(methodReturnType),
                                                Util.ClassUtil.getSimpleClassName(methodReturnType))*/
                                );

                                return MethodSpec
                                        .methodBuilder(((Element) method).getSimpleName().toString())
                                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                        .returns(complexType/*ClassName.get(
                                                Util.ClassUtil.getPackageName(methodReturnType),
                                                Util.ClassUtil.getSimpleClassName(methodReturnType))*/
                                        )
                                        .addAnnotation(
                                                AnnotationSpec
                                                        .builder(Query.class)
                                                        .addMember("value", String.format("\"%s\"", getQuery(queryPath)))
                                                        .addMember("nativeQuery", "true")
                                                        .build()
                                        )
                                        .build();
                            } catch (IOException e) {
                                System.out.println(e.getStackTrace().toString());
                                return null;
                            }
                        })
                        .collect(Collectors.toList());

                TypeSpec enrichedRepo = TypeSpec
                        .interfaceBuilder(repositoryName)
                        .addAnnotation(Repository.class)
                        .addSuperinterface(ParameterizedTypeName.get(
                                ClassName.get(JpaRepository.class),
                                ClassName.get("com.veinhorn.spring.sqlfile.example.domain", "User"),
                                ClassName.get(Integer.class)
                        ))
                        .addModifiers(Modifier.PUBLIC)
                        .addMethods(methods)
                        .build();

                System.out.println("new full repo name = " + repositoryName);
                String packageName = annotatedElement.toString().substring(0, annotatedElement.toString().lastIndexOf("."));
                JavaFile javaFile = JavaFile.builder(packageName, enrichedRepo).build();
                // Trying to write into file
                try {
                    Filer filer = processingEnv.getFiler();
                    JavaFileObject javaFileObject = filer.createSourceFile(repositoryName, null);
                    Writer writer = javaFileObject.openWriter();
                    writer.write(javaFile.toString());
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Cannot read SQL query file.");
                }

                System.out.println("name = " + annotatedElement.getSimpleName().toString());
                // System.out.println("class.yo, size = " + elements.size());
                System.out.println("source code = " + annotatedElement.toString());
            }
        }

        return true;
    }

    private String getQuery(String queryPath) throws IOException {
        Filer filer = processingEnv.getFiler();
        FileObject queryFile = filer.getResource(StandardLocation.CLASS_OUTPUT, "", queryPath);
        return IOUtils
                .toString(queryFile.openInputStream(), "UTF-8")
                .replaceAll(System.lineSeparator(), " "); // use this to create one line query string
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(SqlFromResource.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}
