package com.kovalevaelena371;

import java.lang.reflect.Method;
import java.util.List;


public class TestInst {

    private final Class<?> TestedClass;
    private final List<Method> beforeMethods;
    private final List<Method> afterMethods;
    private final Method test;

    public TestInst(Class<?> TestedClass,
                    List<Method> beforeMethods,
                    List<Method> afterMethods,
                    Method test) {
        this.TestedClass = TestedClass;
        this.beforeMethods = beforeMethods;
        this.afterMethods = afterMethods;
        this.test = test;
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

    public Class<?> getTestedClass() {
        return TestedClass;
    }
}
