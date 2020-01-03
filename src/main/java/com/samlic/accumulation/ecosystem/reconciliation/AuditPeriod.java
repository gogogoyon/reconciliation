package com.samlic.accumulation.ecosystem.reconciliation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 对账周期
 * @author yuanpeng
 *
 */
public enum AuditPeriod {
	Day("yyyyMMdd"), Month("yyyyMM");
	
	private String format;
	
	AuditPeriod(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}
	
	/**
	 * 获取当前周期的审核时间
	 * @return 对账时间字符串
	 */
	public String getAuditTimeStr() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Calendar rightNow = Calendar.getInstance();
		if(this == Day) {
			rightNow.add(Calendar.DAY_OF_MONTH, -1);
		} else if(this == Month) {
			rightNow.add(Calendar.MONTH, -1);
		}
		
		return dateFormat.format(rightNow.getTime());
	}
	
	/**
	 * 检查对账时间字符串格式
	 * @param auditTime 对账时间字符串
	 */
	public void checkAuditTime(String auditTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			dateFormat.parse(auditTime);
		} catch (ParseException e) {
			throw new IllegalArgumentException("'auditTime' is illegal.");
		}
	}
	
}
