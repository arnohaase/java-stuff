package finalize;


public class FinalizeBombMain {
	public static void main(String[] args) throws Exception {
		for (int i=0; i<1000; i++) {
			new Bomb();
		}
		
		int i=0;
		while (true) {
			if (i%10000 == 0) {
				System.out.println(Runtime.getRuntime().freeMemory());
			}
		}
	}
}

class Bomb {
	@Override
	protected void finalize() throws Throwable {
		for (int i=0; i<1000; i++) {
			new Bomb();
		}
	}
}
