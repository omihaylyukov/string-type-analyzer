package org.ftc.statistics;

import java.io.PrintStream;

public class CountStatisticsCollector implements IStatisticsCollector {
    private int count;
    private final String name;
    private final PrintStream out;

    public CountStatisticsCollector(String name, PrintStream out) {
        this.count = 0;
        this.name = name;
        this.out = out;
    }

    @Override
    public void collect(String value) {
        count++;
    }

    @Override
    public void printStatistics() {
        out.println(name);
        out.println("Count: " + count + '\n');
    }
}
