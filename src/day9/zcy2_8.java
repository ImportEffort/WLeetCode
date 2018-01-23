package day9;

import java.util.HashMap;

/**
 * 赋值含有随机指针节点的链表
 */
public class zcy2_8 {


    private static class Node {
        public int value;
        public Node next;
        public Node rand;

        public Node(int value) {
            this.value = value;
        }
    }


    /**
     * 使用 HashMap 时间复杂度O(1)
     * @param head
     * @return
     */
    public static Node copyListWithRand1(Node head) {
        HashMap<Node, Node> map = new HashMap<>();
        Node cur = head;
        while (cur != null) {
            map.put(cur,new Node(cur.value));
            cur = cur.next;
        }

        cur = head;
        while (cur != null){
            map.get(cur).next = cur.next;
            map.get(cur).rand = cur.rand;
        }

        return map.get(head);
    }

    public static Node copyListWithRand2(Node head){
        if (head == null){
            return head;
        }
        Node cur = head;
        Node next = null;
        while (cur != null){
            next = cur.next;
            cur.next = new Node(cur.value);
            cur.next.next = next;
            cur = next;
        }
        cur = head;
        Node curCopy = null;

        while(cur!= null){
            next  = cur.next.next;
            curCopy = cur.next;
            curCopy.rand = cur.rand == null ? cur.rand.next : null;
            cur = next;
        }

        Node res = head.next;
        cur = head;
        while (cur!=null){
            next = cur.next.next;
            curCopy = cur.next;
            cur.next = next;
            curCopy.next = next != null ? next.next : null;
            cur = next;
        }
        return res;
    }


}
