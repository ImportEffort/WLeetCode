package ArrayAMartix;

import java.util.Arrays;
import java.util.Random;

public class GetMinLengthNeedSort {

    public static void main(String[] args) {
        GetMinLengthNeedSort getMinLengthNeedSort = new GetMinLengthNeedSort();
        int[] arr = getMinLengthNeedSort.buildArr(10);
        System.out.println("原数组为 ："  + Arrays.toString(arr));
        int minLenNeedSort = getMinLengthNeedSort.findMinLenNeedSort(arr);
        System.out.println("最小应该排序的数数组长度为 ：" +  minLenNeedSort);
    }

    private int findMinLenNeedSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }

        int min = arr[arr.length - 1];
        int noMinIndex = -1;

        //遍历后 min 为当前数组最小的元素， noMinIndex 为数组无序的开始
        for (int i = arr.length - 2; i >= 0; i--) {
            //如果 arr[i] > min 代表 arr[i]应该位于 min 的右边 则 i 是无序的开始
            if (arr[i] > min) {
                noMinIndex = i;
            } else {
                //如果当前值小于所设置的 min 的 则表示 有比 min 小的数，去更新 min
                min = Math.min(arr[i], min);
            }
        }

        //表示数组整体升序
        if (noMinIndex == -1) {
            return 0;
        }

        int max = arr[0];
        int noMaxIndex = -1;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < max) {
                noMaxIndex = i;
            } else {
                max = Math.max(arr[i], max);
            }
        }
        for (int i = noMinIndex; i <= noMaxIndex; i++) {
            System.out.print(arr[i] + " ");
        }
        return noMaxIndex - noMinIndex + 1;
    }


    private int[] buildArr(int len) {
        int[] arr = new int[len];
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            arr[i] = random.nextInt(10);
        }

        return arr;
    }
}
