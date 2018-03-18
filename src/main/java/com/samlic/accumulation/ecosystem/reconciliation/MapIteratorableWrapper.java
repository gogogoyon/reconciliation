package com.samlic.accumulation.ecosystem.reconciliation;

import java.util.List;
import java.util.Map;

public class MapIteratorableWrapper implements Iteratorable {

	private Map<String, Object> data;
	private List<String> keyList;
	
	public MapIteratorableWrapper(Map<String, Object> data, List<String> keyList) {
		this.data = data;
		this.keyList = keyList;
	}
	
	@Override
	public List<String> getKeyList() {
		return keyList;
	}

	@Override
	public Object getValue(String name) {
		return data.get(name);
	}

}
