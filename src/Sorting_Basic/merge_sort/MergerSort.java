package Sorting_Basic.merge_sort;

import Sorting_Basic.Selection_Sort.SortTestHelper;

import java.util.Arrays;


public class MergerSort {
    public static void main(String[] args) {
        int N = 200;
        Integer[] num = SortTestHelper.generateRandomArray(N, 0, N);
        mergeSort(num, num.length - 1);
        mergeSortBU(num, num.length);
        System.out.println(Arrays.toString(num));

        int[] arr = {8, 3, 1, 6, 7, 4, 2};
        BubbleSort(arr, 8);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * @param arr 待排序数组
     * @param n   数组长度
     */
    private static void BubbleSort(int[] arr, int n) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = 1; j < n - i - 1; j++) {
                if (arr[j - 1] > arr[j]) {
                    //交换两个元素
                    int temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                }
            }
        }
    }

    private static void mergeSort(Integer[] num, int n) {
        __mergeSort(num, 0, n - 1);
    }

    private static void __mergeSort(Integer[] arr, int l, int r) {
        if (l >= r) {
            return;
        }

        //开始归并排序
        int mid = (l + r) / 2;
        //递归划分数组
        __mergeSort(arr, l, mid);
        __mergeSort(arr, mid + 1, r);

        //检查是否上一步归并完的数组是否有序，如果有序则直接进行下一次归并
        if (arr[mid] <= arr[mid + 1]) {
            return;
        }
        //将两边的元素归并排序
        merge(arr, l, mid, r);
    }

    /**
     * 自低向上的归并排序
     *
     * @param n   为数组长度
     * @param arr 数组
     */
    private static void mergeSortBU(Integer[] arr, int n) {
        //外层遍历从归并区间长度为1 开始 每次递增一倍的空间 1 2 4 8 sz 需要遍历到数组长度那么大
        //sz = 1 : [0] [1]...
        //sz = 2 : [0,1] [2.3] ...
        //sz = 4 : [0..3] [4...7] ...
        for (int sz = 1; sz <= n; sz += sz) {

            //内层遍历要比较 arr[i,i+sz-1] arr[i+sz,i+sz+sz-1] 两个区间的大小 也就是每次对 sz - 1 大小的数组空间进行归并
            // 注意每次 i 递增  两个 sz 的长度 ，因为每次 merge 的时候已经归并了两个 sz 长度 部分的数组
            for (int i = 0; i + sz < n; i += sz + sz) {
                merge(arr, i, i + sz - 1, Math.min(i + sz + sz - 1, n - 1));
            }
        }
    }

    /**
     * arr[l,mid] 和  arr[mid+1,r] 两部分进行归并
     */
    private static void merge(Integer[] arr, int l, int mid, int r) {

        // 复制数组 用来比较归并的数组两边的值
        int[] aux = new int[r - l + 1];

        for (int i = l; i <= r; i++) {
            aux[i - l] = arr[i];
        }

        int i = l;
        int j = mid + 1;

        for (int k = l; k <= r; k++) {
            if (i > mid) {
                //说明左边部分已经全都放入临时数组了
                arr[k] = aux[j - l];
                j++;
            } else if (j > r) {
                arr[k] = aux[i - l];
                i++;
            } else if (aux[i - l] < aux[j - l]) {
                arr[k] = aux[i - l];
                i++;
            } else {
                arr[k] = aux[j - l];
                j++;
            }
        }
    }
}
