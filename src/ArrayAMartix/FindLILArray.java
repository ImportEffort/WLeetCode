package ArrayAMartix;

import java.util.Arrays;
import java.util.HashSet;

/***
 * 最长的可整合子数组的长度
 *
 * 最长可整合子数组： 如果一个数组在排序之后，每个相邻的数之差的绝对值都为1 那么该数组为最常可整合数组
 */
public class FindLILArray {
    public static void main(String[] args) {
        int[] arr = {5, 5, 3, 2, 6, 4, 3};
        FindLILArray findLILArray = new FindLILArray();
        int len = findLILArray.getLIL(arr);
        System.out.println(" 最大可整合子数组长度为 " + len);
        int lilAdvance = findLILArray.getLILAdvance(arr);
        System.out.println(" 最大可整合子数组长度为 " + lilAdvance);
    }

    /**
     * 实现方法1 每一个子数组排序，然后判断是不是整合数组
     *
     * @param arr
     * @return
     */
    private int getLIL(int[] arr) {
        if (arr == null || arr.length < 1) {
            return 0;
        }
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                if (isIntegrated(arr, i, j)) {
                    len = Math.max(len, j - i + 1);
                }
            }
        }

        return len;
    }


    private boolean isIntegrated(int[] arr, int left, int right) {
        int[] newArr = Arrays.copyOfRange(arr, left, right + 1);//[i,j)

        sortArray(newArr);

        for (int i = 1; i < newArr.length; i++) {
            if (newArr[i - 1] != newArr[i] - 1) {
                return false;
            }
        }
        return true;
    }


    /**
     * 由于可整合数组 中必定不包含重复元素 又因为 如果一个数组中没有重复元素，其最大值减去最小值，再加上1结果就等于元素个数 len
     *
     * @param arr
     * @return
     */
    private int getLILAdvance(int[] arr) {
        if (arr == null || arr.length < 1) {
            return 0;
        }
        int len = 0;
        int max = 0;
        int min = 0;

        HashSet<Integer> set = new HashSet<>();//用于判断重复
        for (int i = 0; i < arr.length; i++) {
            max = Integer.MIN_VALUE;
            min = Integer.MAX_VALUE;

            for (int j = i; j < arr.length; j++) {
                if (set.contains(arr[j])) {
                    break;
                }

                set.add(arr[j]);

                max = Math.max(max, arr[j]);
                min = Math.min(min, arr[j]);

                //max - min +1 等于 数组长度的时候表示该数组为可整合数组 更新长度
                if (max - min + 1 == j - i + 1) {
                    len = Math.max(j - i + 1, len);
                }
            }
            //记得清空 set
            set.clear();
        }


        return len;
    }


    private void sortArray(int[] newArr) {
        quickSort(newArr, 0, newArr.length - 1);
    }

    private void quickSort(int[] arr, int l, int r) {
        if (l >= r) {
            return;
        }

        int index = partition(arr, l, r);
        quickSort(arr, l, index - 1);
        quickSort(arr, index + 1, r);
    }

    private int partition(int[] arr, int l, int r) {
        int randomIndex = (int) (Math.random() * (r - l + 1) + l);
        swap(arr, randomIndex, l);
        int v = arr[l];
        int i = l + 1;
        int j = r;

        while (true) {
            while (i <= r && arr[i] <= v) {
                i++;
            }

            while (j > l + 1 && arr[j] >= v) {
                j--;
            }

            if (i > j) {
                break;
            }

            swap(arr, i, j);
            i++;
            j--;
        }

        swap(arr, l, j);
        return j;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
