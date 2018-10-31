package DesignMode;

public class LazyBasicSingleTon {

    private static LazyBasicSingleTon singleTon = null;

    public static LazyBasicSingleTon getInstance() {
        //延迟初始化
        if (singleTon == null) {
            singleTon = new LazyBasicSingleTon();
        }

        return singleTon;
    }

    private LazyBasicSingleTon() {
    }
}
