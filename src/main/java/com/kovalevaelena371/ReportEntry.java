package com.kovalevaelena371;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


public class ReportEntry {
    public enum Status {
        SUCCESS, FAILED, ASSERTION_ERROR
    }

    private final String className;
    private final String testName;
    private final Status status;
    private final String causeMessage;
    private final String stackTrace;

    private ReportEntry(String className, String testName, Status status, String cause, String stackTrace) {
        this.className = className;
        this.testName = testName;
        this.status = status;
        this.causeMessage = cause;
        this.stackTrace = stackTrace;
    }

    private ReportEntry(String className, String testName, Status status) {
        this(className, testName, status, null, null);
    }

    public static ReportEntry success(TestInst test) {
        return new ReportEntry(test.getTestClass().getName(), test.getTest().getName(), ReportEntry.Status.SUCCESS);
     }

    public static ReportEntry failed(TestInst test, Throwable cause, Status status) {
        String stackTrace;
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            cause.printStackTrace(pw);
            stackTrace = sw.toString();
        }
        catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }

        return new ReportEntry(test.getTestClass().getName(), test.getTest().getName(), status, cause.getMessage(), stackTrace);
    }

    public String getClassName() {
        return className;
    }

    public String getTestName() {
        return testName;
    }

    public Status getStatus() {
        return status;
    }

    public String getCauseMessage() {
        return causeMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }
}
