package Set;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/***
 *  字母异位词 两个字符串长度相同，包含的字符相同（包括同一个字符的个数）相同，（字符顺序可以相同也可以不同）
 *  给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的一个字母异位词。
 *
 *  输入: s = "anagram", t = "nagaram"
 *  输出: true
 *
 *  输入: s = "rat", t = "car"
 *  输出: false
 *
 *  计数每个字符出现的次序，如果
 */
public class isAnagram {
    public static void main(String[] args) {
        String s = "anagram";
        String t = "anagram";

        isAnagram isAnagram = new isAnagram();
        boolean anagram = isAnagram.isAnagram(s, t);
        System.out.println("anagram = " + anagram);

        // 用例 ab aa false // ab ca  true
        boolean isomorphic = isAnagram.isIsomorphic("ab", "aa");
        System.out.println("isomorphic = " + isomorphic);


    }

    public boolean isIsomorphic(String s, String t) {
        if (s == null || t == null) {
            return false;
        }

        if (s.length() != t.length()) {
            return false;
        }

        if (s.length() == 1) {
            return true;
        }


        char[] charsS = s.toCharArray();
        char[] charsT = t.toCharArray();

        HashMap<Character, Character> map = new HashMap<>();
        Set<Character> set = new HashSet<>();

        for (int i = 0; i < s.length(); i++) {
            char sChar = charsS[i];
            char tChar = charsT[i];

            if(map.containsKey(sChar)){
                if(map.get(sChar) != tChar){
                    return false;
                }
            }else {
                if(set.contains(tChar)){
                    return false;
                }else {
                    map.put(sChar, tChar);
                    set.add(tChar);
                }
            }
        }

        return true;
    }

    private boolean isAnagram(String s, String t) {
        if (s == null || t == null) {
            return false;
        }

        if (s.length() != t.length()) {
            return false;
        }

        char[] sArr = s.toCharArray();
        char[] tArr = t.toCharArray();

        //不考虑特殊情况时，因为共有26个字符
        int[] result = new int[26];

        for (int i = 0; i < s.length(); i++) {
            result[sArr[i] - 'a']++;
            result[tArr[i] - 'a']--;
        }

        for (int res : result) {
            if (res != 0) {
                return false;
            }
        }

        return true;
    }
}
