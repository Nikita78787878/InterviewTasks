package org.example.RateLimiter;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реализуйте Rate Limiter по алгоритму Sliding Window.
 * <p>
 * Требования:
 * <p>
 * 1. У каждого ключа (key) есть свой лимит запросов
 * <p>
 * 2. Метод allowRequest(key) проверяет: можно ли выполнить запрос для данного ключа
 * <p>
 * 3. Ограничение работает в sliding window: учитываются только запросы за последние windowMillis
 * <p>
 * 4. Если лимит превышен — вернуть false
 * <p>
 * через некоторое время старые запросы "выпадают" из окна и новые снова разрешаются
 *
 * Пример:
 *                            | NOW
 *                            |
 *  <------ 10 секунд ------> |
 *
 * Мы проверяем сколько запросов было в этом промежутке (now - 10 sec) предшевствующие для endpoint key
 *
 * Подсказка: в какой структуре удобно хранить timestamps, где можно быстро удалять старые элементы
 * и добавлять новые?
 *
 * Дополнительно (уровень бог):
 *
 * Что будет если ключей миллион? Держать ли в памяти редкие запросы (e.g. 1 в сутки)?
 * Попробуйте улучшить решение через LRU.
 *
 */
public class RateLimiter {
    private Map<String, Integer> limits = new ConcurrentHashMap<>();
    private Map<String, Deque<Long>> request = new LinkedHashMap<>(16, 0.75f, true){
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Deque<Long>> eldest) {
            return size() > 1000;
        }
    };
    private Long windowMillis;

    private Clock clock;

    public RateLimiter(long windowMillis, Clock clock) {
        this.windowMillis = windowMillis;
        this.clock = clock;
    }

    public static void main(String[] args) {
        Clock baseClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        RateLimiter test = new RateLimiter(5000L, Clock.offset(baseClock, Duration.ofMillis(5000)));
        test.configureLimit("Test", 3);

        for(int i = 0; i < 10; i++){
            boolean res = test.allowRequest("Test");
            System.out.println(String.format("Номер запроса: %d. Результат: %s", i, res));
        }
    }

    /**
     *
     * @param key endpoint (key), например login, getPhotos, createOrder
     * @return
     */
    public boolean allowRequest(String key){
        Integer count = limits.getOrDefault(key, Integer.MAX_VALUE);

        if(!request.containsKey(key)){
            request.computeIfAbsent(key, k -> new ArrayDeque<>());
        }
        Deque<Long> timestamps = request.get(key);

        // удаляет все из окна 10 секунд
        long now = clock.millis();
        while(!timestamps .isEmpty() &&
                ((now - timestamps .peekFirst()) > windowMillis)){
            timestamps .pollFirst(); // удаляет
        }

        if(timestamps .size() < count){
            timestamps .add(now);
            return true;
        }

        return false;
    }

    public void configureLimit(String key, int maxRequests) {
        limits.put(key, maxRequests);
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

}

