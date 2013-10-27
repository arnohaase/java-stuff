package de.arnohaase.javastuff._60_unchecker;


// besser als Wrapping: TX-Semantik von Exceptions (Spring, EJB)

public class Unchecker {
	public static void rethrow (Throwable th) {
		Unchecker.<RuntimeException>hide (th);
	}

	private static <T extends Throwable> void hide (Throwable exc) throws T {
		throw (T) exc;
	}
}
