package com.samlic.accumulation.ecosystem.reconciliation;

/**
 * 对账数据消费记录器
 * @author yuanpeng
 *
 * @param <T>
 */
public interface ConsumeRecorder<T extends BaseConsumeItem> extends FileHandleRecorder {
	/**
	 * 获取最近的明细记录
	 * @param auditId
	 * @return
	 */
	T getLatestItem(Long auditId);
	/**
	 * 保存明细记录
	 * @param record
	 */
	void saveItem(T record);
}
