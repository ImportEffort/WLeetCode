package Set;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSum {

    public static void main(String[] args) {
        int[] nums = {-1, 0, 1, 2, -1, -4};
        ThreeSum three = new ThreeSum();
        List<List<Integer>> threeSum = three.threeSum(nums);
        System.out.println("threeSum = " + threeSum);

        int[] fourTest = {5,0,2,-5,-5,4,-5,1,-1};// -5 -5 -5 -1 0 1 2 4 5

        List<List<Integer>> fourSum = three.fourSum(fourTest,-5);

        System.out.println("fourSum = " + fourSum);


    }


    public List<List<Integer>> threeSum(int[] nums) {
        if (nums == null || nums.length < 3) {
            return null;
        }

        ArrayList<List<Integer>> result = new ArrayList<>();

        Arrays.sort(nums);

        for (int k = 0; k < nums.length - 2; k++) {
            //我们要寻求的目标解的值
            int target = 0 - nums[k];

            //忽略相同的目标值只比较同一个相同的值

            int i = k + 1;
            int j = nums.length - 1;

            //对撞指针求解有续数组两数只和为target的数
            while (i < j) {
                if (nums[i] + nums[j] < target) {
                    i++;
                } else if (nums[i] + nums[j] > target) {
                    j--;
                } else {
                    ArrayList<Integer> twoSumList = new ArrayList<>();
                    twoSumList.add(nums[k]);
                    twoSumList.add(nums[i]);
                    twoSumList.add(nums[j]);
                    result.add(twoSumList);
                    //滤除可能的重复解
                    while (i < j && nums[i] == nums[i + 1]) i++;
                    while (i < j && nums[j] == nums[j - 1]) j--;
                    //上述遍历之后是最后一个相等的位置，所以还要++
                    i++;
                    j--;
                }
            }

            while (k < nums.length -2 && nums[k] == nums[k + 1])
                k++;

        }

        return result;
    }


    public List<List<Integer>> fourSum(int[] nums,int target) {


        ArrayList<List<Integer>> result = new ArrayList<>();

        if (nums == null || nums.length < 4) {
            return result;
        }

        Arrays.sort(nums);// -5 -5 -5 -1 0 1 2 4 5


        for (int i = 0; i < nums.length - 3; i++) {

            for (int j = i + 1; j < nums.length - 2; j++) {

                //我们要寻求的目标解的值
                int sum = target - nums[i] - nums[j];

                int a = j + 1;
                int b = nums.length - 1;

                //对撞指针求解有续数组两数只和为target的数
                while (a < b) {
                    if (nums[a] + nums[b] < sum) {
                        a++;
                    } else if (nums[a] + nums[b] > sum) {
                        b--;
                    } else {
                        ArrayList<Integer> twoSumList = new ArrayList<>();
                        twoSumList.add(nums[i]);
                        twoSumList.add(nums[j]);
                        twoSumList.add(nums[a]);
                        twoSumList.add(nums[b]);
                        result.add(twoSumList);
                        //滤除可能的重复解
                        while (a < b && nums[a] == nums[a + 1]) a++;
                        while (a < b && nums[b] == nums[b - 1]) b--;
                        //上述遍历之后是最后一个相等的位置，所以还要++
                        a++;
                        b--;
                    }
                }

                //如果放到循环 ab 前边会忽略两个 0 的情况
                while (j < nums.length - 2 && nums[j] == nums[j + 1])
                    j++;

            }

            //忽略相同的目标值只比较同一个相同的值
            while ( i < nums.length -3 && nums[i + 1] == nums[i])
                i++;
        }

        return result;
    }

}
