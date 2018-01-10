package day2;

import java.util.Arrays;

/***
 * Given an array of 2n integers, your task is to group these integers into n pairs of integer, say (a1, b1), (a2, b2), ..., (an, bn)
 * which makes sum of min(ai, bi) for all i from 1 to n as large as possible.
 *
 * Input: [1,4,3,2]
 * Output: 4
 * Explanation: n is 2, and the maximum sum of pairs is 4.
 *
 * Note:
 *
 * n is a positive integer, which is in the range of [1, 10000].
 * All the integers in the array will be in the range of [-10000, 10000].
 *
 * 这道题让我们分割数组，两两一对，让每对中较小的数的和最大。这题难度不大，用 贪婪算法 就可以了。
 * 由于我们要最大化每对中的较小值之和，那么肯定是每对中两个数字大小越接近越好，因为如果差距过大，而我们只取较小的数字，那么大数字就浪费掉了。
 * 明白了这一点，我们只需要给数组排个序，然后按顺序的每两个就是一对，我们取出每对中的第一个数即为较小值累加起来即可，
 *
 */
public class leetCode561 {
    public static void main(String[] args) {
        int[] nums = {1, 4, 3, 2};
        System.out.println(arrayPairSum(nums));
    }

    private static int arrayPairSum(int[] nums) {

        int arrayPairSum = 0;
        Arrays.sort(nums);

        for (int i = 0; i < nums.length; i = i + 2) {
            arrayPairSum += nums[i];
        }

        return arrayPairSum;
    }
}
