package day5;

import java.util.Stack;

/**
 * 使用一个栈实现另一个栈的排序
 * <p>
 * 1,3,2,6,4,5
 */
public class zcy5 {

    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(3);
        stack.push(2);
        stack.push(6);
        stack.push(4);
        stack.push(5);

        System.out.println("sortStack 前:" + stack);
        System.out.println("sortStack 后:" + sortStackByaAnotherStack(stack));
    }

    /**
     * 结果 最小的元素在下边 最大的元素在上边
     *
     * 思路  needSortStack 出栈一个元素 与 现有的 anotherStack 栈顶元素比较
     * 1. 如果 anotherStack 栈顶元素比 sortNum 要小 说明 sortNum 应该在 anotherStack 栈的前边
     * 2. 所以此时的 anotherStack 用元素要都倒入 needSortStack 中 再把
     * 3. 带 anotherStack 为空的时候讲 sortNum 放进  needSortStack 中 依次类推
     */
    private static Stack<Integer> sortStackByaAnotherStack(Stack<Integer> needSortStack) {
        Stack<Integer> anotherStack = new Stack<>();

        while (!needSortStack.isEmpty()) {
            Integer sortNum = needSortStack.pop();
            while (!anotherStack.isEmpty() && anotherStack.peek() < sortNum) {
                needSortStack.push(anotherStack.pop());
            }
            anotherStack.push(sortNum);
        }

        //这里得到的 anotherStack 元素最小的在上边 元素最大在下面
        while (!anotherStack.isEmpty()) {
            needSortStack.push(anotherStack.pop());
        }

        return needSortStack;
    }
}
