package de.arnohaase.javastuff.weak_soft_phantom;


import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author arno
 */
public class TempFileDeleterMain {
    public static final TempFileDeleter del = new TempFileDeleter();

    public static void main(String[] args) throws Exception {
        File f = new File("a");
        f.delete();
        System.out.println("initial: " + f.exists());

        del.register(f);

        final Writer out = new FileWriter(f);
        out.write("");
        out.close();

        f = null;
        Thread.sleep(10);

        del.cleanup();
        System.out.println("before gc: " + new File("a").exists());
        System.gc();
        Thread.sleep(10);
        del.cleanup();
        System.out.println("after gc: " + new File("a").exists());
    }
}

class TempFileDeleter {
    private final ReferenceQueue<File> queue = new ReferenceQueue<>();
    private Set<FileRef> refs = Collections.newSetFromMap(new ConcurrentHashMap<FileRef, Boolean>());

    public void register(File f) {
        deleteUnused();
        refs.add(new FileRef(f, queue));
    }
    public void unregister(File f) {
        deleteUnused();
        refs.remove(new FileRef(f, queue));
    }

    public void cleanup() {
        deleteUnused();
    }

    private void deleteUnused() {
        while (true) {
            final FileRef ref = (FileRef) queue.poll();
            if(ref == null) {
                return;
            }
            refs.remove(ref);
            new File(ref.getPath()).delete();
        }
    }

    private static class FileRef extends PhantomReference<File> {
        private final String path;

        public FileRef(File f, ReferenceQueue<? super File> q) {
            super(f, q);
            path = f.getPath();
        }

        private String getPath() {
            return path;
        }

        @Override
        public boolean equals(Object obj) {
            if(! (obj instanceof FileRef)) {
                return false;
            }
            return ((FileRef)obj).path.equals(path);
        }

        @Override
        public int hashCode() {
            return path.hashCode();
        }
    }
}
