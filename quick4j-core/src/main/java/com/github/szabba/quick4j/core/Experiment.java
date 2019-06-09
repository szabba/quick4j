package com.github.szabba.quick4j.core;

import com.github.szabba.quick4j.core.generator.Generators;
import com.github.szabba.quick4j.core.result.AttemptFailure;
import com.github.szabba.quick4j.core.result.AttemptReport;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Value
@Builder
public class Experiment {

    private final int runs;
    private final Generators generators;

    public Optional<AttemptFailure> test(final Property property) {
        return IntStream
                .range(0, runs)
                .mapToObj(i -> testOne(property))
                // TODO: Shrinking.
                .flatMap(report -> report
                        .getFailure()
                        .map(Stream::of)
                        .orElse(Stream.empty()))
                .findAny();
    }

    private AttemptReport testOne(final Property property) {
        final List<Object> arguments = generateArguments(property);
        final AttemptFailure result = property
                .runAttempt(arguments)
                .orElse(null);
        return AttemptReport
                .builder()
                .property(property)
                .arguments(arguments)
                .failure(result)
                .build();
    }

    private List<Object> generateArguments(final Property property) {
        return property
                .getParameters()
                .stream()
                .map(generators::generate)
                .collect(Collectors.toList());
    }
}