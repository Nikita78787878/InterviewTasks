package org.example.Kafka;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Consumer — получатель сообщений.
 */
public class Consumer {
    private final Map<String, Long> committedOffsets = new ConcurrentHashMap<>();
    private final Broker broker;

    public Consumer(Broker broker) {
        this.broker = broker;
    }

    /**
     * С самого начала
     */
    public List<Message> poll(String topic) {
        long fromOffset = committedOffsets.getOrDefault(topic, 0L);
        return broker.poll(topic, fromOffset);
    }

    /**
     * Явно указать Offset
     */
    public List<Message> poll(String topic, Long fromOffset) {
        committedOffsets.put(topic, fromOffset);
        return broker.poll(topic, fromOffset);
    }
}

