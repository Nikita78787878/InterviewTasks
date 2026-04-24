package org.example.BDindex;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserTable table = new UserTable();

        int size = 1_000_000;

//        // наполняем
//        for (int i = 0; i < size; i++) {
//            table.insert(new User((long) i, "User" + i, i % 100, false));
//        }
//
////        testFindByActive();
//
//
//        table.insert(new User(1_000_001L, "User1", 20, false));
//        table.insert(new User(1_000_002L, "User2", 20, false));
//        table.insert(new User(1_000_003L, "User3", 20, false));
//
//        long targetId = size - 1;
//        User userTest = table.users.get((int) targetId);
//
//        // scan
//        long start = System.nanoTime();
//        table.findByIdScan(targetId);
//        long scanTime = System.nanoTime() - start;
//
//        // index
//        start = System.nanoTime();
//        table.findByIdIndexed(targetId);
//        long indexTime = System.nanoTime() - start;
//
//        // scan age+name
//        start = System.nanoTime();
//        table.findByAgeNameScan(userTest.getAge(), userTest.getName());
//        long ageNameTime = System.nanoTime() - start;
//
//        // composite key age+name
//        start = System.nanoTime();
//        table.findByAgeAndName(userTest.getAge(), userTest.getName());
//        long composTime = System.nanoTime() - start;
//
//        System.out.println("Scan time:  " + scanTime);
//        System.out.println("Index time: " + indexTime);
//        System.out.println("Scan ageName time: " + ageNameTime);
//        System.out.println("Composite key time: " + composTime);
//
//        System.out.println("Разница между O(n) и O(1) " + ageNameTime/composTime);
//        System.out.println("Разница между O(n) и композит " + ageNameTime/composTime);
//
//        searchByAgeNameLeftMost(table, 20, null); // Поиск по возрасту без имени
//        searchByAgeNameLeftMost(table, 20, "User1"); // Поиск по возрасту без имени
//
//        System.out.println("Кол-во юзеров с диапазоном возраста: " + table.findByAgeBetween(20, 22).size()); // диапазон выборки по возрасту

    }

    public static void testFindByActive() {
        UserTable table = new UserTable();

        // Добавляем 10 пользователей с разным флагом active
        for (int i = 0; i < 10; i++) {
            boolean active = (i % 2 == 0); // чётные индексы – активны, нечётные – нет
            table.insert(new User((long) i, "User" + i, 20 + i, active));
        }

        // Дополнительно добавим ещё нескольких, чтобы показать динамику
        table.insert(new User(10L, "ExtraActive", 30, true));
        table.insert(new User(11L, "ExtraInactive", 31, false));

        System.out.println("=== Активные пользователи ===");
        List<User> activeUsers = table.findByActive(true);
        activeUsers.forEach(u -> System.out.println(u + " active=" + u.isActive()));

        System.out.println("\n=== Неактивные пользователи ===");
        List<User> inactiveUsers = table.findByActive(false);
        inactiveUsers.forEach(u -> System.out.println(u + " active=" + u.isActive()));
    }

    // Поиск по возрасту без имени
    private static void searchByAgeNameLeftMost(UserTable table, int age, String name){
        List<User> userFindByAgeNotName = table.findByAgeAndNameLeftMost(age, name);
//        userFindByAgeNotName.forEach(System.out::println);
        if(name == null){
            System.out.println("Кол-во юзерой с определенным возрастом: " + userFindByAgeNotName.size());
        }else{
            userFindByAgeNotName.forEach(System.out::println);
        }
    }
}
