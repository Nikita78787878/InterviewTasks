package org.example.BDindex;

import org.example.BDindex.UserQuery;

import java.util.*;

public class UserTable {
    List<User> users = new ArrayList<>(); // Просто наполнение посмотреть
    Map<Long, User> idIndex = new HashMap<>(); // Посмотреть с индексом
    Map<AgeNameKey, List<User>> ageNameIndex = new HashMap<>(); // Посмотреть с составным ключом

    //Task2 leftmost prefix rule
    Map<Integer, Map<String, List<User>>> ageNameMap = new HashMap<>();

    //Task2 leftmost prefix rule
    TreeMap<Integer, List<User>> treeMapIndex = new TreeMap<>();

    // Bitmap Index
    private final BitSet bitSet = new BitSet();

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

        bitSet.set(users.size() -1, user.isActive());
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

    List<User> findByActive(boolean active) {
        List<User> result = new ArrayList<>();

        if (active) {
            int i = bitSet.nextSetBit(0);
            while (i != -1) {
                result.add(idIndex.get((long) i));   // ← каст к long
                i = bitSet.nextSetBit(i + 1);
            }
        } else {
            int i = bitSet.nextClearBit(0);
            while (i != -1 && i < users.size()) {
                result.add(idIndex.get((long) i));   // ← каст к long
                i = bitSet.nextClearBit(i + 1);
            }
        }

        return result;
    }

    public List<User> findUserQuery(UserQuery query) {
        if (query == null) {
            return users;
        }

        //  Поиск по ID
        if (query.id() != null) {
            User user = findByIdIndexed(query.id());
            return user != null ? List.of(user) : Collections.emptyList();
        }

        // Точный возраст (с именем или без)
        if (query.age() != null) {
            List<User> result = findByAgeAndNameLeftMost(query.age(), query.name());
            if (result == null || result.isEmpty()) {
                return Collections.emptyList();
            }
            if (query.active() != null) {
                result = result.stream()
                        .filter(u -> u.isActive() == query.active())
                        .toList();
            }
            return result;
        }

        // Диапазон возраста (с комбинированием active через bitmap)
        if (query.ageFrom() != null || query.ageTo() != null) {
            List<User> ageRangeUsers;

            // Получаем пользователей по range индексу (TreeMap)
            if (query.ageFrom() != null && query.ageTo() != null) {
                ageRangeUsers = findByAgeBetween(query.ageFrom(), query.ageTo());
            } else if (query.ageFrom() != null) {
                ageRangeUsers = treeMapIndex.tailMap(query.ageFrom(), true).values().stream()
                        .flatMap(List::stream)
                        .toList();
            } else {
                ageRangeUsers = treeMapIndex.headMap(query.ageTo(), true).values().stream()
                        .flatMap(List::stream)
                        .toList();
            }

            // Если задан active – используем bitmap индекс и пересекаем результаты
            if (query.active() != null) {
                List<User> activeUsers = findByActive(query.active()); // bitmap index
                if (activeUsers.isEmpty()) {
                    return Collections.emptyList();
                }
                Set<User> activeSet = new HashSet<>(activeUsers);
                ageRangeUsers = ageRangeUsers.stream()
                        .filter(activeSet::contains)
                        .toList();
            }

            // Дополнительная фильтрация по имени (если есть)
            if (query.name() != null && !query.name().isEmpty()) {
                ageRangeUsers = ageRangeUsers.stream()
                        .filter(u -> query.name().equals(u.getName()))
                        .toList();
            }

            return ageRangeUsers;
        }

        // Только active (без условий по возрасту)
        if (query.active() != null) {
            return findByActive(query.active());
        }

        //  Только имя (нет индекса → полный скан)
        if (query.name() != null && !query.name().isEmpty()) {
            return users.stream()
                    .filter(u -> query.name().equals(u.getName()))
                    .toList();
        }

        return users;
    }

}