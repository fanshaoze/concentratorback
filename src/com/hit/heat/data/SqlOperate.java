package com.hit.heat.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.hit.heat.model.Energy;
import com.hit.heat.util.Util;
import com.hit.heat.util.rdc_EF_Control;
import com.hit.heat.util.WriteDataToFile;

public class SqlOperate {
	static Connection conn = null;
	static String url;
	static Statement stat = null;

	static WriteDataToFile CommandDownFile;

	// 数据库连接 //测试通过

	public static void connect(String location) {
		try {
			url = location;
			// create a connection to the database
			conn = DriverManager.getConnection(url);
			stat = conn.createStatement();
			System.out.println("Connection to SQLite has been established.");
			//stat.executeUpdate("create table if not exists tbl1(name varchar(20), salary int);");
			createtables();
		} catch (SQLException e) {
			System.out.println("database connect fail");
			System.out.println(e.getMessage());
		}
	}
	public static void createtables() throws SQLException{
		//System.out.println("Start to create tables");
		try{
			stat.executeUpdate("CREATE TABLE if not exists File(FileName varchar PRIMARY KEY,FilePath varchar);");
			stat.executeUpdate("CREATE TABLE if not exists NodePlace(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "NodeID varchar,MeterID varchar,Place text);");
			stat.executeUpdate("CREATE TABLE if not exists CommandCache(ID INTEGER PRIMARY KEY AUTOINCREMENT,Command varchar);");
			stat.executeUpdate("CREATE TABLE if not exists NetMonitor(ID INTEGER PRIMARY KEY AUTOINCREMENT,NodeID varchar,"
					+ "ParentID varchar,CPU bigint,LPM bigint,TX bigint,RX bigint,volage float,syntime int,"
					+ "beacon int,numneighbors int,rtimetric int,reboot int,cycletime int,"
					+ "cycletimeDirection varchar,Nodecurrenttime time,currenttime time,electric float);");
			stat.executeUpdate("CREATE TABLE if not exists CommandDown(ID INTEGER PRIMARY KEY AUTOINCREMENT,NodeID varchar,"
					+ "Place varchar,Command varchar);");
			stat.executeUpdate("CREATE TABLE if not exists ApplicationData(ID INTEGER PRIMARY KEY AUTOINCREMENT,NodeID varchar,"
					+ "currenttime time,Data varchar);");
		}catch (SQLException e) {
			System.out.println("database connect fail");
			System.out.println(e.getMessage());
		}
	}
		
	/**
	 * @param args
	 *            the command line arguments
	 */

	// 添加数据到检测表 //测试通过
	public static void append(Energy data) {
		connect("jdbc:sqlite:topo3.db");
		try {
			String temp = null;
			rdc_EF_Control.calCurrent(data);
			temp = "null,'" + data.getId() + "','" + data.getParentID() + "'," + data.getCPU() + "," + data.getLPM()
					+ "," + data.getSend_time() + "," + data.getReceive_time() + "," + data.getVoltage() + ","
					+ data.getSynTime() + ",'" + data.getBeacon() + "'," + data.getNum_neighbors() + ","
					+ data.getRtmetric() + "," + data.getReboot() + "," + data.getCycleTime() + ",'"
					+ data.getCycleTimeDirection() + "','" + data.getNodecurrenttime() + "','" + Util.getCurrentTime()
					+ "',"+rdc_EF_Control.calCurrent(data);;

			stat.executeUpdate("insert into NetMonitor values (" + temp + ")");
			//System.out.println(Util.getCurrentTime()+"append to netMonitor success"+"append values:"+temp);//for log
			System.out.println(Util.getCurrentTime()+ " topo:"+ data.getId());
		} catch (SQLException e) {
			System.out.println("netMonitor append fail");
			System.out.println(e.getMessage());
			close();
		}
		close();
	}
	// 网络检测上报 //测试通过
	public static void topo_out(int day_length, String filename) throws IOException {
		connect("jdbc:sqlite:topo3.db");
		WriteDataToFile AppFile = null;
		ResultSet rs;
		Calendar cal = Calendar.getInstance();
		long time1 = 0;
		long begintime = 0;
		String Currenttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		try {
			cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Currenttime));
			time1 = cal.getTimeInMillis();
			AppFile = new WriteDataToFile(filename);
			// System.out.println(filename+"-App.txt");
			begintime = time1 - (day_length * 24) * (1000 * 3600);
			System.out.println(Util.getCurrentTime()+"netMonitor out from "+time1+" to "+begintime);//for log
		} catch (Exception e) {
			e.printStackTrace();
			close();
		}
		try {
			Date d = new Date(begintime);
			String begint = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
			String com = "SELECT * FROM NetMonitor where currenttime >= '" + begint + "'";
			//System.out.println(com);
			rs = stat.executeQuery("SELECT * FROM NetMonitor where currenttime >= '" + begint + "'");
			while (rs.next()) {
				int topo_ID = rs.getInt("ID");
				String topo_NodeID = rs.getString("NodeID");
				String topo_ParentID = rs.getString("ParentID");
				long topo_CPU = rs.getLong("CPU");
				long topo_LPM = rs.getLong("LPM");
				long topo_TX = rs.getLong("TX");
				long topo_RX = rs.getLong("RX");
				float topo_volage = rs.getFloat("volage");
				int topo_syntime = rs.getInt("syntime");
				String topo_beacon = rs.getString("beacon");
				int topo_numneighbors = rs.getInt("numneighbors");
				int topo_rtimetric = rs.getInt("rtimetric");
				int topo_reboot = rs.getInt("reboot");
				int topo_cycletime = rs.getInt("cycletime");
				String topo_cycletimeDirection = rs.getString("cycletimeDirection");
				String topo_Nodecurrenttime = rs.getString("Nodecurrenttime");
				String topo_currenttime = rs.getString("currenttime");
				float topo_electric = rs.getFloat("electric");
				AppFile.append(topo_ID + ":" + topo_NodeID + ":" + topo_ParentID + ":" + topo_CPU + ":" + topo_LPM + ":"
						+ topo_TX + ":" + topo_RX + ":" + topo_volage + ":" + topo_syntime + ":" + topo_beacon + ":"
						+ topo_numneighbors + ":" + topo_rtimetric + ":" + topo_reboot + ":" + topo_cycletime + ":"
						+ topo_cycletimeDirection + ":" + topo_Nodecurrenttime + ":" + topo_currenttime + ":"
						+ topo_electric);
			}
			AppFile.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			close();
		}
		close();
	}

	// 添加数据到应用数据表
	public static void ApplicationData_a(String NodeID, String currenttime, String data) {
		connect("jdbc:sqlite:topo3.db");
		try {
			String temp = null;
			temp = "null,'" + NodeID + "','" + Util.getCurrentTime() + "','" + data + "'";

			stat.executeUpdate("insert into ApplicationData values(" + temp + ")");
			//System.out.println(Util.getCurrentTime()+"append to ApplicationData success"+"append values:"+temp);//for log
			System.out.println(Util.getCurrentTime()+" app:"+NodeID);//for log
		} catch (SQLException e) {
			System.out.println("ApplicationData append fail");
			System.out.println(e.getMessage());
			close();
		}
		close();
	}

	// 应用数据上报 //测试通过
	public static void ApplicationData_out(int day_length, String filename) throws IOException {
		connect("jdbc:sqlite:topo3.db");
		WriteDataToFile AppFile = null;
		ResultSet rs;
		// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		long time1 = 0;
		long begintime = 0;
		String Currenttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		try {
			cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Currenttime));
			time1 = cal.getTimeInMillis();
			AppFile = new WriteDataToFile(filename);
			// System.out.println(filename+"-App.txt");
			begintime = time1 - (day_length * 24) * (1000 * 3600);
			String duration=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(begintime))+"~"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time1));

			//System.out.println(Util.getCurrentTime()+" ApplicationData out from "+duration);//for log
		} catch (Exception e) {
			e.printStackTrace();
			close();
		}
		try {
			Date d = new Date(begintime);
			String begint = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
			String com = "SELECT * FROM ApplicationData where currenttime >= '" + begint + "'";
			//System.out.println(com);
			rs = stat.executeQuery(com);
			while (rs.next()) {
				String A_ID = rs.getString("ID");
				String A_NodeID = rs.getString("NodeID"); // Column 1
				String A_currenttime = rs.getString("currenttime"); // Column 1
				String A_Data = rs.getString("Data"); // Column 1
				//System.out.println(A_ID + ":" + A_NodeID + ":" + A_currenttime + ":" + A_Data);
				AppFile.append(A_NodeID + ":" + A_currenttime + ":" + A_Data);
			}
			AppFile.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			close();
		}
		close();
	}

	// 应用数据删除 //测试通过
	public static void ApplicationData_drop() throws IOException {
		connect("jdbc:sqlite:topo3.db");
		WriteDataToFile AppFile;
		ResultSet rs;
		String Currenttime = new SimpleDateFormat("yyyy-MM-dd#HH:mm:ss").format(new Date());
		try {
			AppFile = new WriteDataToFile(Currenttime + "before.txt");
			rs = stat.executeQuery("SELECT Data FROM ApplicationData where Currenttime < '" + Currenttime + "'");

			while (rs.next()) {
				String A_NodeID = rs.getString("NodeID"); // Column 1
				String A_currenttime = rs.getString("currenttime"); // Column 1
				String A_Data = rs.getString("ApplicationData"); // Column 1
				AppFile.append(A_NodeID + ":" + A_currenttime + ":" + A_Data);
			}
			rs.close();
			AppFile.close();
			stat.executeUpdate("delete from CommandDown where Currenttime < '" + Currenttime + "'");
			System.out.println("delete from CommandDown success");//for log
		} catch (SQLException e) {
			close();
			System.out.println(e.getMessage());
		}
		close();
	}

	// 添加数据到指令下发表 //测试通过
	public static void commanddown_a(String NodeID, String Place, String Message) {
		connect("jdbc:sqlite:topo3.db");
		try {
			String temp = null;
			temp = "null,'" + NodeID + "','" + Place + "','" + Message + "'";
			System.out.println("commanddown append values:"+temp);//for log
			stat.executeUpdate("insert into CommandDown values (" + temp + ")");
			System.out.println("insert into CommandDown success");//for log
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			close();
		}
		close();
	}

	// 指令下发记录输出 //测试通过
	public static void commanddown_out() throws IOException {
		connect("jdbc:sqlite:topo3.db");
		ResultSet rs;
		try {
			CommandDownFile = new WriteDataToFile("CommadDown.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
		}
		try {
			rs = stat.executeQuery("SELECT * FROM CommandDown");
			while (rs.next()) {
				String CD_NodeID = rs.getString("NodeID"); // Column 1
				String CD_Place = rs.getString("Place"); // Column 1
				String CD_Data = rs.getString("Command"); // Column 1
				CommandDownFile.append(CD_NodeID + ":" + CD_Place + ":" + CD_Data);
			}
			rs.close();
			CommandDownFile.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			close();
		}
		close();
	}

	// 缓存数据表判满 //测试通过
	public static boolean CommandCache_full() throws SQLException {
		int count = CommandCache_count();
		System.out.println(Util.getCurrentTime()+"CommandCache_count="+count);
		if (count < 10) {
			System.out.println("the commandCache is not full");
			return true;
		} else {
			System.out.println("the commandCache is full");
			return false;
		}
	}

	// 缓存数据表判空 //测试通过
	public static boolean CommandCache_empty() throws SQLException {
		int count = CommandCache_count();
		if (count == 0) {
			System.out.println("the commandCache is empty");
			return true;
		} else {
			System.out.println("the commandCache is not empty");
			return false;
		}
	}

	// 缓存数据表计算条数 //测试通过
	public static int CommandCache_count() throws SQLException {
		ResultSet rset = stat.executeQuery("select * from CommandCache");
		// rset.last();
		int count = 0;
		String com = null;
		while (rset.next()) {
			com = rset.getString("Command");
			count += 1;
		}
		return count;
	}

	// 添加数据到指令缓存表 //测试通过
	public static void commandCache_a(String Message) {
		connect("jdbc:sqlite:topo3.db");
		try {
			if (CommandCache_full()) {
				String temp = null;
				temp = "null,'" + Message + "'";
				//System.out.println();//for log

				stat.executeUpdate("insert into CommandCache values (" + temp + ");");
				System.out.println(Util.getCurrentTime()+" insert into CommandCache "+" command values:"+temp);//for log
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			close();
		}
		close();
	}

	// 读取缓存指令 //测试通过
	public static String CommandCache_get() {
		connect("jdbc:sqlite:topo3.db");
		ResultSet rs;
		int first = 0;
		String Command = null;
		try {
			if (CommandCache_count() != 0) {
				rs = stat.executeQuery("SELECT * FROM CommandCache");
				if (rs.next()) {
					first = rs.getInt("ID");
					Command = rs.getString("Command");
					System.out.println("" + first + ":" + Command);
				}
				rs.close();
				stat.executeUpdate("delete from CommandCache where ID=" + first);
			} else {
				System.out.println(Util.getCurrentTime()+" CommandCache is empty");
				Command = "500000";
			}
		} catch (SQLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			close();
		}
		close();
		return Command;
	}

	// 添加数据到节点位置表
	public static void NodePlace_a(String NodeID, String Place) {
		connect("jdbc:sqlite:topo3.db");
		try {
			String temp = null;
			temp = "null,'" + NodeID + "','" + Place + "'";

			stat.executeUpdate("insert into NodePlace values (" + temp + ")");
			System.out.println(Util.getCurrentTime()+"insert into NodePlace success"+" values:"+temp);//for log
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			close();
		}
		close();
	}

	// 添加数据到文件表
	public static void File_a(String FileName, String FilePath) {
		connect("jdbc:sqlite:topo3.db");
		try {
			String temp = null;
			temp = "'" + FileName + "','" + FilePath + "'";

			stat.executeUpdate("insert into File values(" + temp + ")");
			//System.out.println(Util.getCurrentTime()+"insert into File success"+"append values:"+temp);//for log
			// c.execute('''CREATE TABLE topo
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			close();
		}
		close();
	}
	public static void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		//connect("jdbc:sqlite:topo3.db");
		//commandCache_a("100000");
//		try {
//			topo_out(2,"NetMonitor-out");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//close();
		// String Command = CommandCache_get();
		//commanddown_a("1", "1", "110000");
		try {
		commanddown_out();
		 } catch (IOException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		//String Command = CommandCache_get();
		// System.out.println(Command);

	}
}
