package com.veinhorn.spring.sqlfile;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public class TypeRecognizer {
    private static final String REGEX = "\\(.*\\)";

    public TypeName recognize(String inputType) {
        inputType = inputType.replaceAll(REGEX, "");

        if (inputType.contains("<") || inputType.contains(">")) {
            String outerType = inputType.substring(0, inputType.indexOf("<"));
            String innerType = inputType.substring(inputType.indexOf("<") + 1, inputType.lastIndexOf(">"));

            return ParameterizedTypeName.get(
                    ClassName.bestGuess(outerType),
                    ClassName.bestGuess(innerType)
            );
        } else {
            return ClassName.bestGuess(inputType);
        }
    }
}
