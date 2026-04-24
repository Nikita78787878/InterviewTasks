/**
* MiniKafka v0.2
*
* Теперь мы улучшаем модель:
* - сообщения НЕ читаются "все сразу"
* - у каждого сообщения есть offset (позиция в логе)
* - consumer сам решает, откуда читать.
*
* Изменить сигнатуры poll() & send() под новые требования.
*
* Гарантировать корректность offset:
*     - не должно быть дубликатов offset
*     - не должно быть "дыр" (0,1,3 — нельзя)
*
*/


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