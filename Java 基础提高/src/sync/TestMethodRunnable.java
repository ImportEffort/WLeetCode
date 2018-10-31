package sync;

public class TestMethodRunnable implements Runnable {
    private SyncObjectTest objectTest;
    private boolean meathodA;

    public TestMethodRunnable(SyncObjectTest objectTest, boolean meathodA) {
        this.objectTest = objectTest;
        this.meathodA = meathodA;
    }

    @Override
    public void run() {
        if (meathodA)
            objectTest.methodA();
        else
            objectTest.methodB();
    }
}
