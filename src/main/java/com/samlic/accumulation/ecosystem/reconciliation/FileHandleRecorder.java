package com.samlic.accumulation.ecosystem.reconciliation;

import com.samlic.accumulation.ecosystem.reconciliation.FileHandleRecord.AuditRole;

/**
 * 对账文件处理记录器(包括生产和消费)
 * @author yuanpeng
 *
 */
public interface FileHandleRecorder {
	/**
	 * 获取当前生产记录
	 * 根据当前时间查询当前周期的对账记录
	 * @param role 对账角色
	 * @param pattern 定义文件名称模式
	 * @param auditTime 对账时间
	 * @return
	 */
	FileHandleRecord getRecord(AuditRole role, String pattern, String auditTime);
	/**
	 * 保存记录
	 * @param record
	 */
	Long save(FileHandleRecord record);
	/**
	 * 更新记录
	 * @param record
	 */
	void update(FileHandleRecord record);
}
