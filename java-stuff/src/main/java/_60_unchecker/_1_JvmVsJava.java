package _60_unchecker;

import java.io.IOException;


public class _1_JvmVsJava {
	public static void main(String[] args) {
		try {
			X.class.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}

class X {
	public X() throws IOException {
		throw new IOException("Hallo, Welt");
	}
}
