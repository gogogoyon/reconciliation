package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.File;
import java.io.IOException;

/**
 * 定义上传组件
 * @author yuanpeng
 *
 */
public interface Uploader {
	void upload(File file) throws IOException;
}
