package de.arnohaase.javastuff.classpathscan;

import org.objectweb.asm.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;


/**
 * @author arno
 */
public class ClassPathScanMain {
    private static final Map<String, Integer> invocationCounter = new HashMap<> ();
    private static final Map<String, Integer> complexity = new HashMap<> ();

    public static void main (String[] args) throws Exception {
        for (URL url: getRootUrls ()) {
            System.out.println (url);

            if (new File (url.getPath()).isDirectory ()) {
                visitFile (new File (url.getPath ()));
            }
            else {
                visitJar (url);
            }
        }

//        final Map<Integer, Collection<String>> byCount = new TreeMap<> (Comparator.<Integer> naturalOrder ().reversed ());
//        for (Map.Entry<String, Integer> entry: invocationCounter.entrySet ()) {
//            if (! byCount.containsKey (entry.getValue ())) {
//                byCount.put (entry.getValue (), new ArrayList<> ());
//            }
//            byCount.get (entry.getValue ()).add (entry.getKey ());
//        }
//
//        int num=0;
//        for (int count: byCount.keySet ()) {
//            System.out.println (count + " invocations: ");
//            for (String mtd: byCount.get (count)) {
//                System.out.println ("  " + mtd);
//            }
//            num += 1;
//            if (num >= 20) {
//                break;
//            }
//        }
        final Map<Integer, Collection<String>> byCount = new TreeMap<> (Comparator.<Integer> naturalOrder ().reversed ());
        for (Map.Entry<String, Integer> entry: complexity.entrySet ()) {
            if (! byCount.containsKey (entry.getValue ())) {
                byCount.put (entry.getValue (), new ArrayList<> ());
            }
            byCount.get (entry.getValue ()).add (entry.getKey ());
        }

        int num=0;
        for (int count: byCount.keySet ()) {
            System.out.println ("complexity " + count + ": ");
            for (String mtd: byCount.get (count)) {
                System.out.println ("  " + mtd);
            }
            num += 1;
            if (num >= 20) {
                break;
            }
        }

//        Map.Entry<String, Integer> mostComplex = complexity.entrySet ().iterator ().next ();
//        for (Map.Entry<String, Integer> entry: complexity.entrySet ()) {
//            if (entry.getValue () > mostComplex.getValue ()) {
//                mostComplex = entry;
//            }
//        }
//
//        System.out.println ("most complex method (" + mostComplex.getValue () + "): " + mostComplex.getKey ());
    }

    static void visitJar (URL url) throws IOException {
        try (InputStream urlIn = url.openStream ();
             JarInputStream jarIn = new JarInputStream (urlIn)) {
            JarEntry entry;
            while ((entry = jarIn.getNextJarEntry ()) != null) {
                if (entry.getName ().endsWith (".class")) {
                    handleClass (jarIn);
                }
            }
        }
    }

    static void visitFile (File f) throws IOException {
        if (f.isDirectory ()) {
            final File[] children = f.listFiles ();
            if (children != null) {
                for (File child: children) {
                    visitFile (child);
                }
            }
        }
        else if (f.getName ().endsWith (".class")) {
            try (FileInputStream in = new FileInputStream (f)) {
                handleClass (in);
            }
        }
    }

    static void handleClass (InputStream in) throws IOException {
        MyClassVisitor cv = new MyClassVisitor ();
        new ClassReader (in).accept (cv, 0);

        if (cv.hasSuffix && !cv.hasAnnotation) {
            System.out.println ("service without annotation: " + cv.className);
        }
        if (cv.hasAnnotation && !cv.hasSuffix) {
            System.out.println ("wrong name: " + cv.className);
        }
    }

    static class MyClassVisitor extends ClassVisitor {
        public boolean hasSuffix;
        public boolean hasAnnotation;
        public String className;

        MyClassVisitor () {
            super (Opcodes.ASM5);
        }

        @Override public void visit (int version, int access, String name, String signature, String superName, String[] interfaces) {
            className = name.replace('(', '.');
            hasSuffix = name.endsWith("Service");
        }

        @Override public AnnotationVisitor visitAnnotation (String desc, boolean visible) {
//            System.out.println ("  " + desc);
            if (desc.equals ("Ljava/lang/annotation/Retention;")) hasAnnotation = true;
            return null;
        }

        @Override public MethodVisitor visitMethod (int access, String name, String desc, String signature, String[] exceptions) {
//            return new InvocationCountMethodVisitor ();
            return new ComplexityMethodVisitor (className + "." + name + " " + desc);
        }
    }

    static class InvocationCountMethodVisitor extends MethodVisitor {
        InvocationCountMethodVisitor () {
            super (Opcodes.ASM5);
        }

        @Override public void visitMethodInsn (int opcode, String owner, String name, String desc, boolean itf) {
            final String calledMethod = owner + "." + name + " " + desc;
            final int prev = invocationCounter.containsKey (calledMethod) ? invocationCounter.get (calledMethod) : 0;
            invocationCounter.put (calledMethod, prev+1);
        }
    }

    static class ComplexityMethodVisitor extends MethodVisitor {
        private final String mtd;
        int count = 0;

        ComplexityMethodVisitor (String mtd) {
            super (Opcodes.ASM5);
            this.mtd = mtd;
        }

        @Override public void visitJumpInsn (int opcode, Label label) {
            count += 1;
        }

//        @Override public void visitTableSwitchInsn (int min, int max, Label dflt, Label... labels) {
//            count += 1;
//        }
//
//        @Override public void visitLookupSwitchInsn (Label dflt, int[] keys, Label[] labels) {
//            count += 1;
//        }

        @Override public void visitEnd () {
            if (mtd.startsWith ("com/sun/") ||
                    mtd.startsWith("sun/")) {
                return;
            }

            complexity.put (mtd, count);
        }
    }

    void handlePerReflection (String className) throws Exception {
        Class cls = Class.forName (className);

        boolean hasSuffix = className.endsWith ("Service");
        boolean hasAnnotation = cls.getAnnotation (Service.class) != null;

        if (hasSuffix && !hasAnnotation) {
            System.out.println ("service without annotation: " + className);
        }
        if (hasAnnotation && !hasSuffix) {
            System.out.println ("wrong name: " + className);
        }
    }

    private static List<URL> getRootUrls () {
        List<URL> result = new ArrayList<> ();

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        while (cl != null) {
            if (cl instanceof URLClassLoader) {
                URL[] urls = ((URLClassLoader) cl).getURLs();
                result.addAll (Arrays.asList (urls));
            }
            cl = cl.getParent();
        }
        return result;
    }
}

@Target (ElementType.TYPE)
@Retention (RetentionPolicy.RUNTIME)
@interface Service {

}
