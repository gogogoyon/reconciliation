package com.samlic.accumulation.ecosystem.reconciliation;

/**
 * 下载异常
 * @author yuanpeng
 *
 */
public class DownloadException extends RuntimeException {

	private static final long serialVersionUID = -57779629753733291L;

	public DownloadException() {
		super();
	}
	
	public DownloadException(String message) {
		super(message);
	}
	
	public DownloadException(String message, Throwable cause)  {
		super(message, cause);
	}
	
	public DownloadException(Throwable cause)  {
		super(cause);
	}
}
