package de.arnohaase.javastuff.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SimpleDateFormatMain {
	public static void main(String[] args) throws ParseException {
		final SimpleDateFormatMain main = new SimpleDateFormatMain();
		for (int i=0; i<1000*1000; i++) {
			main.doIt();
		}
		
		System.out.println(main.doIt() + "ns");
	}
	
	private static long doIt() throws ParseException {
		final long start = System.nanoTime();
		final DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		df.format(new Date());
		df.parse("1.2.2012");
		return System.nanoTime() - start;
	}
}
