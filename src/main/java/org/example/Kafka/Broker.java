package org.example.Kafka;


import java.util.List;

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
