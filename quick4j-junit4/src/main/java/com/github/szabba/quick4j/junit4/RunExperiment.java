package com.github.szabba.quick4j.junit4;

import com.github.szabba.quick4j.core.*;
import com.github.szabba.quick4j.core.generator.Generator;
import com.github.szabba.quick4j.core.generator.Generators;
import com.github.szabba.quick4j.core.generator.IntGenerator;
import com.github.szabba.quick4j.core.generator.StringGenerator;
import com.github.szabba.quick4j.core.result.AttemptFailure;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class RunExperiment extends Statement {

    private static final Object[] OBJECT_ARRAY_WITNESS = new Object[0];

    private final FrameworkMethod method;
    private final Object test;

    @Override
    public void evaluate() {
        final Property property = buildProperty();
        final Experiment experiment = buildExperiment();
        experiment
                .test(property)
                .map(AttemptFailure::getThrown)
                .ifPresent(this::fail);
    }

    @SneakyThrows
    private void fail(final Throwable exc) {
        throw exc;
    }

    private Property buildProperty() {
        final List<Parameter> parameters = buildParameters();
        return Property
                .builder()
                .name(method.getDeclaringClass().getCanonicalName() + "#" + method.getName())
                .parameters(parameters)
                .runAttempt(this::runAttempt)
                .build();
    }

    private List<Parameter> buildParameters() {
        final java.lang.reflect.Parameter[] parameterArray = method
                .getMethod()
                .getParameters();
        return Stream
                .of(parameterArray)
                .map(this::convertParameter)
                .collect(Collectors.toList());
    }

    private Parameter convertParameter(final java.lang.reflect.Parameter parameter) {
        return Parameter
                .builder()
                .name(parameter.getName())
                .type(parameter.getType())
                .annotations(Arrays.asList(parameter.getAnnotations()))
                .build();
    }

    private Generators buildGenerators() {
        final Set<Generator<?>> generators = Stream
                .of(
                        new IntGenerator(),
                        new StringGenerator())
                .collect(Collectors.toSet());

        return Generators.of(generators);
    }

    private Experiment buildExperiment() {
        final Generators generators = buildGenerators();
        return Experiment
                .builder()
                // TODO: Override with annotation.
                .runs(1000)
                .generators(generators)
                .build();
    }

    @SneakyThrows
    private Optional<AttemptFailure> runAttempt(final List<Object> arguments) {
        final Object[] argumentsArray = arguments.toArray(OBJECT_ARRAY_WITNESS);
        try {
            method.invokeExplosively(test, argumentsArray);
            return Optional.empty();
        } catch (final Throwable thrown) {
            if (indicatesImplementationError(thrown)) {
                throw thrown;
            }
            return Optional.of(AttemptFailure.of(thrown));
        }
    }

    private boolean indicatesImplementationError(final Throwable thrown) {
        return Objects.equals(thrown.getMessage(), "wrong number of arguments");
    }
}
