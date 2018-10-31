package DesignMode;

public class VolatileSingleTon {

    //使用 Volatile 保证了指令重排序在这个对象创建的时候不可用
    private volatile static  VolatileSingleTon singleTon = null;

    public static VolatileSingleTon getInstance() {


        if (singleTon == null) {
            synchronized (VolatileSingleTon.class) {
                if (singleTon == null) {
                    singleTon = new VolatileSingleTon();
                }
            }
        }
        return singleTon;
    }

    private VolatileSingleTon() {
    }
}
