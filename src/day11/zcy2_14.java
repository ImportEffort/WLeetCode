package day11;

import TestUItls.Node;

/***
 * 单链表的选择排序 要求空间复杂度为 O(1)
 */
public class zcy2_14 {


    private static Node selectionSort(Node head) {
        Node tail = null;//排序部分的末尾节点
        Node cur = head;//未排序的链表头部
        Node smallPre = null;//最小节点的前一个节点
        Node small = null;//最小节点

        //思路 1.每次循环找到最小元素 和最小元素的上一个节点 2. 删除最小元素 并另 tail 等于此次循环的最小元素
        //3 cur 一直保持 未排序的头部

        while (cur != null) {
            small = cur;
            smallPre = getSmallestPreNode(cur);
            //samllPre = null 代表链表已经排序完成
            if (smallPre != null) {
                small = smallPre.next;
                smallPre.next = small.next;
            }
            // 只有在第一循环时三元表达式成立
            cur = cur == small ? cur.next : cur;
            //链表head = 链表最小值
            if (tail == null) {
                head = small;
            } else {
                tail.next = small;
            }
            tail = small;
        }

        return head;
    }

    /**
     * 获取当前剩余链表最小值得前一个元素
     * @param head
     * @return
     */
    private static Node getSmallestPreNode(Node head) {
        Node smallPre = null;
        Node small = head;
        Node pre = head;
        Node cur = head.next;
        while (cur != null) {
            if (cur.value < small.value) {
                smallPre = pre;
                small = cur;
            }
            pre = cur;
            cur = cur.next;
        }
        return smallPre;
    }



}
