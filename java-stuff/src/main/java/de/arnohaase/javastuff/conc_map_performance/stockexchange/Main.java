package de.arnohaase.javastuff.conc_map_performance.stockexchange;

import de.arnohaase.javastuff.conc_map_performance.stockexchange.impl.*;
import javafx.util.Pair;

import java.text.NumberFormat;


/**
 * @author arno
 */
public class Main {
    public static void main (String[] args) throws InterruptedException {

        try {
//        testSingleThreaded (stockExchange);

            // run any number of these in a single run

//            testMultiThreaded (new SynchronizedStockExchange ());
//            testMultiThreaded (new ReadWriteLockStockExchange ());
//            testMultiThreaded (new ConcHashMapStockExchange ());
//            testMultiThreaded (new SyncHashMapStockExchange ());
//            testMultiThreaded (new AMapStockExchange ());
//            testMultiThreaded (new AMapLossyStockExchange ());

            // only one of these can be run safely in a single process, process must be killed manually

            testWorkerThread (new SingleWorkerThreadRingBufferStockExchange ());
//            testWorkerThread (new SingleWorkerThreadLinkedBlockingDequeStockExchange());
        }
        catch(Exception exc) {
            exc.printStackTrace ();
        }
        finally {
            System.exit (0);
        }
    }

    static final int NUM_WRITERS = 1;
    static final int NUM_READERS = 10;

    static void testWorkerThread (StockExchange stockExchange) {
        System.out.println (stockExchange.getClass ().getSimpleName () + ", " + NUM_WRITERS + " writers, " + NUM_READERS + " readers");
        new TestBed (stockExchange).measureWorkerThread (NUM_WRITERS, NUM_READERS);
    }

    static void testMultiThreaded (StockExchange stockExchange) throws InterruptedException {
        final TestBed testBed = new TestBed (stockExchange);

        System.out.println (stockExchange.getClass ().getSimpleName () + "\t\tw/s\tr/s");
        for (int i=1; i<20; i++) {
            doMeasure (testBed, i, i);
        }
        for (int i=1; i<20; i++) {
            doMeasure (testBed, 1, i);
        }
        for (int i=1; i<20; i++) {
            doMeasure (testBed, i, 1);
        }
    }

    static void doMeasure (TestBed testBed, int numWriters, int numReaders) throws InterruptedException {
        final Pair<Long, Long> duration = testBed.measureMultiThreaded (numWriters, numReaders);
        System.out.println (numWriters + "\t" + numReaders + "\t" +
                formatted (TestBed.NUM_ITERATIONS *  30 * 1000L * numWriters / duration.getKey   ()) + "\t" +
                formatted (TestBed.NUM_ITERATIONS * 200 * 1000L * numReaders / duration.getValue ()));
    }

    static String formatted (long n) {
        return NumberFormat.getIntegerInstance ().format (n);
    }

    static void testSingleThreaded (StockExchange stockExchange) {
        final TestBed testBed = new TestBed (stockExchange);
        testBed.measureSingleThreaded (); // warm up

        final Pair<Long, Long> duration = testBed.measureSingleThreaded ();
        System.out.println (formatted (duration.getKey ()) + " ms / " + formatted (duration.getValue ()) + " ms");
    }
}

/*

Diagramme:
* 1 - 10 r/w, Schreibdurchsatz, Single Threaded, Worker Thread
* 1 - 10 r/w, Lesedurchsatz,    Single Threaded, Worker Thread
* 1:10 --> 5:5 --> 10:1



single threaded: 1.000.000 beide updates (d.h. je 10 bzw. 20), 1.000.000 mal alle currentPrices (d.h. je 200)

SingleThreadedStockExchange single threaded: 2,5 s + 2,1 s
    --> bei dieser Lastverteilung:
         30.000.000 Updates  in 4,6 Sekunden =  6.500.000 Updates  / s
        200.000.000 Abfragen in 4,6 Sekunden = 43.000.000 Abfragen / s

SynchronizedStockExchange single threaded: 9,5 s + 5,7 s

ConcHashMapStockExchange single threaded: 10,2 s + 1,6 s

SyncHashMapStockExchange single threaded: 9,2 s + 9,6 s


ACHTUNG: ALLE DURCHSÄTZE SIND JE THREAD!!!


ConcHashMapStockExchange, 1 writers, 1 readers
21.239 ms / 3.884 ms
1.412.495 writes/s
51.493.305 reads/s

ConcHashMapStockExchange, 2 writers, 2 readers
24.119 ms / 4.270 ms
1.243.832 writes/s
46.838.407 reads/s

ConcHashMapStockExchange, 4 writers, 4 readers, 1.000.000 iterations
45.945 ms / 6.186 ms
652.954 writes/s
32.331.070 reads/s

ConcHashMapStockExchange, 5 writers, 5 readers, 100000 iterations
6.001 ms / 473 ms
499.916 writes/s
42.283.298 reads/s

ConcHashMapStockExchange, 8 writers, 8 readers, 100000 iterations
16.183 ms / 882 ms
185.379 writes/s
22.675.736 reads/s

ConcHashMapStockExchange, 1 writers, 10 readers, 100.000 iterations
6.095 ms / 496 ms
492.206 writes/s
40.322.580 reads/s

ConcHashMapStockExchange, 10 writers, 1 readers, 100.000 iterations
8.759 ms / 465 ms
342.504 writes/s
43.010.752 reads/s


SynchronizedStockExchange, 1 writers, 1 readers, 100.000 iterations
4.003 ms / 3.462 ms
749.437 writes/s
5.777.007 reads/s

SynchronizedStockExchange, 2 writers, 2 readers, 100.000 iterations
9.100 ms / 6.312 ms
329.670 writes/s
3.168.567 reads/s

SynchronizedStockExchange, 4 writers, 4 readers, 100.000 iterations
16.484 ms / 11.343 ms
181.994 writes/s
1.763.201 reads/s

SynchronizedStockExchange, 5 writers, 5 readers, 100.000 iterations
20.020 ms / 15.162 ms
149.850 writes/s
1.319.087 reads/s

SynchronizedStockExchange, 8 writers, 8 readers, 100.000 iterations
36.404 ms / 22.041 ms
82.408 writes/s
907.399 reads/s

SynchronizedStockExchange, 10 writers, 1 readers, 100.000 iterations
25.894 ms / 16.329 ms
115.856 writes/s
1.224.814 reads/s

SynchronizedStockExchange, 1 writers, 10 readers, 100.000 iterations
26.044 ms / 16.243 ms
115.189 writes/s
1.231.299 reads/s



SyncHashMapStockExchange, 1 writers, 1 readers, 10.000 iterations
534 ms / 912 ms
561.797 writes/s
2.192.982 reads/s

SyncHashMapStockExchange, 2 writers, 2 readers, 100.000 iterations
5.637 ms / 14.965 ms
532.197 writes/s
1.336.451 reads/s

SyncHashMapStockExchange, 4 writers, 4 readers, 10.000 iterations
860 ms / 1.315 ms
348.837 writes/s
1.520.912 reads/s

SyncHashMapStockExchange, 5 writers, 5 readers, 100.000 iterations
11.420 ms / 33.361 ms
262.697 writes/s
599.502 reads/s

SyncHashMapStockExchange, 8 writers, 8 readers, 10.000 iterations
2.127 ms / 5.957 ms
141.043 writes/s
335.739 reads/s

SyncHashMapStockExchange, 1 writers, 10 readers, 100.000 iterations
6.812 ms / 25.703 ms
440.399 writes/s
778.119 reads/s

SyncHashMapStockExchange, 10 writers, 1 readers, 100.000 iterations
17.499 ms / 55.095 ms
171.438 writes/s
363.009 reads/s


AMapStockExchange, 1 writers, 1 readers, 100.000 iterations
3.967 ms / 1.482 ms
756.238 writes/s
13.495.276 reads/s

AMapStockExchange, 2 writers, 2 readers, 100.000 iterations
8.986 ms / 1.996 ms
333.852 writes/s
10.020.040 reads/s

AMapStockExchange, 4 writers, 4 readers, 100.000 iterations
21.160 ms / 2.448 ms
141.776 writes/s
8.169.934 reads/s

AMapStockExchange, 5 writers, 5 readers, 100.000 iterations
26.788 ms / 2.829 ms
111.990 writes/s
7.069.635 reads/s

AMapStockExchange, 8 writers, 8 readers, 100.000 iterations
45.768 ms / 4.266 ms
65.547 writes/s
4.688.232 reads/s

AMapStockExchange, 1 writers, 10 readers, 100.000 iterations
7.222 ms / 2.129 ms
415.397 writes/s
9.394.081 reads/s

AMapStockExchange, 10 writers, 1 readers, 100.000 iterations
62.368 ms / 3.738 ms
48.101 writes/s
5.350.454 reads/s



AMapLossyStockExchange, 1 writers, 1 readers, 100.000 iterations
3.758 ms / 1.351 ms
798.296 writes/s
14.803.849 reads/s

AMapLossyStockExchange, 2 writers, 2 readers, 100.000 iterations
4.965 ms / 2.063 ms
604.229 writes/s
9.694.619 reads/s

AMapLossyStockExchange, 4 writers, 4 readers, 100.000 iterations
6.663 ms / 3.045 ms
450.247 writes/s
6.568.144 reads/s

AMapLossyStockExchange, 5 writers, 5 readers, 100.000 iterations
7.721 ms / 3.883 ms
388.550 writes/s
5.150.656 reads/s

AMapLossyStockExchange, 8 writers, 8 readers, 100.000 iterations
14.444 ms / 4.664 ms
207.698 writes/s
4.288.164 reads/s

AMapLossyStockExchange, 10 writers, 1 readers, 100.000 iterations
9.666 ms / 5.146 ms
310.366 writes/s
3.886.513 reads/s

AMapLossyStockExchange, 1 writers, 10 readers, 100.000 iterations
8.308 ms / 1.808 ms
361.097 writes/s
11.061.946 reads/s



ReadWriteLockStockExchange, 1 writers, 1 readers, 100.000 iterations
6.573 ms / 10.588 ms
456.412 writes/s
1.888.930 reads/s

ReadWriteLockStockExchange, 2 writers, 2 readers, 100.000 iterations
7.602 ms / 13.895 ms
394.632 writes/s
1.439.366 reads/s

ReadWriteLockStockExchange, 4 writers, 4 readers, 100.000 iterations
15.372 ms / 24.109 ms
195.160 writes/s
829.565 reads/s

ReadWriteLockStockExchange, 5 writers, 5 readers, 100.000 iterations
22.151 ms / 28.036 ms
135.434 writes/s
713.368 reads/s

ReadWriteLockStockExchange, 8 writers, 8 readers, 100.000 iterations
54.778 ms / 38.966 ms
54.766 writes/s
513.267 reads/s

ReadWriteLockStockExchange, 10 writers, 1 readers, 1.000 iterations
235 ms / 9.425 ms
127.659 writes/s
21.220 reads/s

ReadWriteLockStockExchange, 1 writers, 10 readers, 1.000 iterations
48.258 ms / 393 ms
621 writes/s
508.905 reads/s

=========================================================

SingleWorkerThreadLinkedBlockingDequeStockExchange, 2 writers, 2 readers
6.000.000 writes, 4.000.000 reads in 8.756 ms
685.244 updates / s
456.829 reads / s

SingleWorkerThreadLinkedBlockingDequeStockExchange, 10 writers, 1 readers
9.375.000 writes, 625.000 reads in 5.596 ms
1.675.303 updates / s
111.686 reads / s

SingleWorkerThreadLinkedBlockingDequeStockExchange, 1 writers, 10 readers
1.304.400 writes, 8.695.600 reads in 4.091 ms
318.846 updates / s
2.125.543 reads / s

---------------------------------------------------------

WITH PROCESSOR CACHE PREFETCH

SingleWorkerThreadLinkedBlockingDequeStockExchange, 2 writers, 2 readers
6.000.000 writes, 4.000.000 reads in 3.652 ms
1.642.935 updates / s
1.095.290 reads / s

SingleWorkerThreadLinkedBlockingDequeStockExchange, 1 writers, 10 readers
1.304.400 writes, 8.695.600 reads in 3.892 ms
335.149 updates / s
2.234.224 reads / s

SingleWorkerThreadLinkedBlockingDequeStockExchange, 10 writers, 1 readers
9.375.000 writes, 625.000 reads in 4.948 ms
1.894.704 updates / s
126.313 reads / s

=========================================================

SingleWorkerThreadRingBufferStockExchange, 2 writers, 2 readers
6.000.000 writes, 4.000.000 reads in 1.546 ms
3.880.000 updates / s
2.587.000 reads / s

SingleWorkerThreadRingBufferStockExchange, 10 writers, 1 readers
9.375.000 writes, 625.000 reads in 1.619 ms
5.790.000 updates / s
386.000 reads / s

SingleWorkerThreadRingBufferStockExchange, 1 writers, 10 readers
1.304.400 writes, 8.695.600 reads in 1.879 ms
694.000 updates / s
4.627.000 reads / s

----------------------------------------------------------

WITH PROCESSOR CACHE PREFETCH

SingleWorkerThreadRingBufferStockExchange, 2 writers, 2 readers
6.000.000 writes, 4.000.000 reads in 1.780 ms
3.370.786 updates / s
2.247.191 reads / s

SingleWorkerThreadRingBufferStockExchange, 10 writers, 1 readers
9.375.000 writes, 625.000 reads in 1.660 ms
5.647.590 updates / s
376.506 reads / s

SingleWorkerThreadRingBufferStockExchange, 1 writers, 10 readers
1.304.400 writes, 8.695.600 reads in 1.939 ms
672.717 updates / s
4.484.579 reads / s

 */

