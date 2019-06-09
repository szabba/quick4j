package com.github.szabba.quick4j.core.result;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
public class AttemptFailure {

    private final boolean failed;
    private final String message;
    private final Throwable thrown;

    public static AttemptFailure of(final Throwable thrown) {
        return AttemptFailure.of(true, thrown.getMessage(), thrown);
    }
}