package Sorting_Basic.Insertion_Sort;

import Sorting_Basic.Selection_Sort.SortTestHelper;

import java.util.Arrays;

/***
 * 插入排序
 */
public class InsertIonSort {

    public static <T extends Comparable<T>> void sort(T[] arr) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            //内层循环比较 i 与前边所有元素值，如果 j 索引所指的值小于 j- 1 则交换两者的位置
            for (int j = i; j > 0 && arr[j].compareTo(arr[j - 1]) < 0; j--) {
                SortTestHelper.swap(arr, j, j - 1);
            }
        }
    }


    // 测试InsertionSort
    public static void main(String[] args) {

        int N = 20000;
        Integer[] arr1 = SortTestHelper.generateRandomArray(N, 0, N);
        Integer[] arr2 = Arrays.copyOf(arr1, arr1.length);

        // 比较SelectionSort和InsertionSort两种排序算法的性能效率
        // 此时，插入排序比选择排序性能略低
        SortTestHelper.testSort("Sorting_Basic.Selection_Sort.SelectionSort", arr2);
        SortTestHelper.testSort("Sorting_Basic.Insertion_Sort.InsertIonSort", arr1);

    }

}
