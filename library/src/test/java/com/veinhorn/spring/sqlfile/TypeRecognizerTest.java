package com.veinhorn.spring.sqlfile;

import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TypeRecognizerTest {
    @Test
    public void testTypeRecognizer() {
        String methodType = "()java.util.List<com.veinhorn.spring.sqlfile.example.domain.User>";
        TypeName typeName = new TypeRecognizer().recognize(methodType);
        assertTrue(true);
    }

    @Test
    public void testSimpleType() {
        TypeName typeName = new TypeRecognizer().recognize("()com.domain.User");
        assertTrue(true);
    }
}
