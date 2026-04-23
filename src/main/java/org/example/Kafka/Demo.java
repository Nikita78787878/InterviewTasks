package org.example.Kafka;

import java.util.List;

public class Demo {

    public static void main(String[] args) {
        Broker broker = new InMemoryBroker();

        Producer producer = new Producer(broker);
        Consumer consumer = new Consumer(broker);

        producer.send("orders", "order-1");
        producer.send("orders", "order-2");

        List<String> messages = consumer.poll("orders");

        System.out.println(messages);
        // ожидается: [order-1, order-2]
    }
}
