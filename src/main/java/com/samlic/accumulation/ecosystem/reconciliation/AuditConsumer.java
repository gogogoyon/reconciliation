package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.File;

/*
 * 对账消费者
 */
public interface AuditConsumer {
	/**
	 * 消费对账数据
	 */
	void consume(File file);
}
