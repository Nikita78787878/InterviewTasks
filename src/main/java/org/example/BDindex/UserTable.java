package org.example.BDindex;

import java.util.*;

public class UserTable {
    List<User> users = new ArrayList<>(); // Просто наполнение посмотреть
    Map<Long, User> idIndex = new HashMap<>(); // Посмотреть с индексом
    Map<AgeNameKey, List<User>> ageNameIndex = new HashMap<>(); // Посмотреть с составным ключом

    //Task2 leftmost prefix rule
    Map<Integer, Map<String, List<User>>> ageNameMap = new HashMap<>();

    //Task2 leftmost prefix rule
    TreeMap<Integer, List<User>> treeMapIndex = new TreeMap<>();

    // Общая вставка для всех видов
    public void insert(User user) {
        users.add(user);
        idIndex.put(user.getId(), user);

        AgeNameKey key = new AgeNameKey(user.getAge(), null);
        ageNameIndex.computeIfAbsent(key, k -> new ArrayList<>()).add(user);

        //Task2
        ageNameMap
                .computeIfAbsent(user.getAge(), k -> new HashMap<>())
                .computeIfAbsent(user.getName(), k -> new ArrayList<>())
                .add(user);

        treeMapIndex.computeIfAbsent(user.getAge(), k->new ArrayList<>()).add(user);
    }

    // Прямой поиск
    public List<User> findByAgeNameScan(int age, String name) {
        if(name == null){
            return null;
        }

        List<User> result = new ArrayList<>();
        for (User user : users) {
            if (user.getAge() == age && user.getName().equals(name)) {
                result.add(user);
            }
        }
        return result;
    }

    // Прямой поиск
    public User findByIdScan(Long id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    // Поиск по индексу
    public User findByIdIndexed(long targetId) {
        return idIndex.get(targetId);
    }

    // Поиск по составному индексу нужно И возраст И имя
    public List<User> findByAgeAndName(int age, String name){
        AgeNameKey key = new AgeNameKey(age, name);
        return ageNameIndex.getOrDefault(key, Collections.emptyList());
    }

    //Поиск по возрасту или по возрасту и имени
    public List<User> findByAgeAndNameLeftMost(int age, String name){
        List<User> found;
        if(name == null || name.isEmpty()){
            found = ageNameMap.get(age).values().stream().flatMap(List::stream).toList();
        } else {
            found = ageNameMap.get(age).get(name);
        }

        if(found == null) return Collections.emptyList();

        return new ArrayList<>(found);
    }

    //Поиск по диапазону возраста

    /**
     * Важно понимать: метод не создает новую независимую карту.
     * Он возвращает представление, которое "смотрит" на часть исходной карты.
     * Поэтому любые изменения (добавление, удаление, замена)
     * в представлении немедленно отражаются в исходной карте, и наоборот
     */
    public List<User> findByAgeBetween(int from, int to){
        return treeMapIndex.subMap(from, true, to, true).values().stream().flatMap(List::stream).toList();
    }
}