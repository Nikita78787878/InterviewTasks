package org.example.Kafka;

/**
 * Сообщение в топике.
 *
 * offset — уникальный номер сообщения в рамках топика.
 * начинается с 0 и увеличивается на 1.
 */
public class Message {

    private final long offset;
    private final String payload;

    public Message(long offset, String payload) {
        this.offset = offset;
        this.payload = payload;
    }

    public long getOffset() {
        return offset;
    }

    public String getPayload() {
        return payload;
    }
}
