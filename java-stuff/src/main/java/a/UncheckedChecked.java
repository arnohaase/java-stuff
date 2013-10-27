package a;

import java.io.IOException;


public class UncheckedChecked {
	public static void main(String[] args) {
		Unchecker.rethrow (new IOException ());
	}
}


class Unchecker {
	public static void rethrow (Throwable checkedException ) {
		Unchecker.<RuntimeException> thrownInsteadOf( checkedException );
	}

	private static <T extends Throwable> void thrownInsteadOf (Throwable t) throws T {
		throw (T) t;
	}
}

