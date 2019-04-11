package com.veinhorn.spring.sqlfile;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import org.apache.commons.io.IOUtils;
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
            if (annotatedElement.getKind().isInterface()) {
                // Here we create type using JavaPoet library
                System.out.println("full repository name = " + annotatedElement.toString());

                String repositoryName = annotatedElement.getSimpleName().toString() + "Generated";
                TypeSpec enrichedRepo = TypeSpec
                        .interfaceBuilder(repositoryName)
                        .addMethod(
                                MethodSpec
                                        .methodBuilder("test")
                                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                        .returns(void.class)
                                        .addParameter(String[].class, "args") // here we need add parameters
                                        .addAnnotation(
                                                AnnotationSpec
                                                        .builder(Query.class)
                                                        .addMember("value", "\"some sql here\"") // here we should put SQL from file
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();

                System.out.println("new full repo name = " + repositoryName);
                String packageName = annotatedElement.toString().substring(0, annotatedElement.toString().lastIndexOf("."));
                JavaFile javaFile = JavaFile.builder(packageName, enrichedRepo).build();

                try {
                    Filer filer = processingEnv.getFiler();
                    JavaFileObject javaFileObject = filer.createSourceFile(repositoryName, null);
                    Writer writer = javaFileObject.openWriter();
                    writer.write(javaFile.toString());
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Cannot read SQL query file.");
                }

                List<? extends Element> methods = annotatedElement.getEnclosedElements();
                // here we should for each method check annotation
                methods.forEach(p -> {
                    System.out.println("method = " + p.getSimpleName().toString());

                    Annotation annotation = p.getAnnotation(SqlFile.class);
                    System.out.println(((SqlFile) annotation).path());
                });

                System.out.println("name = " + annotatedElement.getSimpleName().toString());
                System.out.println("class.yo, size = " + elements.size());
                System.out.println("source code = " + annotatedElement.toString());
            }
        }

            /*Filer filer = processingEnv.getFiler();
            FileObject file = filer.getResource(StandardLocation.CLASS_OUTPUT, "", "query.sql"); // sql file we need to pass here

            String result = IOUtils.toString(file.openInputStream(), "UTF-8");*/
            //System.out.println(result);



            /*JavaFile javaFile = create();

            JavaFileObject javaFileObject = filer.createSourceFile("HelloWorld", null);
            Writer writer = javaFileObject.openWriter();
            writer.write(javaFile.toString());
            writer.close();*/


            // filer.createSourceFile("test", (Element[]) create().toArray());


        return true;
    }

    private JavaFile create() {
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC/*, Modifier.STATIC*/)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        // this package we need to pass here from arguments
        JavaFile file = JavaFile.builder("com.veinhorn.spring.sqlfile.example", helloWorld).build();

        return file;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(SqlFile.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}
