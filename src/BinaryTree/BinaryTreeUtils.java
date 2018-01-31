package BinaryTree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinaryTreeUtils {

    public TreeNode root = null;

    public BinaryTreeUtils() {
        root = new TreeNode<String>("A");
    }

    /**
     * 二叉树的构建首先需要跟节点，一般在构造方法里面创建
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

    public TreeNode createBinaryTree2() {
        TreeNode root = new TreeNode<>(8);
        TreeNode nodeB = new TreeNode<>(7);
        TreeNode nodeF = new TreeNode<>(6);

        TreeNode nodeC = new TreeNode<>(4);
        TreeNode nodeD = new TreeNode<>(20);
        TreeNode nodeE = new TreeNode<>(1);

        TreeNode nodeG = new TreeNode<>(3);
        TreeNode nodeH = new TreeNode<>(11);
        TreeNode nodeI = new TreeNode<>(10);

        root.leftChild = nodeB;
        root.rightChild = nodeF;

        nodeB.leftChild = nodeC;
        nodeB.rightChild = nodeE;

        nodeC.leftChild = nodeD;

        nodeF.leftChild = nodeG;
        nodeF.rightChild = nodeI;

        nodeG.rightChild = nodeH;

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
     * <p>
     * 思想是利用队列，遇到节点将其放入队列，然后出队的时候将分别将其左子树和右子树按顺序入队 再依次出队，如果 A 的孩子为 B(left) C(right)
     * A 入队 A出队 B 入队 C 入队  B 出队 C 出队 循环结束的条件是 队列为空且要入队的节点为空
     *
     * @param root
     */
    public static void levelTraverse(TreeNode<String> root) {
        if (root == null) {
            System.out.print("二叉树为空二叉树");
            return;
        }
        // 用于记录当前处理的结点
        TreeNode curNode = root;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(curNode);
        while (curNode != null && !queue.isEmpty()) {
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

    /**
     * 递归先序遍历 根左右
     *
     * @param root
     */
    public static void beforeTraverse(TreeNode root) {
        if (root == null) {
            return;
        }
        System.out.print(root.data + " ");
        beforeTraverse(root.leftChild);
        beforeTraverse(root.rightChild);
    }

    /**
     * 使用栈遍历二叉树 循环前根节点先进站 ，栈中出站一个元素 并打印这个元素 每个节点出站时 将其右子树 和 左子树 一次放入栈中
     *
     * @param root
     * @param <T>
     */
    public static <T> void beforeTraverseByStack(TreeNode<T> root) {

        System.out.println("beforeTraverseByStack start ");

        if (root == null) {
            System.out.println("二叉树为空");
            return;
        }

        Stack<TreeNode<T>> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            root = stack.pop();
            System.out.print(root.data + " ");
            if (root.rightChild != null) {
                stack.push(root.rightChild);
            }
            if (root.leftChild != null) {
                stack.push(root.leftChild);
            }
        }
        System.out.println();
        System.out.println("beforeTraverseByStack end ");
    }


    /**
     * 中序遍历 左根右
     *
     * @param root
     */
    public static void middleTraverse(TreeNode root) {
        if (root == null) {
            return;
        }
        middleTraverse(root.leftChild);
        System.out.print(root.data + " ");
        middleTraverse(root.rightChild);
    }


    /**
     * 中序遍历 左根右
     * <p>
     * 创建一个栈
     * <p>
     * 1、 根节点先入栈，当前节点的左节点入栈，
     * 2、 直到发现一个节点的左节点为空 则将栈顶节点出栈并打印
     * 3、 栈顶出站的同时令当前节点为其右节点 即如果该节点存在右节点则右节点入栈 如果右节点不存在则 继续步骤2
     *
     * @param root
     */
    public static <T> void middleTraverseByStack(TreeNode<T> root) {
        System.out.println("middleTraverseByStack start ");

        if (root == null) {
            System.out.println("二叉树为空");
            return;
        }
        Stack<TreeNode<T>> stack = new Stack<>();
        while (!stack.isEmpty() || root != null) {
            if (root != null) {
                stack.push(root);
                root = root.leftChild;
            } else {
                root = stack.pop();
                System.out.print(root.data + " ");
                root = root.rightChild;
            }
        }
        System.out.println();
        System.out.println("middleTraverseByStack end ");
    }

    /**
     * 后续遍历 左右根
     *
     * @param root
     */
    public static void afterTraverse(TreeNode root) {
        if (root == null) {
            return;
        }
        afterTraverse(root.leftChild);
        afterTraverse(root.rightChild);
        System.out.print(root.data + " ");
    }


    /**
     * 后续遍历 左右根
     * 使用后续栈遍历比较难 这里给出两种解决方案
     * <p>
     * 第一种解决方案 使用两个栈
     * 1. 将根节点入栈 s1
     * 2. 从s1 中弹出一个节点记为 cur ，然后依次将 cur 的左孩子和右孩子压入栈 s1 中
     * 3. 整个过程中，每个从 s1 中弹出的节点 入s2栈中
     * 4. 直到s1为空 s2中从栈顶到栈顶元素顺序即为后续遍历过程
     *
     * @param root
     */
    public static <T> void afterTraverseByStack1(TreeNode<T> root) {
        System.out.println("afterTraverseByStack start ");

        if (root == null) {
            System.out.println("二叉树为空");
            return;
        }
        Stack<TreeNode<T>> s1 = new Stack<>();
        Stack<TreeNode<T>> s2 = new Stack<>();
        s1.push(root);
        TreeNode<T> cur = null;
        while (!s1.isEmpty()) {
            cur = s1.pop();
            s2.push(cur);
            if (cur.leftChild != null) {
                s1.push(cur.leftChild);
            }

            if (cur.rightChild != null) {
                s1.push(cur.rightChild);
            }
        }
        while (!s2.isEmpty()) {
            System.out.print(s2.pop().data + " ");
        }
        System.out.println();
        System.out.println("afterTraverseByStack end ");
    }

    /**
     * 方法2
     * 1. 申请一个栈将根节点压入栈中，同时设置两个变量 h c 。 h 代表最近一次从栈中弹出且打印的元素， c 代表栈顶元素 h 初始化为 根节点，c = null
     * 2. 每次令 c 等于当前栈顶节点，但是不从栈中弹出 peek 操作
     * 3. ① 如果 c 的左孩子不为空 h 不等 c 的左孩子 也不等于 c 的右孩子 则把 c 的左孩子压入栈中
     *      因为如果 h 等于 c 的左孩子或者右孩子则说明 c 的左子树或者右子树遍历完了（要求），此时不应该将 c 的左孩子放入栈中，否则左孩子还没处理过，那么此时应该将 c 的左孩子压入栈中。
     *    ② 第一个条件不满足，并且 c 的右孩子不为空，h 不等于 c 的右孩子，则将 c 的右孩子压入栈中。
     *    ③ 如果上述条件均不满足，说明 c 的左子树和右子树已经遍历完全了，此时应该讲 c 弹出栈并打印 然后令 h = c
     *
     * @param root
     * @param <T>
     */
    public static <T> void afterTraverseByStack2(TreeNode<T> root) {
        System.out.println("afterTraverseByStack start ");

        if (root == null) {
            System.out.println("二叉树为空");
            return;
        }
        Stack<TreeNode<T>> stack = new Stack<>();
        stack.push(root);

        TreeNode<T> h = root;
        TreeNode<T> c = null;

        while (!stack.isEmpty()) {
            c = stack.peek();
            if (c.leftChild != null && c.leftChild != h && c.rightChild != h) {
                stack.push(c.leftChild);
            }else if (c.rightChild!= null && h != c.rightChild){
                stack.push(c.rightChild);
            }else {
                System.out.print(stack.pop().data +" ");
                h = c;
            }
        }

        System.out.println();
        System.out.println("afterTraverseByStack end ");
    }
}
