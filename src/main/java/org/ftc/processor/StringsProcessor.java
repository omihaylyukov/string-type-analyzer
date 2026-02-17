package org.ftc.processor;

import org.ftc.Main;
import org.ftc.processor.analyzer.IAnalyzer;
import org.ftc.processor.analyzer.StateAnalyzer;
import org.ftc.processor.analyzer.StringType;
import org.ftc.processor.statistics.CountStatisticsCollector;
import org.ftc.processor.statistics.IStatisticsCollector;
import org.ftc.processor.statistics.NumberStatisticsCollector;
import org.ftc.processor.statistics.StringStatisticsCollector;
import org.ftc.processor.writer.BlockingQueueWriter;
import org.ftc.processor.writer.QueueItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StringsProcessor {

    private final List<String> inputFilesPaths;
    private final StringProcessor stringProcessor;
    private final Map<StringType, BlockingQueue<QueueItem>> queueMap;
    private final List<BlockingQueueWriter> writers;
    private final Map<StringType, IStatisticsCollector> statisticsMap;

    public StringsProcessor(String output, String prefix, boolean appendMode, List<String> inputFilesPaths, Main.StatsMode statsMode) {
        this.inputFilesPaths = inputFilesPaths;

        String outputPath;
        if (output.isEmpty()) {
            outputPath = "";
        } else {
            outputPath = output + "/";
        }
        outputPath += prefix;

        this.queueMap = new HashMap<>();
        this.writers = new ArrayList<>();

        BlockingQueue<QueueItem> integerQueue = new ArrayBlockingQueue<>(20);
        queueMap.put(StringType.INTEGER, integerQueue);
        BlockingQueueWriter integerWriter = new BlockingQueueWriter(outputPath + "integers.txt", appendMode, integerQueue);
        writers.add(integerWriter);

        BlockingQueue<QueueItem> floatQueue = new ArrayBlockingQueue<>(20);
        queueMap.put(StringType.FLOAT, floatQueue);
        BlockingQueueWriter floatWriter = new BlockingQueueWriter(outputPath + "floats.txt", appendMode, floatQueue);
        writers.add(floatWriter);

        BlockingQueue<QueueItem> stringQueue = new ArrayBlockingQueue<>(20);
        queueMap.put(StringType.STRING, stringQueue);
        BlockingQueueWriter stringWriter = new BlockingQueueWriter(outputPath + "strings.txt", appendMode, stringQueue);
        writers.add(stringWriter);

        this.statisticsMap = new HashMap<>();
        PrintStream printStream = new PrintStream(System.out);
        if (statsMode.shortStats) {
            statisticsMap.put(StringType.INTEGER, new CountStatisticsCollector("Integer statistics:", printStream));
            statisticsMap.put(StringType.FLOAT, new CountStatisticsCollector("Float statistics:", printStream));
            statisticsMap.put(StringType.STRING, new CountStatisticsCollector("String statistics:", printStream));
        } else {
            statisticsMap.put(StringType.INTEGER, new NumberStatisticsCollector("Integer statistics:", printStream));
            statisticsMap.put(StringType.FLOAT, new NumberStatisticsCollector("Float statistics:", printStream));
            statisticsMap.put(StringType.STRING, new StringStatisticsCollector(printStream));
        }

        this.stringProcessor = new StringProcessor(new StateAnalyzer(), queueMap, statisticsMap);
    }

    public void process() {
        try (ExecutorService executor = Executors.newFixedThreadPool(writers.size())) {
            for (BlockingQueueWriter writer : writers) {
                executor.submit(writer);
            }

            for (String path: inputFilesPaths) {
                try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        stringProcessor.process(line);
                    }
                } catch (IOException e) {
                    System.out.println("WARNING! File " + path + " not found!");
                }
            }

            for (BlockingQueue<QueueItem> queue: queueMap.values()) {
                try {
                    queue.put(QueueItem.poisonPill());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            for (IStatisticsCollector statisticsCollector: statisticsMap.values()) {
                statisticsCollector.printStatistics();
            }
        }
    }
}
