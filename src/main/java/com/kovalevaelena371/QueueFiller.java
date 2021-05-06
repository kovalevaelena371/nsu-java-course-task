package com.kovalevaelena371;

import com.kovalevaelena371.annotation.After;
import com.kovalevaelena371.annotation.Before;
import com.kovalevaelena371.annotation.Test;
import com.kovalevaelena371.TesterKit.BooleanHolder;

import java.lang.reflect.Method;
import java.util.*;

public class QueueFiller implements Runnable {
    private final Queue<String> testClassNames;
    private  final Queue<TestInst> testMethodQueue;
    private final BooleanHolder thereWillBeBoMore;

    QueueFiller(Queue<String> testClassNames,  Queue<TestInst> testInstQueue, BooleanHolder thereWillBeBoMore) {
        this.testClassNames = testClassNames;
        this.testMethodQueue = testInstQueue;
        this.thereWillBeBoMore = thereWillBeBoMore;
    }

    public void run() {
        Class<?> testClass;
        while (true) {
            synchronized (testClassNames) {
                if (testClassNames.isEmpty()) {
                    thereWillBeBoMore.value = true;
                    break;
                }
                try {
                    testClass = Class.forName(testClassNames.poll());
                } catch (ClassNotFoundException e) {
                    System.err.println("No such class. It must be full class name, example: com.kovalevaelena371.Test1");
                    continue;
                }
                addClass(testClass);
            }
        }
    }

    public void addClass(Class<?> testClass) {

        List<Method> beforeMethodList = new ArrayList<>();
        List<Method> afterMethodList = new ArrayList<>();

        for (var method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethodList.add(method);
            }

            if (method.isAnnotationPresent(After.class)) {
                afterMethodList.add(method);
            }
        }

        for (var method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                synchronized (testMethodQueue) {
                    testMethodQueue.add(new TestInst(testClass, beforeMethodList, afterMethodList, method));
                    testMethodQueue.notify();
                }
            }
        }
    }
}
