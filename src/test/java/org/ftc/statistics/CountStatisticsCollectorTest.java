package org.ftc.statistics;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CountStatisticsCollectorTest {
    @Test
    void countIsThree() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CountStatisticsCollector stats =
                new CountStatisticsCollector("String statistics:", new PrintStream(out));

        stats.collect("a");
        stats.collect("b");
        stats.collect("c");

        stats.printStatistics();

        assertTrue(out.toString().contains("Count: 3"));
    }
}
