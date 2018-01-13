package day5;

import java.util.Stack;

/***
 * 递归实现一个栈的逆序
 *
 *如 1，2，3，4，5 顺序进站  栈顶为5 现在使用递归的方法将元素重新进栈 5，4，3，2，1
 * 考察点 递归思想： 函数自己调用自己
 *
 *  举个简单的例子：
 *
 *  栈中现在有 1，2，3
 *      现在 getLastElementInStack 方法 执行的内容为注释部分
 *      刚开始困惑 为什么 判断条件是站为空则返回 pop 结果：
 *          1. 最小情况下 站内只存在一个元素 栈顶 = 栈底 所以逆序只是该元素出站在进站的过程 （出口的来源）
 *          2. 如果栈包含多个元素 执行的顺序为 外层n 出栈 外层 n-1 出栈 ... 第 1 层 返回栈底值出栈栈底元素 第 2 层 进栈 原栈底开始第 2 个元素 ...
 *          3. result 保存每次一层调用出栈的元素 这个元素只有是栈底值得时候才 作为函数结果，如果不是则再次调用本身 并在内层递归结束后一层一层 由内而外再次进站
 */
public class zcy3 {
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);

        System.out.println("翻转前的 stack：： " + stack);
        reverse(stack);
        System.out.println("翻转后的 stack：： " + stack);
    }

    private static int getLastElementInStack(Stack<Integer> stack) {
        Integer result = stack.pop();
        if (stack.isEmpty()) {
            return result;
        } else {
            int element = getLastElementInStack(stack);
//            // 内层调用
//            Integer reslut1 = stack.pop();//栈中此时为空 [1] reslut1 = 2
//            if (stack.isEmpty()){
//                return reslut1;
//            }else {
//                Integer reslut2 = stack.pop();//栈中此时为空 [] reslut1 = 2
//                if (stack.isEmpty()){
//                    return reslut2;//直接 return 1；即  element = 1；
//                }else {
//                    //因为第3层 stack 为空了 那么就不会执行 push 操作
//                }
//                //内层push 2
//                stack.push(reslut2);
//                //  return 1
//                return element;
//            }
//
//            // 这里 push 的是 3
//            stack.push(result);
//            // 并且 return 1
//            return element;

            // 这里 push 的是 3
            stack.push(result);
            // 并且 return 1
            return element;
        }
    }

    private static void reverse(Stack<Integer> stack) {
        if (stack.isEmpty()) {
            return;
        }
        int lastElementInStack = getLastElementInStack(stack);
        reverse(stack);
        stack.push(lastElementInStack);
    }
}
