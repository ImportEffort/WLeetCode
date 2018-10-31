package ArrayAMartix;

/***
 * 盛水最多的容器：
 * 给定 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0)。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 * 说明：你不能倾斜容器，且 n 的值至少为 2。
 *
 * 即求 sum = (j - i) * min(i,j) 最大值
 * 此题需要一定的理论证明，即证明 只需要自动较小值的角标位置可以一定遍历到 sum 为最大的情况。
 * https://segmentfault.com/a/1190000008824222
 *
 */
public class MaxArea {
    public static void main(String[] args) {
        int[] arr = {1,8,6,2,5,4,8,3,7};
        System.out.println(new MaxArea().maxArea(arr));
    }

    public int maxArea(int[] arr) {
        int l = 0;
        int r = arr.length - 1;
        int max = 0;
        while (l < r) {
            int height = Math.min(arr[l], arr[r]);
            int width = r - l;

            max = Math.max(max, width * height);

            if (arr[l] < arr[r]) {
                l++;
            } else {
                r--;
            }

        }

        return max;
    }
}
