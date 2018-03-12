package ArrayAMartix;

public class MatrixPrint {
    public static void main(String[] args) {
        System.out.println();
        System.out.println("------- 旋转打印矩阵 ------");

        MatrixPrint matrixPrint = new MatrixPrint();
        int[][] matrix = matrixPrint.buildMatrix();
        matrixPrint.spiralOrderPrint(matrix);

        System.out.println();
        System.out.println("------- 之字型打印矩阵 ------");


        int[][] matrix2 = matrixPrint.buildMatrix2();
        matrixPrint.printMatrixZigZag(matrix2);

        System.out.println();
        System.out.println("------将矩阵旋转 90° -------");

        int[][] matrix3 = matrixPrint.buildMatrix3();
        matrixPrint.rotateMatrix(matrix3);
        matrixPrint.spiralOrderPrint(matrix3);

    }

    /***
     * 旋转的打印矩阵 一个矩阵的一圈可以用左上角的角标 (tR,tC) 右下角的角标 (dR,dC)来表示
     * 旋转矩阵的打印临界值判断 就是每次打印完一圈以后左上角标就+1 和右下角标-1，知道右下角标的位置出现在左上角标的左上方，则打印结束
     * 打印的过程 就是打印最上边一行（tR 不变 ，tC++），打印最右边一列（tR++，tC = dC 不变），
     * 打印最下边一行（tR= dR 不变，tC--），打印最左边一列（tR--，tC =初始值不变），
     * @param matrix
     */
    private void spiralOrderPrint(int[][] matrix) {
        int tR = 0;
        int tC = 0;
        int dR = matrix.length - 1;
        int dC = matrix[0].length - 1;

        while (tR <= dR && tC <= dC) {
            printEdge(matrix, tR++, tC++, dR--, dC--);
        }

    }

    /**
     * @param matrix 矩阵
     * @param tR     左上角的行数
     * @param tC     左上角的列数
     * @param dR     右上角的行数
     * @param dC     右下角的列数
     *               <p>
     *               行 r == row  列 c == column
     */
    private void printEdge(int[][] matrix, int tR, int tC, int dR, int dC) {
        //行数相同，只有一行的情况
        if (tR == dR) {
            for (int i = tC; i <= dC; i++) {
                System.out.print(matrix[tR][i] + " ");
            }
        } else if (tC == dC) {
            for (int i = tR; i <= dR; i++) {
                System.out.print(matrix[i][tC] + " ");
            }
        } else {

            int curR = tR;
            int curC = tC;

            //打印上边一行 tR 行不包括最右一个元素 列数递增
            while (curC != dC) {
                System.out.print(matrix[tR][curC] + " ");
                curC++;
            }

            //打印最右一列 dC 不包括最下边一个元素 行数递增
            while (curR != dR) {
                System.out.print(matrix[curR][dC] + " ");
                curR++;
            }

            //打印最下边一行 dR 不包括最左边一个元素 列数递减
            while (curC != tC) {
                System.out.print(matrix[dR][curC] + " ");
                curC--;
            }
            //打印最左边一列 tC 不包括最上边一个元素 行数递减
            while (curR != tR) {
                System.out.print(matrix[curR][tC] + " ");
                curR--;
            }
        }
    }


    /***
     * 之字形打印矩阵 之字型打印 每一斜行打印顺序相反 可以用 boolean 标志打印方向
     * 如果从上往下打印则现象是行数递增，列数递减 行数临界值为当前打印的最大行数 dR
     * 从下往上打印的现象是行数递减，列数递增 临界值为当前起始的行数 tR
     * @param matrix 矩阵
     */
    private void printMatrixZigZag(int[][] matrix) {

        int tR = 0;
        int tC = 0;
        int dR = 0;
        int dC = 0;
        int endR = matrix.length - 1;
        int endC = matrix[0].length - 1;
        boolean fromUp = false;

        //tR 最大等于矩阵的行数
        while (tR != endR + 1) {
            printLevel(matrix, tR, tC, dR, dC, fromUp);
            //这里必须让 tR 先让 tR 判断 因为当列数没达到最大的时候，tR 应该是一直为 0 的
            tR = tC == endC ? tR + 1 : tR;
            tC = tC == endC ? tC : tC + 1;
            //这里必须让 dC 先让 dC 判断 因为当行数没达到最大的时候，dC 应该是一直为 0 的
            dC = dR == endR ? dC + 1 : dC;
            dR = dR == endR ? dR : dR + 1;
            fromUp = !fromUp;
        }

    }


    private void printLevel(int[][] matrix, int tR, int tC, int dR, int dC, boolean fromUp) {
        if (fromUp) {
            while (tR != dR + 1) {//从上往下列数减行数递增
                System.out.print(matrix[tR++][tC--] + " ");
            }
        } else {
            while (dR != tR - 1) {//从下往上行数递减，列数递增
                System.out.print(matrix[dR--][dC++] + " ");
            }
        }

    }

    /**
     * 将正方形顺时针旋转 90°
     *
     * @param matrix 矩阵
     */
    private void rotateMatrix(int[][] matrix) {
        int tR = 0;
        int tC = 0;
        int dR = matrix.length - 1;
        int dC = matrix[0].length - 1;
        // M * M 的矩阵 只有 行数不同的时候才能旋转 若已有一行或者只有一列的时候无法旋转
        while (tR < dR) {
            rotateEdge(matrix, tR++, tC++, dR--, dC--);
        }
    }

    private void rotateEdge(int[][] matrix, int tR, int tC, int dR, int dC) {
        int time = dC - tC;
        int temp = 0;
        for (int i = 0; i < time; i++) {
            //记录第一行的值 从第一行来看 遍历翻转的时候只列在递增
            temp = matrix[tR][tC + i];
            //最左边一列变成最上边一行
            matrix[tR][tC + i] = matrix[dR - i][tC];
            //最下边一行变成最左边一列
            matrix[dR - i][tC] = matrix[dR][dC - i];
            //最右边一列变成最下边一行
            matrix[dR][dC - i] = matrix[tR + i][dC];
            //最上边一行变成最右边一列
            matrix[tR + i][dC] = temp;
        }
    }

    private int[][] buildMatrix() {
        return new int[][]{{1, 2, 3, 4}, {12, 13, 14, 5}, {11, 16, 15, 6}, {10, 9, 8, 7}};
    }

    private int[][] buildMatrix2() {
        return new int[][]{{1, 2, 6, 7}, {3, 5, 8, 13}, {4, 9, 12, 14}, {10, 11, 15, 16}};
    }

    private int[][] buildMatrix3() {
        return new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
    }
}
