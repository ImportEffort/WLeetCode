package BinaryTree;


import java.util.LinkedList;
import java.util.Queue;

public class BinaryOrder {
    public static void main(String[] args) {
        BinaryTreeUtils tree = new BinaryTreeUtils();
        TreeNode binaryTree = tree.createBinaryTree();
        BinaryTreeUtils.levelTraverse(binaryTree);
    }
}
