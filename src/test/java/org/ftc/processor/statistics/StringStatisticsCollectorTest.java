package org.ftc.processor.statistics;

import org.ftc.processor.statistics.StringStatisticsCollector;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringStatisticsCollectorTest {

    private StringStatisticsCollector create(ByteArrayOutputStream out) {
        return new StringStatisticsCollector(new PrintStream(out));
    }

    @Test
    void emptyStatistics() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StringStatisticsCollector stats = create(out);

        stats.printStatistics();

        String s = out.toString();
        assertTrue(s.contains("String statistics"));
        assertTrue(s.contains("Count: 0"));
        assertFalse(s.contains("Shortest string length: "));
        assertFalse(s.contains("Longest string length: "));
    }

    @Test
    void singleString() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StringStatisticsCollector stats = create(out);

        stats.collect("hello");
        stats.printStatistics();

        String s = out.toString();
        assertTrue(s.contains("Count: 1"));
        assertTrue(s.contains("Shortest string length: 5"));
        assertTrue(s.contains("Longest string length: 5"));
    }

    @Test
    void multipleStrings() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StringStatisticsCollector stats = create(out);

        stats.collect("a");
        stats.collect("abcd");
        stats.collect("abc");
        stats.collect("abcdef");

        stats.printStatistics();

        String s = out.toString();
        assertTrue(s.contains("Count: 4"));
        assertTrue(s.contains("Shortest string length: 1"));
        assertTrue(s.contains("Longest string length: 6"));
    }

    @Test
    void stringsWithSameLength() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StringStatisticsCollector stats = create(out);

        stats.collect("aa");
        stats.collect("bb");
        stats.collect("cc");

        stats.printStatistics();

        String s = out.toString();
        assertTrue(s.contains("Count: 3"));
        assertTrue(s.contains("Shortest string length: 2"));
        assertTrue(s.contains("Longest string length: 2"));
    }

    @Test
    void emptyStringIncluded() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StringStatisticsCollector stats = create(out);

        stats.collect("");
        stats.collect("abc");

        stats.printStatistics();

        String s = out.toString();
        assertTrue(s.contains("Count: 2"));
        assertTrue(s.contains("Shortest string length: 0"));
        assertTrue(s.contains("Longest string length: 3"));
    }
}
