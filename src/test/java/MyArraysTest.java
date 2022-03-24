import homework6.MyArrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class MyArraysTest {
    private static MyArrays myArrays;
    @BeforeAll
    public static void init() {
        myArrays = new MyArrays();
    }
    @ParameterizedTest
    @MethodSource("dataForArrayOperation")
    void afterTheLastFour(int[] array, int[] resultArray) {
        Assertions.assertArrayEquals(resultArray, myArrays.afterTheLastFour(array));
    }
    @ParameterizedTest
    @MethodSource("dataForArrayOperationException")
    void afterTheLastFourException(int[] array) {
        Assertions.assertThrows(RuntimeException.class, () -> myArrays.afterTheLastFour(array));
    }
    @ParameterizedTest
    @MethodSource("dataForArrayOperation1and4")
    void isArrayComposition(int[] array, boolean isArray) {
        Assertions.assertEquals(isArray, myArrays.isArrayComposition(array));
    }
    private static Stream<Arguments> dataForArrayOperation() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new int[] { 1, 4, 1, 1 }, new int[] { 1, 1 }));
        out.add(Arguments.arguments(new int[] { 2, 2, 2, 4 }, new int[] {}));
        out.add(Arguments.arguments(new int[] { 4, 1, 1, 4, 4, 5 }, new int[] { 5 }));
        out.add(Arguments.arguments(new int[] { 4, 2, 3, 2, 5 }, new int[] { 2, 3, 2, 5 }));
        return out.stream();
    }
    private static Stream<Arguments> dataForArrayOperationException() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new int[] { 1, 1, 1, 1 }, new int[] { 1, 1 }));
        out.add(Arguments.arguments(new int[] { 2, 2, 2, 2 }, new int[] { 2, 2 }));
        out.add(Arguments.arguments(new int[] {  }, new int[] {  }));
        out.add(Arguments.arguments(new int[] { 1, 2, 3, 2, 5 }, new int[] { 2, 3, 2, 5 }));
        return out.stream();
    }

    private static Stream<Arguments> dataForArrayOperation1and4() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new int[] { 1, 4, 1, 4 }, true));
        out.add(Arguments.arguments(new int[] { 4, 1, 4, 4 }, true));
        out.add(Arguments.arguments(new int[] { 1, 1, 1, 1 }, false));
        out.add(Arguments.arguments(new int[] { 4, 4, 4, 4 }, false));
        out.add(Arguments.arguments(new int[] { 4, 1, 4, 3 }, false));
        return out.stream();
    }
}
