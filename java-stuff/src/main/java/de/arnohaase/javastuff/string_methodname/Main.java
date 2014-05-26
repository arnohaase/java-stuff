package de.arnohaase.javastuff.string_methodname;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author arno
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread.sleep(100);

        X.a();

        final String s1 = new StringBuilder("Hallo").toString();
        final String s2 = s1.intern();
        assert(s1 != "Hallo");
        assert(s2 == "Hallo");
//        System.out.println("Hallo");
        magic();
//        System.out.println("Hallo");
        System.out.println(s1);
        System.out.println(s2);

        X.a();
    }

    static void magic() throws Exception {
        final Field field = String.class.getDeclaredField("value");
        field.setAccessible(true);

        final Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set("Hallo", "Tschüs".toCharArray());
//        final char[] value = (char[]) field.get("Hallo");
//        System.arraycopy("Tschüs".toCharArray(), 0, value, 0, 6);
    }
}


class X {
    static void a() {
        System.out.print("aus X: ");
        System.out.println("Hallo");
    }
}