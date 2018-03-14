package ArrayAMartix;

/***
 * 一个纵向横向都递增的 N * M 矩阵中是否包含 k
 */
public class MatrixContainK {
    public static void main(String[] args) {
        MatrixContainK matrixContainK = new MatrixContainK();
        int[][] matrix = matrixContainK.buildMatrix();
        System.out.println("矩阵中是否包含 k = 7 的元素 ： " + matrixContainK.containK(matrix, 7));
    }

    private boolean containK(int[][] matrix, int k) {
        if (matrix == null) {
            return false;
        }
        //从矩阵右上角开始找
        int row = 0;
        int col = matrix[0].length - 1;
        while (row < matrix.length && col > -1) {
            if (matrix[row][col] == k) {
                return true;
            } else if (matrix[row][col] < k) {
                row++;
            } else {
                col--;
            }
        }

        return false;
    }

    private int[][] buildMatrix() {
        return new int[][]{{0, 1, 2, 5}, {2, 3, 4, 7}, {4, 4, 4, 8}, {5, 7, 7, 9}};
    }
}
