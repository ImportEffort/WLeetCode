package sync;

public class TestDoubleSyncMethod {


    public static void main(String[] args) {
        SyncObjectTest objectTest = new SyncObjectTest();
//        Thread t1 = new Thread(new TestMethodRunnable(objectTest,false));
//        Thread t2 = new Thread(new TestMethodRunnable(objectTest,true));

        Thread t1 = new Thread(new TestOtherMethodRunnable(objectTest,false));
        Thread t2 = new Thread(new TestOtherMethodRunnable(objectTest,true));

//        Thread t1 = new Thread(new TestStaticMethodRunnable(false));
//        Thread t2 = new Thread(new TestStaticMethodRunnable(true));

        t1.start();
        t2.start();
    }
}
