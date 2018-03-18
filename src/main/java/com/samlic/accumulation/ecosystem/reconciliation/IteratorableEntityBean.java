package com.samlic.accumulation.ecosystem.reconciliation;

import java.lang.reflect.Field;

public abstract class IteratorableEntityBean implements Iteratorable {
	@Override
	public Object getValue(String name) {
		try {
			Field field = this.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(this);
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}
}
