package com.veinhorn.spring.sqlfile;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.junit.Test;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

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
        // TypeSpec.classBuilder(ClassName.get(String.class)).superclass(TypeName.get())
    }
}
