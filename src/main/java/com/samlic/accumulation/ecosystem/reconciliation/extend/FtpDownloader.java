package com.samlic.accumulation.ecosystem.reconciliation.extend;

import java.io.File;
import java.io.IOException;

import com.samlic.accumulation.ecosystem.reconciliation.Downloader;

public class FtpDownloader implements Downloader {
	
	private String url;			// 服务器地址
	private String port; 		// 服务器端口
	private String username; 	// 用户登录名
	private String password; 	// 用户登录密码
	private String remotePath;	// 文件保存路径
	private String localPath;
	
	public FtpDownloader(String url, String port, String username, String password, String remotePath, String localPath) {
		this.url = url;
	    this.port = port;
	    this.password = password;
	    this.username = username;
	    this.remotePath = remotePath;
	    this.localPath = localPath;
	}


	@Override
	public File download(String fileName) throws IOException {
		FTPUtils ftpUtils = new FTPUtils(url, port, username, password, remotePath);
		ftpUtils.retrieveFile(localPath, fileName);
		return new File(localPath+fileName);
	}

	@Override
	public File[] downloadFiles(String suffix) throws IOException {
		FTPUtils ftpUtils = new FTPUtils(url, port, username, password, remotePath);		
		return ftpUtils.retrieveFiles(localPath, suffix);
	}

}
