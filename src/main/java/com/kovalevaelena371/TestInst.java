package com.kovalevaelena371;

import com.kovalevaelena371.annotation.Test;

import java.lang.reflect.Method;
import java.util.List;


public class TestInst {

    private final Class<?> testClass;
    private final List<Method> beforeMethods;
    private final List<Method> afterMethods;
    private final Method test;

    public TestInst(Class<?> TestedClass,
                    List<Method> beforeMethods,
                    List<Method> afterMethods,
                    Method test) {
        this.testClass = TestedClass;
        this.beforeMethods = beforeMethods;
        this.afterMethods = afterMethods;
        this.test = test;
    }

    public boolean expectsException() {
        return getExpectedException() != null;
    }

    public Class<? extends Throwable> getExpectedException() {
        Test annotation = test.getAnnotation(Test.class);
        if (annotation.expected() == Test.None.class) {
            return null;
        } else {
            return annotation.expected();
        }
    }

    public List<Method> getBeforeMethods() {
        return beforeMethods;
    }

    public List<Method> getAfterMethods() {
        return afterMethods;
    }

    public Method getTest() {
        return test;
    }

    public Class<?> getTestClass() {
        return testClass;
    }
}
