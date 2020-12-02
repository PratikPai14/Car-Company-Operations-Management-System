package test;
import java.sql.*;
import java.util.*;
public class warehouse extends logout{
	private boolean sessionlogout = false;
	private int id ;
	private ResultSet rs;
	private Connection con;
	private Statement st;
	warehouse(int id, Connection con, Statement st){
		
		this.id = id;
		this.con = con;
		this.st = st;
		//this.rs = rs;
	}
	
	private void choices() {
		
System.out.println(" 1. View available cars and quantity\n 2. Add/Remove Cars\n 3. View Supply Chain\n 4. logout");
		
		Scanner sc = new Scanner(System.in);
		boolean isoption = false;
		int option = sc.nextInt();
		if((option != 1) || (option!= 2)||(option!= 3)||(option!= 4)) isoption = true;
		while(!isoption) {
			
			System.out.println("Invalid choice. Try Again\n");
			System.out.println("1. View available cars and quantity\n 2. Add/Remove Cars\n 3. View Supply Chain\n 4. logout");
			option = sc.nextInt();
			if((option != 1) || (option!= 2)||(option!= 3)||(option!= 4)) isoption = true;
			
		}
		
		if(option == 1) viewcars();
		
		if(option == 2) modifycars();
		
		if(option == 3) supplychain();
		
		if(option ==4 ) {
			sessionlogout = logoutsession(id);
		}

		
		
	}
	
	public void viewcars() {
		
		System.out.println("inside viewcars");
		System.out.println("");
		try {
			
			st.executeUpdate("create or replace view car.wrview as select distinct car.pr_avail.pr_id, car.product.pr_name, car.product.color, wr_quant from car.pr_avail  left join car.product on car.pr_avail.pr_id = car.product.pr_id  where car.pr_avail.Wr_id = "+id);
			
			rs = st.executeQuery("Select * from car.wrview");
			
			System.out.println("PR_ID\t\tPR_NAME\t\tCOLOR\t\tWR_QUANT");
			
			while(rs.next()) {
				
				int prid = rs.getInt("pr_id");
				int wrquant = rs.getInt("wr_quant");
				String prname = rs.getString("pr_name");
				String color = rs.getString("color");
				System.out.println(prid+"\t\t"+ prname+"\t\t"+color+"\t\t"+wrquant);
				
			}
			
			
			

			int min = 0, max = 0, total = 0;
			rs = st.executeQuery("Select sum(wr_quant) total, min(wr_quant) as min_quant, max(wr_quant) max_quant from car.wrview ");
			if(rs.next()) {
				 min = rs.getInt("min_quant");
				 max = rs.getInt("max_quant");
				 total = rs.getInt("total");
			}
			
			System.out.println("------------------------------------------------");
			System.out.println("TOTAL CARS: "+ total );
			
			rs = st.executeQuery("Select * from car.wrview where wr_quant = "+max);
			System.out.println("MAXIMUM: ");
			while(rs.next()) {
				
				int prid = rs.getInt("pr_id");
				String prname = rs.getString("pr_name");
				String color = rs.getString("color");
				System.out.println(prid+"\t\t"+ prname+"\t\t"+color+"\t\t"+max);
				
			}
			
			
			
			rs = st.executeQuery("Select * from car.wrview where wr_quant = "+min);
			
			System.out.println("MINIMUM: ");
			while(rs.next()) {
				
				int prid = rs.getInt("pr_id");
				String prname = rs.getString("pr_name");
				String color = rs.getString("color");
				System.out.println(prid+"\t\t"+ prname+"\t\t"+color+"\t\t"+min);
				
			}
			
			
			
			
			
			
			
			
		}
		
		catch(Exception e) {
			
			System.out.println("Error: " + e.getMessage());
		}
		
		System.out.println("");
		choices();
		
		
	}
	
	private void modifycars() {
		
		System.out.println("inside modify");
		
		try {
			
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter product ID to be updated:");
			int prid = sc.nextInt();
			System.out.println("Enter new quantity:");
			int updatequant = sc.nextInt();
			
			st.executeUpdate("create  or replace view car.wrview as select  pr_avail.pr_id,  wr_quant from car.pr_avail   where pr_avail.Wr_id ="+id);
			st.executeUpdate("update car.wrview set wr_quant = "+updatequant +" where pr_id ="+prid);
			
			
		} catch (Exception e) {
			
			System.out.println("Error:"+ e.getMessage());
		}
		
		System.out.println("");
		choices();
		
	}
	
	public void supplychain() {
		
		System.out.println("inside supplychain");
		System.out.println("");
		
		try {
			
			rs = st.executeQuery("select dc.id dcid, dc.city dccity , dc.state dcstate, dc.address dcaddr, dc.zipcode dczip, dc.email dcemail,dc.phone dcphone, dealer.id dlid, dealer.city dlcity, dealer.state dlstate, dealer.address dladdr, dealer.zipcode dlzip, dealer.email dlemail,dealer.phone dlphone from car.warehouse left join car.dc on warehouse.id = dc.wr_id left join car.dealer on dc.id = dealer.dc_id where warehouse.id = "+ id);
			System.out.println("Distribution Center:");
			if(rs.next()) {
				
				int dcid = rs.getInt("dcid");
				String dccity = rs.getString("dccity");
				String dcstate = rs.getString("dcstate");
				String dcaddr = rs.getString("dcaddr");
				int dczip = rs.getInt("dczip");
				String dcemail = rs.getString("dcemail");
				int dcphone = rs.getInt("dcphone");
				
				System.out.println(dcid + " "+ dcaddr + " "+ dccity + " "+ dcstate+ " "+ dczip+ " "+ dcemail+ " "+ dcphone );
				System.out.println("");
				
			}
			rs.beforeFirst();
			System.out.println("Dealers:");
			while(rs.next()) {
				
				int dlid = rs.getInt("dlid");
				String dlcity = rs.getString("dlcity");
				String dlstate = rs.getString("dlstate");
				String dladdr = rs.getString("dladdr");
				int dlzip = rs.getInt("dlzip");
				String dlemail = rs.getString("dlemail");
				int dlphone = rs.getInt("dlphone");
				
				System.out.println(dlid + " "+ dladdr + " "+ dlcity + " "+ dlstate+ " "+ dlzip+ " "+ dlemail+ " "+ dlphone );
			}
			
		} catch (Exception e) {
			
			System.out.println("Error: "+ e.getMessage());
		}
		
		
		System.out.println("");
		choices();
	}
	
	public boolean functions() {
		
		choices();
		
		return sessionlogout;
	}
	
	
}
