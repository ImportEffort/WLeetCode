package day6;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 题目描述：给定一个数组和滑动窗口的大小，请找出所有滑动窗口里的最大值。如果输入数组{2,3,4,2,6,2,5,1}及滑动窗口的大小3，
 * 那么一共存在6个滑动窗口，他们的最大值分别为{4.4,6,6,6,5}。
 * 输入描述：请输入一个数组：以空格隔开
 * 4 3 5 4 3 3 6 7
 * 请输入滑动窗口的大小：
 * 3
 * 程序输出： 滑动窗口的最大值为：
 * [5, 5, 5, 4, 6, 7]
 * <p>
 * 问题分析：因为是寻求最优解，所以通常第一能想到的解法： for 循环全部数组，每一次右移动 找出当前窗口的最大值存入数组的 O(wn)的时间复杂度
 * 不足以征服面试官，所以题中给出双向链表的解法。
 */
public class zcy6 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入一个数组：以空格隔开");
        String str = scanner.nextLine();

        System.out.println("请输入滑动窗口的大小：");
        int k = scanner.nextInt();

        String[] tmp = str.split(" ");
        int[] arrays = new int[tmp.length];
        for (int i = 0; i < tmp.length; i++) {
            arrays[i] = Integer.parseInt(tmp[i]);
        }
        scanner.close();

        SolutionMethod1 solution1 = new SolutionMethod1();
        System.out.println("滑动窗口的最大值为：");
        System.out.println(Arrays.toString(solution1.maxInWindows(arrays, k)));
        System.out.println(Arrays.toString(solution1.maxWindowsNormal(arrays, k)));
    }


    /**
     * 时间复杂度为 O(n) 的解法
     */
    private static class SolutionMethod1 {

        public int[] maxInWindows(int[] arrays, int k) {

            //不存在滑动窗口的情况
            if (arrays == null || arrays.length < k || k < 1) {
                return null;
            }

            //如果 arrays 的长度 为 4 那么滑动窗口大小为3 的时候 存在 2 个窗口 可得出最终滑动窗口最大值的数组的大小
            int[] result = new int[arrays.length - k + 1];

            //双向链表用于存放有可能成为移动窗口最大值得元素的角标
            LinkedList<Integer> qmax = new LinkedList<>();
            int index = 0;
            for (int i = 0; i < arrays.length; i++) {

                //qmax 角标放入条件 1. 当前循环i值 < qmax 队列 队末的角标对应数组中的值得时候讲角标添加到 qmax 中
                //反之则如果来一个比末尾大的值则说明末尾的值不可能是窗口最大值 则从队未出队
                while (!qmax.isEmpty() && arrays[qmax.peekLast()] < arrays[i]) {
                    qmax.pollLast();
                }
                qmax.addLast(i);

                //qmax 的第一个元素什么时候过期？ 如果 qmax 的队头值为 0  那么 i 进行到 3 的时候应该舍弃 3= 3-3
                if (qmax.getFirst() == i - k) {
                    qmax.pollFirst();
                }
                //什么时候该把当前元素放入 result 中 ？ i = 0 放  6  i= 1 放 6 i= 2 放 6 i= 3  放6
                if (i >= k - 1) {
                    result[index++] = arrays[qmax.peekFirst()];
                }

            }
            return result;
        }

        /**
         * 第一反应 能解出的 O(wn) 时间复杂度的解法
         *
         * @param arrays
         * @param w
         * @return
         */
        public int[] maxWindowsNormal(int[] arrays, int w) {
            //不存在滑动窗口的情况
            if (arrays == null || arrays.length < w || w < 1) {
                return null;
            }

            //如果 arrays 的长度 为 4 那么滑动窗口大小为3 的时候 存在 2 个窗口 可得出最终滑动窗口最大值的数组的大小
            int[] result = new int[arrays.length - w + 1];
            int index = 0;

            for (int i = 0; i < arrays.length - w + 1; i++) {
                int max = 0;
                for (int j = i; j < i + w; j++) {
                    if (arrays[j] > max) {
                        max = arrays[j];
                    }
                }
                result[index++] = max;
            }

            return result;
        }
    }
}
