package org.example.BDindex;

import java.util.*;

/**
 * Реализовать in-memory таблицу пользователей с поддержкой индекса.
 * <p>
 * Часть 1. Таблица без индекса
 * <p>
 * Требования:
 * - хранить пользователей в порядке вставки
 * - реализовать метод:
 * <p>
 * User findByIdScan(Long id)
 * <p>
 * который ищет пользователя полным проходом по таблице
 * <p>
 * <p>
 * Часть 2. Индекс
 * <p>
 * Добавить Hash Index:
 * <p>
 * Требования:
 * - при добавлении пользователя обновлять индекс
 * - реализовать метод:
 * <p>
 * User findByIdIndexed(Long id)
 * <p>
 * Часть 3.
 * Составной индекс:
 * - Реализовать поиск по составному индексу:
 * <p>
 * List<User> findByAgeAndName(int age, String name)
 * <p>
 * Часть 4. Сравнение (опционально)
 * <p>
 * - создать 1_000_000 пользователей
 * - замерить время:
 * - findByIdScan
 * - findByIdIndexed
 * <p>
 * <p>
 * public class User {
 * <p>
 * private final Long id;
 * private final String name;
 * private final int age;
 * <p>
 * public User(Long id, String name, int age) {
 * this.id = id;
 * this.name = name;
 * this.age = age;
 * }
 * <p>
 * public Long getId() {
 * return id;
 * }
 * <p>
 * public String getName() {
 * return name;
 * }
 * <p>
 * public int getAge() {
 * return age;
 * }
 * }
 */
public class UserTable {
    List<User> users = new ArrayList<>();
    Map<Long, User> idIndex = new HashMap<>();
    Map<AgeNameKey, List<User>> ageNameIndex = new HashMap<>();

    public static void main(String[] args) {

        UserTable table = new UserTable();

        int size = 1_000_000;

        // наполняем
        for (int i = 0; i < size; i++) {
            table.insert(new User((long) i, "User" + i, i % 100));
        }


        long targetId = size - 1;
        User userTest = table.users.get(999_999);
        // scan
        long start = System.nanoTime();
        table.findByIdScan(targetId);
        long scanTime = System.nanoTime() - start;

        // index
        start = System.nanoTime();
        table.findByIdIndexed(targetId);
        long indexTime = System.nanoTime() - start;

        // scan age+name
        start = System.nanoTime();
        table.findByAgeNameScan(userTest.getAge(), userTest.getName());
        long ageNameTime = System.nanoTime() - start;

        // composite key age+name
        start = System.nanoTime();
        table.findByAgeAndName(userTest.getAge(), userTest.getName());
        long composTime = System.nanoTime() - start;

        System.out.println("Scan time:  " + scanTime);
        System.out.println("Index time: " + indexTime);
        System.out.println("Scan ageName time: " + ageNameTime);
        System.out.println("Composite key time: " + composTime);

        System.out.println("Разница между O(n) и O(1) " + ageNameTime/composTime);
        System.out.println("Разница между O(n) и композит " + ageNameTime/composTime);
    }

    private User findByIdIndexed(long targetId) {
        return idIndex.get(targetId);
    }

    private List<User> findByAgeNameScan(int age, String name) {
        List<User> result = new ArrayList<>();

        for (User user : users) {
            if (user.getAge() == age && user.getName().equals(name)) {
                result.add(user);
            }
        }
        return result;
    }

    private User findByIdScan(Long id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    private void insert(User user) {
        users.add(user);
        idIndex.put(user.getId(), user);

        AgeNameKey key = new AgeNameKey(user.getAge(), user.getName());
        ageNameIndex.computeIfAbsent(key, k -> new ArrayList<>()).add(user);
    }

    private List<User> findByAgeAndName(int age, String name){
        AgeNameKey key = new AgeNameKey(age, name);
        return ageNameIndex.getOrDefault(key, Collections.emptyList());
    }
}