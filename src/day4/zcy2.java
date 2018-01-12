package day4;

import java.util.Stack;

/**
 * 使用两个栈实现一个队列，该队列支持基本操作（add, poll peek）
 * <p>
 * 学习目标
 * 1.了解队列的基本知识
 * 2.学习队列的基本操作
 */
public class zcy2 {

    public static void main(String[] args) {
        int[] testNumbers = new int[]{3, -4, 5, 1, 2, 1, 100, -1};
        TwoStackQueue queue = new TwoStackQueue();

        for (int i = 0; i < testNumbers.length; i++) {
            queue.add(testNumbers[i]);
        }
        System.out.println("执行 peek 操作:: " + queue.peek());
        System.out.println("执行 poll 操作:: " + queue.poll());


    }

    /**
     * 此题不是很难首先明确栈和队列的区别：
     * 1. 栈 先进后出 队列先进先出
     * 2. 要保证在执行队列 pop 和 peek 的时候 stackPop 栈中是空的 然后将 stackPush 栈中的元素一次性压入 stackPop
     * 3. 出队是对 stackPop 进行操操作
     *
     *  值得思考的地方： 如果 TwoStackQueue add  3 ， -4 后 调用一次 poll 或者 peek 那么 stockPop 就不为空了，此时stackPush 为空
     *  那么在执行一次 add 5 后，再去执行 poll 或 peek stackPop 由于 stackPop.isEmpty()为 false 并不会执行进栈，只有在 stackPop 为空后才能再次进栈
     *  在 stackPop 再次进站前 stackPush 是不为空的。压入的元素保存在 stackPush 。
     *
     */
    private static class TwoStackQueue {

        private Stack<Integer> stackPush;
        private Stack<Integer> stackPop;

        public TwoStackQueue() {
            this.stackPush = new Stack<>();
            this.stackPop = new Stack<>();
        }

        /**
         * 如队列操作
         *
         * @param num
         */
        public void add(Integer num) {
            stackPush.push(num);
            System.out.println("stackPush 栈中内容:: " + stackPush);
        }

        public Integer poll() {
            if (stackPop.isEmpty() && stackPush.isEmpty()) {
                throw new RuntimeException("Queue is Empty!");
            }
            if (stackPop.isEmpty()) {
                while (!stackPush.isEmpty()) {
                    stackPop.push(stackPush.pop());
                }
            }
            return stackPop.pop();
        }


        public Integer peek() {
            if (stackPop.isEmpty() && stackPush.isEmpty()) {
                throw new RuntimeException("Queue is Empty!");
            }
            if (stackPop.isEmpty()) {
                while (!stackPush.isEmpty()) {
                    stackPop.push(stackPush.pop());
                }
            }
            return stackPop.peek();
        }
    }
}
