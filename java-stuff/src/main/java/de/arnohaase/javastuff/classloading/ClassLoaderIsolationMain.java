package de.arnohaase.javastuff.classloading;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;


/**
 * @author arno
 */
public class ClassLoaderIsolationMain {
    public static void main (String[] args) throws Exception {
        final URLClassLoader cl1 = new URLClassLoader (new URL[] {new URL("file:///home/arno/tmp/commons-collections4-4.0.jar")});
        final URLClassLoader cl2 = new URLClassLoader (new URL[] {new URL("file:///home/arno/tmp/commons-collections4-4.0.jar")});

        final Class cls = cl1.loadClass ("org.apache.commons.collections4.list.NodeCachingLinkedList");
        final Class cls1 = cl1.loadClass ("org.apache.commons.collections4.list.NodeCachingLinkedList");
        final Class cls2 = cl1.loadClass ("org.apache.commons.collections4.list.NodeCachingLinkedList");

        assert (cls == cls1);
        assert (cls != cls2);

        final List l = (List) cls.newInstance ();
        assert ( cls .isInstance (l));
        assert (!cls2.isInstance (l));
    }
}
