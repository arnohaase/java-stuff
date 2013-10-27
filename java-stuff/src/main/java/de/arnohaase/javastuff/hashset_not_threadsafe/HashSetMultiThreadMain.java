package de.arnohaase.javastuff.hashset_not_threadsafe;

import java.util.*;

/**
 * @author arno
 */
public class HashSetMultiThreadMain {
    public static void main(String[] args) throws InterruptedException {
//        final Map<String, String> crashMap = new ConcurrentHashMap<String, String>();
        final Map<String, String> crashMap = new HashMap<String, String>();

		for(int i = 0; i < 10; i++){
			new Thread(new Runnable() {
				@Override public void run() {
					for(int i = 0; i < 10000; i++){
						crashMap.put("foo" + UUID.randomUUID().toString(), "bar");
						crashMap.remove("foo"+i);
					}
					System.out.println("[Thread "+Thread.currentThread().getName()+"] End: " + new Date());
				}
			}).start();
		}
	}
}
