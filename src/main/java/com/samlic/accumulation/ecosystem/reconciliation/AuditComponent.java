package com.samlic.accumulation.ecosystem.reconciliation;

import java.nio.charset.Charset;

/**
 * 对账组件基类
 * @author yuanpeng
 *
 */
@SuppressWarnings("unchecked")
public abstract class AuditComponent<T extends AuditComponent<T>> {
	protected String delimiter = "|";
	protected String lineSeparator = "\r\n";	
	protected Charset charset = Charset.defaultCharset();
	protected String filePath = "";
	protected boolean delete;
	protected String fileNameSeparator="_";

	public T fileNameSeparator(String fileNameSeparator) {
		this.fileNameSeparator = fileNameSeparator;
		return (T)this;
	}
	
	/**
	 * 设置数据列分隔符
	 * @param delimiter 数据列分隔符
	 * @return 当前对象
	 */
	public T delimiter(String delimiter) {
		this.delimiter = delimiter;
		return (T)this;
	}
	
	/**
	 * 设置文件存放临时目录
	 * @param filePath 文件路径
	 * @return 当前对象
	 */
	public T filePath(String filePath) {
		this.filePath = filePath;
		return (T)this;
	}
	
	/**
	 * 设置是否移除处理失败的文件
	 * @param delete 是否移除处理失败的文件
	 * @return 当前对象
	 */
	public T delete(boolean delete) {
		this.delete = delete;
		return (T)this;
	}
	
	/**
	 * 设置行分隔符
	 * @param lineSeparator 行分隔符
	 * @return 当前对象
	 */
	public T lineSeparator(String lineSeparator) {
		this.lineSeparator = lineSeparator;
		return (T)this;
	}
	
	/**
	 * 设置字符编码
	 * @param charset 字符编码
	 * @return 当前对象
	 */
	public T charset(Charset charset) {
		this.charset = charset;
		return (T)this;
	}
}
