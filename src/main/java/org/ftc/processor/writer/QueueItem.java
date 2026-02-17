package org.ftc.processor.writer;

public class QueueItem {
    private final String value;
    private final boolean isPoison;

    public QueueItem(String value) {
        this.value = value;
        this.isPoison = false;
    }

    private QueueItem(boolean isPoison) {
        this.value = null;
        this.isPoison = isPoison;
    }

    public static QueueItem poisonPill() {
        return new QueueItem(true);
    }

    public boolean isPoison() {
        return isPoison;
    }

    public String getValue() {
        return value;
    }
}

