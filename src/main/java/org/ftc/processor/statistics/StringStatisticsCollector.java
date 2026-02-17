package org.ftc.processor.statistics;

import java.io.PrintStream;

public class StringStatisticsCollector implements IStatisticsCollector {
    private int max;
    private int min;
    private int count;
    private final PrintStream out;

    public StringStatisticsCollector(PrintStream out) {
        this.count = 0;
        this.min = Integer.MAX_VALUE;
        this.max = -1;
        this.out = out;
    }

    @Override
    public void collect(String value) {
        count++;
        if (value.length() > max) {
            max = value.length();
        }
        if (value.length() < min) {
            min = value.length();
        }
    }

    @Override
    public void printStatistics() {
        out.println("String statistics: ");
        out.println("Count: " + count);
        if (count == 0) {
            out.println();
            return;
        }
        out.println("Shortest string length: " + min);
        out.println("Longest string length: " + max + "\n");
    }
}
