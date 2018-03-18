package com.samlic.accumulation.ecosystem.reconciliation;

/**
 * 上传异常
 * @author yuanpeng
 *
 */
public class UploadException extends RuntimeException {

	private static final long serialVersionUID = -57779629753733291L;

	public UploadException() {
		super();
	}
	
	public UploadException(String message) {
		super(message);
	}
	
	public UploadException(String message, Throwable cause)  {
		super(message, cause);
	}
	
	public UploadException(Throwable cause)  {
		super(cause);
	}
}
