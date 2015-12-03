package de.arnohaase.javastuff.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author arno
 */
public class UnsafeMain {
    public static void main(String[] args) throws Exception {
//        final Unsafe unsafe = Unsafe.getUnsafe();

        final Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        final Unsafe unsafe = (Unsafe) f.get(null);


//        unsafe.throwException(new Exception("checked?!"));



        final long mem = unsafe.allocateMemory(100);
        try {
            unsafe.putChar(mem, '\u1234');
            System.out.println(Integer.toHexString(unsafe.getByte(mem)));
            System.out.println(Integer.toHexString(unsafe.getByte(mem + 1)));
        }
        finally {
            unsafe.freeMemory(mem);
        }


        final long offsetA = unsafe.objectFieldOffset(Struct.class.getDeclaredField("a"));
        final long offsetB = unsafe.objectFieldOffset(Struct.class.getDeclaredField("b"));

        System.out.println("offsets: " + offsetA + ", " + offsetB);

        final Struct s = new Struct();
        s.a = 123;

        System.out.println(unsafe.getInt(s, offsetA));
        unsafe.putInt(s, offsetB, 99);

        System.out.println(s.b);

        for(int i=0; i<100; i++) {
            new Thread() {
                @Override public void run() {
                    final boolean entered = unsafe.tryMonitorEnter(s);
                    if(entered) {
                        try {
                            System.out.println("Yeah");

                        }
                        finally {
                            unsafe.monitorExit(s);
                        }
                    }
                    else {
                        System.out.println("OK - ich mach was Anderes");
                    }
                }
            }.start();
        }
    }
}

class Struct {
    int a;
    int b;
}