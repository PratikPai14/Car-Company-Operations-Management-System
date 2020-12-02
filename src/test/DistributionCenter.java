package test;
import java.sql.*;
import java.util.*;
import java.io.*;
public class DistributionCenter extends logout{
	private boolean sessionlogout = false;
	private int id ;
	private ResultSet rs;
	private Connection con;
	private Statement st;
	DistributionCenter(int id, Connection con, Statement st){
		
		this.id = id;
		this.con = con;
		this.st = st;
		//this.rs = rs;
	}
	
	private void choices() {
		
System.out.println(" 1. View available cars and quantity\n 2. Add/Remove Cars\n 3. View Supply Chain\n 4. Place an Order \n 5. logout");
		
		Scanner sc = new Scanner(System.in);
		boolean isoption = false;
		int option = sc.nextInt();
		if((option != 1) || (option!= 2)||(option!= 3)||(option!= 4)||(option!= 5)) isoption = true;
		while(!isoption) {
			
			System.out.println("Invalid choice. Try Again\n");
			System.out.println(" 1. View available cars and quantity\n 2. Add/Remove Cars\n 3. View Supply Chain\n 4. Place an Order \n 5. logout");
			option = sc.nextInt();
			if((option != 1) || (option!= 2)||(option!= 3)||(option!= 4)||(option!= 5)) isoption = true;
			
		}
		
		if(option == 1) viewcars();
		
		if(option == 2) modifycars();
		
		if(option == 3) supplychain();
		
		if(option == 4) ordercars();
		
		if(option ==5 ) sessionlogout = logoutsession(id);
				
		
	}
	
public void viewcars() {
		
		System.out.println("inside viewcars");
		try {
			
			st.executeUpdate("create or replace view car.dcview as select distinct car.pr_avail.pr_id, car.product.pr_name, car.product.color, dc_quant from car.pr_avail  left join car.product on car.pr_avail.pr_id = car.product.pr_id  where car.pr_avail.dc_id = "+id);
			
			rs = st.executeQuery("Select * from car.dcview");
			
			System.out.println("PR_ID\t\tPR_NAME\t\tCOLOR\t\tDC_QUANT");
			
			while(rs.next()) {
				
				int prid = rs.getInt("pr_id");
				int dcquant = rs.getInt("dc_quant");
				String prname = rs.getString("pr_name");
				String color = rs.getString("color");
				System.out.println(prid+"\t\t"+ prname+"\t\t"+color+"\t\t"+dcquant);
				
			}
			rs.beforeFirst();
			
			int min = 0, max = 0, total = 0;
			rs = st.executeQuery("Select sum(dc_quant) total, min(dc_quant) as min_quant, max(dc_quant) max_quant from car.dcview ");
			if(rs.next()) {
				 min = rs.getInt("min_quant");
				 max = rs.getInt("max_quant");
				 total = rs.getInt("total");
			}
			
			System.out.println("------------------------------------------------");
			System.out.println("TOTAL CARS: "+ total );
			
			rs = st.executeQuery("Select * from car.dcview where dc_quant = "+max);
			System.out.println("MAXIMUM: ");
			while(rs.next()) {
				
				int prid = rs.getInt("pr_id");
				String prname = rs.getString("pr_name");
				String color = rs.getString("color");
				System.out.println(prid+"\t\t"+ prname+"\t\t"+color+"\t\t"+max);
				
			}
			
			
			
			rs = st.executeQuery("Select * from car.dcview where dc_quant = "+min);
			
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
			
			st.executeUpdate("create  or replace view car.dcview as select  pr_avail.pr_id,  dc_quant from car.pr_avail   where pr_avail.dc_id ="+id);
			st.executeUpdate("update car.dcview set dc_quant = "+updatequant +" where pr_id ="+prid);
			
			
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
			
			rs = st.executeQuery("select warehouse.id wrid, warehouse.city wrcity , warehouse.state wrstate, warehouse.address wraddr, warehouse.zip_code wrzip, warehouse.email wremail,warehouse.phone wrphone, dealer.id dlid, dealer.city dlcity, dealer.state dlstate, dealer.address dladdr, dealer.zipcode dlzip, dealer.email dlemail,dealer.phone dlphone from car.dc left join car.warehouse on warehouse.id = dc.wr_id left join car.dealer on dc.id = dealer.dc_id where dc.id ="+ id);
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
	
	
	public void ordercars() {
		
		
		
		System.out.println("inside ordercars");
		boolean placedorder = true;
		
		try {
			
			rs = st.executeQuery("select warehouse.id wrid, warehouse.city wrcity, warehouse.state wrstate from car.dc left join car.warehouse on warehouse.id = dc.wr_id  where dc.id ="+ id);
			
			int wrid = 0;
			if(rs.next()) {
				
				 wrid = rs.getInt("wrid");
				 System.out.println("Order from Warehouse: "+ rs.getString("wrcity")+ " " +rs.getString("wrstate"));
				
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
				 shipcode = wrid +"_"+ rs.getString("pr_code") + id + "_" + quant+"_"+s_date;
				 prname = rs.getString("pr_name");
				 color = rs.getString("color");
				 
			}
			if(shipcode.isEmpty()) {
				System.out.println("Invalid product id");
				ordercars();
			}
			st.executeUpdate("insert into car.shipment(s_date, s_dist, s_qty, s_to, s_from, shipcode) values "+ "( '"+s_date +"', "+dist+", "+quant+", "+ id+", "+wrid+", '"+shipcode+"' )");
			
			
			try {
				
				st.executeUpdate("create  or replace view car.wrvieworder as select  pr_avail.pr_id,  wr_quant from car.pr_avail   where pr_avail.Wr_id ="+wrid);
				st.executeUpdate("update car.wrvieworder set wr_quant = wr_quant - "+quant+" where pr_id ="+prid);
				
			} catch (Exception e1) {
				
				System.out.println("Error: Not enough quantity available in warehouse");
				st.executeUpdate("Delete from car.shipment where shipcode = '"+shipcode+"'");
				placedorder = false;
			}
			
			if(placedorder) {
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
				
				
				
				
				
				
				
				try {
					
					st.executeUpdate("create  or replace view car.dcvieworder as select  pr_avail.pr_id,  dc_quant from car.pr_avail   where pr_avail.dc_id ="+id);
					st.executeUpdate("update car.dcvieworder set dc_quant = dc_quant +"+quant +" where pr_id ="+prid);

				} catch (Exception e2) {
					
					System.out.println("Error: "+ e2.getMessage());
					
				}
			}
			
			
		} catch (Exception e) {
			
			System.out.println("Error: "+ e.getMessage());
		}
		
		choices();
	}
	
	public boolean functions() {
		
		choices();
		
		return sessionlogout;
	}
	
	
}
