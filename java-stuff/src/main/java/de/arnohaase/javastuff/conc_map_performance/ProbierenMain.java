package de.arnohaase.javastuff.conc_map_performance;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author arno
 */
public class ProbierenMain {
    static final int NUM_READ_ITERATIONS  = 200_000;
    static final int NUM_WRITE_ITERATIONS = 200_000;

    static final int NUM_WRITERS = 1;
    static final int NUM_READERS = 10;

    static final int NUM_KEYS = 20;

    public static void main (String[] args) throws Exception {
        final Map<Integer, Integer> map = Collections.synchronizedMap (new HashMap<> ());
//        final Map<Integer, Integer> map = new ConcurrentHashMap<> ();

        long start = System.currentTimeMillis ();

        final CountDownLatch latch = new CountDownLatch (NUM_WRITERS + NUM_READERS);

        for (int i=0; i<NUM_WRITERS; i++) {
            final int offset = i*NUM_KEYS*NUM_WRITE_ITERATIONS;
            new Thread(() -> {
                write (map, offset);
                latch.countDown ();
            }).start();
        }
        for (int i=0; i<NUM_READERS; i++) {
            final int offset = i*NUM_KEYS*NUM_READ_ITERATIONS;
            new Thread(() -> {
                read (map, offset);
                latch.countDown ();
            }).start ();
        }

        latch.await ();

        final long end = System.currentTimeMillis ();
        System.out.println ((end - start) + "ms");
    }

    static void write(Map<Integer, Integer> map, int keyOffset) {
        for (int i=0; i<NUM_WRITE_ITERATIONS / NUM_WRITERS; i++) {
            for (int key=0; key < NUM_KEYS; key++) {
                map.put (keyOffset++, keyOffset);
            }
        }
    }

    static void read(Map<Integer, Integer> map, int keyOffset) {
        for (int i=0; i<NUM_READ_ITERATIONS / NUM_READERS; i++) {
            for (int key=0; key < NUM_KEYS; key++) {
                map.get (keyOffset++);
            }
        }
    }
}

// feste 20 keys, 5.000.000 Iterationen:

// HashMap mit write(); read():         1.452 ms

// synchronizedMap mit write(); read():  5.522 ms
//  1w,  1r:                            14.238 ms
// 10w, 10r:                            12.738 ms
// 20w, 20r:                            12.833 ms

// ConcHashMap mit write(); read():      3.463 ms
// #w:  1, #r:  1 :                      3.561 ms
// #w: 10, #r: 10 :                      2.690 ms
// #w:  1, #r: 10 :                      3.225 ms
// #w: 10, #r:  1 :                      2.812 ms
// 20w, 20r:                             2.602 ms

// jeder key nur einmal, 200.000 Iterationen (mal je 20 Zugriffe)

// sync
// 10w, 10r:     7.459 ms
// 1w, 10r:      5.127 ms

// conc
// 10w, 10r:     6.946 ms
// 1w, 10r:      4.950 ms
