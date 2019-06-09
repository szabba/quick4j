package com.github.szabba.quick4j.core.exception;

import com.github.szabba.quick4j.core.result.AttemptFailure;
import com.github.szabba.quick4j.core.Property;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public class PropertyFalsifiedException extends RuntimeException {

    private final String name;
    private final String[] arguments;

    public PropertyFalsifiedException(final Property property, final List<Object> arguments, final AttemptFailure failure) {
        super(failure.getThrown());
        this.name = property.getName();
        this.arguments = arguments
                .stream()
                .map(Objects::toString)
                .toArray(String[]::new);
    }

    public String getMessage() {
        return "Property " + name + " failed with arguments " + Arrays.asList(arguments) +  ": " + getCause().getMessage();
    }
}
