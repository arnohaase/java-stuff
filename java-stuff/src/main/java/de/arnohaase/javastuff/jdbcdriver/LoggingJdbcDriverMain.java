package de.arnohaase.javastuff.jdbcdriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class LoggingJdbcDriverMain {
	public static void main(String[] args) throws Exception {
        org.h2.tools.Server.main(new String[] {"-tcp", "-web"});

        DriverManager.registerDriver(new LoggingJdbcDriver());
		
		final Connection conn = DriverManager.getConnection("jdbc:logger:jdbc:h2:tcp://localhost/omd", "sa", "");
		final PreparedStatement stmt = conn.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.TABLES where table_name like ?");
		stmt.setString(1, "I%");
		final ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			System.out.println(rs.getString("TABLE_NAME"));
		}
		conn.close();
	}
}

