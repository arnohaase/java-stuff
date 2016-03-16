package de.arnohaase.javastuff.become;

import sun.misc.Unsafe;

import java.lang.reflect.Field;


public class BecomeMain {
    public static void main (String[] args) throws NoSuchFieldException, IllegalAccessException {
        final Field f = Unsafe.class.getDeclaredField ("theUnsafe");
        f.setAccessible (true);
        final Unsafe UNSAFE = (Unsafe) f.get (null);

        // Every object's header consists of 64 bits with a MarkOOP and a 32 bit (Compressed OOP) pointer to the object's klass struct,
        //  allowing the actual object fields to start at offset 12 (on 64bit Hotspot JVMs)

        // 'long' fields are aligned to start at round 64 bit offsets, so the JVM adds padding before a long field if / when needed.

        // This is to verify memory layout / alignment of the fields. This is susceptible to
        //  changes in Hotspot releases and config parameters. pointers are stored as Compressed OOPs by default...


        System.out.println (UNSAFE.objectFieldOffset (ClassA.class.getDeclaredField ("s")));
        System.out.println (UNSAFE.objectFieldOffset (ClassB.class.getDeclaredField ("i")));

        final int klassB = UNSAFE.getInt (new ClassB(), 8L);


        final Object o = new ClassA ("Hallo");
        System.out.println (o);

        UNSAFE.putInt (o, 8L, klassB);
        System.out.println (o);
    }
}
