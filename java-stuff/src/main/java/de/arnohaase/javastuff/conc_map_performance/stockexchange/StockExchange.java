package de.arnohaase.javastuff.conc_map_performance.stockexchange;

import java.util.Currency;


/**
 * @author arno
 */
public interface StockExchange {
    Currency EUR = Currency.getInstance ("EUR");

    void updateRate (Currency currency, double fromEuro);
    void updatePrice (String wkz, double euros);

    double currentPrice (String wkz, Currency currency);
}
