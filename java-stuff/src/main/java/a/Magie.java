package a;

import java.lang.reflect.Field;


public class Magie {
	public static void machWas () throws Exception {
		final Field charField = String.class.getDeclaredField ("value");
		charField.setAccessible (true);
		final char[] hallo = (char[]) charField.get ("Hallo, Welt!");
		System.arraycopy ("Und tsch�s!".toCharArray(), 0, hallo, 0, 11);
		
		final Field lenField = String.class.getDeclaredField ("count");
		lenField.setAccessible (true);
		lenField.set ("Hallo, Welt!", "Und tsch�s!".length());
	}
}
