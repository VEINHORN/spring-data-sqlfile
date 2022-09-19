package com.veinhorn.spring.sqlfile;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.veinhorn.spring.sqlfile.generator.MethodsGenerator;
import com.veinhorn.spring.sqlfile.generator.TypeGenerator;
import org.springframework.stereotype.Repository;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
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
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Repository.class);

        for (Element annotatedElement : elements) {
            System.out.println("element name = " + annotatedElement.getSimpleName().toString());

            // Here we check that our Repositories is interfaces
            if (isInterface(annotatedElement)) {
                // Here we create type using JavaPoet library
                System.out.println("full repository name = " + annotatedElement.toString());

                // Getting JpaRepository types for entity and key
                List<? extends TypeMirror> interfaces = ((TypeElement) annotatedElement).getInterfaces();

                String repositoryName = annotatedElement.getSimpleName().toString() + getClassPostfix();
                TypeSpec enrichedRepo = new TypeGenerator(
                        repositoryName,
                        getEntityType(interfaces),
                        getKeyType(interfaces),
                        new MethodsGenerator(getQueryRetriever()).getMethods(annotatedElement)
                ).generate();

                saveJavaFile(repositoryName, JavaFile.builder(getPackageName(annotatedElement), enrichedRepo).build());

                System.out.println("new full repo name = " + repositoryName);
                System.out.println("name = " + annotatedElement.getSimpleName().toString());
                System.out.println("source code = " + annotatedElement.toString());
            }
        }

        return true;
    }

    private boolean isInterface(Element annotatedElement) {
        return annotatedElement.getKind().isInterface() && !annotatedElement.getSimpleName().toString().endsWith(getClassPostfix());
    }

    private String getEntityType(List<? extends TypeMirror> interfaces) {
        return ((DeclaredType) interfaces.get(0)).getTypeArguments().get(0).toString();
    }

    private String getKeyType(List<? extends TypeMirror> interfaces) {
        return ((DeclaredType) interfaces.get(0)).getTypeArguments().get(1).toString();
    }

    private String getPackageName(Element annotatedElement) {
        return annotatedElement.toString().substring(0, annotatedElement.toString().lastIndexOf("."));
    }

    private void saveJavaFile(String repositoryName, JavaFile javaFile) {
        try {
            Filer filer = processingEnv.getFiler();
            JavaFileObject javaFileObject = filer.createSourceFile(repositoryName, null);
            Writer writer = javaFileObject.openWriter();
            writer.write(javaFile.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Cannot read SQL query file.");
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(SqlFromResource.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    private String getClassPostfix() {
        return processingEnv.getOptions().getOrDefault("classPostfix", "Impl");
    }

    protected QueryRetriever getQueryRetriever() {
        return new QueryRetriever(processingEnv);
    }
}
