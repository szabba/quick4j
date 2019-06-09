package com.github.szabba.quick4j.core;

import com.github.szabba.quick4j.core.result.AttemptFailure;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Value
@Builder
public class Property {
    private final String name;
    private final List<Parameter> parameters;
    private final Function<List<Object>, Optional<AttemptFailure>> runAttempt;

    public Optional<AttemptFailure> runAttempt(final List<Object> arguments) {
        return runAttempt.apply(arguments);
    }
}