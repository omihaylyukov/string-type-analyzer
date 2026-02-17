package org.ftc.processor;

import org.ftc.processor.analyzer.IAnalyzer;
import org.ftc.processor.analyzer.StringType;
import org.ftc.processor.statistics.IStatisticsCollector;
import org.ftc.processor.writer.QueueItem;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

class StringProcessor {
    private final IAnalyzer analyzer;
    private final Map<StringType, BlockingQueue<QueueItem>> queueMap;
    private final Map<StringType, IStatisticsCollector> statisticsMap;

    public StringProcessor(IAnalyzer analyzer, Map<StringType, BlockingQueue<QueueItem>> queueMap, Map<StringType, IStatisticsCollector> statisticsMap) {
        this.analyzer = analyzer;
        this.queueMap = queueMap;
        this.statisticsMap = statisticsMap;
    }

    public void process(String str) {
        StringType stringType = analyzer.analyze(str);
        try {
            queueMap.get(stringType).put(new QueueItem(str));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        statisticsMap.get(stringType).collect(str);
    }
}
