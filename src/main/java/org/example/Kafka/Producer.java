package org.example.Kafka;

/**
 * Producer — отправитель сообщений.
 *
 * Это просто обёртка над Broker.
 */
public class Producer {

    private final Broker broker;

    public Producer(Broker broker) {
        this.broker = broker;
    }

    /**
     * Отправить сообщение в топик.
     */
    public void send(String topic, String message) {
        broker.send(topic, message);
    }
}