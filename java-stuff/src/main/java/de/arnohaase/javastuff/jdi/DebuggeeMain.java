package de.arnohaase.javastuff.jdi;

import java.io.File;
import java.util.Random;

/**
 * @author arno
 */
public class DebuggeeMain {
    public static void main(String[] args) throws Exception {
        System.out.println(new File("/proc/self").getCanonicalPath());

        while(true) {
            new Random().nextInt();
            Thread.sleep(1000);
        }
    }
}
