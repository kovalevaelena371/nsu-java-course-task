package com.kovalevaelena371;


public class ReportEntry {
    public enum Status {
        SUCCESS, FAILED, ASSERTION_ERROR
    }

    private final String className;
    private final String testName;
    private final Status status;
    private final String cause;

    private ReportEntry(String className, String testName, Status status, String cause) {
        this.className = className;
        this.testName = testName;
        this.status = status;
        this.cause = cause;
    }

    private ReportEntry(String className, String testName, Status status) {
        this(className, testName, status, "");
    }

    public static ReportEntry success(TestInst test) {
        return new ReportEntry(test.getTestedClass().getName(), test.getTest().getName(), ReportEntry.Status.SUCCESS);
     }

    public static ReportEntry failed(TestInst test, Throwable cause, Status status) { 
        return new ReportEntry(test.getTestedClass().getName(), test.getTest().getName(), status, cause.getCause().getMessage());
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

    public String getCause() {
        return cause;
    }
}
