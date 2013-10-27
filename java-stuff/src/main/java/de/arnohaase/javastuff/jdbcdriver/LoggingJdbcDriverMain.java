package de.arnohaase.javastuff.jdbcdriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class LoggingJdbcDriverMain {
	public static void main(String[] args) throws Exception {
//		DriverManager.registerDriver(new LoggingJdbcDriver());
		
		final Connection conn = DriverManager.getConnection("jdbc:logger:jdbc:h2:tcp://localhost/omd", "sa", "");
		final PreparedStatement stmt = conn.prepareStatement("select * from TBLUSERPROFILE where oid > ?");
		stmt.setLong(1, 2L);
		final ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			System.out.println(rs.getString("userid"));
		}
		conn.close();
	}
}

