package ArrayAMartix;

/**
 * 求未排序正数数组红乐嘉何为给定值得最长连续子数组的长度
 *
 * 思路：
 * 1. 两个指针 left 和 right 分别指向数组的头部表示长度为1的滑动窗口
 * 2. sum 始终表示子数组 arr[left,right]的和
 * 3. len 始终表示 sum == k 的时候数组的最大长度
 * 3. 每一次指针变化都进行 sum 和 k 比较
 *   3.1 如果 sum == k 则表示 right 再向右移动 sum 就大于 K 了 此时需要left++ 改变滑动窗口的大小，并改变当前的 sum 值
 *   3.2 如果 sum < k 则移动 right 此时需要注意 right 向右移动以后 可能 right 已经等于数组长度了，但是此时 sum <k 则直接 退出循环
 *   3.3 如果 sum > k 则表示窗口比大了，所以此时缩小窗口 left ++
 */
public class FindSumKMaxLength {
    public static void main(String[] args) {
        int[] arr = {1, 2, 1, 1, 1};
        FindSumKMaxLength findSumKMaxLength = new FindSumKMaxLength();

        System.out.println("和为 K的最长子数组长度为  " + findSumKMaxLength.getMaxLength(arr, 4));
    }

    private int getMaxLength(int[] arr, int k) {

        if (arr == null || arr.length == 0 || k <= 0) {
            return 0;
        }

        int left = 0;
        int right = 0;
        int sum = arr[0];
        int len = 0;

        while (right < arr.length) {
            if (sum == k) {
                len = Math.max(len, right - left + 1);
                sum -= arr[left++];
            } else if (sum < k) {
                right++;
                if (right == arr.length) {
                    break;
                }
                sum += arr[right];
            } else {
                sum -= arr[left];
                left++;
            }

        }
        return len;
    }
}
