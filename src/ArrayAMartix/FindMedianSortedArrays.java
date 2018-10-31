package ArrayAMartix;

import java.util.Stack;

public class FindMedianSortedArrays {


    public static void main(String[] args) {
        int[] nums1 = {1,2};
        int[] nums2 = {3,4};
        double medianSortedArrays = new FindMedianSortedArrays().findMedianSortedArrays(nums1, nums2);
        System.out.println("medianSortedArrays = " + medianSortedArrays);
    }
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {

        int[] result;

        if(nums1.length == 0){
            result = nums2;
        }else if(nums2.length == 0){
            result = nums1;
        }else{
            result = meger(nums1,nums2);
        }

        if(result.length % 2  == 1){
            return result[result.length/2];
        }else{
            return (result[result.length /2 ] + result[result.length / 2 - 1 ]) / 2.0d;
        }
    }

    private int[] meger(int[] nums1, int[] nums2){
        int[] result = new int[nums1.length + nums2.length];
        int i = 0;
        int j = 0;
        int k = 0;
        while(i < nums1.length && j< nums2.length){
            if(nums1[i] < nums2[j]){
                result[k++] = nums1[i++];
            }else{
                result[k++] = nums2[j++];
            }
        }

        while(i < nums1.length ){
            result[k++] = nums1[i++];
        }

        while(j < nums2.length ){
            result[k++] = nums2[j++];
        }

        return result;
    }


}
