package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;

/**
 * 对账文件生成组件
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
class AuditFileBuilder extends AuditComponent<AuditFileBuilder> {
	
	private Iterator<AuditIterator> data;
	private Uploader uploader;
	private File file;
	private int totalSize;
	private String auditTime;
	protected AuditPeriod period = AuditPeriod.Month;
	protected String fileNamePattern;
	
	public AuditPeriod getPeriod() {
		return period;
	}

	public String getFileNamePattern() {
		return fileNamePattern;
	}
	
	public int getTotalSize() {
		return totalSize;
	}

	public Uploader getUploader() {
		return uploader;
	}
	
	public AuditFileBuilder auditTime(String auditTime) {
		this.auditTime = auditTime;
		return this;
	}
	
	/**
	 * 设置对账周期
	 * @param period
	 * @return
	 */
	public AuditFileBuilder period(AuditPeriod period) {
		this.period = period;
		return this;
	}
	
	/**
	 * 设置文件名模式，由文件名称和时间格式组成，以下划线分隔
	 * Set pattern of file name compose of name and time format, separate with underline. 
	 * Example: vip_present_detail_yyyymm.dat
	 * @param fileNamePattern
	 * @return
	 */
	public AuditFileBuilder fileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
		return this;
	}
	
	/**
	 * 设置文件内容
	 * @param dataList
	 * @return
	 */
	public AuditFileBuilder content(Iterator<AuditIterator> data, int totalSize) {
		this.data = data;
		this.totalSize = totalSize;
		return this;
	}
	
	/**
	 * 设置上传组件
	 * @param uploader
	 * @return
	 */
	public AuditFileBuilder uploader(Uploader uploader) {
		this.uploader = uploader;
		return this;
	}
	
	/**
	 * 获取结果文件
	 * @return
	 */
	public File getResult() {
		if(file == null) {
			throw new IllegalStateException("File must be build first.");
		}
		
		return this.file;
	}
	
	/**
	 * 构造文件
	 * @return
	 */
	public void build() {
		if(this.fileNamePattern == null ||
				this.fileNamePattern.length() < 1) {
			throw new IllegalStateException("File name pattern must be set first.");
		}
		
		if(this.data == null) {
			throw new IllegalStateException("Content must be set first.");
		}
		
		File resultFile = makeFile();
		OutputStream os = null;
		BufferedOutputStream bos = null;
		try {
			os = new FileOutputStream(resultFile);
			bos = new BufferedOutputStream(os);
			writeContent(bos);
			bos.flush();
			
			this.file = resultFile;
		} catch (IOException e) {
			throw new IllegalStateException("Failed to build file.", e);
		} finally {
			IOUtils.closeQuietly(bos);
			IOUtils.closeQuietly(os);
		}
	}
	
	/**
	 * 上传结果文件
	 */
	public void upload() {
		if(this.uploader == null) {
			throw new IllegalStateException("Uploader must be set first.");
		}
		
		File file = getResult();
		try {
			this.uploader.upload(file);
			if(delete) {
				if(!file.delete()) {
					throw new DeleteException("Failed to delete file.");
				}
			}
		} catch (IOException e) {
			throw new UploadException("Failed to upload file.", e);
		}
	}
	
	private File makeFile() {
		if(auditTime == null) {
			auditTime = period.getAuditTimeStr();
		}
		
		String fileName = this.fileNamePattern;		
		int index = this.fileNamePattern.lastIndexOf(".");
		if(index > -1) {
			String pattern = fileName.substring(0, index);	
			String suffix = fileName.substring(index, fileName.length());
			fileName = pattern + fileNameSeparator + auditTime + suffix;
		} else {
			fileName += fileNameSeparator + auditTime;
		}
		
		return new File(filePath + fileName);
	}
	
	private void writeContent(BufferedOutputStream bos) throws IOException {
		if(auditTime == null) {
			auditTime = period.getAuditTimeStr();
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		StringBuilder firstLine =  new StringBuilder();
		firstLine.append(auditTime).append(delimiter)
			.append(this.totalSize).append(delimiter)
			.append(dateFormat.format(new Date())).append(lineSeparator);
		bos.write(firstLine.toString().getBytes(charset));
		int count = 0;
		while(data.hasNext()) {
			AuditIterator auditIterator = data.next();
			while(auditIterator.hasNext()) {
				bos.write(auditIterator.next().getBytes(charset));
				if(auditIterator.hasNext()) {
					bos.write(delimiter.getBytes(charset));
				}
			}
			bos.write(lineSeparator.getBytes(charset));
			
			count ++;
		}
		
		if(count != this.totalSize) {
			throw new IllegalStateException("Size of content is incorrect.");
		}
	}
}
