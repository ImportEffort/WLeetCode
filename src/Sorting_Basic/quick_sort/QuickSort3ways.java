package Sorting_Basic.quick_sort;

import Sorting_Basic.Selection_Sort.SortTestHelper;

import java.util.Arrays;

/***
 * 三路快速排序
 * 将 arr[l...r] 分为 <v == v v> 三部分
 *
 * arr[l+1...lt] <v
 * arr[lt+1..i) =v
 * arr[gt...r] > v
 */
public class QuickSort3ways {

    public static void main(String[] args) {
        int N = 20;
        Integer[] num = SortTestHelper.generateRandomArray(N, 0, N);
        quickSort3(num, num.length);
        System.out.println(Arrays.toString(num));
    }


    private static void quickSort3(Integer[] num, int length) {
        __quickSort(num, 0, length - 1);
    }

    private static void __quickSort(Integer[] arr, int l, int r) {

        if (l >= r) {
            return;
        }

        // 为了提高效率，减少造成快速排序的递归树不均匀的概率，
        // 对于一个数组，每次随机选择的数为当前 partition 操作中最小最大元素的可能性 降低 1/n!

        int randomNum = (int) (Math.random() * (r - l + 1) + l);
        swap(arr, l, randomNum);

        int v = arr[l];
        // 三路快速排序即把数组划分为大于 小于 等于 三部分
        //arr[l+1...lt] <v  arr[lt+1..i) =v  arr[gt...r] > v 三部分
        // 定义初始值得时候依旧可以保证这初始的时候这三部分都为空
        int lt = l;
        int gt = r;
        int i = l + 1;

        while (i < gt) {
            if (arr[i] < v) {
                swap(arr, i, lt + 1);
                i++;
                lt++;
            } else if (arr[i] == v) {
                i++;
            } else {
                swap(arr, i, gt - 1);
                gt--;
                //i++ 注意这里 i 不需要加1 因为这次交换后 i 的值仍不等于 v 可能小于 v 也可能等于 v 所以交换完成后 i 的角标不变
            }
        }
        //循环结束的后 lt 所处的位置为 <v 的最后一个元素 i 肯定与 gt 重合
        //但是 最终v 要放的位置并不是 i 所指的位置 因为此时 i 为大于 v 的第一个元素 v
        //而 v 应该处的位置为 lt 位置 并不是 i-1 所处的位置（arr[i-1] = arr[l]）
        swap(arr, l, lt);
    }


    private static void swap(Integer[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
