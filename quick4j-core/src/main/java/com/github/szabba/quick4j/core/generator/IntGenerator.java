package com.github.szabba.quick4j.core.generator;

import java.util.concurrent.ThreadLocalRandom;

public class IntGenerator implements Generator<Integer> {

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public Integer generate() {
        return ThreadLocalRandom.current().nextInt();
    }
}
