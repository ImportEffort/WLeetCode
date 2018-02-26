package Sorting_Basic.quick_sort;

import Sorting_Basic.Selection_Sort.SortTestHelper;

import java.util.Arrays;

/***
 * 快速排序优化版 双路快速排序 采用双指针的方式来遍历数组
 *
 * 跟单路一样，双路快速排序
 *
 * 同样选择数组的第一个元素当做标志位（经过随机选择后的）
 *
 * 双路快速排序要求有两个指针，指针i j 分别指向 l+1 和 r 的位置
 * 然后两者同时向数组中间遍历 在遍历过程中要保证
 *
 * arr[l+1 ... i) <= v
 * arr(j....r] >= v
 *
 * 因此我们可以初始化 i = l+1 以保证左侧区间初始为空
 * j = r 保证右侧空间为空
 *
 * 遍历过程中要 i <= r 且 arr[i] <= v 的时候 i ++ 就可以了 当 arr[i] > v 时表示遇到了 i 的值大于 v 数值 此刻能等待 j 角标的值
 *
 * 而 j 角标的移动过程为 j >= l + 1 为临界点 ，从右向左遍历数组 当 arr[i] < v 表示遇到了 j 的值小于 v 的元素，它不该在这个位置呆着
 *
 * 得到了 i j 的角标后 先要判断是否到了循环结束的时候了，即 i 是否已经 大于 j 了
 *
 * 否则 应该讲 i 位置的元素和 j 位置的元素交换位置，然后 i++ j-- 继续循环
 *
 *
 * 遍历结束的条件是 i>j 此时 arr[j]为最后一个小于 v 的元素 arr[i] 为第一个大于 v 的元素
 * 因此 j 这个位置 就应该是 v 所应该在数组中的位置 因此遍历结束后需要交换 arr[l] 与 arr[j]
 *
 */
public class QuickSort2Ways {

    public static void main(String[] args) {
        int N = 20;
        Integer[] num = SortTestHelper.generateRandomArray(N, 0, N);
        quickSort2(num, num.length);
        System.out.println(Arrays.toString(num));
    }


    private static void quickSort2(Integer[] num, int length) {
        __quickSort(num, 0, length - 1);
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

        int i = l + 1;
        int j = r;

        while (true) {

            while (i <= r && arr[i] <= v) i++;
            while (j >= l + 1 && arr[j] >= v) j--;

            if (i > j) break;

            swap(arr, i, j);
            i++;
            j--;
        }
        //j 最后角标停留在 i > j 即为 比 v 小的最后一个一元素位置
        swap(arr, l, j);

        return j;
    }


    private static void swap(Integer[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
