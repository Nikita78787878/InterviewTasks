package org.example.Kafka;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Consumer — получатель сообщений.
 */
public class Consumer {
    // надо сделать запоминание через поле

    private final Broker broker;

    public Consumer(Broker broker) {
        this.broker = broker;
    }

    /**
     * С самого начала
     */
    public List<Message> poll(String topic) {
        return broker.poll(topic, 0L);
    }

    /**
     * Явно указать Offset
     */
    public List<Message> poll(String topic, Long fromOffset) {
        return broker.poll(topic, fromOffset);
    }
}

