package com.veinhorn.spring.sqlfile.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.lang.model.element.Modifier;
import java.util.List;

/**
 * Generates result Repository type
 */
public class TypeGenerator implements Generator<TypeSpec> {
    private String repositoryName;
    private String entityType; // type of entity in JpaRepository
    private String keyType;    // type of key in JpaRepository

    private List<MethodSpec> methods; // methods inside JpaRepository

    public TypeGenerator(String repositoryName, String entityType, String keyType, List<MethodSpec> methods) {
        this.repositoryName = repositoryName;
        this.entityType = entityType;
        this.keyType = keyType;
        this.methods = methods;
    }

    @Override
    public TypeSpec generate() {
        return TypeSpec
                .interfaceBuilder(repositoryName)
                .addAnnotation(Repository.class)
                .addSuperinterface(ParameterizedTypeName.get(
                        ClassName.get(JpaRepository.class),
                        ClassName.bestGuess(entityType),
                        ClassName.bestGuess(keyType)
                ))
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methods)
                .build();
    }
}
