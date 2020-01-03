package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.File;

/**
 * 定义文件数据处理方法
 * @author yuanpeng
 *
 */
public interface FileDataHandler {
	/**
	 * 处理第一行
	 * @param file 数据文件
	 * @param lineItems 第一行数据
	 */
	void handleFirstLine(File file, String[] lineItems);
	/**
	 * 处理文件内容
	 * @param lineItems 明细数据
	 */
	void handleContent(String[] lineItems);	
	/**
	 * 结束处理
	 */
	void handleEnd();
}
