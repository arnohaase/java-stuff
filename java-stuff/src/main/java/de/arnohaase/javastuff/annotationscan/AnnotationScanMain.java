package de.arnohaase.javastuff.annotationscan;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;


public class AnnotationScanMain {
	// http://code.google.com/p/annovention/
	public static void main(String[] args) throws Exception {
//		for (Annotation annot: Class.forName("de.arnohaase.javastuff.annotationscan.MyClass").getAnnotations()) {
//			if (annot instanceof MyAnnotation) {
//				System.out.println("yeah!");
//			}
//		}

        final Discoverer discoverer = new ClasspathDiscoverer();
		discoverer.addAnnotationListener(new ClassAnnotationDiscoveryListener() {
			@Override
			public String[] supportedAnnotations() {
				return new String[] {MyAnnotation.class.getName()};
			}
			
			@Override
			public void discovered(String cls, String annot) {
				System.out.println("annotierte Klasse: " + cls);
			}
		});
		discoverer.discover(true, false, false, true, false);
	}

}
