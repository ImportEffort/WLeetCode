package ArrayAMartix;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrOrMatrixMaxSum {
    public static void main(String[] args) {
        ArrOrMatrixMaxSum matrixMaxSum = new ArrOrMatrixMaxSum();
        int[] arr = {1, -2, 3, 5, 2, 6, -1};
        int arrMaxSum = matrixMaxSum.getArrMaxSum(arr);
        System.out.println("arrMaxSum :  "+arrMaxSum);

        int[][] matrix = {{-90, 48, 78}, {64, -40, 64}, {-81, -7, 66}};
        int matrixMaxSum1 = matrixMaxSum.getMatrixMaxSum(matrix);
        System.out.println("matrixMaxSum1 :  "+matrixMaxSum1);
    }
    //

    private int getArrMaxSum(int[] arr) {
        if (arr == null || arr.length < 1) {
            return -1;
        }

        int max = Integer.MIN_VALUE;
        int cur = 0;

        for (int i = 0; i < arr.length; i++) {
            cur += arr[i];
            max = Math.max(cur, max);
            cur = cur < 0 ? 0 : cur;
        }

        return max;
    }

    /**
     * @param matrix
     * @return
     */
    private int getMatrixMaxSum(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }

        int max = Integer.MIN_VALUE;
        int cur = 0;

        int[] s = null;

        for (int i = 0; i < matrix.length; i++) {
            s = new int[matrix[0].length];
            for (int j = i; j < matrix.length; j++) {
                cur = 0;
                for (int k = 0; k < s.length; k++) {
                    s[k] += matrix[j][k];
                    cur += s[k];
                    max = Math.max(cur, max);
                    cur = cur < 0 ? 0 : cur;
                }
            }

        }

        return max;
    }
}
