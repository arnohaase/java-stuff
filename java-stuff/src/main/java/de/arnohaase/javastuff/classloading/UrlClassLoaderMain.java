package de.arnohaase.javastuff.classloading;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;


/**
 * @author arno
 */
public class UrlClassLoaderMain {
    public static void main (String[] args) throws Exception {
        final URLClassLoader cl = new URLClassLoader (new URL[] {new URL("https://repo1.maven.org/maven2/org/apache/commons/commons-collections4/4.0/commons-collections4-4.0.jar")});
//        final URLClassLoader cl = new URLClassLoader (new URL[] {new URL("file:///home/arno/tmp/commons-collections4-4.0.jar")});

        final Class cls = cl.loadClass ("org.apache.commons.collections4.list.NodeCachingLinkedList");
        final List<String> coll = (List<String>) cls.newInstance ();

        coll.add ("a");
        coll.add ("b");

        for (String s: coll) {
            System.out.println (s);
        }

        System.out.println (coll.getClass ());
        System.out.println (coll.getClass().getSuperclass ());
        System.out.println (coll instanceof java.util.List);
    }
}
