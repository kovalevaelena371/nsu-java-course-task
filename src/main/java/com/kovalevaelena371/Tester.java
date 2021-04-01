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
            
            Object obj = null;
            ReportEntry result;
            try {
                
                obj = testInst.getTestedClass().getDeclaredConstructor().newInstance();
                runBefore(testInst, obj);
                testInst.getTest().invoke(obj);
                result = ReportEntry.success(testInst);

            } catch (Throwable e) {

                if (testInst.getTest().getAnnotation(Test.class).expected().isInstance(e.getCause())) {
                    result = ReportEntry.success(testInst);
                } else if (e.getCause() instanceof AssertionError) {
                    result =  ReportEntry.failed(testInst, e, ReportEntry.Status.ASSERTION_ERROR);
                } else {
                    result = ReportEntry.failed(testInst, e, ReportEntry.Status.FAILED);
                }
            } finally {
                runAfter(testInst, obj);
            }

            synchronized (reportQueue) {
                reportQueue.add(result);
            }
        }
    }

    private void runBefore(TestInst testInst, Object obj) {
        if (obj == null) return;
        for (var beforeMethod : testInst.getBeforeMethods()) {
            try {
                beforeMethod.invoke(obj);
            } catch (Throwable e) {
                e.printStackTrace();
            }
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
