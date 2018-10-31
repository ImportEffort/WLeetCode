package Set;

import java.util.HashSet;
import java.util.Set;

public class isHappyNums {

    public static void main(String[] args) {
        boolean happy = new isHappyNums().isHappy(19);
        System.out.println("happy = " + happy);
    }
    public boolean isHappy(int n) {
        HashSet<Integer> set = new HashSet<>();

        while( n != 1){
            n =  getNextHappyNum(n);

            System.out.println("next = " + n);

            if(set.contains(n)){
                return false;
            }else{
                set.add(n);
            }
        }
        return true;
    }

    private int getNextHappyNum(int num){
        int result = 0;
        while (num > 0) {
            result += (num % 10) * (num % 10);
            num = num / 10;
        }
        return result;
    }


}
