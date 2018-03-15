package ArrayAMartix;

import java.util.Arrays;

public class SortArrAsNature {
    public static void main(String[] args) {
        int[] arr = {1, 2, 5, 3, 4};

        SortArrAsNature asNature = new SortArrAsNature();
        int[] arr1 = Arrays.copyOf(arr, arr.length);
        asNature.sort2(arr1);
        int[] arr2 = Arrays.copyOf(arr, arr.length);
        asNature.sort(arr2);

        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr2));

    }

    private void sort(int[] arr) {

        int temp = 0;

        for (int i = 0; i < arr.length; i++) {
            while (arr[i] != i + 1) {
                temp = arr[arr[i] - 1];
                arr[arr[i] - 1] = arr[i];
                arr[i] = temp;
            }
        }
    }

    /**
     * @param arr
     */
    private void sort2(int[] arr) {

        int temp = 0;
        int next = 0;

        for (int i = 0; i != arr.length; i++) {
            temp = arr[i];

            while (arr[i] != i + 1) {
                next = arr[temp - 1];
                arr[temp - 1] = temp;
                temp = next;
            }
        }
    }
}
