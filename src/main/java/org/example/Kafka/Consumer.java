package org.example.Kafka;

import java.util.List;

/**
 * Consumer — получатель сообщений.
 */
public class Consumer {

    private final Broker broker;

    public Consumer(Broker broker) {
        this.broker = broker;
    }

    /**
     * Получить сообщения из топика.
     */
    public List<String> poll(String topic) {

        return broker.poll(topic);
    }
}

