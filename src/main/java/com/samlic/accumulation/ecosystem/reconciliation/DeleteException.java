package com.samlic.accumulation.ecosystem.reconciliation;

/**
 * 删除异常
 * @author yuanpeng
 *
 */
public class DeleteException extends RuntimeException {

	private static final long serialVersionUID = -206903712876213553L;

	public DeleteException() {
		super();
	}
	
	public DeleteException(String message) {
		super(message);
	}
	
	public DeleteException(String message, Throwable cause)  {
		super(message, cause);
	}
	
	public DeleteException(Throwable cause)  {
		super(cause);
	}
}
