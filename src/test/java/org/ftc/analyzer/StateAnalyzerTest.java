package org.ftc.analyzer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateAnalyzerTest {

    private final StateAnalyzer classifier = new StateAnalyzer();

    @Test
    void testInteger() {
        assertEquals(StringType.INTEGER, classifier.analyze("123"));
        assertEquals(StringType.INTEGER, classifier.analyze("0"));
        assertEquals(StringType.INTEGER, classifier.analyze("999999"));

        assertEquals(StringType.INTEGER, classifier.analyze("-1"));
        assertEquals(StringType.INTEGER, classifier.analyze("-999"));

        assertEquals(StringType.INTEGER, classifier.analyze("+123"));
    }

    @Test
    void testFloat() {
        assertEquals(StringType.FLOAT, classifier.analyze("12.34"));
        assertEquals(StringType.FLOAT, classifier.analyze(".5"));
        assertEquals(StringType.FLOAT, classifier.analyze("5."));
        assertEquals(StringType.FLOAT, classifier.analyze("0.0"));

        assertEquals(StringType.FLOAT, classifier.analyze("1e10"));
        assertEquals(StringType.FLOAT, classifier.analyze("1E10"));
        assertEquals(StringType.FLOAT, classifier.analyze("-1e5"));
        assertEquals(StringType.FLOAT, classifier.analyze("1.2e-3"));
        assertEquals(StringType.FLOAT, classifier.analyze("-1.5E+4"));

        assertEquals(StringType.FLOAT, classifier.analyze("+.5"));
        assertEquals(StringType.FLOAT, classifier.analyze("-0.0"));
    }

    @Test
    void testStrings() {
        assertEquals(StringType.STRING, classifier.analyze(""));
        assertEquals(StringType.STRING, classifier.analyze("abc"));
        assertEquals(StringType.STRING, classifier.analyze("1e"));
        assertEquals(StringType.STRING, classifier.analyze("."));
        assertEquals(StringType.STRING, classifier.analyze("1..2"));
        assertEquals(StringType.STRING, classifier.analyze("--5"));

        assertEquals(StringType.STRING, classifier.analyze("+"));
        assertEquals(StringType.STRING, classifier.analyze("-"));
    }
}
