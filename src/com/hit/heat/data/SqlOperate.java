package com.hit.heat.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.hit.heat.model.Energy;
import com.hit.heat.util.Util;

public class SqlOperate {
	static Connection conn = null;
	static String url;
	static Statement stat = null;
	
	static{
		
	}
	public static void connect(String location) {
        try {
            // db parameters
            url = location;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            stat = conn.createStatement();
            System.out.println("Connection to SQLite has been established.");
            /*
            ResultSet rs = stat.executeQuery("SELECT * FROM topo");
            while (rs.next()) {
                String id   = rs.getString("ParentID");   // Column 1
                System.out.println("ID: "+id);
            }*/
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } /*finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }*/
    }
    /**
     * @param args the command line arguments
     */
	public static void append(Energy data) {
        try {
        	String temp = null;
    
        	temp = "'"+data.getId()+"','"+data.getParentID()+"',"+data.getCPU()+","+data.getLPM()+","+data.getSend_time()+","+
        	data.getReceive_time()+","+data.getVoltage()+","+data.getSynTime()+",'"+data.getBeacon()+"',"+
        	data.getNum_neighbors()+","+data.getRtmetric()+","+data.getReboot()+","+data.getCycleTime()+",'"+
        	data.getCycleTimeDirection()+"','"+data.getNodecurrenttime()+"','"+Util.getCurrentTime()+"'";
        	System.out.println("&&"+temp);
            
        	stat.executeUpdate("insert into topo1 values ("+temp+")");
        	System.out.println("&&");
        	
      		//c.execute('''CREATE TABLE topo
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            }
        } 
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        connect("jdbc:sqlite:/home/fan/topo3.db");
        
    }
}
