package test;

import java.sql.*;
import java.util.*;
import java.io.*;




public class MainClass {

    public static void main(String[] args) {

    	login log = new login();
    	
    	String table = log.logtype();
    	int id = log.log_userid();
    	Connection con = log.connection();
    	Statement st = log.statement();
    	
    	
    	
    	if(table == "Warehouse") {
    		
    		warehouse wh = new warehouse(id, con, st);
    		
    		boolean closesession = wh.functions();
    		
    		if(closesession == true) log.closeconnection();
    		
    		
    	}
    	
    
    	if(table == "DC") {
    		
    		
    		DistributionCenter dc = new DistributionCenter(id, con,st);
    		
    		boolean closesession = dc.functions();
    		
    		if(closesession == true) log.closeconnection();
    	}
    	
    	
    	
    	
    	if(table == "Dealer") {
    		
    		
    		Dealer dealer = new Dealer(id, con, st);
    		
    		boolean closesession = dealer.functions();
    		
    		if(closesession == true) log.closeconnection();
    	}
    	
    	
    }
    
}