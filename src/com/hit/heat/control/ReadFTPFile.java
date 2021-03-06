 
package com.hit.heat.control;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
//import org.apache.log4j.Logger;

import com.hit.heat.control.FTPUtil;
import com.hit.heat.util.Util;

public class ReadFTPFile {
//	private Logger logger = Logger.getLogger(ReadFTPFile.class);

	/**
	 * 去 服务器的FTP路径下上读取文件
	 * 
	 * @param ftpUserName
	 * @param ftpPassword
	 * @param ftpPath
	 * @param FTPServer
	 * @return
	 */
	public String readConfigFileForFTP(String ftpUserName, String ftpPassword,
			String ftpPath, String ftpHost, int ftpPort, String fileName) {
		StringBuffer resultBuffer = new StringBuffer();
//		FileInputStream inFile = null;
		InputStream in = null;
		FTPClient ftpClient = null;
		System.out.println(Util.getCurrentTime()+" 开始读取绝对路径" + ftpPath + "文件!");
		try {
			ftpClient = FTPUtil.getFTPClient(ftpHost, ftpPassword, ftpUserName,
					ftpPort);

			
			ftpClient.setControlEncoding("UTF-8"); // 中文支持
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalActiveMode();
			if(ftpClient.changeWorkingDirectory(ftpPath))
			{
				System.out.println(Util.getCurrentTime()+" enter ftpPath:"+ftpPath);
			}
			int reply=ftpClient.getReplyCode();
			if(!FTPReply.isPositiveCompletion(reply))
			{
				ftpClient.disconnect();
				System.err.println(Util.getCurrentTime()+" 发生错误啦×××××××××××××××××××");
			}
			in = ftpClient.retrieveFileStream(fileName);
//			System.out.println(ftpPath+fileName);
		} catch (FileNotFoundException e) {
			System.err.println(Util.getCurrentTime()+" 没有找到" + ftpPath + "文件");
			e.printStackTrace();
			return "下载配置文件失败，请联系管理员.";
		} catch (SocketException e) {
			System.err.println(Util.getCurrentTime()+" 连接FTP失败.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(Util.getCurrentTime()+" 文件读取错误。");
			e.printStackTrace();
			return "配置文件读取失败，请联系管理员.";
		}
		if (in != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String data = null;
			try {
				while ((data = br.readLine()) != null) {
					resultBuffer.append(data + "\n");
				}
			} catch (IOException e) {
				System.err.println(Util.getCurrentTime()+" 文件读取错误。");
				e.printStackTrace();
				return "配置文件读取失败，请联系管理员.";
			}finally{
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			System.err.println(Util.getCurrentTime()+" in为空，不能读取。");
			return "配置文件读取失败，请联系管理员.";
		}
		return resultBuffer.toString();
	}
}
