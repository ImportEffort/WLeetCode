package day7;

import TestUItls.Node;

/**
 * 打印两个有序链表的公共部分
 *
 * 给定两个有序链表的头指针 head1 和 head2 ，打印两个链表的公共部分
 *
 * 因为链表是有序的：
 * 如果 head1 的值小于 head2 的值 那么 head1 应该拿下一个值与 head2 比较
 * 如果 head2 的值小于 head1 的值 那么 head2 应该拿下一个值与 head1 比较
 * 如果两者相同那么打印元素，并同时移动两个指针。
 * 如果有一个移动以后为 null 那么说明已经比较完了
 *
 */
public class zcy2_1 {


    /**
     *
     * @param head1 有序链表1
     * @param head2 有序链表2
     */
    private static void printCommonPart(Node head1, Node head2){
        System.out.printf("Common part:: ");
        while (head1 != null && head2 !=null){
            if (head1.value < head2.value){
                head1 = head1.next;
            }else if (head1.value > head2.value){
                head2 = head2.next;
            }else {
                System.out.printf(" " + head1.value);
                head1 = head1.next;
                head2 = head2.next;
            }
        }
    }
}
