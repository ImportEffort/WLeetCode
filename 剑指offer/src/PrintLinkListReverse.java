public class PrintLinkListReverse {

    public static void main(String[] args) {
        Node head = buildLinkList();
        printLinkList(head);
    }

    private static void printLinkList(Node head) {
        Node node = reverseNode(head);
        while (node != null){
            System.out.print(" " + node.value);
            node = node.next;
        }
    }

    private static Node reverseNode(Node head) {
        Node next = null;
        Node pre = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }

        return pre;
    }

    private static Node buildLinkList() {

        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(4);
        head.next.next.next.next = new Node(5);

        return head;

    }

    private static class Node {
        int value;
        Node next;

        public Node(int value) {
            this.value = value;
        }
    }
}
