package ArrayAMartix;

/***
 * 找到数组中出现次数超过一半的那个数
 *
 * 遍历一遍数组，如果相邻的数是相同的则 time + 1 如果相邻的数不同的则 time-1；当 time 为 0的时候需要记录当前为数组中出现次数超过一半的那个数
 * 遍历一遍以后剩下个的那个数 cand 并不一定为数组中出现超过一半的数，比如数组中每个数都不通则最后 cand = arr[arr.length - 1 ]
 * 但是如果一个数出现次数超过长度一半那么最后剩下的数一定是它，因为 time--的操作 相当于删除已经遍历数组长度中的两个数，
 * 如果一个数出现次数大于数组一半最后 time >= 1
 */
public class FindHalfMajor {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 2, 2, 2, 5,2, 2};
        FindHalfMajor findHalfMajor = new FindHalfMajor();
        findHalfMajor.printHalfMajor(arr);
    }

    private void printHalfMajor(int[] arr) {
        int cand = 0;
        int time = 0;

        for (int i = 0; i < arr.length; i++) {
            if (time == 0) {
                cand = arr[i];
                time = 1;
            } else if (arr[i] == cand) {
                time++;
            } else {
                time--;
            }
        }

        time = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == cand) {
                time++;
            }
        }

        if (time > arr.length / 2) {
            System.out.println(cand);
        } else {
            System.out.println("没有一个数出现次数超过数组长度的一半");
        }
    }
}
