package ArrayAMartix;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;

public class MinSubArrayLen {

    public static void main(String[] args) {
//        int[] arr = {2, 3, 1, 2, 4, 3};
        int[] arr = {1, 2, 3, 4, 5};
        HashSet<Integer> set = new HashSet<>();
        int sum = 15;
        System.out.println(new MinSubArrayLen().minSubArrayLen(sum, arr));

    }

    public int minSubArrayLen(int s, int[] nums) {

        for (int area = 1; area <= nums.length; area++) {
            int i = 0;
            int j = i + area - 1;

            while (j < nums.length) {
                if (sum(nums, i, j) >= s) {
                    return j - i + 1;
                }
                j++;
                i++;
            }
        }
        return 0;
    }


    private int sum(int[] nums, int i, int j) {
        int sum = 0;
        System.out.println("i " + i + "  j " + j);
        for (int n = i; n <= j; n++) {
            sum += nums[n];
        }
        return sum;
    }
}
