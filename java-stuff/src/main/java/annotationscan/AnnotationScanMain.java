package annotationscan;

import java.lang.annotation.Annotation;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;


public class AnnotationScanMain {
	// http://code.google.com/p/annovention/
	public static void main(String[] args) throws Exception {
//		for (Annotation annot: Class.forName("annotationscan.MyClass").getAnnotations()) {
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
