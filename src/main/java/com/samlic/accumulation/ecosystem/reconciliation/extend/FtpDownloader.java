package com.samlic.accumulation.ecosystem.reconciliation.extend;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import com.samlic.accumulation.ecosystem.reconciliation.Downloader;
import com.samlic.accumulation.ecosystem.reconciliation.FileNameConstructor;

public class FtpDownloader implements Downloader {
	
	private String url;			// 服务器地址
	private String port; 		// 服务器端口
	private String username; 	// 用户登录名
	private String password; 	// 用户登录密码
	private String remotePath;	// 文件保存路径
	private String localPath;
	
	private FileNameConstructor fileNameConstructor;  //下载文件名称构造器
	
	public FtpDownloader(String url, String port, String username, String password, String remotePath, String localPath) {
		this.url = url;
	    this.port = port;
	    this.username = username;
	    this.password = password;	  
	    this.remotePath = remotePath;
	    this.localPath = localPath;
	}
	
	public FtpDownloader(String url, String port, String username, String password, String remotePath, String localPath,
			FileNameConstructor fileNameConstructor) {
		this(url, port, username, password, remotePath, localPath);
	    this.fileNameConstructor = fileNameConstructor;
	}
	
	@Override
	public File[] downloadFiles(UnaryOperator<String[]> selector) throws IOException {
		FtpHelper ftpHelper = new FtpHelper(url, port, username, password, remotePath);
		List<File> fileList = new ArrayList<>();
		String[] fileNames = null;
		if(fileNameConstructor != null) {
			fileNames = new String[] { fileNameConstructor.getFileName() };
		} else {
			fileNames = ftpHelper.retrieveFileNames();
		}
		
		fileNames = selector.apply(fileNames);
		for (String fileName : fileNames) {			
			fileList.add(ftpHelper.retrieveFile(localPath, fileName));			
		}

		return fileList.toArray(new File[0]);
	}
}
