package org.example.Kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Реализация брокера в памяти.
 *
 * Требования:
 * - Использовать Map<String, List<String>>
 * - Если топика нет — он создаётся автоматически
 * - Сообщения добавляются в конец (append-only)
 */
public class InMemoryBroker implements Broker {

    private final Map<String, List<String>> storage = new ConcurrentHashMap<>();
    private final Map<String, ReentrantReadWriteLock> locks = new ConcurrentHashMap<>();

    @Override
    public void send(String topic, String message) {
        List<String> messages = storage.computeIfAbsent(topic, k -> new ArrayList<>());
        ReentrantReadWriteLock lock = locks.computeIfAbsent(topic, k -> new ReentrantReadWriteLock());

        lock.writeLock().lock();
        try{
            messages.add(message);
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<String> poll(String topic) {
        ReentrantReadWriteLock lock = locks.computeIfAbsent(topic, k -> new ReentrantReadWriteLock());

        List<String> messages = storage.get(topic);
        if(messages == null){
            return List.of();
        }

        lock.readLock().lock();
        try{
            return List.copyOf(messages);
        }finally {
            lock.readLock().unlock();
        }
    }

}
