package sync;

public class TestStaticMethodRunnable implements Runnable {
    private boolean meathodA;

    public TestStaticMethodRunnable(boolean meathodA) {
        this.meathodA = meathodA;
    }

    @Override
    public void run() {
        if (meathodA)
            SyncObjectTest.methodStaticA();
        else
            SyncObjectTest.methodStaticB();
    }
}
