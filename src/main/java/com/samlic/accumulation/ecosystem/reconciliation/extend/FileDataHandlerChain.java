package com.samlic.accumulation.ecosystem.reconciliation.extend;

import java.io.File;

import com.samlic.accumulation.ecosystem.reconciliation.FileDataHandler;

/*
 * 文件数据处理器链
 */
public class FileDataHandlerChain implements FileDataHandler {

	FileDataHandler handler;
	FileDataHandlerChain next;
	
	public FileDataHandlerChain(FileDataHandler handler) {
		this.handler = handler;
	}

	@Override
	public void handleFirstLine(File file, String[] lineItems) {
		handler.handleFirstLine(file, lineItems);
		if(next != null) {
			next.handleFirstLine(file, lineItems);
		}
	}

	@Override
	public void handleContent(String[] lineItems) {
		handler.handleContent(lineItems);
		if(next != null) {
			next.handleContent(lineItems);
		}
	}

	@Override
	public void handleEnd() {
		handler.handleEnd();
		if(next != null) {
			next.handleEnd();
		}
	}
	
	/**
	 * 追加处理器
	 * @param handler
	 * @return
	 */
	public FileDataHandlerChain addHandler(FileDataHandler handler) {
		if(this.next != null) {
			throw new IllegalStateException("Duplicated set.");
		}
		
		this.next  = new FileDataHandlerChain(handler);
		
		return next; 
	}
}
