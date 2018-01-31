package BinaryTree;

import java.util.LinkedList;
import java.util.Queue;

public class BinaryTreeUtils {

    public TreeNode root = null;
    public BinaryTreeUtils() {
        root = new TreeNode<String>( "A");
    }
    /**
     *  二叉树的构建首先需要跟节点，一般在构造方法里面创建
     */

    public TreeNode createBinaryTree() {
        TreeNode nodeB = new TreeNode<>("B");
        TreeNode nodeC = new TreeNode<>("C");
        TreeNode nodeD = new TreeNode<>("D");
        TreeNode nodeE = new TreeNode<>("E");
        TreeNode nodeF = new TreeNode<>("F");
        TreeNode nodeG = new TreeNode<>("G");
        root.leftChild = nodeB;
        root.rightChild = nodeC;
        nodeB.leftChild = nodeD;
        nodeB.rightChild = nodeE;
        nodeC.leftChild = nodeF;
        nodeC.rightChild = nodeG;

        return root;
    }

    public static void printFromToBottom(TreeNode root) {
        // 当结点非空时才进行操作
        if (root != null) {
            // 用于存放还未遍历的元素
            Queue<TreeNode> list = new LinkedList<>();
            // 将根结点入队
            list.add(root);
            // 用于记录当前处理的结点
            TreeNode curNode;
            // 队列非空则进行处理
            while (!list.isEmpty()) {
                // 删除队首元素
                curNode = list.remove();
                // 输出队首元素的值
                System.out.print(curNode.data + " ");
                // 如果左子结点不为空，则左子结点入队
                if (curNode.leftChild != null) {
                    list.add(curNode.leftChild);
                }
                // 如果右子结点不为空，则左子结点入队
                if (curNode.rightChild != null) {
                    list.add(curNode.rightChild);
                }
            }
            System.out.println();
        }
    }

    /**
     * 层级遍历二叉树
     *
     * 思想是利用队列，遇到节点将其放入队列，然后出队的时候将分别将其左子树和右子树按顺序入队 再依次出队，如果 A 的孩子为 B(left) C(right)
     * A 入队 A出队 B 入队 C 入队  B 出队 C 出队 循环结束的条件是 队列为空且要入队的节点为空
     * @param root
     */
    public static void levelTraverse(TreeNode<String> root){
        if (root == null){
            System.out.print("二叉树为空二叉树");
            return;
        }
        // 用于记录当前处理的结点
        TreeNode curNode = root;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(curNode);
        while (curNode != null && !queue.isEmpty()){
            System.out.print(queue.poll().data + " ");
            if (curNode.leftChild != null) {
                queue.add(curNode.leftChild);
            }
            if (curNode.rightChild != null) {
                queue.add(curNode.rightChild);
            }
            curNode = queue.peek();
        }
    }

    private static void  beforTraverse(TreeNode<String> root){

    }
}
