package ArrayAMartix;

public class NaNTest {
    public static void main(String[] args) {
        Double aDouble = Double.valueOf(1.0 / 0.0); //浮点数相除
        System.out.println(aDouble);//1.8 之后Infinity Or 1.8 之前 NaN

        Double bDouble = Double.valueOf(1 / 0); //int 想÷
        System.out.println(bDouble);// java.lang.ArithmeticException: / by zero
    }
}
