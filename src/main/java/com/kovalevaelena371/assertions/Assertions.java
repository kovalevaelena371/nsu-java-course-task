package com.kovalevaelena371.assertions;

import com.kovalevaelena371.exception.AssertionError;


public class Assertions {
    public static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError(expected + " != " + actual);
        }
    }

    public static void assertTrue(boolean cond) {
        if (!cond) {
            throw new AssertionError("false");
        }
    }
}
