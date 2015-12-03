package de.arnohaase.javastuff.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * @author arno
 */
public class FutureHelper {
    private static final ScheduledExecutorService ec = Executors.newSingleThreadScheduledExecutor ();

    static <T> CompletableFuture<T> withTimeout (CompletableFuture<T> f, long timeout, TimeUnit timeUnit, T defaultValue) {
        ec.schedule (() -> {
            f.complete (defaultValue);
        }, timeout, timeUnit);
        return f;
    }
}
