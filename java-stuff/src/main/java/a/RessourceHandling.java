package a;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;


public class RessourceHandling {
    static final DataSource ds = null;
    
    public void machWas () throws SQLException {
        final Connection conn = ds.getConnection();

        Exception exc = null;
        
        try {
            conn.prepareStatement("delete from TEMP_TABLE");
            conn.commit ();
        }
        catch (Exception e) {
            e = exc;
            conn.rollback ();
        }
        finally {
            try {
                conn.close ();
            }
            finally {
                if (exc instanceof RuntimeException) 
                    throw (RuntimeException) exc;
                if (exc instanceof SQLException)
                    throw (SQLException) exc;
            }
        }
    }
}
