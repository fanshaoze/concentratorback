package com.hit.heat.cn;

import com.hit.heat.data.SqlOperate;
import com.hit.heat.util.Util;

public class Main {

	public static ConsoleMainServer coServer;
	
	public static void main(String[] args) {
//		org.apache.log4j.BasicConfigurator.configure();
		// TODO Auto-generated method stub
		//SqlOperate.connect("jdbc:sqlite:/root/build_jar/topo3.db");
		//SqlOperate.connect("jdbc:sqlite:/home/fan/topo3.db");
		SqlOperate.connect("jdbc:sqlite:topo4.db");
		System.out.println(Util.getCurrentTime()+" ======启动集中器后台======");
//		SqlOperate.connect("jdbc:sqlite:/home/gxn/Desktop/topo3.db");
		coServer = new ConsoleMainServer();
	}

}
