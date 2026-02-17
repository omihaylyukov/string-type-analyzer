package org.ftc.processor.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueWriter implements Runnable {
    private final BlockingQueue<QueueItem> queue;
    private final boolean appendMode;
    private BufferedWriter writer;
    private final String path;

    public BlockingQueueWriter(String path, boolean appendMode, BlockingQueue<QueueItem> queue) {
        this.path = path;
        this.appendMode = appendMode;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                QueueItem queueItem = queue.take();
                if (queueItem.isPoison()) {
                    if (writer != null) {
                        writer.close();
                    }
                    return;
                }
                if (writer == null) {
                    writer = new BufferedWriter(new FileWriter(path, appendMode));
                }
                writer.write(queueItem.getValue() + '\n');
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            System.out.println("ERROR! The specified directory does not exist");
        }
    }
}
