package standart;

import standart.sql;

import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
//import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


//import java.sql.SQLException;

public class procedures {
	
	public Map<String, ArrayList<String>> HashMaps = new HashMap<String, ArrayList<String>>();
	
	private Integer Counts = 0;
	
	
	@SuppressWarnings("rawtypes")
	public ArrayList<ArrayList> _InfoArray = new ArrayList<ArrayList>();
	@SuppressWarnings("rawtypes")
	public ArrayList<ArrayList> _InfoArray2 = new ArrayList<ArrayList>();
	@SuppressWarnings("rawtypes")
	public ArrayList<ArrayList> _InfoArray3 = new ArrayList<ArrayList>();
	
	
	public ArrayList<String> getListLink(String _domain)
	{
		ArrayList<String> _list = new ArrayList<String>();
		Connection con = null;
		try
		{
			con = sql.getInstance().getConnect();
			PreparedStatement statement =  con.prepareStatement("SELECT link FROM s_link WHERE domain=?");
			statement.setString(1, _domain);
			
			ResultSet rset = statement.executeQuery();
			while(rset.next())
			{
				_list.add(rset.getString("link"));
			}
			
			
			rset.close();
			statement.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sql.close(con);
		}
		return _list;
	}
	
	public void UpdateLinkWithError(String _link, int ErrorCode)
	{
		Connection con = null;
		try
		{
			long unixTime = System.currentTimeMillis() / 1000L;
			con = sql.getInstance().getConnect();
			PreparedStatement statement =  con.prepareStatement("UPDATE s_link SET error=?, dtime=?, sts=? WHERE link=?");
			statement.setInt(1, ErrorCode);
			statement.setLong(2, unixTime);
			statement.setInt(3, 2);
			statement.setString(4, _link);

			statement.execute();
			
			statement.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sql.close(con);
		}
	}
	
	
	public void UpdateLink(String _title, String _desc, String _link, String _domain)
	{
		Connection con = null;
		try
		{
			long unixTime = System.currentTimeMillis() / 1000L;
			con = sql.getInstance().getConnect();
			PreparedStatement statement =  con.prepareStatement("UPDATE s_link SET sts=?, dtime=?, error=?, title=?, descr=? WHERE domain=? and link=?");
			statement.setInt(1, 2);
			statement.setLong(2, unixTime);
			statement.setInt(3, 0);
			statement.setString(4, _title);
			statement.setString(5, _desc);
			statement.setString(6, _domain);
			statement.setString(7, _link);
			statement.execute();
			
			statement.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sql.close(con);
		}
	}
	
	public void AddLinkToUpdate(String _link, String _domain, int sts, int err)
	{
		
		ArrayList<String> _InfoList = new ArrayList<String>();
		
		_InfoList.add(_link);
		_InfoList.add(_domain);
		_InfoList.add(Integer.toString(sts));
		_InfoList.add(Integer.toString(err));
		
		_InfoArray.add(_InfoList);
	
		Counts++;

	}
	
	public void ExecuteAddLink()
	{
		if(Counts!=0)
		{

			Connection con = null;
			try
			{
				//System.out.println("Executed::"+Integer.toString(fff));
				long unixTime = System.currentTimeMillis() / 1000L;
				con = sql.getInstance().getConnect(); //IN l VARCHAR(1000), IN s INT, IN t BIGINT,IN d VARCHAR(500)
				//PreparedStatement statement =  con.prepareStatement("INSERT s_link SET link=?, sts=?, regtime=?, error=?, domain=?, domain_id=?, dtime=?");
				PreparedStatement statement =  con.prepareStatement("CALL add_link(?,?,?,?,?)");
				
				
				
				//while(fff!=0)
				for(ArrayList<String> tt: _InfoArray)
				{
				
						//fff--;
						//System.out.println("Counts::"+Integer.toString(fff));
						//@SuppressWarnings("unchecked")
						//ArrayList<String> tt = _InfoArray.get(fff);
						//tt = _;
						//System.out.println(tt.get(0).toString());
						//long unixTime = System.currentTimeMillis() / 1000L;
						//con = sql.getInstance().getConnect();
						statement.setString(1, tt.get(0).toString());
						statement.setInt(2, Integer.parseInt(tt.get(2).toString()));
						statement.setLong(3, unixTime);
						//statement.setInt(4, 0);
						statement.setString(4, tt.get(1).toString());
						statement.setInt(5, Integer.parseInt(tt.get(3).toString()));
						//statement.setInt(6, 0);
						//statement.setLong(7, unixTime);
						statement.addBatch();
						//System.out.println("!#"+Counts+" - title :"+tt.get(0).toString()+" | _desc "+tt.get(1).toString()+" | domain "+tt.get(2).toString()+" | LINK "+tt.get(3).toString());
					
		//			Counts--;
				}
				//Counts=0;
				//_InfoArray.clear();
				//fff=0;
				statement.executeBatch();
				//con.commit();
				//statement.execute();
				statement.close();
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				Counts=0;
				_InfoArray.clear();
				//fff=0;
				sql.close(con);
			}
		}
	}
	
	public void ExecuteAddLinkFile(BufferedWriter bw)
	{
		if(Counts!=0)
		{
			//int fff=_InfoArray.size();
			//Connection con = null;
			try
			{
				//System.out.println("Executed::"+Integer.toString(fff));
				long unixTime = System.currentTimeMillis() / 1000L;
				//con = sql.getInstance().getConnect(); //IN l VARCHAR(1000), IN s INT, IN t BIGINT,IN d VARCHAR(500)
				//PreparedStatement statement =  con.prepareStatement("INSERT s_link SET link=?, sts=?, regtime=?, error=?, domain=?, domain_id=?, dtime=?");
				//PreparedStatement statement =  con.prepareStatement("CALL add_link(?,?,?,?,?)");
				
				
				
				 //	FileWriter fw = new FileWriter(dd+".sql");
				 //	BufferedWriter bw = new BufferedWriter(fw);
				    
					for(ArrayList<String> tt: _InfoArray)
					{
						String wString = "INSERT s_link SET link='"+tt.get(0).toString()+"', sts='"+Integer.parseInt(tt.get(2).toString())+"', regtime='"+Long.toString(unixTime)+"', error=0, domain='"+tt.get(1).toString()+"', domain_id=0, dtime='"+Long.toString(unixTime)+"';\n";
					    bw.write(wString);
					
					}
					//bw.close();
				//Counts=0;
				//_InfoArray.clear();
				//fff=0;
				//statement.executeBatch();
				//con.commit();
				//statement.execute();
				//statement.close();
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				Counts=0;
				_InfoArray.clear();
				//fff=0;
				//sql.close(con);
			}
		}
	}
		
	
	public void AddLink(String _link,String _domain, int sts)
	{
		Connection con = null;
		try
		{
			long unixTime = System.currentTimeMillis() / 1000L;
			con = sql.getInstance().getConnect();
			PreparedStatement statement =  con.prepareStatement("INSERT s_link SET link=?, sts=?, regtime=?, error=?, domain=?, domain_id=?, dtime=?");
			statement.setString(1, _link);
			statement.setInt(2, sts);
			statement.setLong(3, unixTime);
			statement.setInt(4, 0);
			statement.setString(5, _domain);
			statement.setInt(6, 0);
			statement.setLong(7, unixTime);
			statement.addBatch();
			
			statement.execute();
			
			statement.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sql.close(con);
		}
	}
	
	public String getCharset(String _link)
	{
		URL connection = null;
		HttpURLConnection urlconn = null;
		String text = "";
		String ContentType = "";
		//String ContentTypeCharset = "";
		String Charset = "windows-1251";
		
		try
		{
			
			connection = new URL(_link);
			
			urlconn = (HttpURLConnection) connection.openConnection();
		    urlconn.addRequestProperty("http.agent", "Java Search System | Index bot | beta 0.1.2 > go.delux.TJ");
			urlconn.addRequestProperty("User-Agent", "Java Search System | Index bot | beta 0.1.2 > go.delux.TJ");
			
		    //urlconn.setReadTimeout(20*1000);
		    urlconn.connect();
		    ContentType=urlconn.getContentType();
		    if(ContentType.length()>8)
			{
				if(ContentType.substring(0, "text/html".length()).equals("text/html"))
				{
				    //java.io.InputStream in = urlconn.getInputStream();
				    InputStreamReader isr = new InputStreamReader(urlconn.getInputStream(), "UTF-8");
				    BufferedReader br = new BufferedReader(isr);
				    
				    //BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				    
				    String line = null;
				    while ((line = br.readLine()) != null)
				    {
				      text += line;
				    }
				}
			}
		}
		catch(Exception e)
		{
			//System.out.println("#Error2 "+e.getMessage());
			connection=null;
		}
		
		if(text.length()>0)
		{
			Charset=this.getCharsetFromHTML(text);
			System.out.println(Charset);
		}
		return Charset;
	}
	
	public String getCharsetFromHTML(String text)
	{
		String ContentTypeCharset = "";
		int Type=0;
		String Charset = "windows-1251";
		if(text.length()>0)
		{
					Document doc = Jsoup.parse(text);
				    Elements h = doc.select("meta[http-equiv=Content-Type]");
				    for(Element head : h) {
				    	ContentTypeCharset=head.attr("content");
				    	Type=1;
				    	//System.out.println("#Type - 1");
				    }
		
				    if(Type==1)
				    {
					    String[] sp = ContentTypeCharset.split("=");
					    if(sp.length==2)
					    {
						    if(sp[1].toString().equals("utf-8"))
						    {
						    	Charset="UTF-8";
						    	
						    }
						    else if(sp[1].toString().equals("UTF-8"))
						    {
						    	Charset="UTF-8";
						    	
						    }
					    }
					    else if(!Charset.equals("UTF-8"))
					    {
					    	Elements hh = doc.select("meta");
						    for(Element head : hh) {
						    	if(head.attr("charset").equals("utf-8"))
						    	{
						    		Charset="UTF-8";
						    		
						    	}
						    	else if(head.attr("charset").equals("UTF-8"))
						    	{
						    		Charset="UTF-8";
						    		
						    	}
						    	else
						    	{
						    		Charset = "windows-1251";
						    		
						    	}
						    }	    
					    }
					    else
					    {
					    	Charset = "windows-1251";
					    }
				    }
				    else
				    {
				    	Elements Links = doc.getElementsByTag("meta");
						for(Element link : Links) {
							if(!link.attr("charset").isEmpty())
					    	ContentTypeCharset = link.attr("charset");
					    	//System.out.println("#Type - 2: "+ContentTypeCharset);
						}
						
						if(ContentTypeCharset.equals("UTF-8"))
						{
							Charset="UTF-8";
							//System.out.println("#Type - 2: "+Charset);
						}
						else
						{
							Charset="windows-1251";
							//System.out.println("#Type - 2: "+Charset);
						}
				    }
		}
		return Charset;
	}
	
	public void ToUpdateLinkError(String _link, int err)
	{
		ArrayList<String> _InfoLists = new ArrayList<String>();
		
		_InfoLists.add(_link);
		_InfoLists.add(Integer.toString(err));
		_InfoArray3.add(_InfoLists);
	}
	
	public void ExecuteToUpdateLinkError()
	{
		//int fff = _InfoArray3.size();
		Connection con = null;
		
		try
		{
			long unixTime = System.currentTimeMillis() / 1000L;
			con = sql.getInstance().getConnect();
			PreparedStatement statement =  con.prepareStatement("UPDATE s_link SET error=?, dtime=?, sts=? WHERE link=?");
			//while(fff!=0)
			for(ArrayList<String> tt: _InfoArray3)
			{
				statement.setInt(1, Integer.parseInt(tt.get(1).toString()));
				statement.setLong(2, unixTime);
				statement.setInt(3, 2);
				statement.setString(4, tt.get(0).toString());
				statement.addBatch();
				
			}
		//	fff=0;
			statement.executeBatch();
			statement.close();			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			_InfoArray3.clear();
			//fff=0;
			sql.close(con);
		}
	}
	
	public void ExecuteToUpdateLinkErrorFile(BufferedWriter bw)
	{

		try
		{
			long unixTime = System.currentTimeMillis() / 1000L;
			
			//FileWriter fw = new FileWriter(dd+".sql");
		 	//BufferedWriter bw = new BufferedWriter(fw);
		  
			for(ArrayList<String> tt: _InfoArray3)
			{
				String wString = "UPDATE s_link SET error='"+Integer.parseInt(tt.get(1).toString())+"', dtime='"+Long.toString(unixTime)+"', sts='2' WHERE link='"+tt.get(0).toString()+"';\n";
			    bw.write(wString);
			}
			//bw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			_InfoArray3.clear();
		}
	}
	
	public void ToUpdateLink(String _title, String _desc, String _link, String _domain, int err)
	{
		
		ArrayList<String> _InfoLists = new ArrayList<String>();
		
		_InfoLists.add(_title);
		_InfoLists.add(_desc);
		_InfoLists.add(_link);
		_InfoLists.add(_domain);
		_InfoLists.add(Integer.toString(err));
		
		_InfoArray2.add(_InfoLists);
		
		//CountsLink++;
		
	}
	
	public void AllExecute(Runtime r)
	{
		if(_InfoArray2.size()>2000)
		{
			this.ExecuteAddLink();
			this.ExecuteToUpdateLinkError();
		    this.ExecuteToUpdateLink();
		    r.gc();
		}
	}
	
	
	public void ExecuteToUpdateLink()
	{
		
			Connection con = null;
			System.out.println("Start ExecuteToUpdateLink");
			try
			{
				long unixTime = System.currentTimeMillis() / 1000L;
				con = sql.getInstance().getConnect();
				PreparedStatement statement =  con.prepareStatement("UPDATE s_link SET sts=?, dtime=?, error=?, title=?, descr=? WHERE link=?");
				
				for(ArrayList<String> tt: _InfoArray2)
				{
					statement.setInt(1, 2);
					statement.setLong(2, unixTime);
					statement.setInt(3, 0);
					statement.setString(4, tt.get(0).toString());
					statement.setString(5, tt.get(1).toString());
					//statement.setString(6, tt.get(3).toString());
					statement.setString(6, tt.get(2).toString());
					statement.addBatch();
				}
				statement.executeBatch();
				statement.close();
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				_InfoArray2.clear();
				sql.close(con);
			}
	}
	
	public void ExecuteToUpdateLinkFile(BufferedWriter bw)
	{
		
			//Connection con = null;
			//System.out.println("Start Create File");
			try
			{
				long unixTime = System.currentTimeMillis() / 1000L;
				//con = sql.getInstance().getConnect();
				//PreparedStatement statement =  con.prepareStatement("UPDATE s_link SET sts=?, dtime=?, error=?, title=?, descr=? WHERE domain=? and link=?");
				//String  fileName = "d:\\file.txt";
		        //FileWriter fw = new FileWriter(dd+".sql");
		    	//BufferedWriter bw = new BufferedWriter(fw);
			    
				for(ArrayList<String> tt: _InfoArray2)
				{
					String wString = "UPDATE s_link SET sts='2', dtime='"+Long.toString(unixTime)+"', error='0', title='"+tt.get(0).toString()+"', descr='"+tt.get(1).toString()+"' WHERE domain='"+tt.get(3).toString()+"' and link='"+tt.get(2).toString()+"';\n";
				    bw.write(wString);
			        
				//	statement.setInt(1, 2);
				//	statement.setLong(2, unixTime);
				//	statement.setInt(3, 0);
				//	statement.setString(4, tt.get(0).toString());
				//	statement.setString(5, tt.get(1).toString());
				//	statement.setString(6, tt.get(3).toString());
				//	statement.setString(7, tt.get(2).toString());
				//	statement.addBatch();
				}
				
				//bw.close();
			//	statement.executeBatch();
			//	statement.close();
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				
				_InfoArray2.clear();
				//	sql.close(con);
			}
	}
	
	
	public boolean HasLink(String _link)
	{
		boolean s=false;
		Connection con = null;
		try
		{

			con = sql.getInstance().getConnect();
			PreparedStatement statement =  con.prepareStatement("SELECT id FROM s_link WHERE link=? LIMIT 1");
			statement.setString(1, _link);
			
			ResultSet rset = statement.executeQuery();
			while(rset.next())
			{
				s=true;
			}
			
			rset.close();
			statement.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sql.close(con);
		}
		
		return s;		
	}
	
	public int CountOnStatus(int Status)
	{
		Connection con = null;
		int i = 0;
		
		try
		{
			con = sql.getInstance().getConnect();
			PreparedStatement statement =  con.prepareStatement("SELECT COUNT(*) as c FROM s_domain WHERE sts=?");
			statement.setInt(1, 1);
			
			ResultSet rset = statement.executeQuery();
			while(rset.next())
			{
				i = rset.getInt("c");
			}
			
			rset.close();
			statement.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sql.close(con);
		}
		return i;
	}
	
	public void setDomainStatus(String _domain, int statusID)
	{
		Connection con = null;
		
		try
		{
			long unixTime = System.currentTimeMillis() / 1000L;
			con = sql.getInstance().getConnect();
			PreparedStatement statement =  con.prepareStatement("UPDATE s_domain SET sts=?, lastupdate=? WHERE domain=?");
			statement.setInt(1, statusID);
			statement.setLong(2, unixTime);
			statement.setString(3, _domain);
			statement.execute();
			
			statement.close();
			
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		finally
		{
			sql.close(con);
		}
	}
	
	
	
	public void setLinksStatus(String _domain, int statusID)
	{
		Connection con = null;
		
		try
		{
			con = sql.getInstance().getConnect();
			PreparedStatement statement =  con.prepareStatement("UPDATE s_link SET sts=? WHERE domain=?");
			statement.setInt(1, statusID);
			statement.setString(2, _domain);
			statement.execute();
			
			statement.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sql.close(con);
		}
	}
	
	public int CountLink(String _domain)
	{
		int i = 0;
		Connection con = null;
		try
		{
			con = sql.getInstance().getConnect();
			PreparedStatement statement =  con.prepareStatement("SELECT COUNT(*) as c FROM s_link WHERE domain=?");
			statement.setString(1, _domain);
			
			ResultSet rset = statement.executeQuery();
			while(rset.next())
			{
				i = rset.getInt("c");
			}
			
			rset.close();
			statement.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sql.close(con);
		}
		return i;
	}
	
	public String getUrl(String _domain)
	{
		String url = "http://"+_domain;
		this.setLinksStatus(_domain, 3);
		if(this.CountLink(_domain)!=0)
			url = this.getLinkByDomain(_domain);
	
		return url;
	}
	
	public void Finish(String _domain)
	{
		
		
	}
	
	public String getDomain()
	{
		Connection con = null;
		String _domain = "#busy";
		
		try
		{
			
			int i = this.CountOnStatus(1);

			if(i<4)
			{
				long unixTime = System.currentTimeMillis() / 1000L;
				unixTime = unixTime - (86400*1);
				con = sql.getInstance().getConnect();  //SELECT domain FROM s_domain WHERE sts=2 and zone='tj' and lastupdate<? ORDER by lastupdate LIMIT 1
				PreparedStatement statement =  con.prepareStatement("SELECT domain FROM s_domain WHERE sts=2 and zone='tj' ORDER by lastupdate LIMIT 1");
				//statement.setLong(1, unixTime);
				
				ResultSet rset = statement.executeQuery();
				while(rset.next())
				{
					_domain = rset.getString("domain");
					this.setDomainStatus(_domain, 1);
				}
				rset.close();
				statement.close();
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			sql.close(con);
		}
		
		return _domain;
	}
	
	public void AddLinkByDomamin(String _link)
	{
		long unixTime = System.currentTimeMillis() / 1000L;
		Connection con = null;
		String domain = null;	
		
			
		try
		{
			URL currentUrl = new URL(_link);
			domain=currentUrl.getHost();
			
			con = sql.getInstance().getConnect();
			PreparedStatement statement =  con.prepareStatement("INSERT s_link SET link=?, sts=?, regtime=?, error=?, domain=?");
			statement.setString(1, _link);
			statement.setInt(2, 1);
			statement.setLong(3, unixTime);
			statement.setInt(4, 0);
			statement.setString(5, domain);
			statement.execute();
			
			statement.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sql.close(con);
		}
	}
	
	public String getLinkByDomain(String _domain)
	{
		if(Counts!=0)
			this.ExecuteAddLink();
			
			
		Connection con = null;
		String url = "#busy";
	
		try
		{

				con = sql.getInstance().getConnect();
				PreparedStatement statement =  con.prepareStatement("SELECT link FROM s_link WHERE domain=? and (sts=1 or sts=3) LIMIT 1");
				statement.setString(1, _domain);
				
				ResultSet rset = statement.executeQuery();
				while(rset.next())
				{
					url = rset.getString("link");
				}
				rset.close();
				statement.close();
			//}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			sql.close(con);
		}
		
		return url;
	}
	
	
}
