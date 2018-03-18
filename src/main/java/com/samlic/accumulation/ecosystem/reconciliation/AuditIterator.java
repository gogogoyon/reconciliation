package com.samlic.accumulation.ecosystem.reconciliation;

import java.util.Iterator;

/**
 * 定义对账字段顺序
 * @author yuanpeng
 *
 */
public abstract class AuditIterator implements Iterator<String> {
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
