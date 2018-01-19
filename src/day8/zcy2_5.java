package day8;

import TestUItls.Node;
import TestUItls.TestUtils;

/**
 * 环形单链表的约瑟夫问题
 */
public class zcy2_5 {


    public static void main(String[] args) {
        Node roundList = TestUtils.getRoundList();
        Node remain = jusephuskill1(roundList, 3);
        System.out.println("活下来的节点：：" + remain.value);
    }

    /**
     * @param head 环形单链表
     * @param m    报 m 的人将被 kill
     * @return 最后幸存下来的节点
     */
    private static Node jusephuskill1(Node head, int m) {
        //如果环形单链表 中就一个元素 head.next == head  它就是幸存者
        if (head == null || head.next == head || m < 1) {
            return head;
        }

        Node last = head;
        while (last.next != head) {
            last = last.next;
        }
        int count = 0;
        // head 环形开始的节点  last 环形结束的节点
        while (head != last) {
            if (count == m) {
                last.next = head.next;
                //删除一个节点后 从0 开始报数
                count = 0;
            } else {
                last = head;
            }
            count = count+1;
            head = last.next;
            System.out.println("last: " + last + "   head:: " + head);
        }

        return head;
    }

}
