package de.arnohaase.javastuff.date;

import java.util.GregorianCalendar;


public class GregorianCalendarMain {
	public static void main(String[] args) {
		final GregorianCalendarMain main = new GregorianCalendarMain();
		
		for (int i=0; i<1000*1000; i++) {
			main.doIt();
		}
		
		System.out.println(main.doIt() + "ns");
	}
	
	private long doIt() {
		final long start = System.nanoTime();
		new GregorianCalendar();
		return System.nanoTime() - start;
	}
}
