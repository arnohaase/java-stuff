package de.arnohaase.javastuff;

import java.util.concurrent.*;


/**
 * @author arno
 */
public class AsyncLogger {
   final ExecutorService exec = Executors.newSingleThreadExecutor ();

    public void log (String msg, Object... args) {
        final String formatted = doFormat (msg, args);
        final Future<String> enhanced = CompletableFuture.supplyAsync (() -> doEnhance (formatted));

        exec.execute (() -> {
            try {
                doLog (enhanced.get ());
            }
            catch (Exception e) {
                e.printStackTrace ();
            }
        });
    }

    private String doFormat (String msg, Object... args) {
        return String.format (msg, args);
    }

    private String doEnhance (String msg) {
        return msg;
    }

    private void doLog (String msg) {
        System.out.println (msg);
    }

    public static void main (String[] args) {
        final AsyncLogger log = new AsyncLogger ();
        log.log ("Hallo");
        log.log ("Arno");
    }
}