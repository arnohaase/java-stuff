package jdbcdriver;

import java.sql.Driver;
import java.util.Iterator;
import java.util.ServiceLoader;


public class ShowDrivers {
	public static void main(String[] args) {
		final ServiceLoader<Driver> serviceLoader = ServiceLoader.load(Driver.class);
		for (Iterator<Driver> iter = serviceLoader.iterator(); iter.hasNext(); ) {
			System.out.println(iter.next().getClass().getName());
		}
	}
}
// und auf der Basis "Referenced Libraries" aufklappen, META-INF/services/java.sql.Driver