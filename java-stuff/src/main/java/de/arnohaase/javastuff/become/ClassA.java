package de.arnohaase.javastuff.become;


public class ClassA {
    String s;

    public ClassA (String s) {
        this.s = s;
    }

    @Override
    public String toString () {
        return "ClassA{" +
                "s='" + s + '\'' +
                '}';
    }
}
