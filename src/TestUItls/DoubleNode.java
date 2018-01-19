package TestUItls;

public class DoubleNode {
    public int value;
    public DoubleNode next;
    public DoubleNode last;

    public DoubleNode(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DoubleNode{" +
                "value=" + value +
                ", next=" + (next == null ? "null" : next.toString()) +
                '}';
    }
}
