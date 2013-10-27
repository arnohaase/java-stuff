package a;


public class ImplVererbungCtor {
    public static void main(String[] args) {
        new Child ("a");
    }
}


abstract class Parent {
    public Parent () {
        init ();
    }

    abstract void init ();
}

class Child extends Parent {
    final String id;
    
    public Child (String id) {
        this.id = id;
    }

    void init () {
        System.out.println ("creating " + id);
    }
}