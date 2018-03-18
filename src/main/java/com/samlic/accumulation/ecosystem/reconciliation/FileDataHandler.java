package com.samlic.accumulation.ecosystem.reconciliation;

/**
 * 定义文件数据处理方法
 * @author yuanpeng
 *
 */
public interface FileDataHandler {
	/**
	 * 处理第一行
	 * @param lineItems
	 */
	void handleFirstLine(String[] lineItems);
	/**
	 * 处理文件内容
	 * @param lineItems
	 */
	void handleContent(String[] lineItems);	
	/**
	 * 结束处理
	 */
	void handleEnd();
}
