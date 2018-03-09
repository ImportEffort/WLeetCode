package day8;

import TestUItls.Node;
import TestUItls.TestUtils;

/***
 * 翻转单链表的部分
 */
public class zcy2_4 {

    public static void main(String[] args) {
        System.out.println(TestUtils.getList(6));
        System.out.println("翻转结果：； " + reversePart(TestUtils.getList(6), 2, 4));

        System.out.println("翻转结果：； " + reversePart(TestUtils.getList(6), 1, 3));

    }

    // 1 2(node1) 3(next) 4 5 6  1 3 2 4 5 6
    private static Node reversePart(Node head, int from, int to) {

        Node node1 = head;

        int len = 0;//用来计算链表的长度

        Node preNode = null;
        Node toNode = null;

        while (node1 != null) {

            len++;

            if (len == from - 1) {
                preNode = node1;
            }

            if (len == to + 1) {
                toNode = node1;
            }

            node1 = node1.next;
        }

        if (from < 1 || to > len || from > to) {
            return head;
        }

        node1 = preNode == null ? head : preNode.next;

        Node node2 = node1.next;
        node1.next = toNode;
        Node next = null;

        while (node2 != toNode) {
            next = node2.next;
            node2.next = node1;
            node1 = node2;
            node2 = next;
        }

        if (preNode != null) {
            preNode.next = node1;
            return head;
        }

        //如果 preNode 等于 null 那么翻转后的第一个元素就是 head
        return node1;
    }
}
