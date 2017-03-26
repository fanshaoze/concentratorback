/*GConfig.java
 * 2015年7月19日
 * Lhy
*/
package com.hit.heat.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.hit.heat.model.NetParameter;



/**
 * @lhy Lhy
 *
 */
public class GConfig {
	
	JSONObject json;
	boolean hasException;
	public GConfig(String path) throws IOException,JSONException{
		hasException = false;
//		String parentPath = GetPath.getProjectPath();
//		System.out.println((parentPath+"\\"+path));
//		File fp = new File(parentPath+"/"+path); 
		File fp = new File(path); 
		if(!fp.exists()){
			throw new FileNotFoundException("文件" + path +"不存在");
		}
		StringBuilder sb = new StringBuilder();
		FileInputStream fileInputStream = new FileInputStream(fp);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
		byte [] buffer = new byte[1024];
		int readLen;
		while((readLen = bufferedInputStream.read(buffer,0,1024)) > 0){
			byte[]read = new byte[readLen];
			System.arraycopy(buffer, 0, read, 0, readLen);
			sb.append(new String(read));
		}
		bufferedInputStream.close();
		fileInputStream.close();
		json = new JSONObject(sb.toString());
	}
	
	/**
	 * 
	 * @des 从json数据中读取初始化网络参数 
	 * @return 
	 */
	public NetParameter getNetParameter(){
		NetParameter parameter = new NetParameter();
		String value;
		int IntValue;
		try {
			value = json.getString("tcpAddr");
			if(!value.isEmpty()){
				parameter.setTcpAddr(value);
			}else{
				parameter.setTcpAddr("0.0.0.0");
				hasException = true;
			}
			
		} catch (JSONException e) {
			// TODO: handle exception
			parameter.setTcpAddr("0.0.0.0");
			hasException = true;
		}
		try {
			IntValue = json.getInt("tcpPort");
			if(IntValue >= 0 && IntValue <= 65535){
				parameter.setTcpPort(IntValue);
			}else{
				parameter.setTcpPort(12300);
				hasException = true;
			}
			
		} catch (JSONException e) {
			// TODO: handle exception
			parameter.setTcpPort(12300);
			hasException = true;
		}
		try {
			IntValue = json.getInt("tcpByWebPort");
			if(IntValue >= 0 && IntValue <= 65535){
				parameter.setTcpByWebPort(IntValue);
			}else{
				parameter.setTcpByWebPort(12301);
				hasException = true;
			}
			
		} catch (JSONException e) {
			// TODO: handle exception
			parameter.setTcpPort(12300);
			hasException = true;
		}
		try {
			IntValue = json.getInt("tcpRemoteConfigPort");
			if(IntValue >= 0 && IntValue <= 65535){
				parameter.setTcpRemoteConfigPort(IntValue);
			}else{
				parameter.setTcpRemoteConfigPort(12306);
				hasException = true;
			}
			
		} catch (JSONException e) {
			// TODO: handle exception
			parameter.setTcpPort(12300);
			hasException = true;
		}
		
		
		
		
		
		
		try {
			value = json.getString("udpAddr");
			if(!value.isEmpty()){
				parameter.setUdpAddr(value);
			}else{
				parameter.setUdpAddr("::0");
				hasException = true;
			}
		} catch (JSONException e) {
			// TODO: handle exception
			parameter.setUdpAddr("::0");
			hasException = true;
		}
		try {
			IntValue = json.getInt("udpPort");
			if(IntValue >= 0 && IntValue <= 65535){
				parameter.setUdpPort(IntValue);
			}else{
				parameter.setUdpPort(12301);
				hasException = true;
			}
		} catch (JSONException e) {
			// TODO: handle exception
			parameter.setUdpPort(12301);
			hasException = true;
		}
		///
		try {
			value = json.getString("rootAddr");
			if(!value.isEmpty()){
				parameter.setRootAddr(value);
			}else{
				parameter.setRootAddr("aaaa::212:7401:1:101");
				hasException = true;
			}
		} catch (JSONException e) {
			// TODO: handle exception
			parameter.setRootAddr("aaaa::212:7401:1:101");
			hasException = true;
		}
		try {
			IntValue = json.getInt("rootPort");
			if(IntValue >= 0 && IntValue <= 65535){
				parameter.setRootPort(IntValue);
			}else{
				parameter.setRootPort(5678);
				hasException = true;
			}
		} catch (JSONException e) {
			// TODO: handle exception
			parameter.setRootPort(5678);
			hasException = true;
		}
		
		try{
			value = json.getString("rootRoomId");
			if(!value.isEmpty()){
				parameter.setRootRoomId(value);
			}else{
				parameter.setRootRoomId("512");
				hasException = true;
			}
		} catch (JSONException e) {
			parameter.setRootRoomId("512");
			hasException = true;
		}
		
		try {
			IntValue = json.getInt("rootX");
			parameter.setRootX(IntValue);
		} catch (JSONException e) {
			// TODO: handle exception
			parameter.setRootX(10);
		}
		
		try {
			IntValue = json.getInt("rootY");
			parameter.setRootY(IntValue);
		} catch (JSONException e) {
			// TODO: handle exception
			parameter.setRootY(10);
		}
		
		try {
			IntValue =json.getInt("tcpWebServerPort");
			if(IntValue >=0 && IntValue <=65535){
				parameter.setTcpWebServerPort(IntValue);
			}else {
				parameter.setTcpWebServerPort(12303);
				hasException = true;
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			parameter.setTcpWebServerPort(12303);
			hasException = true;
		}
		try {
			value = json.getString("tcpWebServerAddr");
			if(!value.isEmpty()){
				parameter.setTcpWebServerAddr(value);
			}else{
				parameter.setTcpWebServerAddr("192.168.1.141");//::0 //192.168.1.140
				
				hasException = true;
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			parameter.setTcpWebServerAddr("192.168.1.141");//::0
			hasException = true;
		}
		try {
			IntValue =json.getInt("remotePort");
			if(IntValue >=0 && IntValue <=65535){
				parameter.setRemotePort(IntValue);
			}else {
				parameter.setRemotePort(12304);
				hasException = true;
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			parameter.setRemotePort(12304);
			hasException = true;
		}
		try {
			IntValue =json.getInt("netPort");
			if(IntValue >=0 && IntValue <=65535){
				parameter.setNetPort(IntValue);
			}else {
				parameter.setNetPort(12307);
				hasException = true;
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			parameter.setNetPort(12307);
			hasException = true;
		}
		try {
			value = json.getString("remoteAddr");
			if(!value.isEmpty()){
				parameter.setRemoteAddr(value);
			}else{
				parameter.setRemoteAddr("192.168.1.141");
				
				hasException = true;
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			parameter.setRemoteAddr("192.168.1.141");
			hasException = true;
		}
		
		try {
			value = json.getString("id");
			if(!value.isEmpty()){
				parameter.setId(value);
			}else{
				parameter.setId("00000001");
				
				hasException = true;
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			parameter.setId("00000001");
			hasException = true;
		}
		
		try {
			IntValue = json.getInt("HeartIntSec");
			if(IntValue<=0){
				parameter.setHeartIntSec(IntValue);
			}else{
				parameter.setHeartIntSec(40);
				
				hasException = true;
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			parameter.setHeartIntSec(40);
			hasException = true;
		}
		
		try {
			IntValue = json.getInt("AckHeartInt");
			if(IntValue<=0){
				parameter.setAckHeartInt(IntValue);
			}else{
				parameter.setAckHeartInt(3);
				
				hasException = true;
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			parameter.setAckHeartInt(3);
			hasException = true;
		}	
		
		try {
			IntValue = json.getInt("MaxAckFail");
			if(IntValue<=0){
				parameter.setMaxAckFail(IntValue);
			}else{
				parameter.setMaxAckFail(30);
				
				hasException = true;
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			parameter.setMaxAckFail(30);
			hasException = true;
		}
		
		return parameter;
	}
	
	public static void main(String[] args) throws IOException,JSONException{
		GConfig gc = new GConfig("config.json");
		System.out.println(gc.getNetParameter().getRemoteAddr().toString());
	}
}
