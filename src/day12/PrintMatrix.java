package day12;

/**
 * 给出一个整数N，打印出N*N的转圈矩阵，或者是给出一个这样的转圈矩阵，如何将其转圈输出
 */
public class PrintMatrix {
    public static void main(String args[]) {

        printArrayWisely(buildCircleMatrix(7));
        System.out.println();


        printResult(buildCircleMatrix(7));
    }

    public static void printArrayWisely(int[][] num) {
        if (num == null) {
            return;
        }

        int x = 0;
        int y = 0;

        // 行号最大是(num.length-1)/2
        // 列号最大是(num[0].length-1)/2

        while (x * 2 < num.length && y * 2 < num[0].length) {
            printNewInCircle(num, x, y);
            x++;
            y++;
        }
    }

    public static void printNewInCircle(int[][] num, int x, int y) {
        int row = num.length; // 矩阵的行数
        int col = num[0].length; // 矩阵的列数

        // 第一步输出最上面一行的所以元素
        // 是按列来输出的
        for (int i = y; i <= col - y - 1; i++) {
            System.out.print(num[x][i] + " ");
        }

        // row-x-1：表示的是环最下的那一行的行号
        // 第二步，打印最右边， 使用自上向下的方式
        if (row - x - 1 > x) {
            for (int i = x + 1; i <= row - x - 1; i++) {
                System.out.print(num[i][col - y - 1] + " ");
            }
        }

        // 第三步，打印最下面的一行，但是去除第一个元素
        // col - 1 - y > y 这句是保证有列数
        if (row - x - 1 > x && col - 1 - y > y) {
            // 因为环的左下角的位置已经输出了，所以列号从col-y-2开始
            for (int i = col - y - 2; i >= y; i--) {
                System.out.print(num[row - 1 - x][i] + " ");
            }
        }

        // 第四步，在保证有列数的同时，还要保证有行数，
        // 环的宽度至少是2并且环的高度至少是3才会输出最左边那一列
        // rows-x-1：表示的是环最下的那一行的行号
        if (col - 1 - y > y && row - 1 - x > x + 1) {
            // 因为最左边那一列的第一个和最后一个已经被输出了
            for (int i = row - 1 - x - 1; i >= x + 1; i--) {
                System.out.print(num[i][y] + " ");
            }
        }
    }

    private static int[][] buildCircleMatrix(int N) {
        int[][] matix = new int[N][N];
        int k = 0;
        int i = 0;
        int j = 0;
        int z = 1;

        while (k < N) {
            while (j < N - k) {
                matix[i][j] = z;
                j++;
                z++;
            }
            j--;
            i++;

            while (i < N - k) {
                matix[i][j] = z;
                i++;
                z++;
            }
            i--;
            j--;

            while (j >= k) {
                matix[i][j] = z;
                j--;
                z++;
            }
            j++;
            k++;
            i--;

            while (i >= k) {
                matix[i][j] = z;
                i--;
                z++;
            }
            i++;
            j++;
        }

        return matix;
    }

    private static void printResult(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] +"   ");
            }
            System.out.println();
        }
    }
}
