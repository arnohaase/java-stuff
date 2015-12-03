package de.arnohaase.javastuff.conc_map_performance.stockexchange.impl;

import com.ajjpj.afoundation.collection.immutable.AHashMap;
import com.ajjpj.afoundation.collection.immutable.AMap;
import de.arnohaase.javastuff.conc_map_performance.stockexchange.StockExchange;

import java.util.Currency;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author arno
 */
public class AMapLossyStockExchange implements StockExchange {
    private volatile AMap<Currency, Double> rates = AHashMap.empty ();
    private volatile AMap<String, Double> pricesInEuro = AHashMap.empty ();

    @Override public void updateRate (Currency currency, double fromEuro) {
        rates = rates.updated (currency, fromEuro);
    }

    @Override public void updatePrice (String wkz, double euros) {
        pricesInEuro = pricesInEuro.updated (wkz, euros);
    }

    @Override public double currentPrice (String wkz, Currency currency) {
        final double rate = currency.equals (EUR) ? 1 : rates.getRequired (currency);
        return pricesInEuro.getRequired (wkz) * rate;
    }
}
