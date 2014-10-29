package de.arnohaase.javastuff;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author arno
 */
public class AtomicFileIo {
    static final int N = 100;

    public static void main (String[] args) throws IOException, InterruptedException {
        final FileWriter out = new FileWriter ("dummy.txt");

        final CountDownLatch latch = new CountDownLatch (N);
        for (int i=0; i<N; i++) {
            final int threadNo = i;
            new Thread () {
                @Override public void run () {
                    final StringBuilder sb = new StringBuilder ();
                    for (int i=0; i<100; i++) {
                        sb.append (threadNo%10);
                    }
                    final String msg = sb.toString ();


                    for (int i=0; i<50; i++) {
                        System.err.println (msg);
                    }
                    latch.countDown ();
                }
            }.start ();
        }
        latch.await ();
        out.close ();
    }
}
