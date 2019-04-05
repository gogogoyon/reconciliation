package com.samlic.accumulation.ecosystem.reconciliation;

/*
 * 对账消费者
 */
public interface AuditConsumer {
	/**
	 * 消费对账数据
	 */
	void consume();
	
	/**
	 * 添加文件处理器
	 * @param handler
	 */
	AuditConsumer addFileDataHandler(FileDataHandler handler);
}
