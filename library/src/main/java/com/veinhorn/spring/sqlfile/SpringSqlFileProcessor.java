package com.veinhorn.spring.sqlfile;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import org.apache.commons.io.IOUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Collections;
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

                List<? extends TypeMirror> interfaces = ((TypeElement) annotatedElement).getInterfaces();
                String entityType = ((DeclaredType) interfaces.get(0)).getTypeArguments().get(0).toString();
                String keyType = ((DeclaredType) interfaces.get(0)).getTypeArguments().get(1).toString();

                // System.out.println("size = " + ;

                List<MethodSpec> methods = annotatedElement
                        .getEnclosedElements()
                        .stream()
                        .map(method -> {
                            Annotation annotation = ((Element) method).getAnnotation(SqlFromResource.class);
                            String queryPath = ((SqlFromResource) annotation).path();

                            System.out.println("method = " + method.getSimpleName().toString());
                            System.out.println("sql file path = " + queryPath);

                            ExecutableElement executableElement = ((ExecutableElement) ((Element) method));
                            List<? extends VariableElement> m = executableElement.getParameters();
                            if (!m.isEmpty()) {
                                System.out.println(m.get(0).getSimpleName().toString());
                            }

                            // List<String> parameterNames = m.isEmpty() ? Collections.emptyList() :
                            try {
                                return new MethodGenerator(
                                        ((Element) method).getSimpleName().toString(),
                                        getQuery(queryPath),
                                        ((Element) method).asType().toString()
                                ).generate();
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
                                ClassName.bestGuess(entityType),
                                ClassName.bestGuess(keyType)
                                /*ClassName.get("com.veinhorn.spring.sqlfile.example.domain", "User"),
                                ClassName.get(Integer.class)*/
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
