package de.arnohaase.javastuff.weak_soft_phantom;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * @author arno
 */
public class WeakPhantomMain {
    static final ReferenceQueue<Object> queue = new ReferenceQueue<Object>();

    public static void main(String[] args) throws Exception {
        final Reference<Object> ref = new WeakReference<Object>(new WithFinalize(), queue);
//        PhantomReference

        System.out.println("initial: " + ref.get());

        while(true) {
            System.gc();
            final Reference<?> r = queue.remove(100);
            if(r != null) {
                System.out.println("from queue: " + r + " -> " + r.get());
            }
        }
    }

    static class WithFinalize {
        @Override
        protected void finalize() throws Throwable {
            Thread.sleep(100);
            System.out.println("finalize");
        }
    }
}
