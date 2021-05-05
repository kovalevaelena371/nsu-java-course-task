package com.kovalevaelena371;

import com.kovalevaelena371.exception.AssertionError;
import com.kovalevaelena371.annotation.Test;

import java.util.Queue;


public class Tester implements Runnable {

    private final Queue<TestInst> testMethodQueue;
    private final Queue<ReportEntry> reportQueue;

    public Tester(Queue<TestInst> testMethodQueue, Queue<ReportEntry> reportQueue) {
        this.testMethodQueue = testMethodQueue;
        this.reportQueue = reportQueue;
    }

    public void run() {
        
        while (true) {
            TestInst testInst;
            synchronized (testMethodQueue) {
                if (testMethodQueue.isEmpty()) {
                    break;
                }
                testInst = testMethodQueue.poll();
            }
            
            Object obj;
            ReportEntry result;

            try {
                obj = testInst.getTestClass().getDeclaredConstructor().newInstance();
                runBefore(testInst, obj);
            } catch (Throwable e) {
                System.out.println("One of @Before methods failed. Test \"" + testInst.getTest().getName() + "\" will be skipped.");
                continue;
            }

            try {
                testInst.getTest().invoke(obj);
                result = ReportEntry.success(testInst);

            } catch (Throwable e) {

                if (testInst.getTest().getAnnotation(Test.class).expected().isInstance(e.getCause())) {
                    result = ReportEntry.success(testInst);
                } else if (e.getCause() instanceof AssertionError) {
                    result = ReportEntry.failed(testInst, e, ReportEntry.Status.ASSERTION_ERROR);
                } else {
                    result = ReportEntry.failed(testInst, e, ReportEntry.Status.FAILED);
                }
            } finally {
                runAfter(testInst, obj);
            }

            synchronized (reportQueue) {
                reportQueue.add(result);
            }

            String shortResult = "Test " + testInst.getTest().getName() +
                    " from " +  testInst.getTestClass().getName() +
                    " is completed. Short result: " + result.getStatus();
            System.out.println(shortResult);
        }
    }

    private void runBefore(TestInst testInst, Object obj) throws Throwable {
        if (obj == null) return;
        for (var beforeMethod : testInst.getBeforeMethods()) {
            beforeMethod.invoke(obj);
        }
    }

    private void runAfter(TestInst testInst, Object obj) {
        if (obj == null) return;
        for (var afterMethod : testInst.getAfterMethods()) {
            try {
                afterMethod.invoke(obj);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
