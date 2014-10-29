package de.arnohaase.javastuff.non_atomic_assember_instruction;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author arno
 */
public class NonAtomicAssemberInstructionMain {
    private static volatile int a;
    private static AtomicInteger b = new AtomicInteger();

    static class A implements Runnable {
        @Override public void run() {
            while (true) {
                a++;
                b.incrementAndGet();
                a--;
                b.decrementAndGet();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for(int i=0; i<10; i++) {
            new Thread(new A()).start();
        }

        Thread.sleep (1000);

        while (true) {
            System.out.println(Math.abs (a) + ", " + Math.abs (b.intValue()));
        }
    }
}

