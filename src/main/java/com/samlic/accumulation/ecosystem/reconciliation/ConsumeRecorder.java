package com.samlic.accumulation.ecosystem.reconciliation;

/**
 * 对账数据消费记录器
 * @author yuanpeng
 *
 * @param <T> 明细记录对象类型
 */
public interface ConsumeRecorder<T extends BaseConsumeItem> extends FileHandleRecorder {
	/**
	 * 获取最近的明细记录
	 * @param auditId 对账ID
	 * @return 明细记录
	 */
	T getLatestItem(Long auditId);
	/**
	 * 保存明细记录
	 * @param record 明细记录
	 */
	void saveItem(T record);
}
