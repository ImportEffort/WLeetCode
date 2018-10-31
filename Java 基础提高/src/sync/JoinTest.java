package sync;

public class JoinTest {


    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            int count = 10000;

            @Override
            public void run() {
                while (count-- != 0)
                    System.out.println("t1 在执行");
            }
        });

        Thread t2 = new Thread(new Runnable() {
            int count = 10000;

            @Override
            public void run() {
                while (count-- != 0)
                    System.out.println("t2 在执行");
            }
        });

        try {
            t1.start();
            t2.start();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
