package ArrayAMartix;

import java.util.Arrays;
import java.util.Random;

public class FindLastK {
    public static void main(String[] args) {
        FindLastK findLastK = new FindLastK();
        int[] arr = findLastK.buildArr(15);
//        int[] arr = {80, 66, 77, 39, 73, 6, 3, 29, 60, 54};


        System.out.println("原始数组为 :  " + Arrays.toString(arr));

        int[] lastKNums = findLastK.getLastKNums(arr, 3);
        System.out.println("最小的k 个元素的数组为 :  " + Arrays.toString(lastKNums));
    }

    private int[] buildArr(int len) {
        int[] arr = new int[len];
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            arr[i] = random.nextInt(100);
        }

        return arr;
    }


    /**
     * @param arr
     * @param k
     */
    private int[] getLastKNums(int[] arr, int k) {
        if (arr == null || arr.length < k) {
            return arr;
        }

        int start = 0;
        int end = arr.length - 1;

        int index = partition(arr, start, end);

        //如果 index 比 k -1 要大，则说明第一次 partition 出来的左边区域比较大则下次要在左边区域去 partition
        //反之 在右边区域去寻找
        while (index != k - 1) {
            if (index > k - 1) {
                end = index - 1;
            } else if (index < k - 1) {
                start = index + 1;
            }

            index = partition(arr, start, end);
        }

        return Arrays.copyOf(arr, k);
    }

    /**
     * arr[start+1 ... i) <= v
     * arr(i....end] >= v
     */
    private int partition(int[] arr, int start, int end) {
        int provit = arr[start];

        int i = start + 1;
        int j = end;


        while (true) {
            while (i <= end && arr[i] <= provit) {
                i++;
            }
            while (j >= start + 1 && arr[j] >= provit) {
                j--;
            }

            if (i > j) break;

            swap(arr, i, j);
            i++;
            j--;

        }

        //j 最后角标停留在 i > j 即为 比 v 小的最后一个一元素位置
        swap(arr, start, j);
        return j;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
