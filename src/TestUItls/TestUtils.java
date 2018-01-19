package TestUItls;

public class TestUtils {

    public static Node getRoundList() {
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(4);
        head.next.next.next.next = new Node(5);
        head.next.next.next.next.next = head;
        return head;
    }

    public static Node getList(int count) {
        int value = 1;

        Node head = new Node(1);
        value++;

        Node node = head;
        Node next = null;
        do {
            next = new Node(value);
            node.next = next;
            node = next;
        } while (value++ != count);

        return head;
    }

    /**
     * 打印环形链表
     */

}
