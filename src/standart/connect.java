package standart;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.net.URL;
import java.lang.Thread;
import standart.procedures;

//Net
import java.net.UnknownHostException;
import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

//import java.util.Arrays;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.net.URL;

//import com.mysql.jdbc.*;


public class connect {
	
	//String domain = null;
	procedures procedure = new procedures();
	
	/**
	 * @throws IOException
	 * @throws UnknownHostException
	 * @throws InterruptedException 
	 */
	
	public String getLink(int i, ArrayList<String> al)
	{
		if(al.size()>i)
			return al.get(i);
		else
			return "#busy";
	}
	
	public void start() throws IOException, UnknownHostException, InterruptedException
	{
		Runtime r=Runtime.getRuntime(); 
		ArrayList<String> LinstOfLink = new ArrayList<String>();
		String Charset = "windows-1251";
		
		String Domain = new String();
	    Domain=procedure.getDomain();
	    System.out.println("getDomain - "+Domain);
	    System.out.println(Long.toString(r.totalMemory())+" - "+Long.toString(r.freeMemory()));
	    
	    while(!Domain.equals("#busy"))
	    {
	    	
	    	int ff = 0;
	    	String FLink = procedure.getUrl(Domain);
	    	Charset = procedure.getCharset(FLink);
	    	String url = "#busy";
		    LinstOfLink = procedure.getListLink(Domain);
		    ArrayList<String> PublicLink = new ArrayList<String>();
		    
		    url = FLink;
		    
		    System.out.println("URL:"+url);
		    
		    
		    while (!url.equals("#busy")) {
		    	URL connection = null;
				HttpURLConnection urlconn = null;
				String text = "";
				String ContentType = "";
				//String ContentTypeCharset = "";
				
				//String Charset = "UTF-8";
				
				String Title = null;
				String Desc = null;
				//String Title ="";	
				try
				{
					
				 	
					System.out.println("="+Integer.toString(ff)+"-"+Integer.toString(LinstOfLink.size()));
					connection = new URL(url);
					
					urlconn = (HttpURLConnection) connection.openConnection();
					//System.out.println(urlconn.getContent()+"|"+urlconn.getResponseMessage()+"|"+urlconn.getResponseCode());
				    urlconn.addRequestProperty("http.agent", "Java Search System | Index bot | beta 0.1.2 > go.delux.tj");
					urlconn.addRequestProperty("User-Agent", "Java Search System | Index bot | beta 0.1.2 > go.delux.tj");
					
				    //urlconn.setReadTimeout(20*1000);
					urlconn.connect();
					
				    
				    ContentType=urlconn.getContentType();
				    if(ContentType.length()>8)
					{
						if(ContentType.substring(0, "text/html".length()).equals("text/html"))
						{
							//if(Charset.equals("windows-1251"))
							//{
							//	Charset="cp1251";
							//}
							//System.out.println(Charset);
						    //java.io.InputStream in = urlconn.getInputStream();
						    InputStreamReader isr = new InputStreamReader(urlconn.getInputStream(), Charset);
						    BufferedReader br = new BufferedReader(isr);
						    //BufferedReader reader = new BufferedReader(new InputStreamReader(in));					    
						    String line = null;
						    while ((line = br.readLine()) != null)
						    {
						      text += line;
						    }
						    br.close();
						}
					}
				}
				catch(Exception e)
				{
					System.out.println("#Error2 "+e.getMessage());
					connection=null;
				}
				
		
				if(connection!=null)
				{
					if(urlconn.getResponseCode()==200)
					{
						
						ContentType=urlconn.getContentType();
						if(ContentType.length()>8)
						{
							if(ContentType.substring(0, "text/html".length()).equals("text/html"))
							{
								Charset=procedure.getCharsetFromHTML(text);
								
							    //String str;
							   
							   if(Charset.equals("UTF-8"))
							   {
								   //text = text;
								   
								   // byte[] utf8Bytes = text.getBytes("windows-1251");
								   //str = new String(utf8Bytes, "UTF-8");
								  // System.out.println("Charset: utf-8");
							    }
							    else
							    {
							    	//str = text;

							    	 //byte[] defaultBytes = text.getBytes();
							    	 //byte[] utf8Bytes = text.getBytes("UTF-8");
									 // System.out.println("#Charset: windows-1251");
							    	//byte[] bytes = text.getBytes("windows-1251");
							    	//text = new String(text.getBytes(Charset), "UTF-8");
							    
							    }
							   
								Document html = Jsoup.parse(text);
								Elements ElTitle = html.select("title");
								Title = ElTitle.html();
								if(Title.length()==0)
									Title=connection.getHost();
								Elements ElDesc = html.select("meta[name=description]");
								Desc = ElDesc.attr("content");
								if(Desc.length()==0)
									Desc=Title;
								
								//Get All links
								Elements Links = html.getElementsByTag("a");
								for(Element link : Links) {
							    	String _link = link.attr("href");
							    	int status = 0;
							    	
							    if(_link.length()>"javascript:".length())
							    	{
								    	if(_link.substring(0, "javascript:".length()).equals("javascript:"))
								    	{
								    		status=1;		   
								    	}
							    	}
							    	
							    		
							    
							    		if(!_link.equals("#"))
							    		{
							    			
							    			
							    			if(_link.length()>"https://".length())
							    			{
							    			
							    				String f = _link.replace("https://", "");
							    				if(f.length()!=_link.length())
							    					status=1;
								    			if(_link.substring(0, "https://".length())=="https://")
								    			{
								    				status=1;
								    			}
							    			}
							    		    
							    			if(status==0)
							    			{
							    			    
							    				if(_link.length()>"ftp://".length())
							    				{
							    					String f = _link.replace("ftp://", "");
								    				if(f.length()!=_link.length())
								    					status=1;
							    					if(_link.substring(0, "ftp://".length())=="ftp://")
								    				status=1;
							    				}
							    				
							    				
							    				if(status==0)
							    				{
							    					
								    					if((!_link.equals("./"))&(!_link.isEmpty()))
								    					{
								    					
								    						if(_link.length()>3)
								    						{
								    							if(_link.substring(0,3).equals("../"))
								    							{
								    								_link = _link.replaceAll("../","");
								    								String _u = ClearUrl(url, Domain);
								    								if(!_u.substring(_u.length()-1, _u.length()).equals("/"))
								    									_link="http://"+_u+"/"+_link;
								    								else
								    									_link="http://"+_u+_link;
								    							}
								    						}
								    						
								    						if(_link.substring(0,1).equals("?"))
								    						{
								    							String _u=this.ClearUrl(url, Domain);
								    							if(!_u.substring(_u.length()-1, _u.length()).equals("/"))
								    								_link="http://"+_u+"/"+_link;
								    							else
								    								_link="http://"+_u+_link;
								    						}
								    						else if(_link.substring(0,1).equals(" "))
								    						{
								    							_link.substring(1, _link.length());
								    						}
								    						else if(_link.length()>"http://".length())
								    						{	
								    							_link=_link.replaceFirst(" ", "");
									    						if(!_link.substring(0, "http://".length()).equals("http://"))
									    						{
									    							status=3;
									    						}
									    						
								    						}
								    						else
								    						{
								    							status=3;
								    						}
								    						
								    						
								    						if(status==3)
								    						{
								    							String _u=this.ClearUrl(url, Domain);
								    							//System.out.println("## "+url+"|"+_u+"+"+_link);
									    						if(!_link.substring(0, 1).equals("/"))
								    							{
								    								if(!_u.substring(0,1).equals("/"))
								    									_link=_u+"/"+_link;
								    								else
								    									_link=_u+_link;
								    							}
								    							else
								    							{
								    								//if(!_u.substring(0,1).equals("/"))
								    								//	_link=_u+_link;
								    								//else
								    									_link="http://"+Domain+_link;
								    							}
								    						}
								    					
								    						_link=_link.replaceAll("http://www.", "http://");
								    						
								    						_link=_link.replaceAll("http://", "");
								    						_link=_link.replaceAll("//", "/");
								    						_link=_link.replaceAll("/./", "/");
								    						_link="http://"+_link;
								    						
								    						
								    						//Clear #
								    						_link=this.CleanLink(_link);
								    						

								    						if(getDomainFromLink(_link).equals(Domain))
								    						{
									    						if(!this.InArray(LinstOfLink, _link))
																{
									    							//System.out.println("+#4"+_link);
																	//if(!procedure.HasLink(_link))
																	//{
																		//Add Procedure
																		//System.out.println("-"+_link);
																		procedure.AddLinkToUpdate(_link, getDomainFromLink(_link), 1, 0);
																		LinstOfLink.add(_link);
																		
																		//System.out.println("--#5"+_link);
																	//}
																}
								    						}
								    						else
								    						{
								    							if(!this.InArray(PublicLink, _link))
																{
								    								PublicLink.add(_link);
																}
								    						}
								    						procedure.AllExecute(r);
								    					}
							    			}
							    		}
							    	}
						        }
							System.out.println(Charset+" - "+Title);
							procedure.ToUpdateLink(Title, Desc, url, Domain, 0);
							
						}
						else
							procedure.ToUpdateLinkError(url, 97);
					}
					else
						procedure.ToUpdateLinkError(url, 97);
				}
				else
					procedure.ToUpdateLinkError(url, 404);
					
				}
				else
					procedure.ToUpdateLinkError(url, 99);
				
				
				url = getLink(ff,LinstOfLink);
				ff++;
				
				Thread.sleep(100);
				//System.out.println(Long.toString(r.totalMemory())+" - "+Long.toString(r.freeMemory()));
		    }
		    
		    //String tmpFileName = "tmp"+Domain+".sql";
		    //String FileName = "sql/"+Domain+".sql";
		    
		    //FileWriter fw = new FileWriter(tmpFileName);
		 	//BufferedWriter bw = new BufferedWriter(fw);
		 	
		    System.out.println("#Finish Step1");
		    procedure.ExecuteAddLink();
		    //procedure.ExecuteAddLinkFile(bw);
		    System.out.println("#Finish Step2");
		    procedure.ExecuteToUpdateLinkError();
		    //procedure.ExecuteToUpdateLinkErrorFile(bw);
		    System.out.println("#Finish Step3");
		    procedure.ExecuteToUpdateLink();
		    //procedure.ExecuteToUpdateLinkFile(bw);
		    System.out.println("#Finish Step4"); 
		    r.gc();
		    //System.out.println(Long.toString(r.totalMemory())+" - "+Long.toString(r.freeMemory()));
		    
		    
		    //bw.close();
		    //fw.close();
		    
		    //File oldfile = new File(tmpFileName);
			//File newfile = new File(FileName);
			
			//oldfile.renameTo(newfile);
		    
		   // File
		    
		    procedure.setDomainStatus(Domain, 2);
		    Domain=procedure.getDomain();
		    if(Domain=="#busy")
		    {
			    while(Domain=="#busy")
			    {
			    	Domain=procedure.getDomain();
			    	System.out.println("can't get domain all is busy =(");
			    	Thread.sleep(10000);
			    }
		    }
		    System.out.println("getDomain - "+Domain);
	    }
	   
	    System.out.println("Finish");
	}
	
	public boolean InArray(ArrayList<String> Arr, String Obj)
	{
		boolean s = false;
		if(Arr.isEmpty())
			return false;
			
		for (String string : Arr) {
			if(string.equals(Obj))
				s=true;
		}
		
		return s; 
	}
	
	private String getDomainFromLink(String _link)
	{
		String _domain = "";
		try
		{
		   URL url = new URL(_link);
		   _domain = url.getHost();
		}
		catch(Exception e)
		{
			System.out.println("#error "+e.getMessage());
		}
		return _domain;
	}
	
	private String CleanLink(String _lin)
	{
		String[] _u = _lin.split("#");
		if(_u.length>0)
		{
			_lin=_u[0];
		}
		return _lin;
	}
	
	public String ClearUrl(String Url, String d)
	{		
		String[] _u = Url.split("/");
		if(_u.length>1)
		{
			
			String ss = _u[_u.length-1];
			
			String _ur = ss.replace(".","");
			if(ss.length()!=_ur.length())
				Url=Url.replace(ss, "");
			else
			{
				String[] _s = Url.split("\\?");
				if(_s.length>1)
					Url=_s[_s.length-1];
			}
			
			
			
			String dd = Url.replace(d, "");
			if(Url==dd)
				Url="http://"+d;
			
		}
		else
		{
			Url="";
		}

		
		return Url;
	}
	
}
