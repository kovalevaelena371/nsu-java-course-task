package com.kovalevaelena371;

import com.kovalevaelena371.exception.AssertionError;
import com.kovalevaelena371.TesterKit.BooleanHolder;

import java.util.Queue;

public class Tester implements Runnable {

    private final Queue<TestInst> testMethodQueue;
    private final Queue<ReportEntry> reportQueue;
    private final BooleanHolder thereWillBeNoMore;

    public Tester(Queue<TestInst> testMethodQueue, Queue<ReportEntry> reportQueue, BooleanHolder thereWillBeNoMore) {
        this.testMethodQueue = testMethodQueue;
        this.reportQueue = reportQueue;
        this.thereWillBeNoMore = thereWillBeNoMore;
    }

    public void run() {
        outer:
        while (true) {
            TestInst testInst;
            synchronized (testMethodQueue) {
                while (testMethodQueue.isEmpty()) {
                    if (thereWillBeNoMore.value) {
                        testMethodQueue.notifyAll();
                        break outer;
                    }
                    try {
                        testMethodQueue.wait();
                    }
                    catch (InterruptedException e) {
                        break outer;
                    }
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
                if (testInst.expectsException()) {
                    result = ReportEntry.failed(testInst,
                            new AssertionError("Expected exception: " + testInst.getExpectedException().getName()),
                            ReportEntry.Status.FAILED);
                } else {
                    result = ReportEntry.success(testInst);
                }
            } catch (Throwable e) {
                if (testInst.expectsException() && testInst.getExpectedException().isInstance(e.getCause())) {
                    result = ReportEntry.success(testInst);
                } else if (e.getCause() instanceof AssertionError) {
                    result = ReportEntry.failed(testInst, e.getCause(), ReportEntry.Status.ASSERTION_ERROR);
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
