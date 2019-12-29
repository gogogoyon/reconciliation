package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.File;
import java.io.IOException;
import java.util.function.UnaryOperator;

public interface Downloader {
	
	/**
	 * 下载多个文件
	 * @param selector   文件选择器，输入文件名称列表，返回符合条件的文件名称列表
	 * @return
	 * @throws IOException
	 */
	File[] downloadFiles(UnaryOperator<String[]> selector) throws IOException;
}
