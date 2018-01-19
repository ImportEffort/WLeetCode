package day7;

import TestUItls.DoubleNode;
import TestUItls.Node;

/**
 * 翻转单向链表和双向链表
 */
public class zcy2_3 {


    private static Node revaseList(Node head) {
        Node pre = null;
        Node next = null;

        while (head != null) {
            // 暂存当前元素的下一个元素 作为下一个要反转的元素
            next = head.next;
            //翻转指向
            head.next = pre;
            //要翻转的下个元素的前一个元素为当前元素
            pre = head;
            //下一个要比较的元素为当前元素的下个元素
            head = next;
        }
        //注意这里返回的是赋值当前比较元素
        return pre;
    }


    private static DoubleNode revaseDoubleList(DoubleNode head) {
        DoubleNode pre = null;
        DoubleNode next = null;

        while (head != null) {
            // 暂存当前元素的下一个元素 作为下一个要反转的元素
            next = head.next;

            //翻转当前元素的指向
            head.next = pre;
            head.last = next;

            //要翻转的下个元素的前一个元素为当前元素
            pre = head;
            //下一个要比较的元素为当前元素的下个元素
            head = next;
        }

        return pre;
    }

}
