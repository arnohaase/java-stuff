package de.arnohaase.javastuff.conc_map_performance.stockexchange.impl;

import de.arnohaase.javastuff.conc_map_performance.stockexchange.StockExchange;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author arno
 */
public class ConcHashMapStockExchange implements StockExchange {
    private final Map<Currency, Double> rates = new ConcurrentHashMap<> ();
    private final Map<String, Double> pricesInEuro = new ConcurrentHashMap<> ();

    @Override public void updateRate (Currency currency, double fromEuro) {
        rates.put (currency, fromEuro);
    }

    @Override public void updatePrice (String wkz, double euros) {
        pricesInEuro.put (wkz, euros);
    }

    @Override public double currentPrice (String wkz, Currency currency) {
        final double rate = currency.equals (EUR) ? 1 : rates.get (currency);
        return pricesInEuro.get (wkz) * rate;
    }
}
