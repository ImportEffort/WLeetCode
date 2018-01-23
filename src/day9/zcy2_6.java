package day9;

import TestUItls.Node;
import TestUItls.TestUtils;

import java.util.Stack;

/***
 * 判断一个单链表是否为回文结构
 * 给定一个链表的头节点，判断这个链表是否为回文结构
 * 例如：
 * 1->2->3->2->1 是回文结构
 * 1->2->3->3->1 不是回文结构
 *
 * 进阶：
 * 如果链表长度是 N 则要求算法复杂度为 O(N) 额外的空间复杂度为 O(1)
 */
public class zcy2_6 {

    public static void main(String[] args) {
        Node head = TestUtils.getPalindromeList();
        Node head1 = TestUtils.getList(5);

        System.out.println("链表是回文结构 isPalindrome1 ： " + isPalindrome1(head));
        System.out.println("链表是回文结构 isPalindrome1 ： " + isPalindrome1(head1));

        System.out.println("链表是回文结构 isPalindrome2 ： " + isPalindrome2(head));
        System.out.println("链表是回文结构 isPalindrome2 ： " + isPalindrome2(head1));

        System.out.println("链表是回文结构 isPalindrome3 ： " + isPalindrome3(head));
        System.out.println("链表是回文结构 isPalindrome3 ： " + isPalindrome3(head1));

    }

    /**
     * 普通解法 使用栈结构逆序 然后判断整个链表是否值相同
     * 时间复杂度O(n) 空间复杂度O(n)
     *
     * @param head 根节点
     * @return 是否为回文结构
     */
    private static boolean isPalindrome1(Node head) {
        if (head == null || head.next == null) {
            return true;
        }

        Node cur = head;
        Stack<Node> stack = new Stack<>();
        while (cur != null) {
            stack.push(cur);
            cur = cur.next;
        }

        while (head != null) {
            if (head.value != stack.pop().value) {
                return false;
            }
            head = head.next;
        }
        return true;
    }

    /**
     * 利用链表对折思想 只将链表的右半部分放入栈中 这里用一个很巧妙的方法判断哪个节点是链表中间点
     * 如 1 2 2 1 则将指针1 指向 1，指针 2 指向2，指针1 每次移动两个位置，指针2 每次移动1个位置
     * 当指针1 不可移动（下一个为空 或者 下下个数字为空的时候） 指针2 指向的位置及时链表中点
     * 当链表为奇数的时候会指向中间一个节点，如果为偶数则会指向右侧第一个节点
     * <p>
     * 时间复杂度O(n) 空间复杂度O(n/2)
     *
     * @param head
     * @return
     */
    private static boolean isPalindrome2(Node head) {
        if (head == null || head.next == null) {
            return true;
        }
        Node right = head.next;
        Node cur = head;

        while (cur.next != null && cur.next.next != null) {
            right = right.next;
            cur = cur.next.next;
        }

        Stack<Node> stack = new Stack<>();
        while (right != null) {
            stack.push(right);
            right = right.next;
        }
        while (!stack.isEmpty()) {
            if (head.value != stack.pop().value) {
                return false;
            }
            head = head.next;
        }
        return true;
    }

    /**
     * 进阶版
     * 翻转 中间节点之后的节点 然后比较 两段列表是否相同 如果相同则为回文链表
     * 如： 1 -> 2 -> 3 -> 2 -> 1
     * 翻转后半部分： 1 -> 2 -> 3 -> null <- 3 <- 2 <- 1
     *
     * 难点在于两次链表部分翻转
     * @param head
     * @return
     */
    private static boolean isPalindrome3(Node head) {

        if (head == null || head.next == null) {
            return true;
        }

        // 先找到中间节点
        Node n1 = head.next;
        Node n2 = head;
        while (n2.next != null && n2.next.next != null) {
            n1 = n1.next;//中间点 3
            n2 = n2.next.next;//最右点
        }

        //翻转 n1 之后的节点
        n2 = n1.next;
        n1.next = null;
        Node temp = null;

        while (n2 != null) {
            temp = n2.next;
            n2.next = n1;
            n1 = n2;
            n2 = temp;
        }

        temp = n1;// temp 为原链表最后一个节点
        n2 = head; // 重置为左边第一个节点
        boolean result = true;
        while (n2 != null && n1 != null) {
            if (n2.value != n1.value) {
                result = false;
                break;
            }
            n1 = n1.next;
            n2 = n2.next;
        }

        //判断完成后翻转回来
        n1 = temp.next;
        temp.next = null;
        while (n1 != null) {
            n2 = n1.next;
            n1.next = temp;
            temp = n1;
            n1 = n2;
        }

        return result;
    }
}
