package de.arnohaase.javastuff.upstream;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * @author arno
 */
@SupportedAnnotationTypes(value = "de.arnohaase.javastuff.upstream.DemoProcAnnotation")
public class DemoProcessor extends AbstractProcessor {
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return SourceVersion.latestSupported();
//    }

    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final Messager msg = processingEnv.getMessager();

        msg.printMessage(Diagnostic.Kind.ERROR, "this is a message");
        return true;
    }
}
