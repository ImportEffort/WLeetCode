package day3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/***
 * Given a nested list of integers, return the sum of all integers in the list weighted by their depth.
 * Each element is either an integer, or a list -- whose elements may also be integers or other lists.
 *
 * Example 1:
 * Given the list [[1,1],2,[1,1]], return 10. (four 1's at depth 2, one 2 at depth 1)
 *
 * Example 2:
 * Given the list [1,[4,[6]]], return 27. (one 1 at depth 1, one 4 at depth 2, and one 6 at depth 3; 1 + 4*2 + 6*3 = 27)
 *
 *
 */

/**
 * // This is the interface that allows for creating nested lists.
 * // You should not implement it, or speculate about its implementation
 * public interface NestedInteger {
 * <p>
 * // @return true if this NestedInteger holds a single integer, rather than a nested list.
 * public boolean isInteger();
 * <p>
 * // @return the single integer that this NestedInteger holds, if it holds a single integer
 * // Return null if this NestedInteger holds a nested list
 * public Integer getInteger();
 * <p>
 * // @return the nested list that this NestedInteger holds, if it holds a nested list
 * // Return null if this NestedInteger holds a single integer
 * public List<NestedInteger> getList();
 * }
 */
public class leetCode339 {

    public static void main(String[] args) {
        List<NestedInteger> aNestList = getANestList();
        System.out.println("mSum :: " + mhelper(aNestList, 1));
        System.out.println("sum :: " + depthSum(aNestList));
        System.out.println("sum :: " + getWeightSum(aNestList,1));
    }

    private static int mhelper(List<NestedInteger> aNestList, int depth) {
        int sum = 0;
        for (NestedInteger nestedInteger : aNestList) {
            if (nestedInteger.isInteger()) {
                System.out.println("getInteger:: " + nestedInteger.getInteger() +":: " + depth);
                sum += nestedInteger.integer * depth;
            } else {
                sum += helper(nestedInteger.getNestedIntegers(), depth + 1);
            }

        }
        return sum;
    }

    public static int depthSum(List<NestedInteger> nestedList) {
        return helper(nestedList, 1);
    }

    private static int helper(List<NestedInteger> list, int depth) {
        int ret = 0;
        for (NestedInteger e : list) {

            if (e.isInteger()) {
                ret += e.getInteger() * depth;
                System.out.println("getInteger:: " + e.getInteger() +":: " + depth);
            } else {
                ret += helper(e.getNestedIntegers(), depth + 1);
            }
        }
        return ret;
    }
    public static int getWeightSum(List<NestedInteger> nestedIntegers, int weight) {
        if (nestedIntegers == null || nestedIntegers.size() <= 0 || weight <= 0) {
            return 0;
        }
        int weightSum = 0;
        for (int i = 0; i < nestedIntegers.size(); i++) {
            if (nestedIntegers.get(i) != null && nestedIntegers.get(i).isInteger()) {
                System.out.println("getInteger:: " + nestedIntegers.get(i).getInteger() +":: " + weight);
                weightSum += nestedIntegers.get(i).getInteger() * weight;
            } else {
                weightSum += getWeightSum(nestedIntegers.get(i).getNestedIntegers(), weight + 1);
            }
        }
        return weightSum;
    }

    public static List<NestedInteger> getANestList() {
        NestedInteger nestedInteger6 = new NestedInteger(6);
        List<NestedInteger> list1 = new LinkedList<>();
        list1.add(nestedInteger6);

        NestedInteger nestedInteger4 = new NestedInteger(4);
        nestedInteger4.setNestedIntegers(list1);

        NestedInteger nestedInteger1 = new NestedInteger(1);
        List<NestedInteger> list2 = new LinkedList<>();
        list2.add(new NestedInteger(4));
        list2.add(nestedInteger4);

        nestedInteger1.setNestedIntegers(list2);

        List<NestedInteger> nestedIntegerList = new ArrayList<>();
        nestedIntegerList.add(new NestedInteger(1));
        nestedIntegerList.add(nestedInteger1);

        System.out.println(nestedIntegerList);

        return nestedIntegerList ;
    }


    /**
     * 自定义嵌套数组
     * Created by Administrator on 2017/7/19.
     * [ 1, [ 4 , [ 6 ] ] ]
     */
    public static class NestedInteger {
        //包含一个整数
        private Integer integer;
        //以及一个数组
        private List<NestedInteger> nestedIntegers;

        //初始化Int
        public NestedInteger(Integer integer) {
            this.integer = integer;
        }

        //初始化数组
        public NestedInteger(List<NestedInteger> nestedIntegers) {
            this.nestedIntegers = nestedIntegers;
        }

        //判断是否int
        public Boolean isInteger() {
            if (integer != null && nestedIntegers == null) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }

        //判断是否数组
        public Boolean isNestedIntger() {
            if (integer == null && nestedIntegers != null) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }

        //Getter Setter
        public Integer getInteger() {
            return integer;
        }

        public void setInteger(Integer integer) {
            this.integer = integer;
            this.nestedIntegers = null;
        }

        public List<NestedInteger> getNestedIntegers() {
            return nestedIntegers;
        }

        public void setNestedIntegers(List<NestedInteger> nestedIntegers) {
            this.nestedIntegers = nestedIntegers;
            this.integer = null;
        }

        @Override
        public String toString() {
            return "NestedInteger{" +
                    "integer=" + integer +
                    ", nestedIntegers=" + nestedIntegers +
                    '}';
        }
       String st = "[NestedInteger{integer=1, nestedIntegers=null}," +
               "" +
               "" +
               " NestedInteger{integer=1, nestedIntegers=[NestedInteger{integer=4, nestedIntegers=null}," +
               "" +
               " NestedInteger{integer=4, nestedIntegers=[NestedInteger{integer=6, nestedIntegers=null}]}]}]\n";
    }

}

