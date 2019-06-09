package com.github.szabba.quick4j.core.result;

import java.util.List;
import java.util.Optional;

import com.github.szabba.quick4j.core.Property;
import com.github.szabba.quick4j.core.result.AttemptFailure;
import lombok.*;

@Value
@Builder
public class AttemptReport {

    @NonNull
    private final Property property;
    @NonNull
    private final List<Object> arguments;
    private final AttemptFailure failure;

    public Optional<AttemptFailure> getFailure() {
        return Optional.ofNullable(failure);
    }
}