package a;

import java.util.HashMap;
import java.util.Map;


public class Initializer {
    public static final Initializer DEFAULT = new Initializer ("default");
    private static Map<String, Initializer> all = new HashMap<String, Initializer> ();
    
    public Initializer (String s) {
        all.put (s, this);
    }
    
    public static void main(String[] args) {
        
    }
}

