package com.veinhorn.spring.sqlfile;

import org.apache.commons.io.IOUtils;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class QueryRetriever {
    private ProcessingEnvironment environment;

    public QueryRetriever(ProcessingEnvironment environment) {
        this.environment = environment;
    }

    /**
     * It's made protected for test purposes only, to be able to override it in tests
     * @param queryPath
     * @return
     * @throws IOException
     */
    public String retrieve(String queryPath) throws IOException {
        Filer filer = environment.getFiler();
        String relativePackage = "";
        FileObject queryFile = filer.getResource(StandardLocation.CLASS_OUTPUT, relativePackage, queryPath);

        return IOUtils.toString(queryFile.openInputStream(), StandardCharsets.UTF_8);
    }
}
