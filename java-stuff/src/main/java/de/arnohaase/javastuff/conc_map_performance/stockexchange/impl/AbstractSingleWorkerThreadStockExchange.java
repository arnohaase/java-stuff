package de.arnohaase.javastuff.conc_map_performance.stockexchange.impl;

import de.arnohaase.javastuff.conc_map_performance.stockexchange.StockExchange;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;


/**
 * @author arno
 */
public class AbstractSingleWorkerThreadStockExchange implements StockExchange {
    private final StockExchange inner = new SingleThreadedStockExchange ();

    private final BlockingQueue<Runnable> queue;

    private long startMeasure;
    private long numWritesAtStart;
    private long numReadsAtStart;

    private int numWrites = 0;
    private int numReads = 0;

    public AbstractSingleWorkerThreadStockExchange (BlockingQueue<Runnable> queue) {
        this.queue = queue;
        new Thread(() -> {
            while (true) {
                try {
                    queue.take ().run ();
                }
                catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            }
        }).start();
    }

    private final Map<Object, Runnable> cache = new HashMap<> ();

    @Override public void updateRate (Currency currency, double fromEuro) {
        if (! cache.containsKey (currency)) {
            cache.put (currency, () -> {
                inner.updateRate (currency, fromEuro);
                numWrites += 1;
                measure ();
            });
        }

        enqueue (cache.get (currency));
    }

    @Override public void updatePrice (String wkz, double euros) {
        if (! cache.containsKey (wkz)) {
            cache.put (wkz, () -> {
                inner.updatePrice (wkz, euros);
                numWrites += 1;
                measure ();
            });
        }

        enqueue (cache.get (wkz));
    }

    @Override public double currentPrice (String wkz, Currency currency) {
        final String key = wkz + currency;

        if (! cache.containsKey (key)) {
            cache.put (key, () -> {
                inner.currentPrice (wkz, currency);
                numReads += 1;
                measure ();
            });
        }

        enqueue (cache.get (key));
        return 0;
    }

    private void measure() {
        if (numReads + numWrites == 10_000_000) {
            startMeasure = System.currentTimeMillis ();
            numWritesAtStart = numWrites;
            numReadsAtStart = numReads;
        }
        if (numReads + numWrites == 20_000_000) {
            final long duration = System.currentTimeMillis () - startMeasure;
            System.out.println (formatted (numWrites-numWritesAtStart) + " writes, " + formatted (numReads-numReadsAtStart) + " reads in " + formatted (duration) + " ms");
            System.out.println (formatted ((numWrites-numWritesAtStart) * 1000 / duration) + " updates / s");
            System.out.println (formatted ((numReads -numReadsAtStart)  * 1000 / duration) + " reads / s");
        }
    }

    static String formatted (long n) {
        return NumberFormat.getIntegerInstance ().format (n);
    }

    private void enqueue (Runnable r) {
        try {
            queue.put (r);
        }
        catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }
}
