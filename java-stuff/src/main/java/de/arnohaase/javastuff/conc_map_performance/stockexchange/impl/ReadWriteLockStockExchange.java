package de.arnohaase.javastuff.conc_map_performance.stockexchange.impl;

import de.arnohaase.javastuff.conc_map_performance.stockexchange.StockExchange;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * @author arno
 */
public class ReadWriteLockStockExchange implements StockExchange {
    private final Map<Currency, Double> rates = new HashMap<> ();
    private final Map<String, Double> pricesInEuro = new HashMap<> ();

    private final ReadWriteLock lock = new ReentrantReadWriteLock (false);

    @Override public void updateRate (Currency currency, double fromEuro) {
        lock.writeLock ().lock ();
        try {
            rates.put (currency, fromEuro);
        }
        finally {
            lock.writeLock ().unlock ();
        }
    }

    @Override public void updatePrice (String wkz, double euros) {
        lock.writeLock ().lock ();
        try {
            pricesInEuro.put (wkz, euros);
        }
        finally {
            lock.writeLock ().unlock ();
        }
    }

    @Override public double currentPrice (String wkz, Currency currency) {
        lock.readLock ().lock ();
        try {
            final double rate = currency.equals (EUR) ? 1 : rates.get (currency);
            return pricesInEuro.get (wkz) * rate;
        }
        finally {
            lock.readLock ().unlock ();
        }
    }
}
