package DesignMode;

public class SyncSingleTon {

    private static SyncSingleTon singleTon = null;

    public static SyncSingleTon getInstance() {

        //这次判空是避免了，保证的多线程只有第一次调用getInstance 的时候才会加锁初始化
        if (singleTon == null) {
            synchronized (SyncSingleTon.class) {
                if (singleTon == null) {
                    singleTon = new SyncSingleTon();
                }
            }
        }
        return singleTon;
    }

    private SyncSingleTon() {
    }
}
