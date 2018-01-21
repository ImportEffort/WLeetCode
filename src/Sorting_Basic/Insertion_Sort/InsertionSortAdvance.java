package Sorting_Basic.Insertion_Sort;

import Sorting_Basic.Selection_Sort.SortTestHelper;

import java.util.Arrays;

/**
 * 优化后的 插入排序 插入排序本身是 O(n²) 的时间复杂度 但是之前内层循环在不停的交换 j j-1 (如果 j-1 大于 j 的情况下)
 * 交换需要进行三次赋值 导致效率很低下 因此有改善的版本原理就是 如果  (如果 j-1 大于 j 的情况下)  那么就让 j 等于 j -1 直到
 * 内存循环因为 j-1 小于 j 了 那么此时 j 应该就是 外层循环 i 对应的值所应该处于的角标位置
 */
public class InsertionSortAdvance {

    public static <T extends Comparable<T>> void sort(T[] arr) {
        int length = arr.length;
        for (int i = 0; i < length; i++) {

            T e = arr[i];
            int j = i;
            // 注意内层循环结束条件
            for (; j > 0 && arr[j - 1].compareTo(arr[j]) > 0; j--) {
                arr[j] = arr[j - 1];
            }
            arr[j] = e;
        }
    }

    public static void main(String[] args) {
        int N = 20000;

        // 测试1 一般测试
        System.out.println("Test for random array, size = " + N + " , random range [0, " + N + "]");

        Integer[] arr1 = SortTestHelper.generateRandomArray(N, 0, N);
        Integer[] arr2 = Arrays.copyOf(arr1, arr1.length);
        Integer[] arr3 = Arrays.copyOf(arr1, arr1.length);

        SortTestHelper.testSort("Sorting_Basic.Insertion_Sort.InsertionSortAdvance", arr1);
        SortTestHelper.testSort("Sorting_Basic.Insertion_Sort.InsertIonSort", arr3);
        SortTestHelper.testSort("Sorting_Basic.Selection_Sort.SelectionSort", arr2);

        System.out.println();


        // 测试2 有序性更强的测试
        System.out.println("Test for more ordered random array, size = " + N + " , random range [0,3]");

        arr1 = SortTestHelper.generateRandomArray(N, 0, 3);
        arr2 = Arrays.copyOf(arr1, arr1.length);
        arr3 = Arrays.copyOf(arr1, arr1.length);

        SortTestHelper.testSort("Sorting_Basic.Insertion_Sort.InsertionSortAdvance", arr1);
        SortTestHelper.testSort("Sorting_Basic.Insertion_Sort.InsertIonSort", arr3);
        SortTestHelper.testSort("Sorting_Basic.Selection_Sort.SelectionSort", arr2);

        System.out.println();


        // 测试3 测试近乎有序的数组
        int swapTimes = 100;
        System.out.println("Test for nearly ordered array, size = " + N + " , swap time = " + swapTimes);

        arr1 = SortTestHelper.generateNearlyOrderedArray(N, swapTimes);
        arr2 = Arrays.copyOf(arr1, arr1.length);
        arr3 = Arrays.copyOf(arr1, arr1.length);

        SortTestHelper.testSort("Sorting_Basic.Insertion_Sort.InsertionSortAdvance", arr1);
        SortTestHelper.testSort("Sorting_Basic.Insertion_Sort.InsertIonSort", arr3);
        SortTestHelper.testSort("Sorting_Basic.Selection_Sort.SelectionSort", arr2);
    }
}
