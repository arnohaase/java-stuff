package de.arnohaase.javastuff.weak_soft_phantom;

import java.util.WeakHashMap;

/**
 * @author arno
 */
public class WeakHashMapMain {
    public static void main(String[] args) {
        final WeakHashMap<WithFinalize, Object> map = new WeakHashMap<>();

        WithFinalize key = new WithFinalize();
        map.put(key, "abc");
        System.gc();

        System.out.println("initial: " + map);

        key = null;
        System.gc();
        System.out.println("after key=null: " + map);
    }

    static class WithFinalize {
        @Override
        protected void finalize() throws Throwable {
            Thread.sleep(100);
            System.out.println("finalize");
        }
    }
}
