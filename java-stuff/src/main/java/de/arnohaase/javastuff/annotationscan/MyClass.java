package de.arnohaase.javastuff.annotationscan;


@MyAnnotation
public class MyClass {
	static {
		System.out.println("seeeeehr teure Initialisierung");
	}
}
