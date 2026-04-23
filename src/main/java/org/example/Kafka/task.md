/**
* MiniKafka v0.1 (In-Memory)
*
* В этом задании вам предстоит реализовать упрощённую модель брокера сообщений,
* вдохновлённую принципами Apache Kafka.
*
* Основная идея:
* - Есть "топики" (topics)
* - В топики можно отправлять сообщения
* - Из топиков можно читать сообщения
*
* ВАЖНО:
* - Всё хранится в памяти (in-memory)
* - Никакой сети, файлов и многопоточности пока нет
    */
    public class MiniKafka {
    }

/**
* Брокер сообщений.
*
* Отвечает за:
* - хранение сообщений по топикам
* - приём сообщений от Producer
* - выдачу сообщений Consumer-ам
    */
    public interface Broker {

/**
* Отправить сообщение в топик.
*
* @param topic имя топика
* @param message сообщение
  */
  void send(String topic, String message);

/**
* Получить сообщения из топика.
*
* Упрощение:
* - пока возвращаем ВСЕ сообщения топика
* - порядок должен сохраняться
*
* @param topic имя топика
* @return список сообщений (может быть пустым, но не null)
  */
  List<String> poll(String topic);
  }

                   

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
  // TODO
  }
  }

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
  // TODO
  return List.of();
  }
  }

/**
* Пример использования
  */
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