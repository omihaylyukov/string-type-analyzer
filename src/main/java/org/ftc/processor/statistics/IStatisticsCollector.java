package org.ftc.processor.statistics;

public interface IStatisticsCollector {
    void collect(String value);

    void printStatistics();
}
