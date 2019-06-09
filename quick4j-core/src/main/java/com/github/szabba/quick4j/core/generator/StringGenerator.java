package com.github.szabba.quick4j.core.generator;


import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class StringGenerator implements Generator<String> {

    private static final int LENGTH = 10;
    private static final int MIN_ASCII_PRINTABLE = 32;
    private static final int MAX_ASCII_PRINTABLE = 126;

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String generate() {
        final StringBuilder builder = new StringBuilder();
        IntStream
                .range(0, LENGTH)
                .mapToObj(i -> nextChar())
                .forEach(builder::append);
        return builder.toString();
    }
    private char nextChar() {
        return (char) ThreadLocalRandom
                .current()
                .nextInt(MIN_ASCII_PRINTABLE, MAX_ASCII_PRINTABLE + 1);
    }
}
