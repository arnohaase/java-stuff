package boxing;


public class Widening {
	public static void main(String[] args) {
		compare1(1, 2);
//		compare2(1, 2);
	}

	private static void compare1(long l1, long l2) {
		System.out.println(l1 < l2);
	}
	
	private static void compare2(Long l1, Long l2) {
		System.out.println(l1 < l2);
	}
}
