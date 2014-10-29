package de.arnohaase.javastuff.conc_map_performance;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;


/**
 * @author arno
 */
public class MapPerformanceMain {
    public static void main (String[] args) throws InterruptedException {
        new MapTestBed (Collections.synchronizedMap (new HashMap<> ()), 20, 10, 10);
        new MapTestBed (new ConcurrentHashMap<> (), 20, 10, 10);

        doIt (Collections.synchronizedMap (new HashMap<Integer, Integer> ()), 10);
        doIt (Collections.synchronizedMap (new HashMap<Integer, Integer> ()), 100);
        doIt (Collections.synchronizedMap (new HashMap<Integer, Integer> ()), 1000);
        doIt (new ConcurrentHashMap<Integer, Integer> (), 10);
        doIt (new ConcurrentHashMap<Integer, Integer> (), 100);
        doIt (new ConcurrentHashMap<Integer, Integer> (), 1000);

        System.out.print ("#readers\t#writers");
        for (String run: runs) {
            System.out.print ("\t" + run);
        }
        System.out.println ();

        for (int r=1; r<20; r++) {
            for (int w=1; w<20; w++) {
                System.out.print (r + "\t" + w);
                for (String run: runs) {
                    System.out.print ("\t" + (100_000 / result.get (new ReadersWriters (r, w)).get (run)));
                }
                System.out.println ();
            }
        }
    }

    static final SortedSet<String> runs = new TreeSet<> ();
    static final Map<ReadersWriters, Map<String, Long>> result = new HashMap<> ();

    static void doIt (Map<Integer, Integer> map, int numKeys) throws InterruptedException {
        for (int numReaders = 1; numReaders < 20; numReaders++) {
            for (int numWriters = 1; numWriters < 20; numWriters++) {
                final long duration = new MapTestBed (new ConcurrentHashMap<>(), numKeys, numWriters, numReaders).duration;

                final ReadersWriters rw = new ReadersWriters (numReaders, numWriters);
                if (!result.containsKey (rw)) {
                    result.put (rw, new HashMap<> ());
                }
                final String run = map.getClass ().getSimpleName () + " " + numKeys;
                result.get (rw).put (run, duration); //MapTestBed.NUM_ITERATIONS / duration);
                runs.add (run);
            }
            System.out.print (".");
        }
        System.out.println ("!");
    }
}

class ReadersWriters {
    final int numReaders;
    final int numWriters;
    ReadersWriters (int numReaders, int numWriters) {
        this.numReaders = numReaders;
        this.numWriters = numWriters;
    }
    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;

        ReadersWriters that = (ReadersWriters) o;

        if (numReaders != that.numReaders) return false;
        if (numWriters != that.numWriters) return false;

        return true;
    }
    @Override
    public int hashCode () {
        int result = numReaders;
        result = 31 * result + numWriters;
        return result;
    }
}

class MapTestBed {
    static final int NUM_ITERATIONS = 5_000_000;

    private final Map<Integer, Integer> map;
    private final int numValues;
    private final int numWriters;
    private final int numReaders;

    public final long duration;

    MapTestBed (Map<Integer, Integer> map, int numKeys, int numWriters, int numReaders) throws InterruptedException {
        this.map = map;
        this.numValues = numKeys;
        this.numWriters = numWriters;
        this.numReaders = numReaders;

        map.clear ();
        for (int i=0; i<numKeys; i++) {
            map.put (i, i);
        }

        final CountDownLatch latch = new CountDownLatch (numReaders + numWriters);
        final long start = System.currentTimeMillis ();

        for (int i=0; i<numWriters; i++) {
            new Thread(() -> {
                int k=0;
                for (int j=0; j<NUM_ITERATIONS / numWriters; j++) {
                    doWrite (k++ % numKeys, k++);
                }
                latch.countDown ();
            }).start();
        }

        for (int i=0; i<numReaders; i++) {
            new Thread(() -> {
                int k=0;
                for (int j=0; j<NUM_ITERATIONS / numReaders; j++) {
                    doRead (k++ % numKeys);
                }
                latch.countDown ();
            }).start();
        }

        latch.await ();
        final long end = System.currentTimeMillis ();

        duration = end - start;
    }

    private void doWrite(int key, int value) {
        map.put (key, value);
    }

    private void doRead(int key) {
        map.get (key);
    }

}
