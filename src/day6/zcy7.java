package day6;

import java.util.HashMap;
import java.util.Stack;

/***
 * 构造数组的MaxTree
 * 定义二叉树结点如下：
 *
 * public class Node {
 *      public int value;
 *      public Node left;
 *      public Node right;
 *
 *      public Node(int data) {
 *        this.value = data;
 *      }
 * }
 *
 * 一个数组的MaxTree定义如下。
 *
 * 数组必须没有重复元素。
 *
 * 1、 MaxTree是一颗二叉树，数组的每一个值对应一个二叉树节点。
 * 2、 包括MaxTree树在内且在其中的每一颗树上，值最大的节点都是树的头。
 * 3、 给定一个没有重复元素的数组arr，写出生成这个数组的MaxTree的函数，要求如果数组长度为N，则时间复杂度为O(n)、额外空间复杂度为O(n).
 *
 * in[] arr = { 3 , 4 , 5 , 1 , 2 }
 *
 * 解法:
 * 1. 每一个数的父节点是它左边第一个比他大的数和右边第一个比它大的数中，较小的那个
 * 2. 如果一个数他左边没有比他大的数右边也没有，那么他肯定是数组中的最大值也就是 MaxTree 的 根节点
 *
 * 啊 好难！！！
 */
public class zcy7 {


    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int data) {
            this.value = data;
        }
    }


    public static Node getMaxTree(int[] arr) {
        //1：构造原始节点数组
        Node[] nArr = new Node[arr.length];
        for (int i = 0; i < arr.length; i++) {
            nArr[i] = new Node(arr[i]);
        }

        //构建获取数值左边右两边第一个比他大的数的 map 所有的栈 以及 map
        Stack<Node> stack = new Stack<>();
        HashMap<Node, Node> leftMap = new HashMap<>();
        HashMap<Node, Node> rightMap = new HashMap<>();

        //生成 leftMap 这里边存放 value 是 key 左边比 key 大的第一个数
        for (int i = 0; i < nArr.length; i++) {
            Node curNode = nArr[i];
            //如果当前值小于栈顶的值则直接入栈，如果当前大于栈顶的值则可以确定栈顶的元素左边第一个比它大的数要么 null 要是栈中下一个元素
            //stack.peek().value < curNode.value 可以看出 栈中元素小的在上大的在下
            while ((!stack.isEmpty()) && stack.peek().value < curNode.value) {
                popStackSetMap(stack, leftMap);
            }
            stack.push(curNode);
        }

        while (!stack.isEmpty()) {
            popStackSetMap(stack, leftMap);
        }

        //经过上述操作栈中元素以及入栈元素(每个数)左边第一个比他大的数已经存放在 leftMap 中

        //进行到这里的时候 stack 肯定为空
        for (int i = nArr.length - 1; i > 0; i--) {
            Node curNode = nArr[i];
            while ((!stack.isEmpty()) && stack.peek().value < curNode.value) {
                popStackSetMap(stack, rightMap);
            }
            stack.push(curNode);
        }

        while (!stack.isEmpty()) {
            popStackSetMap(stack, rightMap);
        }

        //经过上述操作栈中元素以及入栈元素(每个数)右边第一个比他大的数已经存放在 rightMap 中

        Node head = null;

        for (int i = 0; i < nArr.length; i++) {

            Node curNode = nArr[i];

            Node left = leftMap.get(curNode);
            Node right = rightMap.get(curNode);

            //如果一个数他左边没有比他大的数右边也没有，那么他肯定是数组中的最大值也就是 MaxTree 的 根节点
            if (left == null && right == null) {//5
                head = curNode;
            } else if (left == null) {// 当一个数左边没有比他更大的数的收优先安排左节点？

                //如果一个数的左边没有比他更大的数 右边有，那么他肯定是这个更大数的子节点，
                // 如果比他大的这个数左节点为空 那么这个数是这个比他更大数的左节点
                if (right.left == null) {//3 左大 null 右大 4  ，4 左大 null  右大 5 当3走到这里的时候 4 的左节点被安排成 3 （为什么不是右节点？）
                    right.left = curNode;
                } else {
                    right.right = curNode;
                }

            } else if (right == null) {//2： 2 左大 5  右大 null

                if (left.left == null) {
                    left.left = curNode;
                } else {
                    left.right = curNode;
                }
                // 走完之后5的右节点为2 为什么不是左？此例子中4 已经先占了 5 左

            } else {// 1 左边大 5 右大 2

                Node parent = left.value < right.value ? left : right;

                if (parent.left == null) {
                    parent.left = curNode;
                } else {
                    parent.right = curNode;
                }

                // 走完以后 2 的左键节点为 1
            }
        }
        return head;
    }

    /**
     * 取出栈顶元素，如果此时栈内为空，则表示对应node 左边（右边）没有比它大的元素
     *
     * @param stack
     * @param map
     */
    private static void popStackSetMap(Stack<Node> stack, HashMap<Node, Node> map) {
        Node popNode = stack.pop();
        if (stack.isEmpty()) {
            map.put(popNode, null);
        } else {
            map.put(popNode, stack.peek());
        }
    }
}
