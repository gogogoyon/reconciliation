package com.samlic.accumulation.ecosystem.reconciliation;

import java.util.List;

public interface Iteratorable {
	List<String> getKeyList();
	
	Object getValue(String name);	
}
