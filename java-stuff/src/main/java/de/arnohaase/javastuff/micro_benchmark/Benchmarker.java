package de.arnohaase.javastuff.micro_benchmark;

import java.text.NumberFormat;
import java.util.Random;
import java.util.UUID;

/**
 * @author arno
 */
public abstract class Benchmarker {
    private volatile boolean isMeasuring = false;

    public Benchmarker() {
        warmup();
    }

    private void warmup() {
        loop(10000);
    }

    public void dumpMeasure() {
        System.out.println(NumberFormat.getIntegerInstance().format(measure()) + "ns");
    }

    public int measure() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) { //
        }
        startBackgroundLoad();
        final long numIter = estimateNumIter();
        final long result = loop(numIter) / numIter;
        endBackgroundLoad();
        return (int) result;
    }

    private long estimateNumIter() {
        final long singleDuration = loop(10000) / 10000;
        // iterations for twenty seconds
        return 20L * 1000 * 1000 * 1000 / singleDuration;
    }

    private void endBackgroundLoad() {
        isMeasuring = false;
    }

    private void startBackgroundLoad() {
        final int numProcessors = Runtime.getRuntime().availableProcessors();

        isMeasuring = true;
        for(int i=0; i < numProcessors-2; i++) {
            new Thread() {
                @Override public void run() {
                    while(isMeasuring) {
                        UUID.randomUUID();
//                        new Random().nextGaussian();
                    }
                }
            }.start();
        }
    }

    static void thrash() {
        for(int i=0; i<10000000; i++) {
            new Random().nextGaussian();
        }
    }

    private long loop(long numIter) {
        final long start = System.nanoTime();
        for(long i=0; i<numIter; i++) {
            doIt();
        }
        final long end = System.nanoTime();
        return end - start;
    }

    public abstract void doIt();

    public static void main(String[] args) throws InterruptedException {
        {
            for(int i=0; i<1_000_000; i++)
                UUID.randomUUID();
//            Thread.sleep(1000);
//            thrash();
//            for(int i=0; i<10000; i++)
//                UUID.randomUUID();

            thrash();
            final long start = System.nanoTime();
//            UUID.randomUUID(); // method call costs (!) --> call site is not JITted here

            for(int i=0; i<100000; i++) {
                UUID.randomUUID();
            }
            final long end = System.nanoTime();


            System.out.println((end - start) / 100000);
        }
        System.out.println("--");

        new Benchmarker() {
            @Override public void doIt() {
                UUID.randomUUID();
            }
        }.dumpMeasure();
    }
}
