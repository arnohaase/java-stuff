package de.arnohaase.javastuff.conc_map_performance.stockexchange.impl;

import de.arnohaase.javastuff.conc_map_performance.stockexchange.StockExchange;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;


/**
 * @author arno
 */
public class SynchronizedStockExchange implements StockExchange {
    private final Map<Currency, Double> rates = new HashMap<> ();
    private final Map<String, Double> pricesInEuro = new HashMap<> ();

    @Override public synchronized void updateRate (Currency currency, double fromEuro) {
        rates.put (currency, fromEuro);
    }

    @Override public synchronized void updatePrice (String wkz, double euros) {
        pricesInEuro.put (wkz, euros);
    }

    @Override public synchronized double currentPrice (String wkz, Currency currency) {
        final double rate = currency.equals (EUR) ? 1 : rates.get (currency);
        return pricesInEuro.get (wkz) * rate;
    }
}
