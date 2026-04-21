package org.example.BDindex;

public class Main {
    public static void main(String[] args) {
        UserTable table = new UserTable();

        int size = 1_000_000;

        // наполняем
        for (int i = 0; i < size; i++) {
            table.insert(new User((long) i, "User" + i, i % 100));
        }

        long targetId = size - 1;
        User userTest = table.users.get((int) targetId);

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
}
