package de.arnohaase.javastuff.conc_map_performance.stockexchange.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author arno
 */
public class SingleWorkerThreadRingBufferStockExchange extends AbstractSingleWorkerThreadStockExchange {
    public SingleWorkerThreadRingBufferStockExchange () {
        super (new RingBufferQueue ());
    }
}

class RingBufferQueue implements BlockingQueue<Runnable> {
    static final int SIZE = 1_000_000;
    final Runnable[] buffer = new Runnable[SIZE];

    volatile int readPointer = 0;
    volatile int writePointer = 0;
    volatile AtomicBoolean writeLock = new AtomicBoolean (false);

    @Override public Runnable take () throws InterruptedException {
        while (readPointer == writePointer) {} // active wait

        final Runnable result = buffer[readPointer];
        readPointer = inc (readPointer);
        return result;
    }

    private static int inc (int i) {
        return (i+1) % SIZE;
    }

    private static int diff (int i, int j) {
        return (i-j + SIZE) % SIZE;
    }

    @Override public void put (Runnable runnable) throws InterruptedException {
        while (! writeLock.compareAndSet (false, true)); // spin lock for exclusive write access

        while (diff (readPointer, writePointer) == 1); // active wait to prevent ring buffer wraparound

        buffer[writePointer] = runnable;
        writePointer = inc (writePointer);

        writeLock.set (false);
    }

    //----------------------------------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------- never called
    //----------------------------------------------------------------------------------------------------------------------------------


    @Override public boolean add (Runnable runnable) {
        return false;
    }
    @Override public boolean offer (Runnable runnable) {
        return false;
    }
    @Override public boolean offer (Runnable runnable, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }
    @Override public Runnable poll (long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }
    @Override public int remainingCapacity () {
        return 0;
    }
    @Override public boolean remove (Object o) {
        return false;
    }
    @Override public boolean contains (Object o) {
        return false;
    }
    @Override public int drainTo (Collection<? super Runnable> c) {
        return 0;
    }
    @Override public int drainTo (Collection<? super Runnable> c, int maxElements) {
        return 0;
    }
    @Override public Runnable remove () {
        return null;
    }
    @Override public Runnable poll () {
        return null;
    }
    @Override public Runnable element () {
        return null;
    }
    @Override public Runnable peek () {
        return null;
    }
    @Override public int size () {
        return 0;
    }
    @Override public boolean isEmpty () {
        return false;
    }
    @Override public Iterator<Runnable> iterator () {
        return null;
    }
    @Override public Object[] toArray () {
        return new Object[0];
    }
    @Override public <T> T[] toArray (T[] a) {
        return null;
    }
    @Override public boolean containsAll (Collection<?> c) {
        return false;
    }
    @Override public boolean addAll (Collection<? extends Runnable> c) {
        return false;
    }
    @Override public boolean removeAll (Collection<?> c) {
        return false;
    }
    @Override public boolean retainAll (Collection<?> c) {
        return false;
    }
    @Override public void clear () {

    }
}