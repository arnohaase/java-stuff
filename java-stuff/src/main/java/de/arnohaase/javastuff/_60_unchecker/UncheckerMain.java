package de.arnohaase.javastuff._60_unchecker;

import java.io.IOException;


public class UncheckerMain {
	public static void main(String[] args) {
//		Unchecker.rethrow (new IOException ("Hallo"));
		Unchecker2.rethrow (new IOException ("Hallo"));
	}

//    void doesNotCompile() {
//        try {
//            Unchecker.rethrow(new IOException("Hallo"));
//        }
//        catch(IOException exc) {
//            System.out.println(exc);
//        }
//    }
}
