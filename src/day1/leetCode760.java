package day1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/***
 *
 * Given two lists Aand B, and B is an anagram of A. B is an anagram of A means B is made by randomizing the order of the elements in A.
 * We want to find an index mapping P, from A to B. A mapping P[i] = j means the ith element in A appears in B at index j.
 * These lists A and B may contain duplicates. If there are multiple answers, output any of them.
 *
 * For example, given
 *
 * A = [12, 28, 46, 32, 50]
 * B = [50, 12, 32, 46, 28]
 * We should return
 * [1, 4, 3, 2, 0]
 *
 *  as P[0] = 1 because the 0th element of A appears at B[1], and P[1] = 4 because the 1st element of A appears at B[4], and so on.
 *
 * Note:
 *
 * A, B have equal lengths in range [1, 100].
 * A[i], B[i] are integers in range [0, 10^5].
 *
 * 该题目的意思是给定两个数组 A B ,B 数组是 A 数组元素 随机排列的后的新数组， 以你认为的最优解给出 A 数组元素在 B 数组中元素的角标数组
 */
public class leetCode760 {
    public static void main(String[] args) {

        int[] A = {12, 28, 46, 32, 50};
        int[] B = {50, 12, 32, 46, 28};

        System.out.println("A 中 元素在 B 数组中的角标 " + Arrays.toString(anagramMappings(A, B)));
        System.out.println("A 中 元素在 B 数组中的角标 " + Arrays.toString(leetCode_anagramMappings(A, B)));

    }

    /**
     * 我的解法
     */
    private static int[] anagramMappings(int[] A, int[] B) {
        int[] indexOfB = new int[A.length];
        int index = 0;
        for (int i = 0; i < A.length; i++) {
            int cellOfA = A[i];
            for (int j = 0; j < B.length; j++) {
                if (B[j] == cellOfA) {
                    indexOfB[index++] = j;
                }
            }
        }
        return indexOfB;
    }

    /**
     * 官方最优解
     */
    private static int[] leetCode_anagramMappings(int[] A, int[] B) {
        Map<Integer, Integer> D = new HashMap();
        for (int i = 0; i < B.length; ++i)
            D.put(B[i], i);

        int[] ans = new int[A.length];
        int t = 0;
        for (int x : A)
            ans[t++] = D.get(x);
        return ans;
    }
}
