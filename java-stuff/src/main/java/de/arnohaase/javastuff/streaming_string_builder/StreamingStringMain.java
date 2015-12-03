package de.arnohaase.javastuff.streaming_string_builder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author arno
 */
public class StreamingStringMain {
    static final File f = new File ("/tmp/file.txt");
    static final String txt = "and the number ";
    static final int size = 10_000_000;

    public static void main (String[] args) throws IOException {
        doIt();
        System.out.println (doIt () + "ms");
        System.out.println ("Size: " + f.length ());
    }

    static volatile int v;

    static long doIt() throws IOException {
        final long start = System.currentTimeMillis ();

        final Writer w = new BufferedWriter (new FileWriter (f));
//        w.write (createString (size));

        String s = createStringBuilder (size).toString ();
        v=v;
        w.write (s);


//        streamDirectly (w, size);
//        createStreaming (size).streamTo (w);
        w.close ();

        return System.currentTimeMillis () - start;
    }

    static String createString (int num) {
        String result = "";
        for (int i=0; i<num; i++) {
            result += txt + i + ". ";
        }
        return result;
    }

    static StringBuilder createStringBuilder (int num) {
        final StringBuilder result = new StringBuilder ();
        for (int i=0; i<num; i++) {
            result.append (txt)
                    .append (i)
                    .append (". ");
        }
        return result;
    }

    static void streamDirectly (Writer w, int num) throws IOException {
        for (int i=0; i<num; i++) {
            w.append (txt)
                    .append (String.valueOf (i))
                    .append (". ");
        }
    }
}

