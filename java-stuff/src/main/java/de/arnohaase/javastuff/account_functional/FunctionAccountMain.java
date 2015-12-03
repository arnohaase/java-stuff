package de.arnohaase.javastuff.account_functional;

import java.text.NumberFormat;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @author arno
 */
public class FunctionAccountMain {
    public static void main (String[] args) throws InterruptedException {
        final Bank bank = new Bank ();

        for (int i=0; i<500_000; i++) {
            bank.deposit (String.valueOf(i), 200);
        }

        final int numReaders = 1;
        final int numWriters = 1;

        final AtomicLong writeRate = new AtomicLong ();
        final AtomicLong readRate = new AtomicLong ();

        final CountDownLatch latch = new CountDownLatch (numReaders + numWriters);

        for (int i=0; i<numWriters; i++) {
            new Thread (() -> {
                final Random rand = new Random ();
                int count = 0;
                long start = 0;
                while (true) { // maintain load until all threads are finished
                    try {
                        bank.transfer (
                                String.valueOf (rand.nextInt (10_000)),
                                String.valueOf (rand.nextInt (10_000)),
                                rand.nextInt (10));

                        // we count only successful transfers

                        if (count == 10_000) {
                            start = System.currentTimeMillis ();
                        }
                        if (count == 20_000) {
                            final long duration = System.currentTimeMillis () - start;
                            writeRate.addAndGet (10_000_000 / duration);
                            latch.countDown ();
                        }

                        count += 1;

                    }
                    catch (Exception e) {
                    }
                }
            }).start ();
        }

        for (int i=0; i<numReaders; i++) {
            new Thread(() -> {
                long start = 0;
                int count = 0;
                while (true) {
                    if (count == 100) {
                        start = System.currentTimeMillis ();
                    }
                    if (count == 200) {
                        final long duration = System.currentTimeMillis () - start;
                        readRate.addAndGet (100_000 / duration);
                        latch.countDown ();
                    }
                    count += 1;

                    bank.balance ();
                    if (bank.balance () != 100_000_000) {
                        System.out.println ("!!!!!!!!!!!!!!!!!!");
                    }
                }
            }).start ();
        }

        latch.await ();

        System.out.println (NumberFormat.getIntegerInstance ().format (writeRate.get ()) + " writes/s");
        System.out.println (NumberFormat.getIntegerInstance ().format (readRate.get ()) + " reads/s");
        System.exit(0);
    }
}

// 1 writer, n readers: 1-3 linear, bump at 4, then linear to 7, then shared saturation (--> 20)
// 0 readers (wg. Dauer), n writers: 1 -> 2 speedup, 3- saturation    (100_000 write iterations --> interactive results)