package BinaryTree;


public class BinaryOrder {
    public static void main(String[] args) {
        BinaryTreeUtils tree = new BinaryTreeUtils();
        TreeNode binaryTree = tree.createBinaryTree();

        BinaryTreeUtils.levelTraverse(binaryTree);
        System.out.println();

        TreeNode binaryTree2 = tree.createBinaryTree2();
        //层级遍历结果 8 7 6 4 1 3 10 20 11
        BinaryTreeUtils.levelTraverse(binaryTree2);
        System.out.println();

        //先序遍历 8 7 4 20 1 6 3 11 10
        BinaryTreeUtils.beforeTraverse(binaryTree2);
        System.out.println();

        //先序遍历 8 7 4 20 1 6 3 11 10
        BinaryTreeUtils.beforeTraverseByStack(binaryTree2);
        System.out.println();

        //中序遍历 20 4 7 1 8 3 11 6 10
        BinaryTreeUtils.middleTraverse(binaryTree2);
        System.out.println();

        //中序遍历 20 4 7 1 8 3 11 6 10
        BinaryTreeUtils.middleTraverseByStack(binaryTree2);
        System.out.println();

        //后续遍历 20 4 1 7 11 3 10 6 8
        BinaryTreeUtils.afterTraverse(binaryTree2);
        System.out.println();

        //后续遍历 20 4 1 7 11 3 10 6 8
        BinaryTreeUtils.afterTraverseByStack1(binaryTree2);
        System.out.println();

        //后续遍历 20 4 1 7 11 3 10 6 8
        BinaryTreeUtils.afterTraverseByStack2(binaryTree2);
        System.out.println();
    }
}
