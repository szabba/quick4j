package com.github.szabba.quick4j.junit4;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Quick4j.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Quick4jRunnerTest {

    @Test
    public void $000_doesNotFailDueToANoArgumentsTest() {
    }

    @Test
    public void $001_doesNotFailDueToATestWithArguments(final int x) {
    }

    @Test(expected = AssertionError.class)
    public void $002_throwsTheException(final int x) {
        assertThat(Math.abs(x)).isLessThan(0);
    }

    private static final Set<Integer> xsUsed = new HashSet<>();

    @Test
    public void $003_rememberesArguments(final int x) {
        xsUsed.add(x);
    }

    @Test
    public void $004_observesDifferentValues() {
        assertThat(xsUsed.size()).isGreaterThan(100);
    }

    @Test
    public void $005_handlesDifferentArgumentCounts(final int x, final int y) {
        assertThat(-x < -y).isEqualTo(x > y);
    }

    @Test
    public void $006_handleDifferentArgumentTypes(final String s) {
        assertThat(s).isInstanceOf(String.class);
    }
}