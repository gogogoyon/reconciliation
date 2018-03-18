package com.samlic.accumulation.ecosystem.reconciliation.extend;

import java.util.HashMap;
import java.util.Map;

import com.samlic.accumulation.ecosystem.reconciliation.FileHandleRecord;
import com.samlic.accumulation.ecosystem.reconciliation.FileHandleRecorder;


/**
 * Map对账文件处理记录类
 * @author yuanpeng
 *
 */
public class MapFileHandleRecorder implements FileHandleRecorder {
	private Map<String, FileHandleRecord> recordMap = new HashMap<String, FileHandleRecord>();
	
	@Override
	public FileHandleRecord getRecord(String pattern, String auditTime) {
		String key = pattern + auditTime;
		return recordMap.get(key);
	}

	@Override
	public Long save(FileHandleRecord record) {
		String key = record.getPattern() + record.getAuditTime();
		recordMap.put(key, record);
		
		return (long)key.hashCode();
	}

	@Override
	public void update(FileHandleRecord record) {
		save(record);
	}

}

