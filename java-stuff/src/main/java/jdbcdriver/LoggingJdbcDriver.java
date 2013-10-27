package jdbcdriver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;


public class LoggingJdbcDriver implements Driver {
	public static final String PREFIX = "jdbc:logger:";
	
	static {
		try {
			DriverManager.registerDriver(new LoggingJdbcDriver());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		if (acceptsURL(url)) {
			final Driver innerDriver = DriverManager.getDriver(url.substring(PREFIX.length()));
			final Connection innerConn = innerDriver.connect(url.substring(PREFIX.length()), info);
			return (Connection) Proxy.newProxyInstance(innerConn.getClass().getClassLoader(), new Class[] {Connection.class}, new LoggingInvocationHandler(innerConn));
		}
		else {
			return null;
		}
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return url.startsWith(PREFIX);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMajorVersion() {
		return 1;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public boolean jdbcCompliant() {
		return true;
	}

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}

class LoggingInvocationHandler implements InvocationHandler {
	private final Object inner;

	public LoggingInvocationHandler(Object inner) {
		this.inner = inner;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		final Object result = method.invoke(inner, args);
		
		System.out.println("invoking " + method + ": " + (args != null ? Arrays.asList(args) : "") + " -> " + result);
		if (result == null) {
			return null;
		}
		
		if (method.getReturnType().isInterface()) {
			return Proxy.newProxyInstance(result.getClass().getClassLoader(), new Class[] {method.getReturnType()}, new LoggingInvocationHandler(result));
		}
		return result;
	}
}