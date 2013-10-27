package serializable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class GenericCloner {
	public static Object clone (Object o) throws IOException, ClassNotFoundException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream (baos);
		
		oos.writeObject(o);
		
		oos.close();
		
		final byte[] bytes = baos.toByteArray();
		
		final ObjectInputStream ois = new ObjectInputStream (new ByteArrayInputStream(bytes));
		final Object result = ois.readObject();
		ois.close();
		
		return result;
	}
}
