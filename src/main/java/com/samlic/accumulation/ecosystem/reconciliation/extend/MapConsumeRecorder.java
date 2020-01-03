package com.samlic.accumulation.ecosystem.reconciliation.extend;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.samlic.accumulation.ecosystem.reconciliation.BaseConsumeItem;
import com.samlic.accumulation.ecosystem.reconciliation.ConsumeRecorder;

/**
 * 对账文件数据消费记录类
 * @author yuanpeng
 *
 * @param <T> 明细记录对象类型
 */
public class MapConsumeRecorder<T extends BaseConsumeItem> extends MapFileHandleRecorder implements ConsumeRecorder<T> {
	private Map<Long, Queue<T>> recordMap = new HashMap<>();
	
	@Override
	public synchronized T getLatestItem(Long auditId) {
		if(recordMap.containsKey(auditId)) {
			return recordMap.get(auditId).peek();
		}
		
		return null;
	}

	@Override
	public synchronized void saveItem(T record) {
		Queue<T> queue = null;
		if(recordMap.containsKey(record.getAuditId())) {
			queue = recordMap.get(record.getAuditId());				
		} else {
			queue = new LinkedList<>();
			recordMap.put(record.getAuditId(), queue);
		}
		
		queue.add(record);
	}
	
}
