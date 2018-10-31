package ArrayAMartix;

public class FindKLagestNum {

    private static int quickSort(int[] nums, int l, int r, int k) {
        if( l == r )
            return nums[l];

        int index = nums.length - k;


        int p = partition(nums, l, r);

        if (index == p) {
            return nums[p];
        } else if (index < p) {// 如果 k < p, 只需要在nums[l...p-1]中找第k小元素即可
            return quickSort(nums, l, p - 1, index);
        } else {//index>p 则在 [p+1,r] 中去排序，返回 p 由于我们传入的数组为nums，所以 index 和 p 始终代表原来角标
            return quickSort(nums, p + 1, r, index);
        }
    }

    /**
     * arr[l+1 ... i) <= v
     * arr(j....r] >= v
     */
    private static int partition(int[] nums, int l, int r) {
        int v = nums[l];
        int j = l;
        for (int i = l + 1; i <= r; i++) {
            if (nums[i] < v) {
                j++;
                swap(nums, i, j);
            }
        }
        swap(nums, l, j);
        return j;
    }

    private static void swap(int[] nums, int a, int b) {
        int temp = nums[a];
        nums[a] = nums[b];
        nums[b] = temp;
    }

    public static void main(String[] args) {
//        int[] arr = {3, 2, 1, 5, 6, 4};
        int[] arr = {3,2,3,1,2,4,5,5,6};
        System.out.println(arr[quickSort(arr, 0, arr.length - 1, 2)]);
    }

}
