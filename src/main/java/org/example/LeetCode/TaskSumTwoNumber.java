package org.example.LeetCode;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Дан массив целых чисел nums и число target.
 * Найди два числа в массиве, сумма которых равна target.
 * Верни их индексы.
 * Пример: nums = [2, 7, 11, 15], target = 9 → вернуть [0, 1] (потому что 2 + 7 = 9)
 */
public class TaskSumTwoNumber {
    public static void main(String[] args) {

        List<Integer> test = new ArrayList<>(List.of(2, 7, 11, 15));


        System.out.println(sumTwoFast(List.of(2, 7, 11, 15), 9));
    }

    private static List<Integer> sumTwoFast(List<Integer> list, int tagret){
        Map<Integer, Integer> valueIndex = new HashMap<>();

        for(int i = 0; i < list.size(); i++){
            int current = list.get(i);
            int searchValue = tagret - current;
            if(valueIndex.containsKey(searchValue)){
                return List.of(valueIndex.get(searchValue), i);
            }

            valueIndex.put(current, i);
        }
        return List.of();

    }

    private static List<Integer> sumTwo(List<Integer> list, int tagret){
        if(list.isEmpty() || list.size() < 2){
            return list;
        }

        List<Integer> result = new ArrayList<>();

        for(int i = 0; i < list.size(); i++){
            int one = list.get(i);
            for(int j = i + 1; j < list.size(); j++){
                int two = list.get(j);
                if(one + two == tagret){
                    result.add(list.indexOf(one) );
                    result.add(list.indexOf(two));
                    break;
                }
            }
        }
        return result;

    }

}
