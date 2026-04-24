package org.example.Kafka;

import java.util.List;

public class Demo {

    public static void main(String[] args) {
        Broker broker = new InMemoryBroker();

        Producer producer = new Producer(broker);
        Consumer consumer = new Consumer(broker);

        producer.send("orders", "order-1");
        producer.send("orders", "order-2");
        producer.send("orders", "order-3");

        List<Message> messages0 = consumer.poll("orders");
        List<Message> messages1 = consumer.poll("orders", 1L);

        //Читаем с начала
        System.out.println("Читаем с 0 оффсета: ");
        messages0.forEach(m -> System.out.println(m.getPayload()));
        System.out.println();

        //Читаем конкретный
        System.out.println("Читаем 1 оффсета");
        messages1.forEach(m -> System.out.println(m.getPayload()));
    }
}
