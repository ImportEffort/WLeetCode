package ArrayAMartix;

/***
 * 不重复打印有序数组中相加和为给定值的所有二元数组和三元数组
 */
public class PrintUniquePairArr {
    public static void main(String[] args) {
        int[] arr = {-8, -4, -3, 0, 1, 2, 4, 5, 8, 9};
        PrintUniquePairArr printUniquePairArr = new PrintUniquePairArr();
        System.out.println("--------- 打印不重复二元数组 -------");
        printUniquePairArr.printUniquePair(arr, 10);
        System.out.println("--------- 打印不重复三元数组 -------");
        printUniquePairArr.printUniqueTriad(arr, 10);
    }

    /**
     * 打印不重复二元数组
     *
     * @param arr
     * @param k
     */
    private void printUniquePair(int[] arr, int k) {
        if (arr == null || arr.length < 1) {
            return;
        }

        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            int sum = arr[left] + arr[right];
            if (sum < k) {
                left++;
            } else if (sum > k) {
                right--;
            } else {
                // arr[left - 1] != arr[left] 保证了 不重复打印
                if (left == 0 || arr[left - 1] != arr[left]) {
                    System.out.println(arr[left] + "," + arr[right]);
                    //同时操作两个指针
                    left++;
                    right--;
                }
            }
        }
    }

    /**
     * 打印不重复三元数组
     * 与二元数组不同的是： 需要记录三元数组的起始值，再去寻找另外两个相加为 k - arr[i]
     *
     * @param arr
     * @param k
     */
    private void printUniqueTriad(int[] arr, int k) {
        if (arr == null || arr.length < 1) {
            return;
        }

        for (int i = 0; i < arr.length - 2; i++) {
            //保证三元数组不重复
            if (i == 0 || arr[i] != arr[i - 1]) {
                printRest(arr, i, i + 1, arr.length - 1, k - arr[i]);
            }
        }
    }

    private void printRest(int[] arr, int f, int l, int r, int k) {
        while (l < r) {
            int sum = arr[l] + arr[r];
            if (sum < k) {
                l++;
            } else if (sum > k) {
                r--;
            } else {
                if (l == f || arr[l - 1] != arr[l]) {
                    System.out.println(arr[f] + "," + arr[l] + "," + arr[r]);
                    //同时操作两个指针
                    l++;
                    r--;
                }
            }
        }
    }
}
