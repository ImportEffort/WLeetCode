import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * 数组中重复的数字
 * 题目：在一个长度为n的数组里的所有数字都在 0 到 n-1 的范围内。数组中某些数字是重复的，
 * 但不知道有几个数字重复了，也不知道每个数字重复了几次。请找出数组中任意一个重复的数字。
 * <p>
 * 思路： 如果一个长度为 n 的数组存储的整数全部在 0 ~ n-1 范围内，那如果数组中没有重复数字，如果重排数组，那么可以刚好使得 0 ~ n-1
 * 数组为 i 的元素 在 arr[i-1]的位置上。如果有重复数字，当第二次往 arr[i-1] 上放置的时候，就会重复，那么这个元素就是我们要找的那个元素。
 */
public class FindTheDupliNum {
    public static void main(String[] args) {
        int[] numbers1 = {2, 1, 3, 1, 4};
        System.out.println(findOneDuplicateNum(numbers1));
        int[] numbers2 = {2, 4, 3, 1, 4};
        System.out.println(findOneDuplicateNum(numbers2));
        int[] numbers3 = {2, 4, 2, 1, 4};
        System.out.println(findOneDuplicateNum(numbers3));
        int[] numbers4 = {2, 1, 3, 0, 4};
        System.out.println(findOneDuplicateNum(numbers4));
    }

    private static int findOneDuplicateNum(int[] arr) {
        if (arr == null || arr.length < 1) {
            return -1;
        }

        for (int i = 0; i < arr.length; i++) {
            //重排数组的过程，使 i 的位置值为 i
            while (arr[i] != i) {
                if (arr[i] == arr[arr[i]]) {
                    return arr[i];
                } else {
                    swap(arr, i, arr[i]);
                }
            }
        }

        return -1;
    }

    private static void swap(int[] arr, int x, int y) {
        int temp = arr[x];
        arr[x] = arr[y];
        arr[y] = temp;
    }
}
