package de.arnohaase.javastuff.conc_map_performance.stockexchange.impl;

import de.arnohaase.javastuff.conc_map_performance.stockexchange.StockExchange;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.concurrent.*;


/**
 * @author arno
 */
public class SingleWorkerThreadLinkedBlockingDequeStockExchange extends AbstractSingleWorkerThreadStockExchange {
    public SingleWorkerThreadLinkedBlockingDequeStockExchange () {
        super (new LinkedBlockingDeque<> ());
    }
}
