package org.example;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTest {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4));

        list.remove(1);


        System.out.println(list.get(1));

        list.stream().forEach(System.out::println);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
