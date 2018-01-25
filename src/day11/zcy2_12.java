package day11;

import TestUItls.Node;
import TestUItls.TestUtils;

/**
 * 向有序环形链单链表中插入新节点
 */
public class zcy2_12 {


    private Node getSortedList() {
        Node head = new Node(1);
        head.next = new Node(3);
        head.next.next = new Node(4);
        head.next.next.next = new Node(5);
        head.next.next.next.next = new Node(7);
        head.next.next.next.next.next = head;
        return head;
    }

    private static Node instertNode(Node head, int num) {
        Node node = new Node(num);
        if (head == null) {
            node.next = node;
            return node;
        }
        Node cur = head.next;
        Node pre = head;
        while (cur != head) {
            if (pre.value <= num && cur.value >= num) {
                break;
            }
            pre = cur;
            cur = cur.next;
        }
        pre.next = node;
        node.next = cur;

        //如果 head 的值 小于要插入的值 则node 是插入了链表中 所以返回 head 如果 head 值大于 node 则插入了链表最前端或者最后端（环形链表其实是一个位置）
        return head.value < num ? head : node;
    }
}
