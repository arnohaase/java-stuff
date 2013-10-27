package _60_unchecker;


public class CastTimeDemoMain {
	public static void main(String[] args) {
		String s1 = CastTimeDemoMain.<String> myCast("a");
		
		CastTimeDemoMain.<String> myCast(7);
		String s2 = CastTimeDemoMain.<String> myCast(7);
	}
	
	private static <T> T myCast (Object o) {
		return (T) o;
	}
	
	// javap -c -private
}
