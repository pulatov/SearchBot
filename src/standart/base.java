package standart;

import standart.connect;



public class base {

	private static connect Conn;
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println("test text");
		Conn = new connect();
		Conn.start();
	}
	
}
