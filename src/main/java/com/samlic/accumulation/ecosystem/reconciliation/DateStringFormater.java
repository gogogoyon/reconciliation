package com.samlic.accumulation.ecosystem.reconciliation;

import java.text.DateFormat;

/**
 * 时间格式化类
 * @author yuanpeng
 *
 */
public class DateStringFormater implements  StringFormater {
	private DateFormat formater;
	
	public DateStringFormater(DateFormat formater) {
		this.formater = formater;
	}

	@Override
	public String format(Object data) {		
		return formater.format(data);
	}

}
