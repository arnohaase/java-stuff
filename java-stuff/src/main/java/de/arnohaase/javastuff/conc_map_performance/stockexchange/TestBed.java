package de.arnohaase.javastuff.conc_map_performance.stockexchange;

import de.arnohaase.javastuff.conc_map_performance.stockexchange.impl.AbstractSingleWorkerThreadStockExchange;
import javafx.util.Pair;

import java.security.SecureRandom;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @author arno
 */
public class TestBed {
    public static final Currency[] currencies = new Currency[] {
            Currency.getInstance ("USD"),
            Currency.getInstance ("CAD"),
            Currency.getInstance ("AUD"),
            Currency.getInstance ("CHF"),
            Currency.getInstance ("GBP"),
            Currency.getInstance ("DKK"),
            Currency.getInstance ("JPY"),
            Currency.getInstance ("INR"),
            Currency.getInstance ("MYR"),
            Currency.getInstance ("CNY")
    };

    public static final String[] wkzs = new String[] {
            "WK01",
            "WK02",
            "WK03",
            "WK04",
            "WK05",
            "WK06",
            "WK07",
            "WK08",
            "WK09",
            "WK10",
            "WK11",
            "WK12",
            "WK13",
            "WK14",
            "WK15",
            "WK16",
            "WK17",
            "WK18",
            "WK19",
            "WK20"
    };

    public static final int NUM_ITERATIONS = 100_000;

    private final StockExchange stockExchange;

    public TestBed (StockExchange stockExchange) {
        this.stockExchange = stockExchange;
        init(new LinkedList ());
    }

    public void measureWorkerThread (int numWriters, int numReaders) {
        final int numOpsPerIter = numWriters*10*30 + numReaders*200;
        final int loopLimit = 28_000_000 / numOpsPerIter;

        LinkedList l = new LinkedList ();
        for (int k=0; k<loopLimit; k++) {
            for (int i=0; i<10*numWriters; i++) {
                updateRates (l);
                updatePrices (l);
            }
            for (int i=0; i<numReaders; i++) {
                getPrices (l);
            }
        }

        for (int i=0; i<10; i++) {
            new Thread(() -> {
                final LinkedList ll = new LinkedList();
                while(true) updateRates(ll);
            }).start();
        }

        ((AbstractSingleWorkerThreadStockExchange) stockExchange).latch.countDown ();


        while (true) {
            updateRates (l);
        }
    }

    volatile boolean shutdown = false;

    volatile int barrier = 0;
    <T> T tunnel(LinkedList<T> l, T o) {
        l.add (o);
        barrier = barrier;
        return l.remove ();
    }

    public Pair<Long, Long> measureMultiThreaded (int numWriters, int numReaders, int numWriteWaits) throws InterruptedException {
        shutdown = false;

        final CountDownLatch latch = new CountDownLatch (numReaders + numWriters - 2);

        for (int i=0; i<numWriters-1; i++) {
            new Thread (() -> {
                LinkedList l = new LinkedList ();

                while (!shutdown) {
                    updateRates(l);
                    updatePrices (l);
                }
                latch.countDown ();
            }).start ();
        }
        for (int i=0; i<numReaders-1; i++) {
            new Thread (() -> {
                LinkedList l = new LinkedList ();
                while (!shutdown) {
                    getPrices (l);
                }
                latch.countDown ();
            }).start ();
        }

        final AtomicLong writeMillis = new AtomicLong ();
        final AtomicLong readMillis = new AtomicLong ();

        final AtomicInteger finishedCounter = new AtomicInteger ();

        final Thread writeMeasurer = new Thread (() -> {
            LinkedList l = new LinkedList ();
            writeLoop(l, numWriteWaits);
            final long start = System.currentTimeMillis ();
            writeLoop(l, numWriteWaits);
            writeMillis.set (System.currentTimeMillis () - start);
            finishedCounter.incrementAndGet ();
            while (finishedCounter.get () == 1) {
                updateRates (l);
                updatePrices (l);
            }
        });
        final Thread readMeasurer = new Thread (() -> {
            LinkedList l = new LinkedList ();
            readLoop (l);
            final long start = System.currentTimeMillis ();
            readLoop (l);
            readMillis.set (System.currentTimeMillis () - start);
            finishedCounter.incrementAndGet ();
            while (finishedCounter.get () == 1) {
                getPrices (l);
            }
        });

        writeMeasurer.start ();
        readMeasurer.start ();
        writeMeasurer.join ();
        readMeasurer.join ();
        shutdown = true;

        latch.await ();

        return new Pair<> (writeMillis.get (), readMillis.get ());
    }

    public Pair<Long, Long> measureSingleThreaded (LinkedList l) {
        final long start = System.currentTimeMillis ();
        writeLoop(l,0);
        final long middle = System.currentTimeMillis ();
        readLoop (l);
        final long end = System.currentTimeMillis ();
        return new Pair<> (middle-start, end-middle);
    }

    private void writeLoop(LinkedList l, int wait) {
        for (int i=0; i<NUM_ITERATIONS / (wait+1); i++) {
            updateRates(l);
            updatePrices (l);
            for (int c=0 ; c<wait;c++)
                new SecureRandom().nextBytes (new byte[100]);
        }
    }

    private void readLoop(LinkedList l) {
        for (int i=0; i<NUM_ITERATIONS; i++) {
            getPrices(l);
        }
    }

    private void init (LinkedList l) {
        updateRates (l);
        updatePrices (l);
    }

    private void updateRates(LinkedList l) { // performs 10*10 = 100 updates
        for (int i=0; i<1; i++) {
            for (Currency currency: currencies) {
                stockExchange.updateRate (tunnel (l, currency), .5 + .1*i);
            }
        }
    }

    private void updatePrices(LinkedList l) { // performs 20*10 = 200 updates
        for (int i=0; i<1; i++) {
            for (String wkz: wkzs) {
                stockExchange.updatePrice (tunnel (l, wkz), 10 + i);
            }
        }
    }

    private void getPrices(LinkedList l) { // performs 20 * 10 = 200 queries
        for (String wkz: wkzs) {
            for (Currency currency: currencies) {
                stockExchange.currentPrice (tunnel (l, wkz), currency);
            }
        }
    }
}




