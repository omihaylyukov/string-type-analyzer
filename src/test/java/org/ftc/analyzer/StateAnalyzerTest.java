package org.ftc.analyzer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateAnalyzerTest {

    private final StateAnalyzer classifier = new StateAnalyzer();

    @Test
    void testInteger() {
        assertEquals(ValueType.INTEGER, classifier.analyze("123"));
        assertEquals(ValueType.INTEGER, classifier.analyze("0"));
        assertEquals(ValueType.INTEGER, classifier.analyze("999999"));

        assertEquals(ValueType.INTEGER, classifier.analyze("-1"));
        assertEquals(ValueType.INTEGER, classifier.analyze("-999"));

        assertEquals(ValueType.INTEGER, classifier.analyze("+123"));
    }

    @Test
    void testFloat() {
        assertEquals(ValueType.FLOAT, classifier.analyze("12.34"));
        assertEquals(ValueType.FLOAT, classifier.analyze(".5"));
        assertEquals(ValueType.FLOAT, classifier.analyze("5."));
        assertEquals(ValueType.FLOAT, classifier.analyze("0.0"));

        assertEquals(ValueType.FLOAT, classifier.analyze("1e10"));
        assertEquals(ValueType.FLOAT, classifier.analyze("1E10"));
        assertEquals(ValueType.FLOAT, classifier.analyze("-1e5"));
        assertEquals(ValueType.FLOAT, classifier.analyze("1.2e-3"));
        assertEquals(ValueType.FLOAT, classifier.analyze("-1.5E+4"));

        assertEquals(ValueType.FLOAT, classifier.analyze("+.5"));
        assertEquals(ValueType.FLOAT, classifier.analyze("-0.0"));
    }

    @Test
    void testStrings() {
        assertEquals(ValueType.STRING, classifier.analyze(""));
        assertEquals(ValueType.STRING, classifier.analyze("abc"));
        assertEquals(ValueType.STRING, classifier.analyze("1e"));
        assertEquals(ValueType.STRING, classifier.analyze("."));
        assertEquals(ValueType.STRING, classifier.analyze("1..2"));
        assertEquals(ValueType.STRING, classifier.analyze("--5"));

        assertEquals(ValueType.STRING, classifier.analyze("+"));
        assertEquals(ValueType.STRING, classifier.analyze("-"));
    }
}
