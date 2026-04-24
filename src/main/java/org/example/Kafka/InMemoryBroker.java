package org.example.Kafka;

import java.util.ArrayList;
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
            long offset = data.messages.size(); // избыток AtomicLong  и замедление, просто размер вызвать
            data.messages.add(new Message(offset, message));
        }finally {
            data.lock.writeLock().unlock();
        }
    }

    @Override
    public List<Message> poll(String topic, long fromOffset) {
        TopicData data = storage.get(topic);

        if(data == null){
            return List.of();
        }

        data.lock.readLock().lock();
        try{
            List<Message> result = new ArrayList<>();

            int startOffset = (int) Math.max(0, fromOffset);
            if(startOffset >= data.messages.size()){
                return List.of();
            }

            for(int i = startOffset; i < data.messages.size(); i++){
                result.add(data.messages.get(i));
            }

            return List.copyOf(result);
        }finally {
            data.lock.readLock().unlock();
        }
    }

}
