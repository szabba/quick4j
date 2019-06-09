package com.github.szabba.quick4j.junit4;

import org.junit.internal.runners.statements.InvokeMethod;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Quick4j extends BlockJUnit4ClassRunner {

    private static final Predicate<String> SHOULD_HAVE_NO_PARAMETERS = Pattern
            .compile("Method [A-Za-z_$][A-Za-z0-9_$]+ should have no parameters")
            .asPredicate();

    /**
     * Creates a Quick4j runner to run {@code klass}
     *
     * @param klass
     * @throws InitializationError if the test class is malformed.
     */
    public Quick4j(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
        final Method rawMethod = method.getMethod();
        if (rawMethod.getParameterTypes().length > 0) {
            return new RunExperiment(method, test);
        }
        return new InvokeMethod(method, test);
    }

    @Override
    protected void collectInitializationErrors(final List<Throwable> errors) {
        super.collectInitializationErrors(errors);
        errors.removeIf(this::complainsAboutParameters);
    }

    private boolean complainsAboutParameters(final Throwable throwable) {
        return SHOULD_HAVE_NO_PARAMETERS.test(throwable.getMessage());
    }
}