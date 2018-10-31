package sync;

public class SyncObjectTest {

    public synchronized void methodA() {
        System.out.println("A 获得执行");
        while (true) {
        }
    }

    public synchronized void methodB() {
        System.out.println("B 获得执行");
        while (true) {
        }
    }


    public synchronized static void methodStaticA() {
        System.out.println("A 获得执行");
        while (true) {
        }
    }

    public static synchronized void methodStaticB() {
        System.out.println("B 获得执行");
        while (true) {
        }
    }

    Object lock = new Object();

    public void methodOtherA() {
        synchronized (lock) {
            System.out.println("A 获得执行");
            while (true) {
            }
        }
    }

    public void methoOtherB() {
        synchronized (lock) {
            System.out.println("B 获得执行");
            while (true) {
            }
        }
    }
}
