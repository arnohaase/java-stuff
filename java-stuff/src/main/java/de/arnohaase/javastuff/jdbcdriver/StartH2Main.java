package de.arnohaase.javastuff.jdbcdriver;


public class StartH2Main {
	public static void main(String[] args) throws Exception {
		org.h2.tools.Server.main(new String[] {"-tcp", "-web", "-baseDir", "/home/arno/ws/omd/omd-database"});
    }
}
