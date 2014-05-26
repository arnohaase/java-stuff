package de.arnohaase.javastuff.conc_count_and_sum;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author arno
 */
public class CountAndSumMain {
    private static final int NUM_THREADS = 10;
    private static final int NUM_KEYS = 10;
    private static final int NUM_READS = 10;

    static final Map<String, CountAndSumSync> mapSync = new ConcurrentHashMap<>();
    static final Map<String, AtomicReference<CountAndSumFinal>> mapRef = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        System.out.println("#threads: " + NUM_THREADS + ", #keys: " + NUM_KEYS + ", #reads: " + NUM_READS);
        doSync();
        doLockFree();
        doSync();
        doLockFree();
    }

    private static void updateSynchronized(String key, int value) {
        if(mapSync.containsKey(key)) {
            mapSync.get(key).add(value);
        }
        else {
            // race condition in case of concurrent 'first' put - we ignore that here
            mapSync.put(key, new CountAndSumSync(value));
        }
    }

    private static void updateLockFree(String key, int value) {
        if(mapRef.containsKey(key)) {
            final AtomicReference<CountAndSumFinal> ref = mapRef.get(key);
            CountAndSumFinal prev;
            do {
                prev = ref.get();
            }
            while(! ref.compareAndSet(prev, prev.withNewValue(value)));
        }
        else {
            // race condition in case of concurrent 'first' put - we ignore that here
            mapRef.put(key, new AtomicReference<>(new CountAndSumFinal(value)));
        }
    }

    public static void doLockFree() throws Exception {
        final CountDownLatch latch = new CountDownLatch(NUM_THREADS);

        for(int i=0; i<NUM_THREADS; i++) {
            new MapThread(latch) {
                @Override void read(String key) {
                    mapRef.get(key).get().getAverage();
                }

                @Override void write(String key, int value) {
                    updateLockFree(key, value);
                }
            }.start();
        }

        final long start = System.nanoTime();
        latch.await();
        System.out.println("lock free: " + NumberFormat.getIntegerInstance().format(System.nanoTime() - start) + "ns");
    }

    public static void doSync() throws Exception {
        final CountDownLatch latch = new CountDownLatch(NUM_THREADS);

        for(int i=0; i<NUM_THREADS; i++) {
            new MapThread(latch) {
                @Override void read(String key) {
                    mapSync.get(key).getAverage();
                }

                @Override void write(String key, int value) {
                    updateSynchronized(key, value);
                }
            }.start();
        }

        final long start = System.nanoTime();
        latch.await();
        System.out.println("synchronized: " + NumberFormat.getIntegerInstance().format(System.nanoTime() - start) + "ns");
    }

    abstract static class MapThread extends Thread {
        private final CountDownLatch latch;
        private final Random rand = new Random();

        MapThread(CountDownLatch latch) {
            this.latch = latch;
        }

        abstract void read(String key);
        abstract void write(String key, int value);

        @Override
        public void run() {
            for(int i=0; i<1_000_000; i++) { // 1.000.000 is enough to dwarf warm-up effects on my machine
                try {
                    for(int j=0; j<NUM_READS; j++) {
                        read("key-" + rand.nextInt(NUM_KEYS));
                    }
                } catch (NullPointerException e) { //
                }

                final String key = "key-" + rand.nextInt(NUM_KEYS);
                write(key, i);
            }
            latch.countDown();
        }
    }
}

class CountAndSumSync {
//    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

    private int count = 1;
    private int sum;

    public CountAndSumSync(int initialValue) {
        sum = initialValue;
    }

//    public void add(int value) {
//        lock.writeLock().lock();
//        try {
//            count += 1;
//            sum += value;
//        }
//        finally {
//            lock.writeLock().unlock();
//        }
//    }
//
//    // Unschärfe durch separate Getter: hier uninteressant, einfach lösbar durch gemeinsamen Getter
//    public int getCount() {
//        lock.readLock().lock();
//        try {
//            return count;
//        }
//        finally {
//            lock.readLock().unlock();
//        }
//    }
//
//    public int getSum() {
//        lock.readLock().lock();
//        try {
//            return sum;
//        }
//        finally {
//            lock.readLock().unlock();
//        }
//    }
//
//    public synchronized double getAverage() {
//        lock.readLock().lock();
//        try {
//            if(count == 0) return 0;
//            return sum / count;
//        }
//        finally {
//            lock.readLock().unlock();
//        }
//    }
    public synchronized void add(int value) {
        count += 1;
        sum += value;
    }

    // Unschärfe durch separate Getter: hier uninteressant, einfach lösbar durch gemeinsamen Getter
    public synchronized int getCount() {
        return count;
    }

    public synchronized int getSum() {
        return sum;
    }

    public synchronized double getAverage() {
        if(count == 0) return 0;
        return sum / count;
    }
}

class CountAndSumFinal {
    private final int count;
    private final int sum;

    CountAndSumFinal(int initialValue) {
        this(1, initialValue);
    }

    private CountAndSumFinal(int count, int sum) {
        this.count = count;
        this.sum = sum;
    }

    CountAndSumFinal withNewValue(int value) {
        return new CountAndSumFinal(count + 1, sum + value);
    }

    int getCount() {
        return count;
    }

    int getSum() {
        return sum;
    }

    double getAverage() {
        if(count == 0) return 0;
        return 1.0 * sum / count;
    }
}
