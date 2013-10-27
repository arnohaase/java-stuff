package de.arnohaase.javastuff.serializable;

import java.io.Serializable;

public class Person extends AbstractBase implements Serializable {
	public static final Person SUPERUSER = new Person("Super", "User");
	
	private final String vorname;
	private final String nachname;
	
	public Person(String vorname, String nachname) {
		super (vorname + "." + nachname);
		this.vorname = vorname;
		this.nachname = nachname;
	}

	public String getVorname() {
		return vorname;
	}
	
	public String getNachname() {
		return nachname;
	}
	
	private Object readResolve() {
		if (SUPERUSER.vorname.equals(vorname) && SUPERUSER.nachname.equals(nachname)) {
			return SUPERUSER;
		}
		return this;
	}
}
