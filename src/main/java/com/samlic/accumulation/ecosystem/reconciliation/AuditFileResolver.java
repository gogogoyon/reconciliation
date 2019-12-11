package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对账文件分解器
 * 文件格式定义
 * 文件名称在变量fileNamePattern中定义，如“vip_present_detail_yyyymm.dat”
 *
 * 文件内容格式：
 * 文件头(第1行):
 * 对账周期period|文件记录条数|对账文件生成时间lineSeparator
 * 对账记录明细(第2行开始为记录明细数据),字段将以delimiter分割，换行符号lineSeparator结束，字段定义及顺序按
 * AuditIterator返回数据的顺序
 *
 * @author yuanpeng
 *
 */
public class AuditFileResolver extends AuditComponent<AuditFileResolver> {
	private File file;
	private FileDataHandler dataHandler;	
	private int columnSize = 0;
	private List<Downloader> downloaderList;
	private String suffix;
	
	private static final int HEADER_SIZE = 3;
	
	private static final Logger logger = LoggerFactory.getLogger(AuditFileResolver.class);
	
	public AuditFileResolver() {
		delimiter = "\\|";
		downloaderList = new ArrayList<Downloader>();
	}
	
	public AuditFileResolver columnSize(int columnSize) {
		this.columnSize = columnSize;
		return this;
	}
	
	public AuditFileResolver dataHandler(FileDataHandler dataHandler) {
		this.dataHandler = dataHandler;
		return this;
	}
	
	public AuditFileResolver addDownloader(Downloader downloader) {
		this.downloaderList.add(downloader);
		return this;
	}
	
	public AuditFileResolver suffix(String suffix) {
		this.suffix = suffix;
		return this;
	}
	
	public AuditFileResolver file(File file) {
		if(file == null || !file.canRead()) {
			throw new IllegalArgumentException("File is null or can not be read.");
		}
		
		this.file = file;
		return this;
	}
	
	public String getFileNamePattern(File sourceFile) {		
		String name = sourceFile.getName();
		int index = name.lastIndexOf(fileNameSeparator);
		if(index < 0) {
			throw new IllegalStateException("File name must contains separator " + fileNameSeparator + "."); 
		}		
		
		return name.substring(0, index);
	}
	
	public String getFileNamePattern() {
		if(file == null) {
			throw new IllegalStateException("A file must be set first."); 
		}
		
		String name = file.getName();
		int index = name.lastIndexOf(fileNameSeparator);
		if(index < 0) {
			throw new IllegalStateException("File name must contains separator " + fileNameSeparator + "."); 
		}		
		
		return name.substring(0, index);
	}
	
	public void resolver() {
		if(file != null) {
			resolver(file);
		}
		
		for(Downloader downloader : downloaderList) {
			if(downloader != null) {
				if(suffix == null) {
					throw new IllegalStateException("File suffix must be set."); 
				}
				
				try {
					File[] files = downloader.downloadFiles(fileNames -> {
						List<String> fileNameList = new ArrayList<>();
						for(String fileName : fileNames) {
							if(fileName.endsWith(suffix)) {
								fileNameList.add(fileName);
							}
						}
						
						return fileNameList.toArray(new String[0]);
					});
					for(File downloadFile : files) {				
						resolver(downloadFile);					
					}
				} catch (IOException e) {
					throw new DownloadException("Failed to download file.", e); 
				}	
			}
		}
	}
	
	private void resolver(File file) {
		if(file == null) {
			throw new IllegalStateException("A file must be set first."); 
		}
		
		if(dataHandler == null) {
			throw new IllegalStateException("A dataHandler must be set first."); 
		}
		
		Exception exception = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, charset);
			br = new BufferedReader(isr);
			String line = br.readLine();
			if(line == null) {
				throw new IllegalStateException("File is empty."); 
			}
			
			String[] headers = line.split(delimiter, HEADER_SIZE);
			if(headers.length != HEADER_SIZE) {
				throw new IllegalStateException("Header size error.");
			}
			
			int count = 0;
			int totalRow = Integer.parseInt(headers[1]);
			
			dataHandler.handleFirstLine(file, headers);
			while((line = br.readLine()) != null) {
				String[] datas = line.split(delimiter, columnSize);		
				if(columnSize > 0 && datas.length != columnSize) {
					throw new IllegalStateException("Data column size error.");
				}
				dataHandler.handleContent(datas);
				count++;
			}
			
			if(count != totalRow) {
				throw new IllegalStateException("Content size is inconsistent.");
			}
			
			dataHandler.handleEnd();			
		} catch (Exception e) {
			exception = e;
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(fis);
		}
		
		if(exception == null) {
			try {
				FileUtils.moveFileToDirectory(file, new File(filePath + "completed/"), true);
			} catch (IOException e) {
				logger.error("Failed to move file to completed: " + file.getAbsolutePath(), e);
			}
		} else {	
			if(delete) {
				try {
					FileUtils.moveFileToDirectory(file, new File(filePath + "failed/"), true);
				} catch (IOException e) {
					logger.error("Failed to move file to failed: " + file.getAbsolutePath(), e);
				}
			}
			
			throw new IllegalStateException("Failed to resolve file: " + file.getAbsolutePath(), exception); 
		}
	}
}
