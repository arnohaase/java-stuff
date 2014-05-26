package de.arnohaase.javastuff.non_atomic_increment;


import java.util.concurrent.CountDownLatch;

/**
 * @author arno
 */
public class NonAtomicIncMain {
    static int a;
    static volatile int b;

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(4);

        for(int i=0; i<4; i++) {
            new Thread(new Runnable() {
                @Override public void run() {
                    for(int j=0; j<500_000; j++) {
                        a++;
                        b++;
                    }
//                    latch.countDown();

                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
//        latch.await();

        Thread.sleep(2000);

        System.out.println(a);
        System.out.println(b);
    }
}
