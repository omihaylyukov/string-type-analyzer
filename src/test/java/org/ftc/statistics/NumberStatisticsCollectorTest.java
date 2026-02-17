package org.ftc.statistics;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class NumberStatisticsCollectorTest {

    private NumberStatisticsCollector create(ByteArrayOutputStream out) {
        return new NumberStatisticsCollector("Number statistics: ", new PrintStream(out));
    }

    @Test
    void emptyStatistics() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        NumberStatisticsCollector stats = create(out);

        stats.printStatistics();

        String s = out.toString();
        assertTrue(s.contains("Count: 0"));
        assertFalse(s.contains("Sum:"));
        assertFalse(s.contains("Min:"));
        assertFalse(s.contains("Max:"));
        assertFalse(s.contains("Average:"));
    }

    @Test
    void singleValue() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        NumberStatisticsCollector stats = create(out);

        stats.collect("10");
        stats.printStatistics();

        String s = out.toString();
        assertTrue(s.contains("Count: 1"));
        assertTrue(s.contains("Sum: 10"));
        assertTrue(s.contains("Max: 10"));
        assertTrue(s.contains("Min: 10"));
        assertTrue(s.contains("Average: 10"));
    }

    @Test
    void multipleValues() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        NumberStatisticsCollector stats = create(out);

        stats.collect("1");
        stats.collect("2");
        stats.collect("3");

        stats.printStatistics();

        String s = out.toString();
        assertTrue(s.contains("Count: 3"));
        assertTrue(s.contains("Sum: 6"));
        assertTrue(s.contains("Max: 3"));
        assertTrue(s.contains("Min: 1"));
        assertTrue(s.contains("Average: 2"));
    }

    @Test
    void decimalValues() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        NumberStatisticsCollector stats = create(out);

        stats.collect("1.5");
        stats.collect("2.5");

        stats.printStatistics();

        String s = out.toString();
        assertTrue(s.contains("Sum: 4.0"));
        assertTrue(s.contains("Max: 2.5"));
        assertTrue(s.contains("Min: 1.5"));
        assertTrue(s.contains("Average: 2"));
    }

    @Test
    void negativeValues() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        NumberStatisticsCollector stats = create(out);

        stats.collect("-5");
        stats.collect("10");
        stats.collect("-3");

        stats.printStatistics();

        String s = out.toString();
        assertTrue(s.contains("Sum: 2"));
        assertTrue(s.contains("Max: 10"));
        assertTrue(s.contains("Min: -5"));
        assertTrue(s.contains("Average: 0.6666666667"));
    }
}
