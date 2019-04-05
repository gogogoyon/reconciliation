package com.samlic.accumulation.ecosystem.reconciliation.extend;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.samlic.accumulation.ecosystem.reconciliation.FileHandleRecord;
import com.samlic.accumulation.ecosystem.reconciliation.FileHandleRecord.AuditRole;
import com.samlic.accumulation.ecosystem.reconciliation.FileHandleRecorder;


/**
 * Map对账文件处理记录类
 * @author yuanpeng
 *
 */
public class MapFileHandleRecorder implements FileHandleRecorder {
	private Map<String, FileHandleRecord> recordMap = new ConcurrentHashMap<String, FileHandleRecord>();
	
	@Override
	public FileHandleRecord getRecord(AuditRole role, String pattern, String auditTime) {
		String key = pattern + auditTime;
		return recordMap.get(key);
	}

	@Override
	public Long save(FileHandleRecord record) {
		String key = record.getPattern() + record.getAuditTime();
		recordMap.putIfAbsent(key, record);
		
		return (long)key.hashCode();
	}

	@Override
	public void update(FileHandleRecord record) {
		String key = record.getPattern() + record.getAuditTime();
		recordMap.put(key, record);
	}

}

