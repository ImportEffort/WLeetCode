package day10;

import TestUItls.Node;
import TestUItls.TestUtils;

import java.util.Stack;

/***
 * 单链表每 K 个节点之间逆序
 */
public class zcy2_10 {

    public static void main(String[] args) {
        Node head = TestUtils.getList(8);
        TestUtils.printList(head);

        TestUtils.printList(reverseKNode1(head, 3));
        TestUtils.printList(reverseKNode2(TestUtils.getList(8), 3));
    }

    private static Node reverseKNode1(Node head, int K) {
        if (K < 2) {
            return head;
        }
        Stack<Node> stack = new Stack<>();
        Node newHead = head;
        Node cur = head;
        Node pre = null;
        Node next = null;

        while (cur != null) {
            next = cur.next;
            stack.push(cur);
            if (stack.size() == K) {
                pre = resign1(stack, pre, next);
                newHead = newHead == head ? cur : newHead;
            }
            cur = next;
        }
        return newHead;
    }

    private static Node resign1(Stack<Node> stack, Node left, Node right) {
        Node cur = stack.pop();
        if (left != null) {
            left.next = cur;
        }

        Node next;
        while (!stack.isEmpty()) {
            next = stack.pop();//2
            cur.next = next;//3->2
            cur = next;//2
        }
        //1-> 4
        cur.next = right;

        return cur;
    }

    private static Node reverseKNode2(Node head, int K) {
        if (K < 2) {
            return head;
        }
        Node cur = head;
        Node start = null;
        Node pre = null;
        Node next = null;
        int count = 1;

        while (cur != null) {
            next = cur.next;
            if (count == K) {
                //start 永远等于翻转的第一个节点
                start = pre == null ? head : pre.next;
                //只在第一次的时候赋值
                head = pre == null ? cur : head;

                resign2(pre, start, cur, next);

                // pre 翻转后的最后一个节点
                pre = start;

                count = 0;
            }
            count++;
            cur = next;
        }

        return head;
    }

    /**
     * @param left  反转的部分链表的前一个节点
     * @param start 开始反转的第一个节点
     * @param end   结束反转的节点
     * @param right 结束翻转节点的下一个节点
     */
    private static void resign2(Node left, Node start, Node end, Node right) {
        Node pre = start;
        Node cur = start.next;
        Node next = null;

        while (cur != right) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }

        if (left != null) {
            left.next = end;
        }

        start.next = right;
    }
}
