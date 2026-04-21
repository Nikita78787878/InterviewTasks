package org.example.BDindex;

import java.util.*;

public class UserTable {
    List<User> users = new ArrayList<>(); // Просто наполнение посмотреть
    Map<Long, User> idIndex = new HashMap<>(); // Посмотреть с индексом
    Map<AgeNameKey, List<User>> ageNameIndex = new HashMap<>(); // Посмотреть с составным ключом

    //Task2
    Map<Integer, Map<String, List<User>>> ageNameMap = new HashMap<>();



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
}