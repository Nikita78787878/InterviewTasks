package org.example.Kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TopicData {
    final List<Message> messages = new ArrayList<>();
    final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    final Long nextOffset = new Long(0L);


}
