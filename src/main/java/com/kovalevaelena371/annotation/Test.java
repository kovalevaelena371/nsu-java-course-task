package com.kovalevaelena371.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is for methods which will be executed as tests.
 * Optionally an expected exception can be specified.
 * If an expected exception is caught during execution it is considered as a success otherwise as a failure.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    class None extends Throwable {}

    /**
     * This method returns the expected exception if it was specified otherwise — the default exception.
     */
    Class<? extends Throwable> expected() default None.class;
}