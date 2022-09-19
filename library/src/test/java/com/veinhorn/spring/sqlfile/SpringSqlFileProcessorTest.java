package com.veinhorn.spring.sqlfile;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SpringSqlFileProcessorTest {
    @Test
    public void testAnnotationProcessor() {
        SpringSqlFileProcessor processor = new SpringSqlFileProcessor() {
            @Override
            protected String getQuery(String queryPath) throws IOException {
                return IOUtils.toString(SpringSqlFileProcessorTest.class.getResource("/find_top_users.sql"), StandardCharsets.UTF_8);
            }
        };

        Compilation compilation = Compiler
                .javac()
                .withOptions("-AclassPostfix=Impl")
                .withProcessors(processor)
                .compile(JavaFileObjects.forResource("UserRepository.java"));

        CompilationSubject.assertThat(compilation).succeeded();
    }
}
