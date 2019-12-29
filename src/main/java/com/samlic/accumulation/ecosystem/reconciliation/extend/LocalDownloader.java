package com.samlic.accumulation.ecosystem.reconciliation.extend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

import org.apache.commons.io.FileUtils;

import com.samlic.accumulation.ecosystem.reconciliation.Downloader;

public class LocalDownloader implements Downloader {
	
	private String sourcePath;
	private String localPath;
	
	public LocalDownloader(String sourcePath, String localPath) {
		this.sourcePath = sourcePath;
		this.localPath = localPath;
	}

	@Override
	public File[] downloadFiles(UnaryOperator<String[]> selector) throws IOException {
		String[] fileNames = retrieveFileNames(selector);
		File[] destFiles = new File[fileNames.length];
		int i = 0;
		for(String fileName : fileNames) {
			destFiles[i++] = downloadFile(fileName);
		}
		
		return destFiles;
	}
	
	private File downloadFile(String fileName) throws IOException {
		File srcFile = new File(sourcePath + fileName);
		File destFile = new  File(localPath + fileName);
		if(srcFile.exists()) {
			FileUtils.moveFile(srcFile, destFile);
			return destFile;
		} 
		
		throw new FileNotFoundException("" + srcFile);
	}
	
	private String[] retrieveFileNames(UnaryOperator<String[]> selector) throws IOException {
		File sourceDir = new File(sourcePath);
		if(sourceDir.exists()) {
			Collection<File> sourceFiles = FileUtils.listFiles(sourceDir, null, false);
			List<String> fileNameList = new ArrayList<>();
			for(File file : sourceFiles) {
				fileNameList.add(file.getName());
			}
			
			String[] fileNames = fileNameList.toArray(new String[0]);
			if(selector != null) {
				fileNames = selector.apply(fileNames);
			}

			return fileNames;
		}

		return new String[0];
	}
}
