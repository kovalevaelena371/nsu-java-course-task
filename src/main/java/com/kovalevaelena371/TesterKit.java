package com.kovalevaelena371;

import java.util.Arrays;
import java.util.Queue;
import java.util.ArrayDeque;


public class TesterKit {

    public static class BooleanHolder {
        public Boolean value;

        BooleanHolder(Boolean value) {
            this.value = value;
        }
    }

    private final Queue<TestInst> testInstQueue = new ArrayDeque<>();
    private final Queue<ReportEntry> reportQueue = new ArrayDeque<>();
    private final BooleanHolder thereWillBeBoMore = new BooleanHolder(false);

    private final int threadNumber;
    private final String[] testClassNames;

    public TesterKit(int n, String[] testClassNames) {
        this.threadNumber = n;
        this.testClassNames = testClassNames;
    }

    public void run() {
        Thread[] threadsForFillers = new Thread[threadNumber];
        Thread[] threadsForTesters = new Thread[threadNumber];
        Queue<String> testClassNameQueue = new ArrayDeque<>(Arrays.asList(testClassNames));
        for (int i = 0; i < this.threadNumber; i++) {
            try {
                threadsForFillers[i] = new Thread(new QueueFiller(testClassNameQueue, testInstQueue, thereWillBeBoMore));
                threadsForTesters[i] = new Thread(new Tester(testInstQueue, reportQueue, thereWillBeBoMore));

                threadsForFillers[i].start();
                threadsForTesters[i].start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < this.threadNumber; i++) {
            try {
                threadsForTesters[i].join();
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
