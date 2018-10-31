package DesignMode;

public class SingleTon {
    public static void main(String[] args) {
        EnumSingleTon instance = EnumSingleTon.INSTANCE;
        EnumSingleTon instance1 = EnumSingleTon.INSTANCE;

        System.out.println("instance1 == instance = " + (instance1 == instance));
    }
}
