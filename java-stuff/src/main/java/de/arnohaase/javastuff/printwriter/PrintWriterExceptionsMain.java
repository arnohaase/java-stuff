package de.arnohaase.javastuff.printwriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * @author arno
 */
public class PrintWriterExceptionsMain {
    public static void main(String[] args) {
        final PrintWriter out = new PrintWriter(new ThrowingWriter());
        System.out.println(out.checkError());
        out.println("Hi");
        System.out.println(out.checkError());
        out.close();
    }
}

class ThrowingWriter extends Writer {
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        System.out.println("I was here");
        throw new IOException("Boo!");
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }
}
