package com.veinhorn.spring.sqlfile;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.io.IOUtils;
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
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Repository.class);

        for (Element annotatedElement : elements) {
            System.out.println("kind of element = " + annotatedElement.getKind().toString());

            System.out.println("element = " + annotatedElement.getKind().toString());
            System.out.println("element name = " + annotatedElement.getSimpleName().toString());

            if (annotatedElement.getKind().isInterface()) {
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

        try {
            Filer filer = processingEnv.getFiler();
            FileObject file = filer.getResource(StandardLocation.CLASS_OUTPUT, "", "query.sql"); // sql file we need to pass here

            String result = IOUtils.toString(file.openInputStream(), "UTF-8");
            //System.out.println(result);



            JavaFile javaFile = create();
            //System.out.println(javaFile.toString());

            JavaFileObject javaFileObject = filer.createSourceFile("HelloWorld", null);
            Writer writer = javaFileObject.openWriter();
            writer.write(javaFile.toString());
            writer.close();


            // filer.createSourceFile("test", (Element[]) create().toArray());
        } catch (IOException e) {
            System.out.println("Cannot read SQL query file.");
        }

        return false;
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
