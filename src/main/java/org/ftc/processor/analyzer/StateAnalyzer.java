package org.ftc.processor.analyzer;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StateAnalyzer implements IAnalyzer {

    private enum State {
        START,
        SIGN,
        INT,
        DOT_NO_INT,
        DOT,
        FRAC,
        EXP,
        EXP_SIGN,
        EXP_INT
    }

    private enum CharClass {
        DIGIT,
        SIGN,
        DOT,
        EXP,
        OTHER
    }

    private static final Map<State, Map<CharClass, State>> TRANSITIONS = new HashMap<>();

    private static final Set<State> ACCEPT_INT =
            EnumSet.of(State.INT);

    private static final Set<State> ACCEPT_FLOAT =
            EnumSet.of(State.DOT, State.FRAC, State.EXP_INT);

    static {
        add(State.START, CharClass.SIGN, State.SIGN);
        add(State.START, CharClass.DIGIT, State.INT);
        add(State.START, CharClass.DOT, State.DOT_NO_INT);

        add(State.SIGN, CharClass.DIGIT, State.INT);
        add(State.SIGN, CharClass.DOT, State.DOT_NO_INT);

        add(State.INT, CharClass.DIGIT, State.INT);
        add(State.INT, CharClass.DOT, State.DOT);
        add(State.INT, CharClass.EXP, State.EXP);

        add(State.DOT_NO_INT, CharClass.DIGIT, State.FRAC);

        add(State.DOT, CharClass.DIGIT, State.FRAC);
        add(State.DOT, CharClass.EXP, State.EXP);

        add(State.FRAC, CharClass.DIGIT, State.FRAC);
        add(State.FRAC, CharClass.EXP, State.EXP);

        add(State.EXP, CharClass.SIGN, State.EXP_SIGN);
        add(State.EXP, CharClass.DIGIT, State.EXP_INT);

        add(State.EXP_SIGN, CharClass.DIGIT, State.EXP_INT);

        add(State.EXP_INT, CharClass.DIGIT, State.EXP_INT);
    }

    private static void add(State from, CharClass cc, State to) {
        TRANSITIONS
                .computeIfAbsent(from, k -> new HashMap<>())
                .put(cc, to);
    }

    private static CharClass classify(char c) {
        if (c >= '0' && c <= '9') return CharClass.DIGIT;
        if (c == '+' || c == '-') return CharClass.SIGN;
        if (c == '.') return CharClass.DOT;
        if (c == 'e' || c == 'E') return CharClass.EXP;
        return CharClass.OTHER;
    }

    public StringType analyze(String s) {
        if (s == null || s.isEmpty()) return StringType.STRING;

        State state = State.START;

        for (int i = 0; i < s.length(); i++) {
            CharClass cc = classify(s.charAt(i));
            Map<CharClass, State> row = TRANSITIONS.get(state);
            if (row == null) return StringType.STRING;

            State next = row.get(cc);
            if (next == null) return StringType.STRING;

            state = next;
        }

        if (ACCEPT_INT.contains(state)) return StringType.INTEGER;
        if (ACCEPT_FLOAT.contains(state)) return StringType.FLOAT;
        return StringType.STRING;
    }
}
