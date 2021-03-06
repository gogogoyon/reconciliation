package com.samlic.accumulation.ecosystem.reconciliation.extend;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.samlic.accumulation.ecosystem.reconciliation.Uploader;

public class FtpUploader implements Uploader {

	private String url;			// 服务器地址
	private String port; 		// 服务器端口
	private String username; 	// 用户登录名
	private String password; 	// 用户登录密码
	private String remotePath;	// 文件保存路径
	
	
	public FtpUploader(String url, String port, String username, String password, String remotePath) {
		this.url = url;
	    this.port = port;
	    this.password = password;
	    this.username = username;
	    this.remotePath = remotePath;
	}
	
	@Override
	public void upload(File file) throws IOException {
		FtpHelper ftpHelper = new FtpHelper(url, port, username, password, remotePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);			
			ftpHelper.storeFile(file.getName(), bis);
		} finally {
			IOUtils.closeQuietly(bis);
			IOUtils.closeQuietly(fis);
		}
	}

}
