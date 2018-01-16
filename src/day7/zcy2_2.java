package day7;

/**
 * 删除单链表和双链表中的倒数第 K 个元素
 * <p>
 * 注意链表的元素删除要注意 next 的指向的位置 双链表则还要注意 last 即上一个元素的的位置
 */
public class zcy2_2 {
    public static void main(String[] args) {
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(4);

        Node node = deleteIndexOfKNum(head, 2);
        System.out.println("Node: " + node);


        DoubleNode doubleNode = new DoubleNode(1);
        DoubleNode doubleNode1 = new DoubleNode(2);
        DoubleNode doubleNode2 = new DoubleNode(3);
        DoubleNode doubleNode3 = new DoubleNode(4);

        doubleNode.last = null;
        doubleNode.next = doubleNode1;

        doubleNode1.next = doubleNode2;
        doubleNode1.last = doubleNode;

        doubleNode2.next = doubleNode3;
        doubleNode2.last = doubleNode1;

        doubleNode3.next = null;
        doubleNode3.last = doubleNode2;

        DoubleNode doubleNodeResult = deleteIndexOfKNumInDoubleList(doubleNode, 2);

        System.out.println("DoubleNode: " + doubleNodeResult);
    }

    private static Node deleteIndexOfKNum(Node head, int k) {

        if (head == null || k < 1) {
            return head;
        }

        //思路 找到要删除的元素的上一个元素，并修改其执行当前所指向元素的下一个元素
        Node cur = head;

        while (cur != null) {
            cur = cur.next;
            k--;
        }

        // 表示要删除的值不存在
        if (k == 0) {
            //如果 K= 0 表示要删除第1个元素，则只需要将第第二个元素返回即可
            head = head.next;
        }

        // 如果 K < 0 则表示要删除中间的一个元素 此时要做的是将这个元素的上一个元素找到，并将其指向下一个元素
        // 如 k = 2 链表长度为 4 倒数第 k 个元素 即为链表中的第 3 个元素，其前一个元素为 4-2 = 2 个元素

        if (k < 0) {
            //循环完成后记得将 cur 再次赋值为 head 否则 cur = null；
            cur = head;
            // k 自加1后跟 0 比较  因为 head 为 链表中第 0 个元素
//            while (k + 1 != 0) {
//                cur = cur.next;
//                k ++;
//            }
            while (++k != 0) {
                cur = cur.next;
            }
            cur.next = cur.next.next;
        }

        return head;
    }


    private static DoubleNode deleteIndexOfKNumInDoubleList(DoubleNode head, int k) {

        if (head == null || k < 1) {
            return head;
        }

        //思路 找到要删除的元素的上一个元素，并修改其执行当前所指向元素的下一个元素
        DoubleNode cur = head;

        while (cur != null) {
            cur = cur.next;
            k--;
        }

        // 表示要删除的值不存在
        if (k == 0) {
            //如果 K= 0 表示要删除第1个元素，则只需要将第第二个元素返回即可
            head = head.next;
            head.last = null;
        }

        if (k < 0) {
            //循环完成后记得将 cur 再次赋值为 head 否则 cur = null；
            cur = head;
            // k 自加1后跟 0 比较  因为 head 为 链表中第 0 个元素
//            while (k + 1 != 0) {
//                cur = cur.next;
//                k ++;
//            }
            while (++k != 0) {
                cur = cur.next;
            }
            DoubleNode newNext = cur.next.next;
            //双向链表还要进行前一个元素的赋值
            cur.next = newNext;
            if (newNext != null) {
                newNext.last = cur;
            }

        }

        return head;

    }
}
