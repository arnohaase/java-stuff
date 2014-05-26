package de.arnohaase.javastuff.escapa_analysis;

import java.text.NumberFormat;

/**
 * @author arno
 */
public class EscapeAnalysisMain {
    public static void main(String[] args) {
        for(int i=0; i<1000; i++) {
            doLoop();
        }
        System.out.println(NumberFormat.getIntegerInstance().format(doLoop()) + "ns");
    }

    public static long doLoop() {
        final long start = System.nanoTime();
        int count = 0;
        for(int i=0; i<100_000; i++) {
//            if(i%7 == 0) {
//            if(IntHelper.isDivisibleBy(i, 7)) {
            if(new RichInt(i).isDivisibleBy(7)) {
                count += 1;
            }
        }
        final long end = System.nanoTime();
        System.out.println("count: " + count);
        return end-start;
    }
}


class IntHelper {
    public static boolean isDivisibleBy(int n, int m) {
        return n%m == 0;
    }
}

class RichInt {
    private int i;
    private final int value;

    public RichInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isDivisibleBy(int i) {
        return value % i == 0;
    }

//    @Override
//    protected void finalize() throws Throwable {
//        i = 1;
//    }
}