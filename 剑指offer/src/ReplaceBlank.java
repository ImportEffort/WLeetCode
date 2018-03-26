public class ReplaceBlank {

    public static void main(String[] args) {
        String str = " We are happy.";
        String replaceBlank = replaceBlank(str);

        System.out.println("replaceBlank = " + replaceBlank);
    }

    private static String replaceBlank(String str) {
        if (str == null || str.length() < 1) {
            return str;
        }

        int blackCount = 0;
        //获取空格的数量
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                blackCount++;
            }
        }
        //新的字符串长度
        int newLength = str.length() + blackCount * 2;
        int originLength = str.length();
        //用于构造新字符串的 char 数组
        char[] newString = new char[newLength];
        //拷贝字符串进数组
        System.arraycopy(str.toCharArray(), 0, newString, 0, originLength);
        //指向新数组末尾的指针
        int indexNew = newLength - 1;
        //指向原数组长度的指针
        int indexOrigin = originLength - 1;
        //从末尾遍历数组每移动一个字符将 indexOrigin 位置的 char 赋值到新长度的末尾，如果 indexOrigin 指向空格则 indexNew 开始替换为 %20
        //只移动 indexOrigin
        while (indexOrigin >= 0 && indexOrigin != indexNew) {
            if (newString[indexOrigin] == ' ') {
                newString[indexNew--] = '0';
                newString[indexNew--] = '2';
                newString[indexNew--] = '%';
            } else {
                newString[indexNew] = newString[indexOrigin];
                indexNew--;
            }

            indexOrigin--;
        }

        return new String(newString,0,newLength);
    }
}
