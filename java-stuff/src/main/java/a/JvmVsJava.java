package a;

import java.io.IOException;


public class JvmVsJava {
    public static void main(String[] args) {
        try {
            Xyz.class.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
    }
}

class Xyz {
    public Xyz () throws IOException {
        throw new IOException ();
    }
}