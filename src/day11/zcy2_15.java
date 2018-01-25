package day11;

import TestUItls.Node;

/**
 * 一种怪异的删除节点的方式
 * 链表节点值均为 int 值，给定链表中的一个节点 node ，但不知道这个链表的头节点 如何在链表中删除 node
 * 要求时间复杂度为 O(1)
 */
public class zcy2_15 {

    /**
     * 实现思路将当前元素的换成下一个元素
     *
     * @param node
     * @return
     */
    private static void deleteNode(Node node) {
        if (node == null) {
            return;
        }

        Node next = node.next;
        if (next == null){
            throw new RuntimeException("无法删除最后一个节点");
        }

        node.value = next.value;
        node.next = next.next;
    }
}
