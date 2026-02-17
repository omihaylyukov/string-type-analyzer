package org.ftc.processor.statistics;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberStatisticsCollector implements IStatisticsCollector {
    private final String name;
    private int count;
    private BigDecimal sum;
    private BigDecimal max;
    private BigDecimal min;
    private final PrintStream out;

    public NumberStatisticsCollector(String name, PrintStream out) {
        this.count = 0;
        this.sum = new BigDecimal("0");
        this.name = name;
        this.out = out;
    }

    @Override
    public void collect(String value) {
        BigDecimal biValue = new BigDecimal(value);
        sum = sum.add(biValue);
        count++;
        if (max == null) {
            max = biValue;
            min = biValue;
            return;
        }
        if (biValue.compareTo(max) > 0) {
            max = biValue;
        }
        if (biValue.compareTo(min) < 0) {
            min = biValue;
        }
    }

    @Override
    public void printStatistics() {
        out.println(name);
        out.println("Count: " + count);
        if (count == 0) {
            out.println();
            return;
        }
        out.println("Sum: " + sum);
        out.println("Max: " + max);
        out.println("Min: " + min);
        String avg = sum.divide(new BigDecimal(String.valueOf(count)), 10, RoundingMode.HALF_UP)
                .stripTrailingZeros().toPlainString();
        out.println("Average: " + avg + '\n');
    }
}
