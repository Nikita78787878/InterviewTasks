package org.example.Kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TopicData {
    final List<String> messages = new ArrayList<>();
    final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
}
