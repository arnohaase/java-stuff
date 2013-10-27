package de.arnohaase.javastuff.serializable;

import java.io.Serializable;


// ohne Serializable --> no-args-ctor
public abstract class AbstractBase implements Serializable {
	private String id;
	
	public AbstractBase(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
}
