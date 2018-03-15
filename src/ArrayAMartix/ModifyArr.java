package ArrayAMartix;

import java.util.Arrays;

public class ModifyArr {

    public static void main(String[] args) {
        ModifyArr modifyArr = new ModifyArr();
        int[] arr = {1, 8, 3, 2, 4, 6};
        modifyArr.modify(arr);

        System.out.println(Arrays.toString(arr));

    }

    private void modify(int[] arr) {
        if (arr == null || arr.length < 1) {
            return;
        }

        int even = 0;
        int odd = 1;
        int end = arr.length - 1;

        while (even <= end && odd <= end) {
            //偶数
            if ((arr[end] & 1) == 0) {
                swap(arr, end, even);
                even += 2;
            } else {
                swap(arr, end, odd);
                odd += 2;
            }
        }

    }

    private void swap(int[] arr, int index1, int index2) {
        int temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;
    }
}
