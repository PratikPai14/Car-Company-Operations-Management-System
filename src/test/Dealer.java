package test;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
public class Dealer extends logout{
	private boolean sessionlogout = false;
	private int id ;
	private ResultSet rs;
	private Connection con;
	private Statement st;
	Dealer(int id, Connection con, Statement st){
		
		this.id = id;
		this.con = con;
		this.st = st;
		//this.rs = rs;
	}
	
	private void choices() {
		
System.out.println(" 1. View Supply Chain \n 2. Place an Order \n 3. logout");
		
		Scanner sc = new Scanner(System.in);
		boolean isoption = false;
		int option = sc.nextInt();
		if((option != 1) || (option!= 2)||(option!= 3)) isoption = true;
		while(!isoption) {
			
			System.out.println("Invalid choice. Try Again\n");
			System.out.println(" 1. View Supply Chain\n 2. Place an Order \n 3. logout");
			option = sc.nextInt();
			if((option != 1) || (option!= 2)||(option!= 3)) isoption = true;
			
		}
		
		if(option == 1) supplychain();
		
		if(option == 2) ordercars();
		
		if(option == 3) sessionlogout = logoutsession(id);
				
		
	}
	

	public void supplychain() {
		
		System.out.println("inside supplychain");
		System.out.println("");
		
		int dcid = 0;
		
		try {
			
			rs = st.executeQuery("select warehouse.id wrid, warehouse.city wrcity , warehouse.state wrstate, warehouse.address wraddr, warehouse.zip_code wrzip, warehouse.email wremail, warehouse.phone wrphone, dc.id dcid, dc.city dccity, dc.state dcstate, dc.address dcaddr, dc.zipcode dczip, dc.email dcemail,dc.phone dcphone from car.dealer left join car.dc on dealer.dc_id = dc.id left join car.warehouse on dc.wr_id = warehouse.id where dealer.id = "+ id);
			
			System.out.println("Warehouse:");
			if(rs.next()) {
				
				int wrid = rs.getInt("wrid");
				String wrcity = rs.getString("wrcity");
				String wrstate = rs.getString("wrstate");
				String wraddr = rs.getString("wraddr");
				int wrzip = rs.getInt("wrzip");
				String wremail = rs.getString("wremail");
				int wrphone = rs.getInt("wrphone");
				
				System.out.println(wrid + " "+ wraddr + " "+ wrcity + " "+ wrstate+ " "+ wrzip+ " "+ wremail+ " "+ wrphone );
				System.out.println("");
				
			}
			rs.beforeFirst();
			
			
			
			System.out.println("Distribution Center:");
			if(rs.next()) {
				
				 dcid = rs.getInt("dcid");
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
			if(rs.next())
			System.out.println(" Other Dealers in "+ rs.getString("wrstate")+" :");
			
			rs = st.executeQuery("select * from car.dealer where dc_id = "+ dcid+" and id != "+ id);
			
			while(rs.next()) {
				

				int dlid = rs.getInt("id");
				String dlcity = rs.getString("city");
				String dlstate = rs.getString("state");
				String dladdr = rs.getString("address");
				int dlzip = rs.getInt("zipcode");
				String dlemail = rs.getString("email");
				int dlphone = rs.getInt("phone");
				
				System.out.println(dlid + " "+ dladdr + " "+ dlcity + " "+ dlstate+ " "+ dlzip+ " "+ dlemail+ " "+ dlphone );
			}
			
		} catch (Exception e) {
			
			System.out.println("Error: "+ e.getMessage());
		}
		
		
		System.out.println("");
		choices();
	}
	
public void ordercars() {
		
		System.out.println("inside ordercars");
		boolean orderplaced = true;
		
		try {
			
			rs = st.executeQuery("select dc.id dcid, dc.city dccity, dc.state dcstate from car.dealer left join car.dc on dc.id = dealer.dc_id  where dealer.id ="+ id);
			
			int dcid = 0;
			if(rs.next()) {
				
				 dcid = rs.getInt("dcid");
				 System.out.println("Order from Distribution Center: "+ rs.getString("dccity")+ " " +rs.getString("dcstate"));
				
			}
			
			rs.beforeFirst();
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter Product id:");
			int prid = sc.nextInt();
			System.out.println("Enter Quantity: ");
			int quant = sc.nextInt();
			sc.nextLine();
			System.out.println("Enter date(yyyy-mm-dd):");
			String s_date = sc.nextLine();
			System.out.println("Enter Distance:");
			int dist = sc.nextInt();
			rs = st.executeQuery("select pr_code, pr_name, color from car.product where pr_id = "+prid);
			String prname = "";
			String color = "";
			String shipcode ="";
			if(rs.next()) {
				 shipcode = dcid +"_"+ rs.getString("pr_code") + id + "_" + quant+"_"+s_date;
				 prname = rs.getString("pr_name");
				 color = rs.getString("color");
				 
			}
			st.executeUpdate("insert into car.shipment(s_date, s_dist, s_qty, s_to, s_from, shipcode) values "+ "( '"+s_date +"', "+dist+", "+quant+", "+ id+", "+dcid+", '"+shipcode+"' )");
			
			
			
			if(orderplaced) {
				
				try {
					
					st.executeUpdate("create  or replace view car.dcvieworder as select  pr_avail.pr_id,  dc_quant from car.pr_avail   where pr_avail.dc_id ="+dcid);
					st.executeUpdate("update car.dcvieworder set dc_quant = dc_quant - "+quant+" where pr_id ="+prid);
					
				} catch (Exception e1) {
					
					System.out.println("Error: Not enough quantity available in distribution center");
					st.executeUpdate("Delete from car.shipment where shipcode = '"+shipcode+"'");
					orderplaced = false;
				}
				if(orderplaced) {
					System.out.println("Oder Placed Successfully. Check order details:");
					System.out.println("------------------DETAILS--------------------------------------------------");
					System.out.println("Date: "+ s_date);
					System.out.println("Product ID: "+prid);
					System.out.println("Product name: "+prname);
					System.out.println("Product color: "+ color);
					System.out.println("Quantity: "+quant);
					System.out.println("---------------------------------------------------------------------------");
					
					
					//Generates Invoice
					FileWriter  writer      = new FileWriter("D:\\eclipse_workspace\\test\\invoices\\"+shipcode+".txt");
					PrintWriter printWriter = new PrintWriter(writer);

					printWriter.println("Oder Placed Successfully. Check order details:");
					printWriter.println("------------------DETAILS--------------------------------------------------");
					printWriter.println("Date: "+ s_date);
					printWriter.println("Product ID: "+prid);
					printWriter.println("Product name: "+prname);
					printWriter.println("Product color: "+ color);
					printWriter.println("Quantity: "+quant);
					printWriter.println("---------------------------------------------------------------------------");

					printWriter.close();
					
					
				}
				
			}
			
			
		} catch (Exception e) {
			
			System.out.println("Error: "+ e.getMessage());
			orderplaced = false;
		}
		
		choices();
	}
	
	public boolean functions() {
		
		choices();
		
		return sessionlogout;
	}
	
	
}
