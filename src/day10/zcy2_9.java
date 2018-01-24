package day10;

import TestUItls.Node;
import TestUItls.TestUtils;

import java.util.Stack;

/**
 * 两个单链表生成相加链表
 */
public class zcy2_9 {
    public static void main(String[] args) {
        Node head = TestUtils.getList(8);
        TestUtils.printList(head);
        Node head1 = TestUtils.getList(2);
        TestUtils.printList(head1);

        TestUtils.printList(addList(head1,head));
        TestUtils.printList(addList2(head1,head));
    }

    /**
     * 使用栈结构
     *
     * @return
     */
    private static Node addList(Node head1, Node head2) {
        Stack<Integer> s1 = new Stack<>();
        Stack<Integer> s2 = new Stack<>();

        while (head1 != null) {
            s1.push(head1.value);
            head1 = head1.next;
        }

        while (head2 != null) {
            s2.push(head2.value);
            head2 = head2.next;
        }

        int ca = 0;
        int n1 = 0;
        int n2 = 0;
        int n = 0;

        Node node = null;
        Node pre = null;

        //两个都有都为为空则终止循环
        while (!s1.isEmpty() || !s2.isEmpty()) {
            //获取链表倒数第一个数的数值
            n1 = s1.isEmpty() ? 0 : s1.pop();
            n2 = s2.isEmpty() ? 0 : s2.pop();
            //求两者的和 ca 表示进位 因为这里算当前位置的节点值 ca 哪上一位进来
            n = n1 + n2 + ca;
            pre = node;
            node = new Node(n % 10);
            node.next = pre;
            ca = n / 10;
        }

        if (ca == 1) {
            pre = node;
            node = new Node(1);
            node.next = pre;
        }

        return node;
    }

    private static Node addList2(Node head1, Node head2) {
        head1 = reverseList(head1);
        head2 = reverseList(head2);

        int ca = 0;
        int n1 = 0;
        int n2 = 0;
        int n = 0;
        Node c1 = head1;
        Node c2 = head2;
        Node pre = null;
        Node node = null;

        while (c1 != null || c2 != null) {

            n1 = c1 != null ? c1.value : 0;
            n2 = c2 != null ? c2.value : 0;

            n = n1 + n2 + ca;
            //
            pre = node;
            node = new Node(n % 10);
            node.next = pre;
            ca = n / 10;


            c1 = c1 != null ? c1.next : null;
            c2 = c2 != null ? c2.next : null;
        }


        if (ca == 1) {
            pre = node;
            node = new Node(1);
            node.next = pre;
        }

        head1 = reverseList(head1);
        head2 = reverseList(head2);

        return node;
    }

    private static Node reverseList(Node head) {

        Node next = null;
        Node pre = null;

        while (head != null) {
            next = head.next;//记住下一个
            head.next = pre;//将当前的下一个赋值为上一个
            pre = head;//移动指针
            head = next;
        }
        TestUtils.printList(pre);
        return pre;
    }

}
