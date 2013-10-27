package parameternames;

import java.lang.reflect.Method;

import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;


public class ParameterNamesMain {
	public static void main(String[] args) throws Exception {
		final Method mtd = ParameterNamesMain.class.getMethod("myMethod", int.class, String.class);
		
		final Paranamer paranamer = new BytecodeReadingParanamer();
		final String[] paramNames = paranamer.lookupParameterNames(mtd);
		
		for (String n: paramNames) {
			System.out.println(n);
		}
	}
	
	public void myMethod(int integerzahl, String irgendwas) {
		System.out.println("Hallo");
	}
}
