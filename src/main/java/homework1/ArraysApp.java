package homework1;

import java.util.ArrayList;
import java.util.Arrays;


public class ArraysApp {


    public static void main(String[] args) {

        String[] stringArr = {"Apple", "Banana", "Orange", "Melon", "Watermelon"};
        Integer[] numberArr = {1, 2, 3, 4, 5, 6};

        System.out.println(Arrays.toString(stringArr));
        changeItems(stringArr, 0, 2);
        System.out.println(Arrays.toString(stringArr));

        System.out.println(Arrays.toString(numberArr));
        changeItems(numberArr, 1, 3);
        System.out.println(Arrays.toString(numberArr));

    }


    private static <T> void changeItems(T[] array, int index1, int index2) {

        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    private static <T> ArrayList<? extends T> convertToList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));

    }
}