package de.arnohaase.javastuff._30_raceconditions;


abstract class Parent {
	public Parent() {
		init();
	}
	void init() {}
}

class Child extends Parent {
	private final String id;
	
	public Child(String id) {
		this.id = id;
	}
	
	@Override
	void init() {
		super.init();
		System.out.println("creating " + id);
	}
}


public class RaceConditionsMain {
	public static void main(String[] args) {
		new Child ("abc");
	}
}
