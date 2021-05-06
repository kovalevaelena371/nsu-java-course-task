package com.kovalevaelena371;


import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        int numberOfThreads = 4;
        try {
            numberOfThreads = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Cannot parse number of threads. Four treads will be used.");
        }

        TesterKit testerKit = new TesterKit(numberOfThreads, Arrays.copyOfRange(args, 1, args.length));
        testerKit.run();
        System.out.println(testerKit.getReport());
    }
}
