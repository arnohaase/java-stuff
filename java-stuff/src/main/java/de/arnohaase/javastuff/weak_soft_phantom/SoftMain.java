package de.arnohaase.javastuff.weak_soft_phantom;

import javax.tools.JavaCompiler;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * @author arno
 */
public class SoftMain {
    static final ReferenceQueue<Object> queue = new ReferenceQueue<Object>();

    // -XX:SoftRefLRUPolicyMSPerMB=1

    public static void main(String[] args) throws Exception {
        final Reference<Object> ref = new SoftReference<Object>(new WithFinalize(), queue);

        System.out.println("initial: " + ref.get());

        while(true) {
//            ref.get();
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
