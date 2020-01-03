package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * 定义内容写入器，主要用于自定义文件内容的写入格式
 * @author yuanpeng
 *
 */
public interface ContentWriter {
	void writeContent(OutputStream os, String auditTime, String delimiter, String lineSeparator, Charset charset,
			int totalSize, Iterator<AuditIterator> data) throws IOException;
}
