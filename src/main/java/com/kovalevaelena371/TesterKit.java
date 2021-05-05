package com.kovalevaelena371;

import com.kovalevaelena371.annotation.After;
import com.kovalevaelena371.annotation.Before;
import com.kovalevaelena371.annotation.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.ArrayDeque;


public class TesterKit {

    private final Queue<TestInst> testInstQueue = new ArrayDeque<>();
    private final Queue<ReportEntry> reportQueue = new ArrayDeque<>();
    private final int threadNumber;

    public TesterKit(int n) {
        this.threadNumber = n;
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
                testInstQueue.add(new TestInst(testClass, beforeMethodList, afterMethodList, method));
            }
        }
    }

    public void run() {

        Thread[] threads = new Thread[threadNumber];
        for (int i = 0; i < this.threadNumber; i++) {
            try {
                threads[i] = new Thread(new Tester(testInstQueue, reportQueue));
                threads[i].start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < this.threadNumber; i++) {
            try {
                threads[i].join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getReport() {
        int success = 0;
        int failed = 0;
        int assertion_error = 0;

        StringBuilder sb = new StringBuilder();
        sb.append("\nTests:\n");

        for (var reportEntry : this.reportQueue) {
            sb.append(reportEntry.getClassName());
            sb.append(" ");
            sb.append(reportEntry.getTestName());
            sb.append(" ");
            sb.append(reportEntry.getStatus());

            String causeMessage = reportEntry.getCauseMessage();
            if (causeMessage != null) {
                sb.append(" ");
                sb.append(reportEntry.getCauseMessage());
            }

            String stackTrace = reportEntry.getStackTrace();
            if (stackTrace != null) {
                sb.append("\n");
                sb.append("Stack trace:");
                sb.append("\n");
                sb.append(reportEntry.getStackTrace());
            }

            sb.append("\n");

            switch (reportEntry.getStatus()) {
                case SUCCESS -> success++;
                case FAILED -> failed++;
                case ASSERTION_ERROR -> assertion_error++;
            }
        }

        sb.append("\nSummary:");

        sb.append("\nsuccess: ");
        sb.append(success);
        sb.append("\nfailed: ");
        sb.append(failed);
        sb.append("\nassertion error: ");
        sb.append(assertion_error);

        return sb.toString();
    }
}
