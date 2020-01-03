package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对账文件生成组件
 * 文件格式定义
 * 文件名称在变量fileNamePattern中定义，如“vip_present_detail.dat”,
 * 当按天对账时，生成的文件名为vip_present_detail{fileNameSeparator}yyyyMMdd.dat
 * 当按月对账时，生成的文件名为vip_present_detail{fileNameSeparator}yyyyMM.dat
 * 文件内容格式：
 * 文件头(第1行):
 * 对账周期period{delimiter}文件记录条数{delimiter}对账文件生成时间{lineSeparator}
 * 对账记录明细(第2行开始为记录明细数据),字段将以delimiter分割，换行符号lineSeparator结束，字段定义及顺序按
 * AuditIterator返回数据的顺序
 *
 * @author yuanpeng
 *
 */
public class AuditFileBuilder extends AuditComponent<AuditFileBuilder> {
	
	private static final Logger logger = LoggerFactory.getLogger(AuditFileBuilder.class);
	
	private Iterator<AuditIterator> data;
	private Uploader uploader;
	private File file;
	private int totalSize;
	private String auditTime;
	protected AuditPeriod period;
	protected String fileNamePattern;
	private ContentWriter contentWriter;
	
	public AuditFileBuilder() {
		period = AuditPeriod.Month;
		contentWriter = new DefaultContentWriter();
	}
	
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
	
	/**
	 * 设置对账时间字符串
	 * @param auditTime 对账时间字符串
	 * @return 当前对象
	 */
	public AuditFileBuilder auditTime(String auditTime) {
		this.auditTime = auditTime;
		return this;
	}
	
	/**
	 * 设置对账周期
	 * @param period 对账周期
	 * @return 当前对象
	 */
	public AuditFileBuilder period(AuditPeriod period) {
		this.period = period;
		return this;
	}
	
	/**
	 * 设置文件名模式，由文件名称和时间格式组成，以下划线分隔
	 * Set pattern of file name compose of name and time format, separate with underline. 
	 * Example: vip_present_detail_yyyymm.dat
	 * @param fileNamePattern 文件名模式
	 * @return 当前对象
	 */
	public AuditFileBuilder fileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
		return this;
	}
	
	/**
	 * 设置文件内容
	 * @param data 对账数据
	 * @param totalSize 总大小
	 * @return 当前对象
	 */
	public AuditFileBuilder content(Iterator<AuditIterator> data, int totalSize) {
		this.data = data;
		this.totalSize = totalSize;
		return this;
	}
	
	/**
	 * 设置内容写入器
	 * @param contentWriter 内容写入器
	 * @return 当前对象
	 */
	public AuditFileBuilder contentWriter(ContentWriter contentWriter) {
		this.contentWriter = contentWriter;
		return this;
	}
	
	/**
	 * 设置上传组件
	 * @param uploader 上传组件
	 * @return 当前对象
	 */
	public AuditFileBuilder uploader(Uploader uploader) {
		this.uploader = uploader;
		return this;
	}
	
	/**
	 * 获取结果文件
	 * @return 结果文件
	 */
	public File getResult() {
		if(file == null) {
			throw new IllegalStateException("File must be build first.");
		}
		
		return this.file;
	}
	
	/**
	 * 构造文件
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
		this.file = resultFile;
		if(resultFile.exists()) {
			logger.info("File exists: {}", resultFile);
			return;
		}
		
		OutputStream os = null;
		BufferedOutputStream bos = null;
		try {
			os = new FileOutputStream(resultFile);
			bos = new BufferedOutputStream(os);
			writeContent(bos);
			bos.flush();
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
		
		String fileName = new AuditFileNameConstructor(fileNamePattern, fileNameSeparator, auditTime).getFileName();
		
		return new File(filePath + fileName);
	}
	
	private void writeContent(BufferedOutputStream bos) throws IOException {
		if(auditTime == null) {
			auditTime = period.getAuditTimeStr();
		}
		
		contentWriter.writeContent(bos, auditTime, delimiter, lineSeparator, charset, totalSize, data);
	}
}
