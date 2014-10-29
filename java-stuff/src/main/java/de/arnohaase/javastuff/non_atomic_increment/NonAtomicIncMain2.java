package de.arnohaase.javastuff.non_atomic_increment;


/**
 * @author arno
 */
public class NonAtomicIncMain2 {
    static final A[] arr = new A[16];

    public static void main(String[] args) throws InterruptedException {
        for(int i=0; i<arr.length; i++) {
            arr[i] = new A();
            new Thread(arr[i]).start();
        }

        while(true) {
//            Thread.sleep(100);
            for(A a: arr) {
//                int i = a.b;
                System.out.print(String.format("%4d", a.a));
            }
            System.out.println();
        }
    }
}

class A implements Runnable {
    public int a;
//    public volatile int b;

    @Override
    public void run() {
        while(true) {
            a=0;
//            b=0;

            for(int i=0; i<10; i++) {
                a+=i;
//                b=0;
            }
        }
    }
}

