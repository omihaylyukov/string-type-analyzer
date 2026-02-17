package org.ftc.statistics;

public interface IStatisticsCollector {
    void collect(String value);

    void printStatistics();
}
