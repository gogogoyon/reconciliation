package com.samlic.accumulation.ecosystem.reconciliation;

/**
 * 对账生产者
 * @author yuanpeng
 *
 */
public interface AuditProducer {
	/**
	 * 生产本周期对账数据，此方法可以在一个对账周期内重复调用
	 */
	void produce();
	
	/**
	 * 生产指定时间对账数据，此方法可以在一个对账周期内重复调用
	 * @param auditTime 对账时间字符串
	 */
	void produce(String auditTime);
}
