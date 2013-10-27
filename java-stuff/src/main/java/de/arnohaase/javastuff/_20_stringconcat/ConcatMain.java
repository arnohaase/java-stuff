package de.arnohaase.javastuff._20_stringconcat;


public class ConcatMain {
	public static void main(String[] args) {
		System.out.println(lit());
		System.out.println(var(1));
	}
	
	private static String lit () {
		return "A" + "B";
	}
	
	private static String var (int x) {
		return "A" + x;
	}
}
