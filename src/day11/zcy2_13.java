package day11;

import TestUItls.Node;

import java.util.Stack;

/**
 * 从单链表中删除指定值得节点
 */
public class zcy2_13 {

    private static Node removeValue1(Node head, int num) {
        Stack<Node> stack = new Stack<>();
        //将左右不等于 num 的节点放入栈中
        while (head != null) {
            if (head.value != num) {
                stack.push(head);
            }
            head = head.next;
        }

        while (!stack.isEmpty()) {
            stack.peek().next = head;
            head = stack.pop();
        }

        return head;
    }


    private static Node removeValue2(Node head, int num) {

        // 找到第一个不等于 num 的节点
        while (head != null) {
            if (head.value != num) {
                break;
            }
            head = head.next;
        }
        Node pre = head;

        //注意起始指针在同一个地方 如果cur 指针移动，如果不等 num 两个第一次循环后就相差了一个节点，实际上此时的头节点不可能相同
        Node cur = head;
        while (cur != null) {
            if (cur.value == num) {
                pre.next = cur.next;
            } else {
                pre = cur;
            }
            cur = cur.next;
        }

        return head;
    }
}
