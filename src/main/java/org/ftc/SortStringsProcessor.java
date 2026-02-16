package org.ftc;

import org.ftc.analyzer.IAnalyzer;
import org.ftc.analyzer.StateAnalyzer;
import org.ftc.analyzer.StringType;
import org.ftc.writer.BlockingQueueWriter;
import org.ftc.writer.QueueItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SortStringsProcessor {

    private final List<String> inputFilesPaths;
    private final IAnalyzer analyzer;
    private final Map<StringType, BlockingQueue<QueueItem>> queueMap;
    private final List<BlockingQueueWriter> writers;

    public SortStringsProcessor(String output, String prefix, boolean appendMode, List<String> inputFilesPaths) {
        this.inputFilesPaths = inputFilesPaths;
        this.analyzer = new StateAnalyzer();
        queueMap = new HashMap<>();
        writers = new ArrayList<>();

        String outputPath;
        if (output.isEmpty()) {
            outputPath = "";
        } else {
            outputPath = output + "/";
        }
        outputPath += prefix;

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
    }

    public void process() {
        for (BlockingQueueWriter writer: writers) {
            new Thread(writer).start();
        }

        for (String path: inputFilesPaths) {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = br.readLine()) != null) {
                    process(line);
                }
            } catch (IOException e) {
                System.out.println("WARNING! File " + path + " not found!");
            }
        }

        for (BlockingQueue<QueueItem> queue: queueMap.values()) {
            try {
                queue.put(QueueItem.poisonPill());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void process(String str) {
        StringType stringType = analyzer.analyze(str);
        try {
            queueMap.get(stringType).put(new QueueItem(str));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
