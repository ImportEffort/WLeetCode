package day3;

/***
 * Given a positive integer, output its complement number. The complement strategy is to flip the bits of its binary representation.

 * Note:
 * The given integer is guaranteed to fit within the range of a 32-bit signed integer.
 * You could assume no leading zero bit in the integer’s binary representation.
 *
 * Example 1:
 * Input: 5
 * Output: 2
 * Explanation: The binary representation of 5 is 101 (no leading zero bits), and its complement is 010. So you need to output 2.
 *
 * Example 2:
 *
 * Input: 1
 * Output: 0
 * Explanation: The binary representation of 1 is 1 (no leading zero bits), and its complement is 0. So you need to output 0.
 */
public class leetCode476 {
    public static void main(String[] args) {
        System.out.println("result:: " + findComplement(5));
        System.out.println("result:: " + leetCode_findComplement(5));

        highestOneBit(5);
    }

    private static int findComplement(int num) {
        String binaryString = Integer.toBinaryString(num);
        char[] complement = new char[binaryString.length()];
        for (int i = 0; i < binaryString.toCharArray().length; i++) {
            complement[i] = binaryString.toCharArray()[i] == '1' ? '0' : '1';
        }

        return Integer.valueOf(String.valueOf(complement),2);
    }


    /**
     * 这个函数调用。使用的第一感觉就是这个函数是干什么用的，通过查看文档得知，这个函数的作用是取 i 这个数的二进制形式最左边的最高一位且高位后面全部补零，
     * 最后返回int型的结果。
     * @param num
     * @return
     */
    private static int leetCode_findComplement(int num) {
        return ~num & (Integer.highestOneBit(num) - 1);
    }

    /**
     * 如果一个数是0, 则返回0；
     * 如果是负数, 则返回 -2147483648：【1000,0000,0000,0000,0000,0000,0000,0000】(二进制表示的数)；
     * 如果是正数, 返回的则是跟它最靠近的比它小的2的N次方
     * @param i
     * @return
     */
    public static int highestOneBit(int i) {
        // HD, Figure 3-1
        i |= (i >>  1);
        i |= (i >>  2);
        i |= (i >>  4);
        i |= (i >>  8);
        i |= (i >> 16);
        return i - (i >>> 1);
    }
}
