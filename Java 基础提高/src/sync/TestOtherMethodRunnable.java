package sync;

public class TestOtherMethodRunnable implements Runnable {
    private boolean meathodA;
    private SyncObjectTest objectTest;

    public TestOtherMethodRunnable(SyncObjectTest objectTest,boolean meathodA) {
        this.meathodA = meathodA;
        this.objectTest = objectTest;
    }

    @Override
    public void run() {
        if (meathodA)
            objectTest.methodOtherA();
        else
            objectTest.methoOtherB();
    }
}
