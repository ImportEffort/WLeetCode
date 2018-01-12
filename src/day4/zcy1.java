package day4;

import java.util.Stack;

/***
 * 由于本人算法基础较差，所以从 day4 开始先刷 左程云 所写的《IT 名企算法与数据结构题目最优解》 一边学习一边巩固算法基础知识
 *
 * 第一章 第一题
 *
 * 设计一个有 getMin 功能的栈
 *
 * 实现一个特殊的栈，在实现站的基本功能的基础上，再实现返回栈中最小元素的操作
 *
 * 要求：
 *
 * 1. pop push getMin 操作的时间复杂度都为 O(1)
 * 2. 设计的栈类型可以使用现成的栈结构
 *
 * 学习目标：1、 时间复杂度的计算方法 2、 栈数据结构的特点 3、理解题目的实现方法
 */
public class zcy1 {

    public static void main(String[] args) {
        int[] testNumbers = new int[]{3, -4, 5, 1, 2, 1, 100, -1};
        GetMinStack getMinStack = new GetMinStack();
        OtherGetMinStack otherGetMinStack = new OtherGetMinStack();

        for (int testNumber : testNumbers) {
            getMinStack.push(testNumber);
        }

        for (int testNumber : testNumbers) {
            otherGetMinStack.push(testNumber);
        }


        System.out.println("getMinStack 最小值:: " + getMinStack.getMin());
        System.out.println("otherGetMinStack 最小值:: " + otherGetMinStack.getMin());
    }

    /***
     * 实现思路
     * 首先理解题目，要我们实现一个栈 ： 栈所包含的基本操作 pop push ，特点先进后出，然后实现 getMin 实现栈中获取最小值的方法
     *
     * 这里我们假设栈中存放的元素为 Integer 即整数
     *
     * 该方法 自定义栈中有两个栈，stackData 持有所进栈的所有数据  每次进进站都会判断 stackMin 栈顶元素 与进站元素的大小，
     * 如果进栈元素较小 则进 stackData 的时候也进 stackMin 否则不进 stackMin 因此 stackMin 栈顶始终与进栈元素中元素最小值相同
     *
     * 毫无疑问进出和获取最小值元素的操作数都是常数，与输入的数大小无关， 也就是时间复杂度都是常数 也就是 O(1)
     */
    private static class GetMinStack {

        private Stack<Integer> stackData;
        private Stack<Integer> stackMin;

        GetMinStack() {
            stackData = new Stack<>();
            stackMin = new Stack<>();
        }

        /**
         * 出栈方式
         *
         * @return 栈顶元素
         */
        public Integer pop() {

            if (stackData.isEmpty()) {
                throw new RuntimeException("Your Stack is empty");
            }

            int value = stackData.pop();

            if (value == getMin()) {
                stackMin.pop();
            }

            return value;
        }

        /**
         * 进栈方式
         *
         * @param num 进栈元素
         */
        public void push(Integer num) {

            if (stackMin.isEmpty()) {
                stackMin.push(num);
            } else if (num <= getMin()) {
                stackMin.push(num);
            }
            stackData.push(num);
        }


        /**
         * 获取栈中最小元素的方式
         *
         * @return 最小元素
         */
        public Integer getMin() {
            if (stackMin.isEmpty()) {
                throw new RuntimeException("Your Stack is empty");
            }
            return stackMin.peek();
        }
    }

    private static class OtherGetMinStack {
        private Stack<Integer> stackData;
        private Stack<Integer> stackMin;

        OtherGetMinStack() {
            stackData = new Stack<>();
            stackMin = new Stack<>();
        }

        /**
         * 出栈方式
         *
         * @return 栈顶元素
         */
        public Integer pop() {

            if (stackData.isEmpty()) {
                throw new RuntimeException("Your Stack is empty");
            }

            int value = stackData.pop();
            //区别第一种 stackData 元素出栈时 stackMin也出
            stackMin.pop();

            return value;
        }

        /**
         * 进栈方式
         *
         * @param num 进栈元素
         */
        public void push(Integer num) {

            if (stackMin.isEmpty()) {
                stackMin.push(num);
            } else if (num <= getMin()) {
                stackMin.push(num);
            }else {
                //区别第一种 stackData 元素进站时 stackMin也进站 只是如果当前要进站的值大于
                // stackMin 栈顶值时 每次进站的元素是当前栈顶元素 这样保证了两个栈大小相等 出栈操作不需要太多判断
                stackMin.push(stackMin.peek());
            }
            stackData.push(num);
        }


        /**
         * 获取栈中最小元素的方式
         *
         * @return 最小元素
         */
        public Integer getMin() {
            if (stackMin.isEmpty()) {
                throw new RuntimeException("Your Stack is empty");
            }
            return stackMin.peek();
        }
    }

}
