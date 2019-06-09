package com.github.szabba.quick4j.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.lang.annotation.Annotation;
import java.util.List;

@Value
@Builder
public class Parameter {
    private final String name;
    private final Class<?> type;
    private final List<Annotation> annotations;
}
