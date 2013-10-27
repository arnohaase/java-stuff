package de.arnohaase.javastuff.boxing;


//-XX:AutoBoxCacheMax=300
public class Compare {
	private static Integer i1, i2 = 3;
	
	public static void main(String[] args) {
		// egal, wie man es macht, muss man aufpassen
		compare1(1, 1);
		compare1(100, 100);
		compare1(200, 200);
		
		compare2(1, 1);
		compare2(i1, i2);
	}
	
	private static void compare1(Integer i1, Integer i2) {
		System.out.println(i1 + " == " + i2 + ": " + (i1 == i2));
	}
	
	private static void compare2(int i1, int i2) {
		System.out.println(i1 + " == " + i2 + ": " + (i1 == i2));
	}
}
