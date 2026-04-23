package org.example.Kafka;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реализация брокера в памяти.
 *
 * Требования:
 * - Использовать Map<String, List<String>>
 * - Если топика нет — он создаётся автоматически
 * - Сообщения добавляются в конец (append-only)
 */
public class InMemoryBroker implements Broker {

    private final Map<String, TopicData> storage = new ConcurrentHashMap<>();

    @Override
    public void send(String topic, String message) {
       TopicData data = storage.computeIfAbsent(topic, k -> new TopicData());

        data.lock.writeLock().lock();
        try{
            data.messages.add(message);
        }finally {
            data.lock.writeLock().unlock();
        }
    }

    @Override
    public List<String> poll(String topic) {
        TopicData data = storage.get(topic);

        if(data == null){
            return List.of();
        }

        data.lock.readLock().lock();
        try{
            return List.copyOf(data.messages);
        }finally {
            data.lock.readLock().unlock();
        }
    }

}
