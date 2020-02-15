import org.junit.Test;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class MainTest
{

    @Test
    public void isOrdered2() throws Exception
    {
        int[][][] arr1 = { new int[][]{
                {1, 2, 3},
                {2, 3, 4},
                {2, 3, 5}
                        }, new int[][]{
                {1, 2, 3},
                {2, 3, 4},
                {2, 3, 4}
                        }, new int[][]{
                {5, 6, 5},
                {1, 2, 3},
                {2, 3, 4}
                        }, new int[][]{
                {1, 2, 5},
                {1, 2, 3},
                {2, 3, 6}
                        }, new int[][]{
                {2, 2, 5},
                {1, 2, 3},
                {2, 3, 6}
                        }, new int[][]{
                {2, 3, 5},
                {2, 2, 3},
                {2, 3, 6}
                        }, new int[][]{
                {2, 3, 5},
                {2, 2, 7},
                {2, 3, 6}
        }};

        boolean[] resArr = new boolean[]{true, false, true, true, true, true, false};
        for (int i = 0; i < resArr.length; i++)
        {
//            boolean ord = Main.isOrdered2(arr1[i]);
            boolean ord = Solved1.mainTst(arr1[i]);
            System.out.println(ord);
            assert ord == resArr[i];
        }
    }

    @Test
    public void fetchOrderArr()
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                for (int k = 0; k < 3; k++)
                {
                    int[] arr = new int[]{i, j, k};
                    printArr(arr, Main.fetchOrderArr(arr));
                }
            }
        }
    }

    @Test
    public void testPrg()
    {
        List<Integer> intList = new ArrayList<>();
        intList.add(34);
        intList.add(35);

        int ret = IntStream.range(0, intList.size()).skip(3).filter(i -> i%2 == 0).peek(i -> System.out.println(i)).map(i -> intList.get(i)).sum();

        new Thread(() -> System.out.println(ret)).start();

//        Map<String, Integer> counts = new HashMap<>();
//        counts.put("Jenny", 1);
//
//        BiFunction<String, Integer, Integer> mapper = (k, v) -> v + 1;
//        Integer jenny = counts.computeIfPresent("Jenny", mapper);
//        Integer sam = counts.computeIfPresent("Sam", mapper);
//        System.out.println(counts); // {Jenny=2}
//        System.out.println(jenny); // 2
//        System.out.println(sam); // null

        Map<String, Integer> counts = new HashMap<>();
        counts.put("Jenny", 15);
        counts.put("Tom", null);

        Function<String, Integer> mapper = (k) -> 1;
        Integer jenny = counts.computeIfAbsent("Jenny", mapper); // 15
        Integer sam = counts.computeIfAbsent("Sam", mapper); // 1
        Integer tom = counts.computeIfAbsent("Tom", mapper); // 1
        System.out.println(counts); // {Tom=1, Jenny=15, Sam=1}
    }

    @Test
    public void fetchOrderArr2()
    {
        int[] arrIn = {2, 0, 0};
        printArr(arrIn, Main.fetchOrderArr(arrIn));
    }

    public void printArr(int[] arrIn, int[] arrOut)
    {

        for (int i : arrIn)
        {
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.print("  - ");
        for (int i : arrOut)
        {
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.println();
    }

    @Test
    public void isOrdered()
    {
    }
}