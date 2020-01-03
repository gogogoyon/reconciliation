package com.samlic.accumulation.ecosystem.reconciliation.extend;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpHelper {

	private FTPClient ftpClient;

	private String url; 
	private String port; 
	private String username; 
	private String password; 
	private String remotePath; 

	/**
	 * 初始化FtpHelper
	 * 
	 * @param url  服务器地址
	 * @param port 服务器端口
	 * @param username  用户名
	 * @param password 密码
	 * @param remotePath 远程文件路径
	 */
	public FtpHelper(String url, String port, String username, String password, String remotePath) {
		this.url = url;
		this.port = port;
		this.password = password;
		this.username = username;
		this.remotePath = remotePath;
		this.ftpClient = new FTPClient();
	}

	private void connectToTheServer() throws IOException {	
		// 连接至服务器，端口默认为21时，可直接通过URL连接
		ftpClient.connect(url, Integer.parseInt(port));
		
		// 登录服务器		
		if(!ftpClient.login(username, password)) {
			throw new IOException("Failed to login ftp server. ReplyCode = " + ftpClient.getReplyCode());
		}
		
		// 判断返回码是否合法
		if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			throw new IOException("Reply code is not a positive completion response. ReplyCode = " + ftpClient.getReplyCode());
		}

		ftpClient.enterLocalPassiveMode();

		// 设置文件操作目录		
		if (!ftpClient.changeWorkingDirectory(remotePath)) {
			throw new IOException("Failed to change directory to " + remotePath + ". ReplyCode = " + ftpClient.getReplyCode());
		}
		
		// 设置文件类型，二进制		
		if(!ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE)) {
			throw new IOException("Failed to set file type to transfer. ReplyCode = " + ftpClient.getReplyCode());
		}
		
		// 设置缓冲区大小
		ftpClient.setBufferSize(3072);
		// 设置字符编码
		ftpClient.setControlEncoding("UTF-8");
	}

	public void makeDirectory(String remotePath, String pathname) throws IOException {
		try {			
			connectToTheServer();
			if (!ftpClient.changeWorkingDirectory(remotePath)) {
				throw new IOException(
						"Failed to change directory to " + remotePath + ". ReplyCode = " + ftpClient.getReplyCode());
			}
			
			String[] names = ftpClient.listNames();
			for (int i = 0; i < names.length; i++) {
				if (StringUtils.equals(pathname, names[i])) {
					return;
				}
			}
			
			if (!ftpClient.makeDirectory(pathname)) {
				throw new IOException(
						"Failed to make directory to " + remotePath + ". ReplyCode = " + ftpClient.getReplyCode());
			}

		} finally {			
			disconnect();
		}
	}

	public void storeFile(String fileName, InputStream is) throws IOException {
		try {
			connectToTheServer();
			if(!ftpClient.storeFile(fileName, is)) {
				throw new IOException(
						"Failed to store file " + fileName + ". ReplyCode = " + ftpClient.getReplyCode());
			}
		} finally {
			disconnect();
		}
	}

	public String[] retrieveFileNames() throws IOException {
		try {
			connectToTheServer();
			String[] remoteNames = ftpClient.listNames();
			if(remoteNames == null) {
				throw new IOException(
						"Failed to list names of directory " + remotePath + ". ReplyCode = " + ftpClient.getReplyCode());
			}
			
			return remoteNames;
		} finally {			
			disconnect();
		}
	}

	/**
	 * 获取文件
	 * @param savePath 本地保存路径
	 * @param fileName 文件名称
	 * @return 文件对象
	 * @throws IOException 可能的IO异常
	 */
	public File retrieveFile(String savePath, String fileName) throws IOException {		
		InputStream is = null;
		File file = new File(savePath + fileName);
		try {
			connectToTheServer();
			is = ftpClient.retrieveFileStream(fileName);
			if(is == null) {
				throw new IOException(
						"Failed to retrive file " + fileName + ". ReplyCode = " + ftpClient.getReplyCode());
			}
			
			FileUtils.copyInputStreamToFile(is, file);
			return file;
		} finally {
			if(is != null) {
				IOUtils.closeQuietly(is);
			}			
			
			disconnect();
		}
	}

	
	public void deleteFile(String fileName) throws IOException {
		try {
			connectToTheServer();
			if (!ftpClient.deleteFile(fileName)) {
				throw new IOException(
						"Failed to delete file " + fileName + ". ReplyCode = " + ftpClient.getReplyCode());
			}
		} finally {			
			disconnect();
		}
	}
	
	public boolean checkFile(String fileName) throws IOException {
		try {
			connectToTheServer();
			String[] remoteNames = ftpClient.listNames();	
			if(remoteNames == null) {
				throw new IOException(
						"Failed to list names of directory " + remotePath + ". ReplyCode = " + ftpClient.getReplyCode());
			}
			
			for (String remoteName : remoteNames) {
				if (fileName.equals(remoteName)) {
					return true;
				}
			}
			return false;
		} finally {			
			disconnect();
		}
	}

	public void moveFile(String oldfileName, String newfileName) throws IOException {
		try {
			connectToTheServer();
			if (!ftpClient.rename(oldfileName, newfileName)) {
				throw new IOException(
						"Failed to rename file " + oldfileName + ". ReplyCode = " + ftpClient.getReplyCode());
			}
		} finally {
			disconnect();
		}
	}

	private void disconnect() throws IOException {		
		if (null != ftpClient && ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}
}
