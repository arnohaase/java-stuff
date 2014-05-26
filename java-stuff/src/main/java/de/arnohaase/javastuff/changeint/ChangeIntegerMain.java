package de.arnohaase.javastuff.changeint;

import java.lang.reflect.Field;

/**
 * @author arno
 */
public class ChangeIntegerMain {
    public static void main(String[] args) throws Exception {
        final Class cls = Class.forName("java.lang.Integer$IntegerCache");
        final Field f = cls.getDeclaredField("cache");
        f.setAccessible(true);
        final Integer[] cache = (Integer[]) f.get(null);
        cache[129] = 5;

        System.out.println((Integer) 1 + 2);

//        System.out.println(add(1, 2));
//        magic();
//        System.out.println(add(1, 2));
//        System.out.println(doIt());
//        for(int i=0; i<1_000_000_000; i++) {
//            doIt();
//        }
//        magic();
//        System.out.println(doIt());
    }

//    static int doIt() {
//        return add(1, 2);
//    }
//
//    static int add(Integer a, Integer b) {
//        return a+b;
//    }
//
//    static void magic() throws Exception {
//        System.out.println("--");
//        final Class cls = Class.forName("java.lang.Integer$IntegerCache");
//        final Field f = cls.getDeclaredField("cache");
//        f.setAccessible(true);
//        final Integer[] cache = (Integer[]) f.get(null);
//        cache[129] = 1000;
//    }
}
