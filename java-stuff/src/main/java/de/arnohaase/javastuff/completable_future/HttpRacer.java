package de.arnohaase.javastuff.completable_future;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CompletableFuture;


/**
 * @author arno
 */
public class HttpRacer {
    public static void main (String[] args) throws InterruptedException {
        new HttpRacer().main ();
    }

    public void main () throws InterruptedException {
        final CompletableFuture<String> microsoft = CompletableFuture.supplyAsync (() -> download ("http://microsoft.com"));
        final CompletableFuture<String> oracle = CompletableFuture.supplyAsync (() -> download ("http://oracle.com"));

        microsoft.acceptEitherAsync (oracle, (html) -> eval(html));

        Thread.sleep(2000);
    }

    void eval (String html) {
        if (html.contains ("oracle")) {
            System.out.println ("Oracle");
        }
        else {
            System.out.println ("Microsoft");
        }
    }

    String download (String url) {
        try {
            final URLConnection conn = new URL (url).openConnection ();

            try (InputStream in = conn.getInputStream ()) {
                return read (in);
            }
        }
        catch (IOException exc) {
            throw new RuntimeException (exc);
        }
    }

    String read (InputStream is) throws IOException {
        final InputStreamReader in = new InputStreamReader (is);
        final StringBuilder result = new StringBuilder ();

        int ch;
        while ((ch = in.read ()) != -1) {
            result.append ((char)ch);
        }

        return result.toString ();
    }
}


