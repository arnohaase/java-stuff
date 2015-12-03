package de.arnohaase.javastuff.classloading;

/**
 * @author arno
 */
public class ClassLoaderHierarchy {
    public static void main (String[] args) {
        new ClassLoaderHierarchy ().x ();
    }

    void x() {
        ClassLoader loader = getClass ().getClassLoader ();

        while (loader != null) {
            System.out.println (loader.getClass ().getName ());
            loader = loader.getParent ();
        }

        System.out.println (String.class.getClassLoader ());
    }
}

