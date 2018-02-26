package Sorting_Basic.quick_sort;

import Sorting_Basic.Selection_Sort.SortTestHelper;

import java.lang.reflect.Array;
import java.util.Arrays;

/***
 * 快速排序：将数组中的一个数，放在合适的位置：
 *  这个位置左边的数全部小于该数值，这个位置右边的数全部大于该数值。
 *
 *  单路快速排序：
 *    假设指定数值为数组第一个元素 int v = arr[l]
 *    假设 j 标记为比 v 小的最后一个元素， 即 arr[j+1] > v
 *    当前考察的元素为 i
 *
 *    则有；
 *    arr[l + 1 ... j] < v
 *    arr[j+1,i) >= v
 *
 *    初始化
 *
 *     int v = arr[l];
 *
 *     int j = l; // 使 arr[l + 1 ... j] 最开始为空
 *     int i = l + 1; 使 arr[j+1,i)  最开始为空
 *
 *    在循环考察的过程中
 *
 *    遇到 arr[i] >= v 的时候 i++
 *    遇到 arr[i] < v  的时候 需要交换 arr[j+1] 与 arr[i] 的位置，交换完成后，j ++
 *
 *    考察完成后
 *
 *    arr[l+1...j] < v
 *    arr[j+1...r] >= v
 *
 *    最后需要做的就是 将 arr[l] 与 arr[j] 交换位置
 *
 *    这样一顿操作后 就能保证
 *
 *    arr[l ... j-1] < v
 *    arr[j+1 ... r] >=v
 *
 *    随后递归的将左右两边进行同样的操作就可以了
 *
 *
 */
public class QuickSort {

    public static void main(String[] args) {
        int N = 200;
        Integer[] num = SortTestHelper.generateRandomArray(N, 0, N);
        quickSort(num, num.length);
        System.out.println(Arrays.toString(num));
    }

    /**
     * 单路快速排序
     *
     * @param num 数组大小
     * @param n   数组长度
     */
    private static void quickSort(Integer[] num, int n) {
        __quickSort(num, 0, n - 1);
    }

    private static void __quickSort(Integer[] arr, int l, int r) {

        if (l >= r) {
            return;
        }

        int p = partition(arr, l, r);

        __quickSort(arr, l, p - 1);
        __quickSort(arr, p + 1, r);
    }

    private static int partition(Integer[] arr, int l, int r) {

        // 为了提高效率，减少造成快速排序的递归树不均匀的概率，
        // 对于一个数组，每次随机选择的数为当前 partition 操作中最小最大元素的可能性 降低 1/n!

        int randomNum = (int) (Math.random() * (r - l + 1) + l);
        swap(arr, l, randomNum);

        int v = arr[l];
        int j = l;

        for (int i = l + 1; i <= r; i++) {
            if (arr[i] < v) {
                swap(arr, j + 1, i);//没想明白
                j++;
            }
        }
        swap(arr, l, j);
        return j;
    }

    private static void swap(Integer[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
