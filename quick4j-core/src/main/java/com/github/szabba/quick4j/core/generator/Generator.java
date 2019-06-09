package com.github.szabba.quick4j.core.generator;

import java.lang.annotation.Annotation;
import java.util.Optional;

public interface Generator<A> {

    Class<A> getType();

    default Optional<Class<? extends Annotation>> getAnnotationType() {
        return Optional.empty();
    }

    A generate();
}
