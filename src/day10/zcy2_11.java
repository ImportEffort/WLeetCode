package day10;

import TestUItls.Node;
import TestUItls.TestUtils;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 删除无序链表中重复出现的节点
 */
public class zcy2_11 {

    public static void main(String[] args) {
        TestUtils.printList(getList());
        TestUtils.printList(removeRep1(getList()));
        TestUtils.printList(removeRep2(getList()));
    }

    private static Node getList(){
        Node head = TestUtils.getList(3);
        head.next = new Node(3);
        head.next.next = new Node(4);
        head.next.next.next = new Node(4);
        head.next.next.next.next = new Node(2);
        head.next.next.next.next.next = new Node(1);
        head.next.next.next.next.next.next = new Node(1);
        return head;
    }
    private static Node removeRep1(Node head) {
        if (head == null) {
            return null;
        }

        HashSet<Integer> set = new HashSet<>();
        Node pre = head;
        Node cur = head.next;
        set.add(head.value);

        while (cur != null) {
            if (set.contains(cur.value)) {
                pre.next = cur.next;
            } else {
                set.add(cur.value);
                pre = cur;
            }
            cur = cur.next;
        }

        return head;
    }


    private static Node removeRep2(Node head) {
        if (head == null) {
            return null;
        }
        Node cur = head;
        Node pre = null;
        Node next = null;

        while (cur != null) {
            pre = cur;
            next = cur.next;

            while (next != null) {
                if (cur.value == next.value) {
                    pre.next = next.next;
                } else {
                    pre = next;
                }
                next = next.next;
            }

            cur = cur.next;
        }
        return head;
    }
}
