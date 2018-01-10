package day1;

/***
 * day1.LeetCode461  汉明距离 即两个数 汉明距离是以理查德·卫斯里·汉明的名字命名的。在信息论中，两个等长字符串之间的汉明距离是两个字符串对应位置的不同字符的个数。
 * 换句话说，它就是将一个字符串变换成另外一个字符串所需要替换的字符个数。
 *
 * The Hamming distance between two integers is the number of positions at which the corresponding bits are different.
 * Given two integers x and y, calculate the Hamming distance.
 * Note:
 * 0 ≤ x, y < 231.
 *
 * Example:
 *
 * Input: x = 1, y = 4
 *
 * Output: 2
 *
 * Explanation:
 * 1   (0 0 0 1)
 * 4   (0 1 0 0)
 *        ↑   ↑
 *
 * The above arrows point to positions where the corresponding bits are different.
 */
public class LeetCode461 {

    public static void main(String[] args) {

        System.out.printf("hamming:: " + leetCodeHammingDistance(1, 3));
        System.out.printf("hamming:: " + hammingDistance(1, 3));
    }

    //我的解法
    private static int hammingDistance(int x, int y) {
        int z = x ^ y;
        char[] chars = Integer.toBinaryString(z).toCharArray();
        int distance = 0;
        for (char a : chars) {
            if (a == '1') {
                distance++;
            }
        }
        return distance;
    }

    /**
     *
     *  异或运算符 如果对应位置相同则是0, 不同则是1
     *
     *  4的二进制形式为0000 0000 0000 0000 0000 0000 0000 0100
     *　10的二进制形式为0000 0000 0000 0000 0000 0000 0000 1010
     *
     *　按照计算规则，结果为0000 0000 0000 0000 0000 0000 0000 1110
     *
     *
     *  z>>1 右移1位  do while 中判断如果右移后的数字为0 则停止执行  即判断有多少位是1
     */
    private static int leetCodeHammingDistance(int x, int y) {
        int z = x ^ y;//
        int distance = 0;
        do {
            distance += z & 1;
            z = z >> 1;
        } while (z != 0);
        return distance;
    }
}
