package String;

/**
 * 验证一个字符串是否是回文字符串） 忽略大小写
 * Character.isLetterOrDigit('1'); 返回true则表示是字符或者数字
 */
public class Palindrome {
    public static void main(String[] args) {
        String s = "A man, a plan, a canal: Panama";
        System.out.println(new Palindrome().isPalindrome(s));
    }

    public boolean isPalindrome(String s) {
        char[] arr = s.toCharArray();

        int l = 0;
        int r = arr.length - 1;

        //如果为偶数 最后两边都 l + r = arr.length
        //如果为奇数 l + r= arr.length-1
        //这两种都为回文字符串
        while (l < r) {
            if (!Character.isLetterOrDigit(arr[l])) {
                l++;
            } else if (!Character.isLetterOrDigit(arr[r])) {
                r--;
            } else {
                if (Character.toLowerCase(arr[l]) != Character.toLowerCase(arr[r])) {
                    return false;
                } else {
                    l++;
                    r--;
                }
            }
        }

        return true;
    }
}
