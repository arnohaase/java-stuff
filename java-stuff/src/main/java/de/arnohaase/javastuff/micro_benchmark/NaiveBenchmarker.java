package de.arnohaase.javastuff.micro_benchmark;

import java.util.Random;


/**
 * @author arno
 */
public class NaiveBenchmarker {
    static final Random random = new Random();

    public static void main (String[] args) {
        doIt ();
        doIt ();
        doIt ();
    }

    static void doIt() {
        final long start = System.nanoTime ();
        for (int i=0; i<1_000_000; i++) {
            random.nextInt ();
        }
        final long end = System.nanoTime ();
        System.out.println (end - start);
    }


}
