package _60_unchecker;


public class Unchecker2 {
	private static final ThreadLocal<Throwable> th = new ThreadLocal<Throwable>();

	public static void rethrow (Throwable t) {
		th.set(t);
		try {
			X.class.newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		finally {
			th.remove();
		}
	}
	
	private static class X {
		public X() throws Throwable {
			throw th.get();
		}
	}
}
