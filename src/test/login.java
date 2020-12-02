package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class login {
	
	private int userid, option;
	private String loguser;
	private ResultSet rs;
	private Connection con;
	private Statement st;
	
	login(){
		
		
		
		 try {

	            Class.forName("com.mysql.cj.jdbc.Driver");
	            System.out.println("Driver Loaded");
	            String url = "jdbc:mysql://localhost/car?user=root&password=pratik";
	             con = DriverManager.getConnection(url);
	            System.out.println("connection to database created");

	            st  = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
	            System.out.println("Statement Object Created");
	            
	          
	            
	            Scanner sc = new Scanner(System.in);
	            HashMap<Integer, String> details = new HashMap<>();
	            details.put(1, "Warehouse");
	            details.put(2, "DC");
	            details.put(3, "Dealer");
	            
	            
	            boolean login_status = false;
	           
	            
	            
	            while(!login_status) {
	            	
	            	System.out.println(" Login as: \n");
	            	details.forEach((k, v) -> System.out.println(k + " : " + v));
	            	System.out.println("\n");
	            	option = sc.nextInt();
	            	
	            	if(!(details.containsKey(option))) {
	            		System.out.println("Invalid option");
	            		continue;
	            	}
	            	
	            	System.out.println("Enter "+ details.get(option) + " ID:\n");
	            	
	            	userid = sc.nextInt();
	            	 rs = st.executeQuery("select * from car."+details.get(option)+" where id = "+ userid);
	            	
	            	if(rs.next() == false) {
	            		System.out.println("Invalid "+details.get(option)+" ID. Try Again\n");
	            		continue;
	            	}
	                
	            	System.out.println("Welcome "+ details.get(option)+ " "+rs.getString("address")+" "+ rs.getString("City")+" "+rs.getString("State")+"\n");
	            	loguser = details.get(option);
	            	login_status = true;
	            	
	            }
	            
	           

	            
	        } catch (Exception e) {
	            //TODO: handle exception

	            System.out.println("Error: " +e.getMessage());
	            
	            
	        }
		 
		 
	}
	
	public int log_userid() {
		
		return userid;
	}
	
	public String logtype() {
		return loguser;
	}
	
	
	
	public Statement statement() {
		return st;
	}
	
	public Connection connection() {
		return con;
	}
	public void closeconnection() {
		try {
			rs.close();
			st.close();
			con.close();
			System.out.println("Closed all bye bye");
			
		}
		catch(Exception e) {
			
			System.out.println("Error: "+ e.getMessage());
			
			
		}
		
	}
}
