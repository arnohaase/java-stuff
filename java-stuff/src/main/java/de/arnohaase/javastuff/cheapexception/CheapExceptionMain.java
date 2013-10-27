package de.arnohaase.javastuff.cheapexception;

import java.text.NumberFormat;


public class CheapExceptionMain {
	public static void main(String[] args) {
		final long start = System.nanoTime();

		new CheapExceptionMain().doIt();
		
		final long end = System.nanoTime();
		System.out.println(NumberFormat.getIntegerInstance().format(end - start) + "ns");
	}
	
	private int counter;
	
	public void doIt() {
		for (int i=0; i<2*1000*1000; i++) {
			try {
				add(i);
			}
			catch(MyException exc) {
			}
		}
	}
	
	public void add(int i) {
		counter += i;
		throw new MyException();
	}
}

class MyException extends RuntimeException {
	@Override
	public /*synchronized*/ Throwable fillInStackTrace() {
		return this;
	}
}