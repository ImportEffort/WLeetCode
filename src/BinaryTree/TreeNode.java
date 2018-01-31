package BinaryTree;

/**
 * 孩子兄弟法表示一个二叉树
 */
public class TreeNode<T> {
    public T data;
    public TreeNode<T> leftChild;
    public TreeNode<T> rightChild;
    public TreeNode(T data) {
        this.data = data;
        this.leftChild = null;
        this.rightChild = null;
    }
}