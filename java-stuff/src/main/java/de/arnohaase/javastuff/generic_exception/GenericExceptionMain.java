package de.arnohaase.javastuff.generic_exception;

import java.io.File;
import java.io.IOException;

/**
 * @author arno
 */
public class GenericExceptionMain {
    public static void main(String[] args) throws Exception{
        final String path = new StopWatch().doWithStopwatch(new StopWatchCode<String, IOException>() {
            @Override public String call() throws IOException {
                return new File(".").getCanonicalPath();
            }
        });
        System.out.println(path);
    }
}

interface StopWatchCode<T, E extends Exception> {
    T call() throws E;
}

class StopWatch {
    public <T,E extends Exception> T doWithStopwatch(StopWatchCode<T,E> code) throws Exception {
        final long start = System.nanoTime();
        try {
            return code.call();
        }
        finally {
            final long end = System.nanoTime();
            System.out.println("Dauer: " + (end - start) + "ns");
        }
    }
}