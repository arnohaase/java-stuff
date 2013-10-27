package _10_stringintern;

import java.lang.reflect.Field;


public class Magie {
	public static void machWas() throws Exception {
		final Field valField = String.class.getDeclaredField ("value");
		valField.setAccessible (true);
		final char[] halloChars = (char[]) valField.get ("Hallo, Welt!");
		System.arraycopy ("Und tschüs!".toCharArray(), 0, halloChars, 0, 11);
		
	    final Field countField = String.class.getDeclaredField ("count");
	    countField.setAccessible (true);
	    countField.set ("Hallo, Welt!", 11);
	}
}
