package day9;

import TestUItls.Node;
import TestUItls.TestUtils;

/***
 * 将单向链表按照某值划分成左小，中间相等 右边大的新链表
 * 例如 9 0 4 5 1 给定数字  5 则新链表的顺序为 1 0 4 5 9
 *
 * 进阶：
 * 原题要求下并满足：从左到右与原来链表中的顺序一致 时间复杂度为 O(N) 空间复杂度为 O(1)
 */
public class zcy2_7 {

    public static void main(String[] args) {
        Node head = generaList();
        System.out.print("旧链表：");
        TestUtils.printList(head);


        Node cur = head;
        while (cur.next != null) {
            cur = cur.next;
        }
        quickSort(head, cur);

        System.out.print("新链表：");

        TestUtils.printList(head);
//        TestUtils.printList(partition(head, 5));

//        System.out.print("新链表：");
//        TestUtils.printList(listPartition2(generaList(), 5));

    }

    private static Node listPartition1(Node head, int partition) {
        if (head == null) {
            return head;
        }

        Node cur = head;

        //获取 链表长度
        int len = 0;
        while (cur != null) {
            len++;
            cur = cur.next;
        }

        //获取链表对应的数组表示
        Node[] nodes = new Node[len];
        cur = head;
        len = 0;
        while (cur != null) {
            nodes[len] = cur;
            len++;
            cur = cur.next;
        }
        partition(nodes, partition);
        int i = 0;
        for (i = 1; i != len; i++) {
            nodes[i - 1].next = nodes[i];
        }

        nodes[i - 1].next = null;

        return nodes[0];
    }


    /**
     * arr[l + 1 ... j] < v
     * arr[j+1,i) >= v
     *
     * @param left
     * @param right
     */
    private static void quickSort(Node left, Node right) {
        if (left == null || right == null || left == right) {
            return;
        }
        //快拍的标志值
        int v = left.value;
        //j = l
        Node l = left;
        Node start = left.next;
        // for (int i = l + 1; i <= r; i++) {
        while (start != right.next && start != null) {
            if (start.value < v) {
                l = l.next;
                //swap(arr,j+1,i)
                int temp = l.value;
                l.value = start.value;
                start.value = temp;
            }
            start = start.next;
        }

        //交换 l 与 与 left 的值
        int temp = l.value;
        l.value = left.value;
        left.value = temp;

        quickSort(left, l);
        quickSort(l.next, right);
    }


    private static Node partition(Node head, int x) {
        Node left = new Node(0);
        Node right = new Node(0);
        Node mid = new Node(0);

        Node leftDummy = left;
        Node rightDummy = right;
        Node midDummy = mid;

        while (head != null) {
            if (head.value < x) {
                left.next = head;
                left = left.next;
            } else if (head.value == x) {
                mid.next = head;
                mid = mid.next;
            } else {
                right.next = head;
                right = right.next;
            }
            head = head.next;
        }

        left.next = midDummy.next;
        mid.next = rightDummy.next;
        right.next = null;

        return leftDummy.next;
    }


    /**
     * 数组 一次快速排序的实现
     *
     * @param nodes
     * @param partition
     */
    private static void partition(Node[] nodes, int partition) {
        int samll = -1;
        int big = nodes.length;
        int index = 0;

        while (index != big) {
            if (nodes[index].value < partition) {
                swap(nodes, ++samll, index++);
            } else if (nodes[index].value == partition) {
                index++;
            } else {
                swap(nodes, --big, index);
            }
        }
    }

    private static void swap(Node[] nodes, int a, int b) {
        Node temp = nodes[a];
        nodes[a] = nodes[b];
        nodes[b] = temp;
    }

    private static Node generaList() {
        Node head = new Node(7);
        head.next = new Node(9);
        head.next.next = new Node(1);
        head.next.next.next = new Node(8);
        head.next.next.next.next = new Node(5);
        head.next.next.next.next.next = new Node(2);
        head.next.next.next.next.next.next = new Node(5);
        return head;
    }

    /**
     * 进阶版 要求两边的内容排好序 并且 空间复杂度为 O(1)
     *
     * @param head
     * @param partition
     * @return
     */
    private static Node listPartition2(Node head, int partition) {
        Node sH = null;//小的头
        Node sT = null;//小的尾
        Node eH = null;//相等的头
        Node eT = null;//相等的尾
        Node bH = null;//大的头
        Node bT = null;//大的未

        //所有的节点分为三个链表中
        Node next = null;
        while (head != null) {
            next = head.next;
            //这一步很重要 如果不把 head.next 置为 null 则所有的赋值操作都将是个死循环
            head.next = null;
            if (head.value < partition) {
                if (sH == null) {
                    sH = head;
                    sT = head;
                } else {
                    sT.next = head;//把小的末尾下一个节点指向 head
                    sT = head;//把小的末尾指向 head
                }
            } else if (head.value == partition) {
                if (eH == null) {
                    eH = head;
                    eT = head;
                } else {
                    eT.next = head;//把小的末尾下一个节点指向 head
                    eT = head;//把小的末尾指向 head
                }
            } else {
                if (bH == null) {
                    bH = head;
                    bT = head;
                } else {
                    bT.next = head;//把小的末尾下一个节点指向 head
                    bT = head;//把小的末尾指向 head
                }
            }

            head = next;
        }

        if (sT != null) {
            sT.next = eH;
            eT = eT == null ? sT : eT;//eT == null 说明该链表中没有与指定数字相同的节点，此时只要将 sT 当做 eT
        }

        if (eT != null) {
            eT.next = bH;
        }

        return sH != null ? sH : eH != null ? eH : bH;
    }
}
