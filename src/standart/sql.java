package standart;

import standart.config;

import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class sql {
	
	private static sql _instance;
	//private Connection conn = null;
	private config conf;
	private ComboPooledDataSource _source;
	
	public sql() throws SQLException
	{
		try
		{
		conf = new config();
		//Class.forName("com.mysql.jdbc.Driver");
		//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+conf.Getdbname()+"?useUnicode=true&characterEncoding=UTF-8", conf.Getlogin(), conf.Getpassword());
		
		_source = new ComboPooledDataSource();
		_source.setAutoCommitOnClose(true);
		
		_source.setInitialPoolSize(10);
		_source.setMinPoolSize(10);
		_source.setMaxPoolSize(Math.max(10, 200));
		
		_source.setAcquireRetryAttempts(0); // try to obtain connections indefinitely (0 = never quit)
		_source.setAcquireRetryDelay(500); // 500 milliseconds wait before try to acquire connection again
		_source.setCheckoutTimeout(0); // 0 = wait indefinitely for new connection
		// if pool is exhausted
		_source.setAcquireIncrement(5); // if pool is exhausted, get 5 more connections at a time
		// cause there is a "long" delay on acquire connection
		// so taking more than one connection at once will make connection pooling
		// more effective.
		
		// this "connection_test_table" is automatically created if not already there
		_source.setAutomaticTestTable("connection_test_table");
		_source.setTestConnectionOnCheckin(false);
		
		// testing OnCheckin used with IdleConnectionTestPeriod is faster than  testing on checkout
		
		_source.setIdleConnectionTestPeriod(3600); // test idle connection every 60 sec
		_source.setMaxIdleTime(0); // 0 = idle connections never expire
		// *THANKS* to connection testing configured above
		// but I prefer to disconnect all connections not used
		// for more than 1 hour
		_source.setDebugUnreturnedConnectionStackTraces(false);
		// enables statement caching,  there is a "semi-bug" in c3p0 0.9.0 but in 0.9.0.2 and later it's fixed
		_source.setMaxStatementsPerConnection(100);
		
		_source.setBreakAfterAcquireFailure(false); // never fail if any way possible
		// setting this to true will make
		// c3p0 "crash" and refuse to work
		// till restart thus making acquire
		// errors "FATAL" ... we don't want that
		// it should be possible to recover
		_source.setDriverClass("com.mysql.jdbc.Driver");
		_source.setJdbcUrl("jdbc:mysql://delux.tj/"+conf.Getdbname()+"?useUnicode=true&characterEncoding=UTF-8");
		_source.setUser(conf.Getlogin());
		_source.setPassword(conf.Getpassword());
		
		/* Test the connection */
		_source.getConnection().close();
		}
		
		catch (SQLException x)
		{
				System.out.println("Database Connection FAILED");
			// re-throw the exception
			throw x;
		}
		
		catch (Exception e)
		{
				System.out.println("Database Connection FAILED");
			throw new SQLException("Could not init DB connection:" + e.getMessage());
		}
	}
	
	public static sql getInstance() throws SQLException
	{
		synchronized (sql.class)
		{
			if (_instance == null)
			{
				_instance = new sql();
			}
		}
		return _instance;
	}
	
	
	public Connection getConnect()
	{
		Connection con1 = null;
		
		while (con1 == null)
		{
			try
			{
				con1 = _source.getConnection();
			}
			catch (SQLException e)
			{
				System.out.println(e.getMessage());
			}
		}
		return con1;
	}
	
	public static void close(Connection con)
	{
		if (con == null)
			return;
		
		try
		{
			con.close();
		}
		catch (SQLException e)
		{
			System.out.println("Failed to close database connection! "+e.toString());
		}
	}
}
