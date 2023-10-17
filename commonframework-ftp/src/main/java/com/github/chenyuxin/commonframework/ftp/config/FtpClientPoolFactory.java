package com.github.chenyuxin.commonframework.ftp.config;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class FtpClientPoolFactory implements PooledObjectFactory<FTPClient> {
	
	private String host;
	private int port;
	private String username;
	private String password;
	
	public FtpClientPoolFactory(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	@Override
	public void activateObject(PooledObject<FTPClient> ftpObject) throws Exception {
		// 在拿到对象前对对象进行配置, 比如可以重置 ftpObject 
		//FTPClient ftpClient = ftpObject.getObject();
		//ftpClient.reinitialize();
		//ftpClient.login(username, password);
		//ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		//ftpClient.changeWorkingDirectory("/");
	}

	@Override
	public void destroyObject(PooledObject<FTPClient> ftpObject) throws Exception {
		// 销毁
		FTPClient ftpClient = ftpObject.getObject();
		if (null != ftpClient && ftpClient.isConnected() ) {
			quit(ftpClient);
		}
		
	}

	@Override
	public PooledObject<FTPClient> makeObject() throws Exception {
		// 新建ftp客户端
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");//必须在连接之前设置
		ftpClient.setAutodetectUTF8(true);
		//ftpClient.setConnectTimeout(6000);
		ftpClient.enterLocalActiveMode();
		ftpClient.enterLocalPassiveMode();
		
		ftpClient.connect(host, port==0||port==-1?21:port);
		ftpClient.login(username, password);
		if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			ftpClient.disconnect();
			throw new RuntimeException("未连接到FTP,用户名或密码错误");
		}
		
		if (ftpClient.getSystemType().toLowerCase().contains("windows")) {
			disconnect(ftpClient);
			ftpClient.setControlEncoding("GBK");//必须在连接之前设置
			ftpClient.connect(host, port==0||port==-1?21:port);
			ftpClient.login(username, password);
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				ftpClient.disconnect();
				throw new RuntimeException("未连接到FTP,用户名或密码错误");
			}
		} 
		
		return new DefaultPooledObject<FTPClient>(ftpClient);
	}

	@Override
	public void passivateObject(PooledObject<FTPClient> ftpObject) throws Exception {
		// 归还池时调用
	}

	@Override
	public boolean validateObject(PooledObject<FTPClient> ftpObject) {
		// 验证连接，false进行销毁
		FTPClient ftpClient = ftpObject.getObject();
		try {
			return ftpClient!=null && ftpClient.isConnected() && ftpClient.sendNoOp();
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * 断开连接
	 * @param ftpClient
	 */
	private void disconnect(FTPClient ftpClient) {
		try {
			ftpClient.logout();
		} catch (IOException logoutIOException) {
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException disconnectIOException) {
			} 
		}
	}
	
	/**
	 * 断开连接并退出
	 * @param ftpClient
	 */
	private void quit(FTPClient ftpClient) {
		try {
			ftpClient.logout();
		} catch (IOException logoutIOException) {
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException disconnectIOException) {
			} finally {
				try {
					ftpClient.quit();
				} catch (IOException quitIOException) {
				}
			}
		} 
	}

}
