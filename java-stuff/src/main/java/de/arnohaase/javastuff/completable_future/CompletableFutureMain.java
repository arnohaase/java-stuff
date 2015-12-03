package de.arnohaase.javastuff.completable_future;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/**
 * @author arno
 */
public class CompletableFutureMain {
    public static void main (String[] args) throws ExecutionException, InterruptedException {
        final List<CompletableFuture<Double>> gaussians = new ArrayList<> ();

        for (int i=0; i<1_000_000; i++) {
            gaussians.add (
                    CompletableFuture.
                            supplyAsync (() -> new Random ().nextGaussian ()).
                            thenApplyAsync ((x) -> x*x)
            );
        }

        CompletableFuture<Double> result = CompletableFuture.completedFuture (0.0);

        for (CompletableFuture<Double> f: gaussians) {
            result = result.thenCombineAsync (f, (f1, f2) -> f1 + f2);
        }

        result.thenAcceptAsync (System.out::println);

        System.out.println ("Warte...");
        Thread.sleep (5_000);
    }
}
