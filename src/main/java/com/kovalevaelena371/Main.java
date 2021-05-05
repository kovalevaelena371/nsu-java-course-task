package com.kovalevaelena371;


public class Main {

    public static void main(String[] args) {

        int numberOfThreads = 4;
        try {
            numberOfThreads = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Cannot parse number of threads. Four treads will be used.");
        }

        TesterKit testerKit = new TesterKit(numberOfThreads);
        for (int i = 1; i < args.length; i++) {
            try {
                Class<?> testClass = Class.forName(args[i]);
                testerKit.addClass(testClass);
            } catch (ClassNotFoundException e) {
                System.err.println("No such class. It must be full class name, example: com.kovalevaelena371.Test1");
            }
        }

        testerKit.run();
        System.out.println(testerKit.getReport());
    }
}
