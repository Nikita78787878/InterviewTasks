package org.example.SafetyPay;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentService {

    private static double balance = 10_000;

    // Замочек на каждый ID транзакции
    private static Map<String, Object> payments = new ConcurrentHashMap<>();
    // Тетрадка для обработанных транзакций
    private static Set<String> processedTransactions = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) throws Exception {
//        pay("1", 100);
//        pay("2", 100);
//        System.out.println(balance);

        Thread th1 = new Thread(() -> pay("1", 100));
        Thread th2 = new Thread(() -> pay("1", 100));

        th1.start();
        th2.start();

        th1.join();
        th2.join();

        System.out.println(balance);
    }

    static void pay(String idTransaction, double amount) {
        Object lock = payments.computeIfAbsent(idTransaction, k -> new Object());

        synchronized (lock) {
            if(processedTransactions.contains(idTransaction)) {
                System.out.println("Дубль!");
                return;
            }

            if(balance > amount){
                balance -= amount;
                payments.put(idTransaction, balance);
                processedTransactions.add(idTransaction);

            } else {
                throw new RuntimeException("Не достаточно средств");
            }
        }
    }
}
