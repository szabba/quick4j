package com.github.szabba.quick4j.core.generator;

import com.github.szabba.quick4j.core.Parameter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor(staticName = "of")
public final class Generators {

    private static final Map<Class<?>, Class<?>> BOXED = buildBoxed();

    private static Map<Class<?>, Class<?>> buildBoxed() {
        final Map<Class<?>, Class<?>> boxed = new HashMap<>();
        boxed.put(Void.TYPE, Void.class);
        boxed.put(Boolean.TYPE, Boolean.class);
        boxed.put(Character.TYPE, Character.class);
        boxed.put(Short.TYPE, Short.class);
        boxed.put(Integer.TYPE, Integer.class);
        boxed.put(Long.TYPE, Long.class);
        boxed.put(Float.TYPE, Float.class);
        boxed.put(Double.TYPE, Double.class);
        return Collections.unmodifiableMap(boxed);
    }

    private final Set<Generator<?>> generators;
    private final ConcurrentMap<Parameter, Generator<?>> parameterGenerators = new ConcurrentHashMap<>();

    public Object generate(final Parameter parameter) {
        final Generator<?> generator = find(parameter);
        return generator.generate();
    }

    private Generator<?> find(final Parameter parameter) {
        return parameterGenerators.computeIfAbsent(parameter, this::findFromScratch);
    }

    private Generator<?> findFromScratch(final Parameter parameter) {
        final List<Generator<?>> found = findAllMatching(parameter);
        checkFound(found, parameter);
        return found.get(0);
    }

    private List<Generator<?>> findAllMatching(final Parameter parameter) {
        return generators
                .stream()
                .filter(gen -> matchesType(gen, parameter) && matchesAnnotation(gen, parameter))
                .distinct()
                .collect(Collectors.toList());
    }

    private boolean matchesType(final Generator<?> gen, final Parameter parameter) {
        final Class<?> target = parameter.getType();
        final Class<?> normalized = BOXED.getOrDefault(target, target);
        return normalized == gen.getType();
    }

    private Boolean matchesAnnotation(final Generator<?> generator, final Parameter parameter) {
        return generator
                .getAnnotationType()
                .map(parameter.getAnnotations()::contains)
                .orElse(true);
    }

    private void checkFound(final List<Generator<?>> matchingGenerators, final Parameter parameter) {
        // TODO: Include diagnostic information when this method fails.
        if (matchingGenerators.size() == 0) {
            throw new IllegalStateException("No generators found for paramter " + parameter + ".");
        }

        if (matchingGenerators.size() > 1) {
            final String message =
                    "Multiple matching generators found for parameter " + parameter + ": " + matchingGenerators + ".";
            throw new IllegalStateException(message);
        }
    }
}
