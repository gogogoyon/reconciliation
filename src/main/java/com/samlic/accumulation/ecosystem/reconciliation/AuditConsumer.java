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
	 * @param handler 文件数据处理器
	 * @return 当前对象
	 */
	AuditConsumer addFileDataHandler(FileDataHandler handler);
}
