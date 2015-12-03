package de.arnohaase.javastuff.conc_map_performance.stockexchange.impl;

import com.ajjpj.afoundation.collection.immutable.AHashMap;
import com.ajjpj.afoundation.collection.immutable.AMap;
import de.arnohaase.javastuff.conc_map_performance.stockexchange.StockExchange;

import java.util.Currency;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author arno
 */
public class AMapStockExchange implements StockExchange {
    private final AtomicReference<AMap<Currency, Double>> rates = new AtomicReference<> (AHashMap.empty ());
    private final AtomicReference<AMap<String, Double>> pricesInEuro = new AtomicReference<> (AHashMap.empty ());

    @Override public void updateRate (Currency currency, double fromEuro) {
        AMap<Currency, Double> prev;
        do {
            prev = rates.get ();
        }
        while (! rates.compareAndSet (prev, rates.get ().updated (currency, fromEuro)));
    }

    @Override public void updatePrice (String wkz, double euros) {
        AMap<String, Double> prev;
        do {
            prev = pricesInEuro.get ();
        }
        while (! pricesInEuro.compareAndSet (prev, pricesInEuro.get ().updated (wkz, euros)));
    }

    @Override public double currentPrice (String wkz, Currency currency) {
        final double rate = currency.equals (EUR) ? 1 : rates.get().getRequired (currency);
        return pricesInEuro.get().getRequired (wkz) * rate;
    }
}
