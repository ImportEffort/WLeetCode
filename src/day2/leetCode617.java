package day2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/***
 * 617. Merge Two Binary Trees
 * Given two binary trees and imagine that when you put one of them to cover the other,
 * some nodes of the two trees are overlapped while the others are not.
 * You need to merge them into a new binary tree. The merge rule is that if two nodes overlap,
 * then sum node values up as the new value of the merged node.
 * Otherwise, the NOT null node will be used as the node of new tree.

 * Example 1:
 * Input:
 *   Tree 1                     Tree 2
 *       1                         2
 *      / \                       / \
 *     3   2                     1   3
 *    /                           \   \
 *   5                            4   7
 *
 *
 * Output:
 * Merged tree:
 *       3
 *      / \
 *     4   5
 *    / \   \
 *   5   4   7
 */
public class leetCode617 {
    public static void main(String[] args) {
        TreeNode treeNode1 = stringToTreeNode("[1,3,2,5]");
        System.out.println(treeNodeToString(treeNode1));
        TreeNode treeNode2 = stringToTreeNode("[2,1,3,null,4,null,7]");
        System.out.println(treeNodeToString(treeNode2));

        System.out.println("合并结果: " + treeNodeToString(mergeTrees(treeNode1, treeNode2)));
        System.out.println("合并结果: " + treeNodeToString(LeetCode_mergeTreesA(treeNode1, treeNode2)));
        System.out.println("合并结果: " + treeNodeToString(LeetCode_mergeTreesB(treeNode1, treeNode2)));
    }

    /**
     *
     * @param t1
     * @param t2
     * @return
     */
    private static TreeNode mergeTrees(TreeNode t1, TreeNode t2) {

        if (t1 == null && t2 == null) {
            return null;
        }
        if (t1 == null | t2 == null) {
            return t1 == null ? t2 : t1;
        }

        TreeNode node = new TreeNode(0);
        node.val = t1.val + t2.val;
        node.left = mergeTrees(t1.left, t2.left);
        node.right = mergeTrees(t1.right, t2.right);

        return node;
    }

    /**
     * 官方解法
     */
    private static TreeNode LeetCode_mergeTreesA(TreeNode t1, TreeNode t2) {
        if (t1 == null)
            return t2;
        if (t2 == null)
            return t1;
        t1.val += t2.val;
        t1.left = mergeTrees(t1.left, t2.left);
        t1.right = mergeTrees(t1.right, t2.right);
        return t1;
    }

    private static TreeNode LeetCode_mergeTreesB(TreeNode t1, TreeNode t2) {
        if (t1 == null)
            return t2;
        Stack< TreeNode[] > stack = new Stack < > ();
        stack.push(new TreeNode[] {t1, t2});
        while (!stack.isEmpty()) {
            TreeNode[] t = stack.pop();
            if (t[0] == null || t[1] == null) {
                continue;
            }
            t[0].val += t[1].val;
            if (t[0].left == null) {
                t[0].left = t[1].left;
            } else {
                stack.push(new TreeNode[] {t[0].left, t[1].left});
            }
            if (t[0].right == null) {
                t[0].right = t[1].right;
            } else {
                stack.push(new TreeNode[] {t[0].right, t[1].right});
            }
        }
        return t1;
    }

    /*** 官方定义 ****/
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }


    private static TreeNode stringToTreeNode(String input) {
        input = input.trim();
        input = input.substring(1, input.length() - 1);
        if (input.length() == 0) {
            return null;
        }

        String[] parts = input.split(",");
        String item = parts[0];
        TreeNode root = new TreeNode(Integer.parseInt(item));
        Queue<TreeNode> nodeQueue = new LinkedList<>();
        nodeQueue.add(root);

        int index = 1;
        while (!nodeQueue.isEmpty()) {
            TreeNode node = nodeQueue.remove();

            if (index == parts.length) {
                break;
            }

            item = parts[index++];
            item = item.trim();
            if (!item.equals("null")) {
                int leftNumber = Integer.parseInt(item);
                node.left = new TreeNode(leftNumber);
                nodeQueue.add(node.left);
            }

            if (index == parts.length) {
                break;
            }

            item = parts[index++];
            item = item.trim();
            if (!item.equals("null")) {
                int rightNumber = Integer.parseInt(item);
                node.right = new TreeNode(rightNumber);
                nodeQueue.add(node.right);
            }
        }
        return root;
    }


    private static String treeNodeToString(TreeNode root) {
        if (root == null) {
            return "[]";
        }

        StringBuilder output = new StringBuilder();
        Queue<TreeNode> nodeQueue = new LinkedList<>();
        nodeQueue.add(root);
        while (!nodeQueue.isEmpty()) {
            TreeNode node = nodeQueue.remove();

            if (node == null) {
                output.append("null, ");
                continue;
            }

            output.append(String.valueOf(node.val)).append(", ");
            nodeQueue.add(node.left);
            nodeQueue.add(node.right);
        }
        return "[" + output.substring(0, output.length() - 2) + "]";
    }

    /*** 官方定义结束 ****/
}

