package standart;

public class config {
	
	private String dbname;
	private String password;
	private String login;
	private String server;
	private int Streams;
	
	public config()
	{
		this.dbname="c5search";
		this.login="c5search";
		this.password="123456";
		this.server="localhost";
		this.Streams=5;
	}
	
	public int GetStreams()
	{
		return this.Streams;
	}
	public String Getdbname()
	{
		return this.dbname;
	}
	
	public String Getpassword()
	{
		return this.password;
	}
	
	public String Getlogin()
	{
		return this.login;
	}
	
	public String GetServer()
	{
		return this.server;
	}

}
