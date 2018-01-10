package day2;

import com.sun.xml.internal.fastinfoset.util.CharArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 求给定两个数之间的 自分数，自分数：可以被自己本身的每一位整除（每一位不包括该为 0 ）,也就是说包括0的数都不是自分数
 * 1、Char是无符号型的，可以表示一个整数，不能表示负数；而byte是有符号型的，可以表示-128—127 的
 * 2、char可以表中文字符，byte不可以
 * 3. char、byte、int对于英文字符，可以相互转化
 */
public class leetCode728 {
    public static void main(String[] args) {
        System.out.println(selfDividingNumbers(1, 22));
        System.out.println(leetCode_selfDividingNumbers(1, 22));
    }

    public static List<Integer> selfDividingNumbers(int left, int right) {
        List<Integer> result = new ArrayList();
        for (int i = left; i < right + 1; i++) {
            if (i > 12) {
                //判断一个数是否能被自己的的每一位整除
                boolean isSelfDividingNum = true;
                char[] chars = String.valueOf(i).toCharArray();
//                System.out.println(Arrays.toString(chars));
                for (int j = 0; j < chars.length; j++) {
                    //一开始忘记了 char 本身就可以做运算 所以多做了很多转换
//                    int parseInt = Integer.parseInt(String.valueOf(chars[j]));
//                    if (parseInt != 0) {
//                        if (i % parseInt != 0) {
//                            isSelfDividingNum = false;
//                        }
//                    }
                    if (chars[j] == '0' || i % chars[j] != '0') {
                        isSelfDividingNum = false;
                    }
                }

                if (isSelfDividingNum) {
                    result.add(i);
                }
            } else {
                result.add(i);
            }

        }

        return result;
    }


    private static List<Integer> leetCode_selfDividingNumbers(int left, int right) {
        List<Integer> ans = new ArrayList();
        for (int n = left; n <= right; ++n) {
            if (selfDividing(n)) ans.add(n);
        }
        return ans;
    }

    private static boolean selfDividing(int n) {
        for (char c : String.valueOf(n).toCharArray()) {
            if (c == '0' || (n % (c - '0') > 0))
                return false;
        }
        return true;
    }


    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }
}
