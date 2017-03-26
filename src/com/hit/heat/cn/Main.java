package com.hit.heat.cn;

import com.hit.heat.data.SqlOperate;

public class Main {

	public static ConsoleMainServer coServer;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SqlOperate.connect("jdbc:sqlite:/home/fan/topo3.db");
		System.out.println("123");
		coServer = new ConsoleMainServer();
	}

}
