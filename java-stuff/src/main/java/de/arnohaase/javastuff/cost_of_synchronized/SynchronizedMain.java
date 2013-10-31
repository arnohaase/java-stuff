package de.arnohaase.javastuff.cost_of_synchronized;

import java.text.NumberFormat;

/**
 * @author arno
 */
public class SynchronizedMain {
    public static final SynchronizedMain o = new SynchronizedMain();

    public static void main(String[] args) {
        doIt();

        final long start = System.nanoTime();
        doIt();
        final long stop = System.nanoTime();
        System.out.println(NumberFormat.getIntegerInstance().format(stop - start));
    }

    public static int doIt() {
        int j=0;

        for(int i=0; i<100_000_000; i++) {
            o.setValue(i);
            j += o.getValue();
        }

        return j;
    }

    private int value = 0;

    public synchronized int getValue() {
        return value;
    }

    public synchronized void setValue(int value) {
        this.value = value;
    }
}
