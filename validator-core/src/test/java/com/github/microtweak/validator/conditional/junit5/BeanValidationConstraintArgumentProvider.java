package com.github.microtweak.validator.conditional.junit5;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanValidationConstraintArgumentProvider implements ArgumentsProvider, AnnotationConsumer<BeanValidationConstraintSource> {

    private Map<String, File> packagesToScan;

    @Override
    public void accept(BeanValidationConstraintSource source) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        this.packagesToScan = Stream.of(source.value())
                .map(p -> {
                    URL dir = classLoader.getResource( p.replace(".", "/") );
                    return new AbstractMap.SimpleEntry<>(p, dir );
                })
                .filter(e -> Objects.nonNull(e.getValue()))
                .map(p -> new AbstractMap.SimpleEntry<>(p.getKey(), new File( p.getValue().getFile() ) ))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return packagesToScan.entrySet().stream()
                .flatMap(e -> findClasses(e.getValue(), e.getKey()).stream())
                .map(annotationType -> Arguments.of(annotationType));
    }

    private List<Class<?>> findClasses(File directory, String packageName) {
        if (!directory.exists()) {
            return Collections.emptyList();
        }

        final List<Class<?>> classes = new ArrayList<>();

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            }

            else if (file.getName().endsWith(".class")) {
                Class<?> annotationType = null;

                try {
                    final String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    annotationType = Class.forName( className );
                } catch (ClassNotFoundException e) {
                    continue;
                }

                if (annotationType.isAnnotation() && !annotationType.isMemberClass()) {
                    classes.add(annotationType);
                }
            }
        }

        return classes;
    }
}
