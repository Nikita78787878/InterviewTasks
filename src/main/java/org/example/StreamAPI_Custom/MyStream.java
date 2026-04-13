package org.example.StreamAPI_Custom;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Реализовать mini-stream API. В первой версии сразу делать toList() терминальным, а все остальные
 * методы - lazy. В первой версии допустимо сделать O(k * n) реализацию, где k - количество методов
 * нашего стрима (например при map + filter + toList k = 3).
 *
 * @param <T> тип элементов коллекции, над которой будем делать stream
 *
 * Пример использования:
 * List<String> result = MyStream.of(List.of("cat", "elephant", "dog"))
 *     .filter(s -> s.length() > 3)
 *     .map(String::toUpperCase)
 *     .toList(); // ожидается ["ELEPHANT"]
 *
 *  * Дополнительно: подумать как сделать O(n) реализацию
 *  * Дополнительно: реализовать еще одну терминальную операцию forEach(). Подумать: как понять,
 *  *           что терминальная операция вызвана и стрим больше не запустить?
 */
public class MyStream<T> {

    public static void main(String[] args) {
          List<String> result = MyStream.of(List.of("cat", "elephant", "dog"))
                    .filter(s -> s.length() > 3)
                    .map(String::toUpperCase)
                    .toList(); // ожидается ["ELEPHANT"]

        System.out.println(result);
    }
    private List<T> list;
    private final List<Function<List<?>, List<?>>> operations;


    public MyStream(List<T> list) {
        this.list = list;
        this.operations = new ArrayList<>();
    }

    public MyStream(List<T> list, List<Function<List<?>, List<?>>> operations) {
        this.list = list;
        this.operations = operations;
    }

    public static <T> MyStream<T> of(List<T> list) {
        return new MyStream<>(list);
    }
    // подсказка: Predicate
    public MyStream<T> filter(Predicate<T> predicate) {
    List<Function<List<?>, List<?>>> newOps = new ArrayList<>(operations);
        operations.add(list -> {
            List<T> result = new ArrayList<>();

            for (Object item : list) {
                T value = (T) item;
                if (predicate.test(value)) {
                    result.add(value);
                }
            }

            return result;

        });
        return new MyStream<>(list, newOps);
    }
    // подсказка: Function
    public <R> MyStream<R> map(Function<T, R> fun) {
        List<R> resultList = new ArrayList<>();
        for(T el : list){
            resultList.add(fun.apply(el));
        }

        List<Function<List<?>, List<?>>> newOps = new ArrayList<>(operations);

        operations.add(list -> {
            List<R> result = new ArrayList<>();

            for (Object item : list) {
                T value = (T) item;
                result.add(fun.apply(value));
            }

            return result;

        });


        return new MyStream<R>(resultList);
    }

    public List<T> toList() {
        List<?> resultList = list;

        for(Function<List<?>, List<?>> fun : operations){
            resultList = fun.apply(list);
        }
        return (List<T>) resultList;
    }
}

// public LazyStream<T> filter(Predicate<T> predicate) {
//    List<Function<List<?>, List<?>>> newOps = new ArrayList<>(operations);
//
//    newOps.add(list -> {
//        List<T> result = new ArrayList<>();
//
//        for (Object item : list) {
//            T value = (T) item;
//            if (predicate.test(value)) {
//                result.add(value);
//            }
//        }
//
//        return result;
//    });
