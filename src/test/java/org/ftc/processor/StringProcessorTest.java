package org.ftc.processor;

import org.ftc.processor.analyzer.IAnalyzer;
import org.ftc.processor.analyzer.StateAnalyzer;
import org.ftc.processor.analyzer.StringType;
import org.ftc.processor.statistics.IStatisticsCollector;
import org.ftc.processor.writer.QueueItem;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StringProcessorTest {
    @Test
    void testStringProcessor() throws InterruptedException {
        BlockingQueue<QueueItem> intQueue = new ArrayBlockingQueue<>(10);
        BlockingQueue<QueueItem> floatQueue = new ArrayBlockingQueue<>(10);
        BlockingQueue<QueueItem> strQueue = new ArrayBlockingQueue<>(10);

        IAnalyzer analyzer = new StateAnalyzer();

        IStatisticsCollector intCollector = mock(IStatisticsCollector.class);
        IStatisticsCollector floatCollector = mock(IStatisticsCollector.class);
        IStatisticsCollector strCollector = mock(IStatisticsCollector.class);

        StringProcessor processor = new StringProcessor(
                analyzer,
                Map.of(
                        StringType.INTEGER, intQueue,
                        StringType.FLOAT, floatQueue,
                        StringType.STRING, strQueue
                ),
                Map.of(
                        StringType.INTEGER, intCollector,
                        StringType.FLOAT, floatCollector,
                        StringType.STRING, strCollector
                )
        );

        processor.process("123");
        assertEquals("123", intQueue.take().getValue());
        verify(intCollector).collect("123");
        verifyNoInteractions(floatCollector);
        verifyNoInteractions(strCollector);

        processor.process("3.14");
        assertEquals("3.14", floatQueue.take().getValue());
        verify(floatCollector).collect("3.14");
        verifyNoMoreInteractions(intCollector);
        verifyNoInteractions(strCollector);

        processor.process("hello");
        assertEquals("hello", strQueue.take().getValue());
        verify(strCollector).collect("hello");
        verifyNoMoreInteractions(intCollector);
        verifyNoMoreInteractions(floatCollector);

    }
}
