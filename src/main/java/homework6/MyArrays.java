package homework6;

import java.util.Arrays;

public class MyArrays {

    public int[] afterTheLastFour(int[] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == 4) {
                return Arrays.copyOfRange(array, i + 1, array.length);
            }
        }
        throw new RuntimeException("В массиве нет числа 4");
    }

    public boolean isArrayComposition(int[] array) {
        boolean countOne = false;
        boolean countFour = false;
        for (int i : array) {
            if (i != 1 && i != 4) {
                return false;
            }
            if (i == 1 && !countOne) {
                countOne = true;
            }
            if (i == 4 && !countFour) {
                countFour = true;
            }
        }
        return countOne && countFour;
    }
}
