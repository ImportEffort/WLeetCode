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

    public static Node getPalindromeList() {
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(2);
        head.next.next.next.next = new Node(1);
        return head;
    }

    public static void printList(Node head) {
        Node cur = head;
        System.out.print("[");
        while (cur != null) {
            System.out.print(" " + cur.value + " ");
            cur = cur.next;
        }
        System.out.println("]");
    }


}
