package de.arnohaase.javastuff.too_many_threads;

/**
 * @author arno
 */
public class TooManyThreadsMain {
    static void doIt() { // auslagern in separate Methode --> Optimierung vor der Messung
        for(int i=0; i<10_000; i++) { // dann 20_000 --> verifizieren, dass linear
            new Thread().start();
        }
    }

    public static void main(String[] args) {
//        doIt();
//
//        final long start = System.currentTimeMillis();
//        doIt();
//        final long end = System.currentTimeMillis();
//        System.out.println((end - start) + "ms");
//        System.exit(0);

        for(int i=0; i<100_000; i++) {
            if(i%100 == 99) {
                System.out.println(i);
            }
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(1_000_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
