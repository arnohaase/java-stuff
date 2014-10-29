package de.arnohaase.javastuff.conc_map_performance.stockexchange;

import javafx.util.Pair;

import java.util.Currency;
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
        init();
    }

    public void measureWorkerThread (int numWriters, int numReaders) {
        for (int k=0; k<1_000_000; k++) {
            for (int i=0; i<10*numWriters; i++) {
                updateRates ();
                updatePrices ();
            }
            for (int i=0; i<numReaders; i++) {
                getPrices ();
            }
        }
    }

    volatile boolean shutdown = false;

    public Pair<Long, Long> measureMultiThreaded (int numWriters, int numReaders) throws InterruptedException {
        shutdown = false;

        final CountDownLatch latch = new CountDownLatch (numReaders + numWriters - 2);

        for (int i=0; i<numWriters-1; i++) {
            new Thread (() -> {
                while (!shutdown) {
                    updateRates();
                    updatePrices ();
                }
                latch.countDown ();
            }).start ();
        }
        for (int i=0; i<numReaders-1; i++) {
            new Thread (() -> {
                while (!shutdown) {
                    getPrices ();
                }
                latch.countDown ();
            }).start ();
        }

        final AtomicLong writeMillis = new AtomicLong ();
        final AtomicLong readMillis = new AtomicLong ();

        final AtomicInteger finishedCounter = new AtomicInteger ();

        final Thread writeMeasurer = new Thread (() -> {
            writeLoop();
            final long start = System.currentTimeMillis ();
            writeLoop();
            writeMillis.set (System.currentTimeMillis () - start);
            finishedCounter.incrementAndGet ();
            while (finishedCounter.get () == 1) {
                updateRates ();
                updatePrices ();
            }
        });
        final Thread readMeasurer = new Thread (() -> {
            readLoop ();
            final long start = System.currentTimeMillis ();
            readLoop ();
            readMillis.set (System.currentTimeMillis () - start);
            finishedCounter.incrementAndGet ();
            while (finishedCounter.get () == 1) {
                getPrices ();
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

    public Pair<Long, Long> measureSingleThreaded () {
        final long start = System.currentTimeMillis ();
        writeLoop();
        final long middle = System.currentTimeMillis ();
        readLoop ();
        final long end = System.currentTimeMillis ();
        return new Pair<> (middle-start, end-middle);
    }

    private void writeLoop() {
        for (int i=0; i<NUM_ITERATIONS; i++) {
            updateRates();
            updatePrices ();
        }
    }

    private void readLoop() {
        for (int i=0; i<NUM_ITERATIONS; i++) {
            getPrices();
        }
    }

    private void init () {
        updateRates ();
        updatePrices ();
    }

    private void updateRates() { // performs 10*10 = 100 updates
        for (int i=0; i<1; i++) {
            for (Currency currency: currencies) {
                stockExchange.updateRate (currency, .5 + .1*i);
            }
        }
    }

    private void updatePrices() { // performs 20*10 = 200 updates
        for (int i=0; i<1; i++) {
            for (String wkz: wkzs) {
                stockExchange.updatePrice (wkz, 10 + i);
            }
        }
    }

    private void getPrices() { // performs 20 * 10 = 200 queries
        for (String wkz: wkzs) {
            for (Currency currency: currencies) {
                stockExchange.currentPrice (wkz, currency);
            }
        }
    }
}




