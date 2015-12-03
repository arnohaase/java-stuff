package de.arnohaase.javastuff.completable_future;

import java.util.Random;
import java.util.concurrent.*;


/**
 * @author arno
 */
public class ServicePar {
    public static void main (String[] args) throws InterruptedException, ExecutionException {
        final ServicePar servicePar = new ServicePar ();

        servicePar.printOrderPar ("4711", 2);
        servicePar.printOrderPar ("1234", 3);
        servicePar.printOrderPar ("0815", 4);

        System.out.println ("Warte...\n");
        Thread.sleep (2000);
        System.exit (0);
    }

    final DescriptionService descriptionService = new DescriptionService ();
    final PriceService priceService = new PriceService ();

    void printOrderSeq (String article, int num) {
        final String description = descriptionService.getDescription (article);
        final double price = priceService.getPrice (article);

        System.out.println ("Anzahl: " + num + "\n" +
                "Artikel: " + article + "\n" +
                "Beschreibung: " + description + "\n" +
                "Gesamtpreis: " + num*price + "\n");
    }

    void printOrderPar (String article, int num) {
        CompletableFuture<String> desc =
                CompletableFuture.supplyAsync (() -> descriptionService.getDescription (article));
        desc = FutureHelper.withTimeout (desc, 100, TimeUnit.MILLISECONDS, "<kein Text>");
        final CompletableFuture<Double> priceFuture = CompletableFuture.supplyAsync (() -> priceService.getPrice (article));

        desc.thenAcceptBothAsync (priceFuture, (description, price) -> {
            System.out.println ("Anzahl: " + num + "\n" +
                    "Artikel: " + article + "\n" +
                    "Beschreibung: " + description + "\n" +
                    "Gesamtpreis: " + num * price + "\n");
        });
    }
}


class DescriptionService {
    String getDescription (String articleNumber) {
        switch (articleNumber) {
            case "4711":
                return "mit Geruch";
            case "1234":
                try {
                    Thread.sleep(1000); // simuliert teuren Lookup
                }
                catch (InterruptedException e) {
                    e.printStackTrace ();
                }
                return "Spezial";
            default:
                return "<kein Text>";
        }
    }
}

class PriceService {
    double getPrice (String articleNumber) {
        switch (articleNumber) {
            case "4711": return 1;
            case "1234": return 2;
            case "0815": return 3;
            default: throw new IllegalArgumentException();
        }
    }
}

