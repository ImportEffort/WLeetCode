package ArrayAMartix;

public class Merge2SortedArr {
    public static void main(String[] args) {
        int[] nums1 = {1,2,3,0,0,0};
        int[] nums2 = {2,5,6};
        int m = 3; int n = 3;
        //归并解法
        meger(nums1,m,nums2,n);
        //尾部插入解法
        mergeByInsertEnd(nums1,m,nums2,n);

    }

    /**
     * 归并排序的思路求解
     */
    private static void meger(int[] nums1, int m, int[] nums2, int n) {
        if (n < 1) return;
        if (m < 1) System.arraycopy(nums2, 0, nums1, 0, n);


        int[] temp = new int[m + n];
        int i = 0;
        int j = 0;
        int index = 0;
        while (i < m && j < n) {
            if (nums1[i] <= nums2[j]) {
                temp[index] = nums1[i];
                i++;
            } else {
                temp[index] = nums2[j];
                j++;
            }
            index++;
        }

        if (i < m) {
            for (int k = i; k < m; k++) {
                temp[index] = nums1[k];
                index++;
            }
        }

        if (j < n) {
            for (int k = j; k < n; k++) {
                temp[index] = nums2[k];
                index++;
            }
        }

        System.arraycopy(temp, 0, nums1, 0, temp.length);

    }

    private static void mergeByInsertEnd(int[] nums1, int m, int[] nums2, int n) {
        // 尾插法 不开拓新的数组节约空间复杂度
        if (n < 1) return;
        if (m < 1) System.arraycopy(nums2, 0, nums1, 0, n);

        int k = m + n - 1; //最 nums1 数组中元素的个数
        int i = m - 1;
        int j = n - 1;


        while (i >= 0 && j >= 0) {
            if (nums1[i] >= nums2[j]) {
                nums1[k] = nums1[i];
                i--;
                k--;
            } else {
                nums1[k] = nums2[j];
                j--;
                k--;
            }
        }

        while (j >= 0) {
            nums1[k] = nums2[j];
            j--;
            k--;
        }
    }
}
