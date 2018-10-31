package ArrayAMartix;

import java.util.Arrays;

/***
 *给定一个包含红色、白色和蓝色，一共 n 个元素的数组，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
 *此题中，我们使用整数 0、 1 和 2 分别表示红色、白色和蓝色。
 *
 * 示例:
 *
 * 输入: [2,0,2,1,1,0]
 * 输出: [0,0,1,1,2,2]
 *
 */
public class SortColors {

    public static void main(String[] args) {
        int[] nums = {2, 0, 2, 1, 1, 0};
//        sortColors(nums);
        sortColorByQuickSort(nums);
        System.out.println(Arrays.toString(nums));
    }

//    public static void sortColors(int[] nums) {
//        int lenCount = 3;
//        int[] countArr = new int[lenCount];
//        //计数
//        for (int i = 0; i < nums.length; i++) {
//            countArr[nums[i]] += 1;
//        }
//
//        //求和
//        for (int i = 1; i < countArr.length; i++) {
//            countArr[i] = countArr[i] + countArr[i - 1];
//        }
//
//        System.out.println(Arrays.toString(countArr));
//
//        //赋值
//        int[] result = new int[nums.length];
//
//        for (int i = nums.length - 1; i >= 0; i--) {
//            //这里减1是必须的？
//            result[countArr[nums[i]] - 1] = nums[i];
//            countArr[nums[i]] -= 1;
//        }
//
//        System.arraycopy(result, 0, nums, 0, nums.length);
//    }


    /**
     * 基于计数排序
     *
     * @param nums 待排序数组
     */
    public static void sortColors(int[] nums) {
        int max = 0;
        int min = 0;
        //求数组中的最大最小值，得到计数集合的大小
        for (int i : nums) {
            if (i > max) {
                max = i;
            }

            if (i < min) {
                min = i;
            }
        }
        //计数

        //数组中最多可能有多少种元素
        int lenResult = max - min + 1;
        //该数组用来存放，对应数出现的次数
        int[] countArr = new int[lenResult];

        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            countArr[num - min] += 1;
        }

        //求和 数组当前角标的值，等于前一个角标的值，与当前角标的值求和 这步的原理是什么？
        //此后每一位并不代表出现的次数了
        // countArr[num[i] - min] 代表小于该位置元素值的元素的个数，所以num[i] 应该处于countArr[num[i] - min] -1 位置

        for (int i = 1; i < countArr.length; i++) {
            countArr[i] = countArr[i] + countArr[i - 1];
        }


        int[] result = new int[nums.length];
        //倒着取元素 赋值
        for (int i = nums.length - 1; i >= 0; i--) {
            //数组中第i个元素的值 - 最小值 - 1  为所代表的数字在 countArr 中的角标
            //而这个i元素在结果数组中的位置正是 上一步得到的角标所在 countArr 的数值？
            int index = countArr[nums[i] - min] - 1;
            result[index] = nums[i];
            //当前角标数减去 1 避免下次取得的相同的数放在同样的位置
            countArr[nums[i] - min] -= 1;
        }

        System.arraycopy(result, 0, nums, 0, nums.length);
    }


    private static void sortColorByQuickSort(int[] nums) {
        quickSort(nums, 0, nums.length - 1);
    }

    private static void quickSort(int[] nums, int l, int r) {

        _quickSort(nums, l, r);

    }

    private static void _quickSort(int[] nums, int l, int r) {
        if (l >= r) {
            return;
        }

        int v = nums[l];
        int lt = l;// arr[l+1...lt] < v
        int gt = r + 1;// arr[gt...r] > v
        int i = l + 1;// arr[lt+1...i) == v

        while (i < gt) {
            int num = nums[i];
            if (num < v) {
                swap(nums, i, lt + 1);//如果不交换 则会得到不正确的结果
                lt++;
                i++;
            } else if (num > v) {
                swap(nums, i, gt - 1);
                gt--;
            } else {
                i++;
            }
        }

        swap(nums, l, lt);

        _quickSort(nums, l, lt - 1);
        _quickSort(nums, gt, r);
    }

    private static void swap(int[] nums, int a, int b) {
        int temp = nums[a];
        nums[a] = nums[b];
        nums[b] = temp;
    }

}
