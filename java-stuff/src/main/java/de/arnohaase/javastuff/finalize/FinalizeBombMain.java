package de.arnohaase.javastuff.finalize;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class FinalizeBombMain {
	public static void main(String[] args) throws Exception {
        final List<Object> l = new ArrayList<>();

        int i=0;
		while(true) {
            i+=1;

            new Bomb();

//			l.add(new Bomb());
//            l.clear();
			if (i%1_000_000 == 0) {
				System.out.println(NumberFormat.getIntegerInstance().format(Bomb.count.get()) + ": " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024);
			}
		}
	}
}

class Bomb {
    final static AtomicInteger count = new AtomicInteger();

    public Bomb() {
        count.incrementAndGet();
//        System.out.println("ctor");
    }
//	@Override
//	protected void finalize() throws Throwable {
////        new Bomb();
//        count.get();
//	}
}
