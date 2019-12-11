package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.File;
import java.io.IOException;
import java.util.function.UnaryOperator;

public interface Downloader {
	
	/**
	 * 下载文件
	 * @param selector   文件选择器，输入文件名称列表，返回文件名称列表
	 * @return
	 * @throws IOException
	 */
	File[] downloadFiles(UnaryOperator<String[]> selector) throws IOException;
}
