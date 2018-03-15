package ArrayAMartix;

public class FindLocalMinimum {

    public static void main(String[] args) {


        int[] arr = {2, 1, 3, 4, 5, 6, 11, 14, 8, 25};

        FindLocalMinimum minimum = new FindLocalMinimum();
        int index = minimum.localMinimum(arr);
        System.out.println("局部最小元素 角标为 " + index + "值为" + arr[index]);

    }

    private int localMinimum(int[] arr) {

        if (arr == null || arr.length == 0) {
            return -1;
        }
        if (arr.length == 1 || arr[0] < arr[1]) {
            return 0;
        }
        if (arr[arr.length - 1] < arr[arr.length - 2]) {
            return arr.length - 1;
        }

        int mid = 0;
        int left = 1;
        int right = arr.length - 2;
        //走到这里说明  arr[0] > arr[1]  arr[n -2 ] < arr[n-1]
        while (left < right) {
            mid = (left + right) / 2;
            System.out.println( " mid " +  arr[mid] );
            //认为局部是上升结构 则局部最小值出现在左侧 且 因为  arr[0] > arr[1] 所以 肯定有一个 最低点
            if (arr[mid] > arr[mid - 1]) {
                right = mid - 1;
            } else if (arr[mid] > arr[mid + 1]) {
                left = mid + 1;
            } else {
                return mid;
            }
        }
        return left;
    }
}
