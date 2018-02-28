package Sorting_Basic.Selection_Sort;
/**
 * 选择排序
 */
public class SelectionSort {
    public static <T extends Comparable<T>>  void sort(T[] arr) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            int minIndex = i;
            // for 循环 i 之后所有的数字 找到剩余数组中最小值得索引
            for (int j = i + 1; j < n; j++) {
                // < 0 比较的对象 小于 被比较的对象
                if (arr[j].compareTo(arr[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            swap(arr, i, minIndex);
        }

    }

    /**
     * 角标的形式 交换元素
     */
    private static void swap(Object[] arr, int i, int j) {
        Object temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
//        Integer[] arr = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
//        SelectionSort.sort(arr);
//        System.out.println(Arrays.toString(arr));
//
//        // 测试Double
//        Double[] doubles = {4.4, 3.3, 2.2, 1.1};
//        SelectionSort.sort(doubles);
//        System.out.println(Arrays.toString(doubles));
//
//        // 测试String
//        String[] strings = {"D", "C", "B", "A"};
//        SelectionSort.sort(strings);
//        System.out.println(Arrays.toString(strings));
//
//
//        // 测试自定义的类 Student
//        Student[] students = new Student[4];
//        students[0] = new Student("D",90);
//        students[1] = new Student("C",100);
//        students[2] = new Student("B",95);
//        students[3] = new Student("A",95);
//        SelectionSort.sort(students);
//
//        System.out.println(Arrays.toString(students));


        // 测试排序算法辅助函数
        int N = 20000;
        Integer[] arr = SortTestHelper.generateRandomArray(N, 0, 100000);
        SortTestHelper.printArray(arr);
        SortTestHelper.testSort("Sorting_Basic.Selection_Sort.SelectionSort", arr);
    }
}
